package TaskManager.controller;

import TaskManager.entities.*;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.AuthService;
import TaskManager.utils.emailUtils.EmailActivationFacade;
import TaskManager.utils.jwtUtils.JWTTokenHelper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    //TODO: localhost:8080/auth/getDetails
    //TODO: CHECK'S
    //TODO: CHECK IF THIS ID THE FIRST TIME THE USER LOGIN THE APPLICATION OR NOT.
    //TODO: found the email + name of the user



    @RequestMapping(method = RequestMethod.POST, path = "/getToken")
    public String getTokenFromCode(@RequestParam String code) {

        //TODO: the first request from the frontend.. (1)
        //TODO: localhost:8080/auth/getToken?code= (2)

        String client_id = "f21614ae68732a9a2cc0";
        String client_secret = "a62b7f0991686edd80a37b4b2aa63411df873fb0";
        String Link = "https://github.com/login/oauth/access_token?";

        String linkGetToken = Link + "client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code;
        ResponseEntity<TokenResponse> result = getTokenFromCodeFunction(linkGetToken);

        return result.getBody().getAccess_token();
    }
    public static ResponseEntity<TokenResponse> getTokenFromCodeFunction(String link) {  //send link return response
        ResponseEntity<TokenResponse> response = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            return restTemplate.exchange(link, HttpMethod.POST, entity, TokenResponse.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!!");
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/getDetails")
    public ResponseEntity<GitUser> getDetailsFromToken(@RequestParam String token) {

        System.out.println(token);

        String linkForName = "https://api.github.com/user"; //out from function

        return getDetailsFromTokenFunction(linkForName, token);

//        GitUser gitUser = result.getBody();
//        if(result.getBody().getEmail()==null) {
//            GithubEmail[] githubEmail = reqFromGitTheEmail("https://api.github.com/user", token).getBody();
//            for (GithubEmail gEmail: githubEmail) {
//                if (gEmail.isPrimary()) {
//                        return null;
//                }
//            ResponseEntity<GithubEmail> resultEmail = reqFromGitTheEmail("https://api.github.com/user/emails", token); //get out the name
//            resultName.getBody().setEmail(resultEmail.getBody().getEmail());
//            }
//        }
    }
    public static ResponseEntity<GitUser> getDetailsFromTokenFunction(String link, String bearerToken) {

        ResponseEntity<GitUser> response = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        try {
//           response=restTemplate.exchange(link, HttpMethod.GET, entity, GitUser.class);
            GitUser gitUser = restTemplate.exchange(link, HttpMethod.GET, entity, GitUser.class).getBody();
            System.out.println(gitUser.getName());
            System.out.println(gitUser.getEmail());

            return  ResponseEntity.ok(gitUser);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }


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
