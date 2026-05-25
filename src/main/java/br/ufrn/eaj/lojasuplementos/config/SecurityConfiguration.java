package br.ufrn.eaj.lojasuplementos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails administrador = User.builder()
                .username("admin")
                .password(encoder.encode("senha123"))
                .roles("ADMIN")
                .build();

        UserDetails visitante = User.builder()
                .username("visitante")
                .password(encoder.encode("senha123"))
                .roles("VISITANTE")
                .build();

        // Entrega os dois para o Spring gerenciar
        return new InMemoryUserDetailsManager(administrador, visitante);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**").permitAll()

                        // RBAC: (Role-Based Access Control)
                        // Cadastro e Exclusão exclusivas do papel ROLE_ADMIN
                        .requestMatchers("/cadastro", "/deletar", "/restaurar", "/salvar", "/editar").hasRole("ADMIN")

                        // As demais rotas qualquer usuário logado tem acesso
                        .anyRequest().authenticated()
                )
                // Pagina de login padrão do Spring
                .formLogin(form -> form
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .build();
    }
}