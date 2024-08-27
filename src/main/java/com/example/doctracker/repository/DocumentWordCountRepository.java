package com.example.doctracker.repository;

import com.example.doctracker.dto.WordFrequencyDTO;
import com.example.doctracker.entity.DocumentWordCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentWordCountRepository extends JpaRepository<DocumentWordCount, Long> {
    @Query("SELECT new com.example.doctracker.dto.WordFrequencyDTO(dwc.word, dwc.count) " +
            "FROM DocumentWordCount dwc WHERE dwc.document.id = :documentId " +
            "AND LOWER(dwc.word) NOT IN :excludedWords " +
            "ORDER BY dwc.count DESC limit 10")
    List<WordFrequencyDTO> findTopWordFrequencies(@Param("documentId") Long documentId,
                                                  @Param("excludedWords") List<String> excludedWords);

    @Query("SELECT dwc.word FROM DocumentWordCount dwc WHERE dwc.document.id = :documentId ORDER BY LENGTH(dwc.word) DESC LIMIT 1")
    String findLongestWordByDocumentId(Long documentId);
}
