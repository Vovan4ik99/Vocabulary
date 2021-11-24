package com.example.vocabulary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,
                reason = "Word already exists!")
public class WordAlreadyExistsException extends RuntimeException {
}
