package TaskManager.controller;

import TaskManager.entities.*;
import TaskManager.entities.entitiesUtils.Validations;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.AuthService;
import TaskManager.service.GitService;
import TaskManager.utils.emailUtils.EmailSenderFacade;
import TaskManager.utils.jwtUtils.JWTTokenHelper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final EmailSenderFacade emailSenderFacade;

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JWTTokenHelper jWTTokenHelper;

    private final GitService gitService;


    /**
     * login request passes through this rout , if it went well , it will return user token & and userDTO
     *
     * @param credentials this is the LoginRequest details
     * @return details if success
     */
    @PostMapping(value = "/login_User", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginData> login(@RequestBody LoginRequest credentials) {
        if (!Validations.isEmailRegexValid(credentials.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid email");
        }

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

    /**
     * Create user pass though this rout , user sends email , password and username ,
     * if validations for his inputs passed well , his data will be saved in the DB .
     *
     * @param user details
     * @return ok if success
     */
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> create(@RequestBody User user) {
        Validations.fullUserValid(user); //TODO we wii add validation here in the near future folks
        System.out.println(user);
        authService.addUser(user);
        logger.info("New user was added");
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    /**
     * it gets a validation code from GitHub , and it returns user UserDTO , such as a way to make signin .
     *
     * @param code from the frontend, take this code and get the details from gitHub
     * @return details of the user from gitHub
     */
    @RequestMapping(method = RequestMethod.GET, path = "/allDetails", produces = "application/json")
    public ResponseEntity<LoginData> OAuth2Request(@RequestParam String code) {
        String gitToken = gitService.getTokenFromCode(code);
        GitUser result = gitService.getDetailsFromToken(gitToken);

        if (result == null || result.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request to github");
        }
        User user = new User();
        user.setEmail(result.getEmail());
        user.setUsername(result.getName());
        String userName = result.getName();
        if (userName != null) {
            user.setUsername(result.getName());
        } else {
            System.out.println(result.getEmail().split("@")[0]);
            user.setUsername(result.getEmail().split("@")[0]);
        }
        UserDTO userDTO = authService.signUpGitUser(user);
        try {
            LoginData loginData = new LoginData(userDTO, jWTTokenHelper.generateToken(userDTO.getEmail()));
            System.out.println(loginData.getUserDTO());
            return new ResponseEntity<>(loginData, HttpStatus.OK);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid request to github");
        }
    }

}
