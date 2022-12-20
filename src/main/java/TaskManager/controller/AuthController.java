package TaskManager.controller;

import TaskManager.entities.*;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.AuthService;
import TaskManager.service.GitService;
import TaskManager.utils.emailUtils.EmailActivationFacade;
import TaskManager.utils.jwtUtils.JWTTokenHelper;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;


@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final EmailActivationFacade emailActivationFacade;

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JWTTokenHelper jWTTokenHelper;


    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginData> login(@RequestBody LoginRequest credentials) {
        //TODO we wii add validation here in the near future folks
        try {
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            LoginData response = new LoginData();
            response.setUserDTO(new UserDTO(securityUser.getUser()));
            response.setToken(jWTTokenHelper.generateToken(securityUser.getUsername()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
        }

    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> create(@RequestBody User user) {
        //TODO we wii add validation here in the near future folks
        System.out.println(user);
        authService.addUser(user);
        logger.info("New user was added");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping
    public void onlyAdmin() {
        System.out.println("hi admin");

        emailActivationFacade.sendVerificationEmail("saraysara1996@gmail.com");
    }


}
