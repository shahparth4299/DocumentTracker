package com.example.doctracker.repository;

import com.example.doctracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.accountCreationDate < :endDate " +
            "AND NOT EXISTS (SELECT d FROM Document d WHERE d.user = u AND d.uploadDateTime BETWEEN :startDateTime AND :endDateTime)")
    List<User> findUsersRegisteredBeforeDateWithoutDocumentInPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDate endDate);
}

