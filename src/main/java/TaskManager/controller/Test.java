package TaskManager.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class Test {
    @GetMapping(value ="/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public void getBoardById(@PathVariable("id") int id, @RequestParam int boardId){
        System.out.println("all allowed here ");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/hi",produces = "application/json")
    public void hi(){
        System.out.println("all allowed here ");
    }
}
