package br.com.bitewisebytes.simpletwittersecurity.user;

import br.com.bitewisebytes.simpletwittersecurity.role.Role;
import br.com.bitewisebytes.simpletwittersecurity.role.RoleDTO;
import br.com.bitewisebytes.simpletwittersecurity.role.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserReppository userReppository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(
            UserReppository userReppository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userReppository = userReppository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public ResponseEntity newUser(CreateUserDTO createUserDTO) {
       var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
       userReppository.findByUsername(createUserDTO.username())
               .ifPresent(user -> {
                   throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
               });
        var user = new User();
        user.setUsername(createUserDTO.username());
        user.setPassword(bCryptPasswordEncoder.encode(createUserDTO.password()));
        user.setRoles(Set.of(basicRole));
        userReppository.save(user);
        return ResponseEntity.ok().build();
    }

    public List<UserDTO> getUsers() {
    return (List<UserDTO>) userReppository.findAll().stream()
            .map(user -> new UserDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail()
                    ));
    }

}
