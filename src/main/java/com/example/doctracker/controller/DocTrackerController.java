package com.example.doctracker.controller;

import com.example.doctracker.dto.UserDTO;
import com.example.doctracker.dto.WordFrequencyDTO;
import com.example.doctracker.service.DocTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class DocTrackerController {
    @Autowired
    private DocTrackerService docTrackerService;

    @GetMapping("document/longest-word/synonyms")
    public Map<String, List<String>> getSynonymsForLongestWord(@RequestParam("documentId") Long documentId) {
        return docTrackerService.findSynonymsByDocument(documentId);
    }

    @GetMapping("/users/filter")
    public List<UserDTO> getUsersFilteredByDate(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        return docTrackerService.getUsersFilteredByDate(startDate, endDate);
    }

    @GetMapping("/document/wordFrequency")
    public List<WordFrequencyDTO> getWordFrequencyFromDocument(
            @RequestParam("documentId") Long documentId) {
        return docTrackerService.getWordFrequencyFromDocument(documentId);
    }
}
