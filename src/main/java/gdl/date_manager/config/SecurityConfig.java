package gdl.date_manager.config;


import gdl.date_manager.data.UserRepository;
import gdl.date_manager.model.UserModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;
    public SecurityConfig(UserRepository userRepository) { this.userRepository = userRepository; }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserModel u = userRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            return User.builder()
                    .username(u.getUserName())
                    .password(u.getPassword()) // já codificada com BCrypt
                    .roles(u.getRole().replace("ROLE_", "")) // ROLE_USER -> USER
                    .disabled(!u.isEnabled())
                    .build();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public DaoAuthenticationProvider authProvider(UserDetailsService uds, BCryptPasswordEncoder enc) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(enc);
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**","/js/**","/img/**","/register","/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/products", true)
                )
                .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")); // se usar APIs REST separadas

        return http.build();
    }
}

