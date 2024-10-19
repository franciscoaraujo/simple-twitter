package br.com.bitewisebytes.simpletwittersecurity.user;


import br.com.bitewisebytes.simpletwittersecurity.role.RoleDTO;

import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id, String userName, String email) {

}
