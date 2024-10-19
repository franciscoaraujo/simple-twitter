package br.com.bitewisebytes.simpletwittersecurity.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.config.Customizer.withDefaults;

/*O Spring Security coloca interceptadores para capturar requisições e verificar a segurança*/

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //Habilita a segurança por anotação
public class SecurityConfig {
    
    @Value("${jwt.public.key}")
    private RSAPublicKey rsaPublicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey rsaPrivateKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers( HttpMethod.POST,"/users").permitAll()
                        .requestMatchers( HttpMethod.POST,"/login").permitAll()

                        .anyRequest().authenticated())//Todas as requisições devem ser autenticadas
                .csrf(csrf -> csrf.disable())//Desabilita o CSRF em producao deve ser habilitado
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))//Configuração do servidor de recursos
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Não cria sessão
                .cors(cors -> cors.disable())//Desabilita o CORS
        ;

       return http.build();
    }

    /**
     * criando um decoder apartir da chave publica, vai fazer a descodificação do JWT quando receber na requisicao
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    /**
     * criando um encoder apartir da chave privada, vai fazer a codificação do JWT quando enviar na resposta
     * @return
     */
    @Bean
    public JwtEncoder jwtEncoder() {
       JWK jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
       var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
       return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder byCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

/*Proximo passa Gerar as chaves publicas e privadas to token JWT
* # Gerar chave privada RSA
openssl genrsa -out private_key.pem 2048
*
# Extrair chave pública da chave privada
openssl rsa -in private_key.pem -pubout -out public_key.pem

# Para visualizar a chave privada
openssl rsa -in private_key.pem -text -noout

# Para visualizar a chave pública
 openssl rsa -in public_key.pem -pubin -text -noout
*
* */