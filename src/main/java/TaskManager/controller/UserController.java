package TaskManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')"+"|| hasAuthority('ROLE_USER')")
    public void onlyAdmin(){
        System.out.println("hi admin");
    }

}
