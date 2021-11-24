package com.example.vocabulary.controllers.mvc;

import com.example.vocabulary.exceptions.WordAlreadyExistsException;
import com.example.vocabulary.models.Word;
import com.example.vocabulary.services.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class WordController {

    final WordService wordService;

    @GetMapping
    public String getWords(@RequestParam(required = false) String wordName,
                           @RequestParam(required = false) String translation,
                           Model model) {
        var words = wordService.getWordsBySearchEngine(wordName, translation, Pageable.unpaged());
        model.addAttribute("words", words);
        return "main";
    }

    @PostMapping
    public String createWord(@Valid Word word, BindingResult bindingResult,
                             Model model) {
        var errorsMap = ControllerUtils.getErrors(bindingResult);
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(errorsMap);
            model.addAttribute("word", word);
        } else {
            model.addAttribute("word", null);
            try {
                wordService.createWord(word);
            } catch (WordAlreadyExistsException exception) {
                model.addAttribute("wordExistsError", "Word already exists!");
            }
        }
        var words = wordService.getWords(Pageable.unpaged());
        model.addAttribute("words", words);
        return "main";
    }

    @GetMapping("/delete/{wordId}")
    public String deleteWord(@PathVariable Long wordId) {
        wordService.deleteWord(wordId);
        return "redirect:/";
    }

}


