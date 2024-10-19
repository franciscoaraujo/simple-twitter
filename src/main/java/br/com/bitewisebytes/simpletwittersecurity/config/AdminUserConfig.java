package br.com.bitewisebytes.simpletwittersecurity.config;

import br.com.bitewisebytes.simpletwittersecurity.role.Role;
import br.com.bitewisebytes.simpletwittersecurity.role.RoleRepository;
import br.com.bitewisebytes.simpletwittersecurity.user.User;
import br.com.bitewisebytes.simpletwittersecurity.user.UserReppository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Slf4j
@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleReppository;
    private UserReppository userReppository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityConfig securityConfig;

    public AdminUserConfig(
            RoleRepository roleReppository,
            UserReppository userReppository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            SecurityConfig securityConfig
    ) {

        this.roleReppository = roleReppository;
        this.userReppository = userReppository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityConfig = securityConfig;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin =  roleReppository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userReppository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                (user) -> {
                    log.info("Admin user already exists");
                },
                ()->{
                    var adminUser = new User();
                    adminUser.setUsername("admin");
                    adminUser.setPassword(bCryptPasswordEncoder.encode("admin"));
                    adminUser.setRoles(Set.of(roleAdmin));
                    userReppository.save(adminUser);
                    log.info("Admin user created");
                }
        );
    }
}
