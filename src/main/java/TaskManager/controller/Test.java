package TaskManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class Test {
    @GetMapping(value = "/hihi",produces = "application/json")
    public void getBoardById(){
        System.out.println("all allowed here ");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/hi",produces = "application/json")
    public void hi(){
        System.out.println("all allowed here ");
    }
}
