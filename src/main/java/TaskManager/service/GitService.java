package TaskManager.service;

import TaskManager.controller.TokenResponse;
import TaskManager.entities.GitUser;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GitService {

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

            return  ResponseEntity.ok(gitUser);
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return null;
    }

}
