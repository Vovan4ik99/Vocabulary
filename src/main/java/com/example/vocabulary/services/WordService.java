package com.example.vocabulary.services;

import com.example.vocabulary.models.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WordService {

    Word getWord(Long id);

    Page<Word> getWords(Pageable pageable);

    Word createWord(Word word);

    Word updateWord(Word word, Long id);

    Word deleteWord(Long id);

    Page<Word> getWordsBySearchEngine(String wordName, String translation, Pageable pageable);
}
