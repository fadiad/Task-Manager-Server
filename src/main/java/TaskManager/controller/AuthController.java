package TaskManager.controller;

import TaskManager.entities.LoginRequest;

import TaskManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")

public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
     @Autowired
     private UserService userService;
    /**
     * User's login
     *
     * @param credentials - email, username and password of the user that wants to login
     * @return LoginData - user's info and generated token
     */

    @PostMapping(value = "/loginUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody LoginRequest credentials) {
        logger.info("hi gow are u");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> create(@RequestBody LoginRequest credentials) {
        userService.addUser();
        logger.info("hi gow are u");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<String> login() {
        logger.info("hi gow are u");
        logger.error("error");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
