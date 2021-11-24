package com.example.vocabulary.services;

import com.example.vocabulary.exceptions.WordAlreadyExistsException;
import com.example.vocabulary.exceptions.WordNotFoundException;
import com.example.vocabulary.models.Word;
import com.example.vocabulary.repositories.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Primary
@Service
public class WordServiceImpl implements WordService {

    final WordRepository wordRepository;

    @Override
    public Word getWord(Long id) {
        return wordRepository.findById(id).orElseThrow(WordNotFoundException::new);
    }

    @Override
    public Page<Word> getWords(Pageable pageable) {
        return wordRepository.findAll(pageable);
    }

    @Override
    public Word createWord(Word word) {
        if (word.getIsVerb() == null) {
            word.setIsVerb(false);
        }
        if (word.getIsVerb()) {
            if (word.getName().startsWith("To ") || word.getName().startsWith("TO ") || word.getName().startsWith("to ")) {
                var changedWordName = word.getName().substring(3);
                var newWordName = "To " + changedWordName.toLowerCase();
                word.setName(newWordName);
            } else {
                var newWordName = "To " + word.getName().toLowerCase();
                word.setName(newWordName);
            }
        } else {
            if (word.getName().startsWith("To ")) {
                var newWordName = word.getName().substring(3);
                word.setName(newWordName);
            }
            if (!Character.isUpperCase(word.getName().charAt(0))) {
                var newWordName = word.getName().substring(0, 1).toUpperCase() + word.getName().substring(1);
                word.setName(newWordName);
            }
        }
        var words = wordRepository.findAll();
        if (words.contains(word)) {
            throw new WordAlreadyExistsException();
        }
        return wordRepository.save(word);
    }

    @Override
    public Word updateWord(Word word, Long id) {
        word.setId(id);
        return wordRepository.save(word);
    }

    @Override
    public Word deleteWord(Long id) {
        var word = getWord(id);
        wordRepository.deleteById(id);
        return word;
    }

    public Page<Word> getWordsBySearchEngine(String wordName, String translation, Pageable pageable) {

        if (wordName != null && translation != null) {
            return wordRepository.
                    getAllByNameContainsAndTranslationContains(wordName, translation, pageable);
        }
        if (wordName != null) {
            return wordRepository.getAllByNameContains(wordName, pageable);
        }
        if (translation != null) {
            return wordRepository.getAllByTranslationContaining(translation, pageable);
        }
        return wordRepository.findAll(pageable);
    }
}
