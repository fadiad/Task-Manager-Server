package TaskManager.controller;

import TaskManager.entities.GitUser;
import TaskManager.service.GitService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/git")
@AllArgsConstructor
public class GitController {

    private final GitService gitService;

    @RequestMapping(method = RequestMethod.GET, path = "/allDetails")
    public ResponseEntity<GitUser> OAuth2Request(@RequestParam String code) {
        String result = getTokenFromCode(code);
        GitUser gitUser = getDetailsFromToken(result).getBody();

        // if exist on userRepository  return token, else add to DB and return token
        return getDetailsFromToken(result);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/getToken")
    public String getTokenFromCode(@RequestParam String code) {

        String client_id = "xxxxxx";
        String client_secret = "xxxxxx";
        String Link = "https://github.com/login/oauth/access_token?";

        String linkGetToken = Link + "client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code;
        ResponseEntity<TokenResponse> result = gitService.getTokenFromCodeFunction(linkGetToken);

        return result.getBody().getAccess_token();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getDetails")
    public ResponseEntity<GitUser> getDetailsFromToken(@RequestParam String token) {

        String linkForName = "https://api.github.com/user"; //out from function
        return gitService.getDetailsFromTokenFunction(linkForName, token);
    }

}
