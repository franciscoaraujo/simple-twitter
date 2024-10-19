package br.com.bitewisebytes.simpletwittersecurity.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

     @PostMapping("/users")
     public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.newUser(createUserDTO);
     }

     @GetMapping("/users/all")
     @PreAuthorize("hasAuthority('SCOPE_ADMIN')")//Habilitado no SecurityConfig com a anotação @@EnableMethodSecurity
     public ResponseEntity<List<UserDTO>> getUsers() {
         return ResponseEntity.ok(userService.getUsers());
     }

}
