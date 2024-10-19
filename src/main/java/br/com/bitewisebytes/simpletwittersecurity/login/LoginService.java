package br.com.bitewisebytes.simpletwittersecurity.login;

import br.com.bitewisebytes.simpletwittersecurity.role.Role;
import br.com.bitewisebytes.simpletwittersecurity.user.UserReppository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final JwtEncoder jwtEncoder;
    private UserReppository userReppository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginService(
            JwtEncoder jwtEncoder,
            UserReppository userReppository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.jwtEncoder = jwtEncoder;
        this.userReppository = userReppository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        var user = userReppository.findByUsername(loginRequestDTO.username());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequestDTO, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get()
                .getRoles()
                .stream()
                .map(Role::getName).collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("simple-twitter")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponseDTO(jwtToken,expiresIn));
    }
}
