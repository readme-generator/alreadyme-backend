package kr.markdown.alreadyme.controller;

import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> badRequestHandler(MethodArgumentNotValidException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", new Date());
        error.put("code", "400");
        error.put("message", "This github URL is invalid");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({NoRemoteRepositoryException.class})
    public ResponseEntity<?> badRequestHandler(NoRemoteRepositoryException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", new Date());
        error.put("code", "400");
        error.put("message", "Error occurred while cloning the repository. Check your github url.");
        return ResponseEntity.badRequest().body(error);
    }
}
