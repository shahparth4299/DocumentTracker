package com.example.doctracker.service;

import com.example.doctracker.dto.UserDTO;
import com.example.doctracker.dto.WordFrequencyDTO;
import com.example.doctracker.entity.DocumentWordCount;
import com.example.doctracker.entity.User;
import com.example.doctracker.repository.DocumentWordCountRepository;
import com.example.doctracker.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.vertexai.VertexAI;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import org.springframework.ai.vertexai.gemini.*;

@Service
public class DocTrackerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentWordCountRepository documentWordCountRepository;
    @Autowired
    private GeminiService geminiService;

    @Value("${api.key}")
    private String apiKey;

    public Map<String, List<String>> findSynonymsByDocument(Long documentId) {
        String longestWord = documentWordCountRepository.findLongestWordByDocumentId(documentId);
        if (longestWord == null || longestWord.isEmpty()) {
            throw new RuntimeException("No words found in the document.");
        }

        String response;
        try {
            response = geminiService.callApi("Provide only a comma-separated list of synonyms for the word '" + longestWord + "' without any additional context or explanations.", apiKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve synonyms from the external API.");
        }

        List<String> synonyms = extractTextFromResponse(response);
        Map<String, List<String>> result = new HashMap<>();
        result.put(longestWord, synonyms);
        return result;
    }

    private List<String> extractTextFromResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode textNode = rootNode.at("/candidates/0/content/parts/0/text");
            return Arrays.asList(textNode.asText().trim().split("\\s*,\\s*"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response JSON", e);
        }
    }

    public List<UserDTO> getUsersFilteredByDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<User> users = userRepository.findUsersRegisteredBeforeDateWithoutDocumentInPeriod(startDateTime, endDateTime, endDate);
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(new UserDTO(user.getEmail(), user.getAccountCreationDate()));
        }
        return userDTOS;
    }

    public List<WordFrequencyDTO> getWordFrequencyFromDocument(Long documentId) {
        List<String> excludedWords = List.of("the", "me", "you", "i", "of", "and", "a", "we");
        return documentWordCountRepository.findTopWordFrequencies(documentId, excludedWords);
    }
}
