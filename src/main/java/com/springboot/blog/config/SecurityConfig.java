package com.springboot.blog.config;


import com.springboot.blog.security.JwtAuthenticalFilter;
import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@SecurityScheme(
        name = "Bear Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticalFilter authenticalFilter;


    SecurityConfig(UserDetailsService userDetailsService,
                   JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                   JwtAuthenticalFilter authenticalFilter){
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.authenticalFilter = authenticalFilter;
    }
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeHttpRequests( (auth)->
////                auth.anyRequest().authenticated()
//                auth.requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/v1/categories/**").permitAll()
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        .requestMatchers("/v3/api-docs/**").permitAll()
//                        .requestMatchers("/swagger-ui/**").permitAll()
//                        .requestMatchers("/swagger-resources/**").permitAll()
//                        .requestMatchers("/swagger-ui.html").permitAll()
//                        .requestMatchers("/webjars/**").permitAll()
//
//                        .anyRequest().authenticated()
//        ).exceptionHandling( (ex)-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//                .sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic(Customizer.withDefaults());
//        http.addFilterBefore(authenticalFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()
                        authorize.requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/categories/**").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/index.html").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .anyRequest().authenticated()

                ).exceptionHandling( exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                ).sessionManagement( session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticalFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails rmsh = User.builder().username("rmsh")
//                .password(passwordEncoder().encode("rmsh")).roles("USER").build();
//        UserDetails admin = User.builder().username("root")
//                .password(passwordEncoder().encode("root")).roles("ADMIN").build();
//
//        return new InMemoryUserDetailsManager(rmsh,admin);
//    }


}
