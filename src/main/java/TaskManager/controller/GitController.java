package TaskManager.controller;

import TaskManager.entities.GitUser;
import TaskManager.entities.LoginRequest;
import TaskManager.entities.User;
import TaskManager.service.GitService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/git")
@AllArgsConstructor
public class GitController {
    private final GitService gitService;
    private final AuthController authController;

    @RequestMapping(method = RequestMethod.GET, path = "/allDetails")
    public ResponseEntity<GitUser> OAuth2Request(@RequestParam String code) {
        String gitToken = gitService.getTokenFromCode(code);
        ResponseEntity<GitUser> result = gitService.getDetailsFromToken(gitToken);
        GitUser gitUser = result.getBody();

        //find user by email,
        if(true){ // TODO: IF USER NOT EXIST IN D"B TO ADD CREATION;
            User user = new User();
            authController.create(user);
        }
        LoginRequest loginRequest= new LoginRequest(gitUser.getEmail(), "GitUser");
        authController.login(loginRequest); //TODO: TO LOGIN THE GITHUB USER WITHE TOKEN;
        return result;
    }


}
