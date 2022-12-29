package TaskManager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NoPermissionException;
import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * take care about the exceptions
     */
    @ExceptionHandler({IllegalArgumentException.class, EntityNotFoundException.class})
    public ResponseEntity<String> badRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getLocalizedMessage());
    }

    /**
     * take care about the exceptions
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> responseException(ResponseStatusException e) {
        return new ResponseEntity<>(e.getReason(), e.getStatus());
    }
    
}
