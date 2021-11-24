package com.example.vocabulary.services;

import com.example.vocabulary.exceptions.WordAlreadyExistsException;
import com.example.vocabulary.models.Word;
import com.example.vocabulary.repositories.WordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mariadb.jdbc.MariaDbConnection;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class WordServiceImplTest {

    @InjectMocks
    WordServiceImpl wordServiceImpl;

    @Mock
    WordRepository wordRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getWord() {
    }

    @Test
    void getWords() {

    }

    @Test
    void createWordWhenIsVerb() {
        var name = "Write down";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setIsVerb(true);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals("To write down", wordOut.getName());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void createWordWhenNotIsVerb() {
        var name = "Word";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setIsVerb(false);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals("Word", wordOut.getName());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void createWordWhenNotIsVerbStartsWithSmallLetter() {
        var name = "word";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setIsVerb(false);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals("Word", wordOut.getName());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void createWordWhenIsVerbFullyWrite() {
        var name = "To Write down";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setIsVerb(true);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals("To write down", wordOut.getName());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void createWordWhenIsVerbFullyWriteNotCorrectly() {
        var name = "TO write down";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setIsVerb(true);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals("To write down", wordOut.getName());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void createWordWhenIsVerbFullyWriteNotCorrectly2() {
        var name = "to write down";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setIsVerb(true);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals("To write down", wordOut.getName());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void createWordWhenWordAlreadyExists() {
        var name = "Word";
        var wordIn = new Word();
        wordIn.setName(name);
        var words = List.of(wordIn);
        Mockito.doReturn(words).when(wordRepository).findAll();
        assertThrows(WordAlreadyExistsException.class, () -> wordServiceImpl.createWord(wordIn));
    }

    @Test
    void updateWordWhenIsVerbIsNull() {
        var name = "Word";
        Boolean isVerb = null;
        var wordIn = new Word();
        wordIn.setIsVerb(isVerb);
        wordIn.setName(name);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals( false, wordOut.getIsVerb().booleanValue());
    }

    @Test
    void updateWordFromVerbToNoun() {
        var name = "To load";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setIsVerb(false);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.createWord(wordIn);
        assertEquals("Load", wordOut.getName());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void updateWord() {
        var name = "Word";
        var wordId = 1L;
        var wordIn = new Word();
        wordIn.setName(name);
        Mockito.doReturn(wordIn).when(wordRepository).save(wordIn);
        var wordOut = wordServiceImpl.updateWord(wordIn, wordId);
        assertEquals("Word", wordOut.getName());
        assertEquals(1L, wordOut.getId());
        Mockito.verify(wordRepository, Mockito.times(1)).save(wordIn);
    }

    @Test
    void deleteWord() {
        var wordIn = new Word();
        var wordId = 1L;
        wordIn.setId(wordId);
        Mockito.doNothing().when(wordRepository).deleteById(wordId);
        Mockito.doReturn(Optional.of(wordIn)).when(wordRepository).findById(wordId);
        var wordOut = wordServiceImpl.deleteWord(wordId);
        assertEquals(wordIn.getId(), wordOut.getId());
        Mockito.verify(wordRepository, Mockito.times(1)).deleteById(wordId);
        Mockito.verify(wordRepository, Mockito.times(1)).findById(wordId);
    }

    @Test
    void getWordsBySearchEngine() {
        var name = "Word";
        var translation = "Слово";
        var wordIn = new Word();
        wordIn.setName(name);
        wordIn.setTranslation(translation);
        Page<Word> words = new PageImpl(List.of(wordIn));
        Mockito.doReturn(words).when(wordRepository).getAllByNameContainsAndTranslationContains(name, translation, Pageable.unpaged());
        var wordListOut = wordServiceImpl.getWordsBySearchEngine(name, translation, Pageable.unpaged());
        assertEquals(words, wordListOut);
    }

    @Test
    void getWordsBySearchEngineOnlyByWordName() {
        var name = "Word";
        var wordIn = new Word();
        wordIn.setName(name);
        Page<Word> words = new PageImpl(List.of(wordIn));
        Mockito.doReturn(words).when(wordRepository).getAllByNameContains(name, Pageable.unpaged());
        var wordListOut = wordServiceImpl.getWordsBySearchEngine(name, null, Pageable.unpaged());
        assertEquals(words, wordListOut);
    }

    @Test
    void getWordsBySearchEngineOnlyByTranslation() {
        var translation = "Слово";
        var wordIn = new Word();
        wordIn.setName(translation);
        Page<Word> words = new PageImpl(List.of(wordIn));
        Mockito.doReturn(words).when(wordRepository).getAllByTranslationContaining(translation, Pageable.unpaged());
        var wordListOut = wordServiceImpl.getWordsBySearchEngine(null, translation, Pageable.unpaged());
        assertEquals(words, wordListOut);
    }

    @Test
    void  getWordsBySearchEngineWhenBothVariablesAreNull() {
        var wordIn = new Word();
        Page<Word> words = new PageImpl(List.of(wordIn, new Word(), new Word()));
        Mockito.doReturn(words).when(wordRepository).findAll(Pageable.unpaged());
        var wordListOut = wordServiceImpl.getWordsBySearchEngine(null, null, Pageable.unpaged());
        assertEquals(wordListOut, words);
    }
}