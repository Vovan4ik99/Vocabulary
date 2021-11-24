package com.example.vocabulary.repositories;

import com.example.vocabulary.models.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Page<Word> getAllByNameContains(String name, Pageable pageable);

    Page<Word> getAllByTranslationContaining(String translation, Pageable pageable);

    Page<Word> getAllByNameContainsAndTranslationContains(String name, String translation, Pageable pageable);
}
