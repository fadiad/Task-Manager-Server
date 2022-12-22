package TaskManager.service;

import TaskManager.entities.GitUser;
import TaskManager.entities.responseEntities.TokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GitService {

    public String getTokenFromCode(@RequestParam String code) {
        String client_id = "f21614ae68732a9a2cc0";
        String client_secret = "a62b7f0991686edd80a37b4b2aa63411df873fb0";
        String Link = "https://github.com/login/oauth/access_token?";

        String linkGetToken = Link + "client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code;
        ResponseEntity<TokenResponse> result = getTokenFromCodeFunction(linkGetToken);

        return result.getBody().getAccess_token();
    }

    public ResponseEntity<GitUser> getDetailsFromToken(@RequestParam String token) {
        String linkForName = "https://api.github.com/user"; //out from function
        return getDetailsFromTokenFunction(linkForName, token);
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

    public static ResponseEntity<GitUser> getDetailsFromTokenFunction(String link, String bearerToken) {
        ResponseEntity<GitUser> response = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        try {
//          response=restTemplate.exchange(link, HttpMethod.GET, entity, GitUser.class);
            GitUser gitUser = restTemplate.exchange(link, HttpMethod.GET, entity, GitUser.class).getBody();
            System.out.println(gitUser.getName());
            System.out.println(gitUser.getEmail());

            return ResponseEntity.ok(gitUser);
        } catch (Exception e) {
            System.out.println("error to get details from git" + e);
        }
        return null;
    }
}
