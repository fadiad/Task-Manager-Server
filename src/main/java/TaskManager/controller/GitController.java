package TaskManager.controller;

import TaskManager.entities.GitUser;
import TaskManager.entities.LoginRequest;
import TaskManager.entities.User;
import TaskManager.service.AuthService;
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
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.GET, path = "/allDetails")
    public ResponseEntity<GitUser> OAuth2Request(@RequestParam String code) {
        String gitToken = gitService.getTokenFromCode(code);
        ResponseEntity<GitUser> result = gitService.getDetailsFromToken(gitToken);

        GitUser gitUser = result.getBody(); //EMAIL+NAME

        User user = new User();

        user.setPassword("gitPassword123456");
        user.setEmail(gitUser.getEmail());
        user.setUsername(gitUser.getName());
        //validations
        if(authService.notExist(gitUser.getEmail())){
            authService.addUser(user);
        }
        //TODO: login validations AND LOGIN WITH TOKEN THE USER

        return result;
    }

}
