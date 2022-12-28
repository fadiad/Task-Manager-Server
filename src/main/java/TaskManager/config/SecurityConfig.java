package TaskManager.config;


import TaskManager.filters.JWTAuthenticationFilter;
import TaskManager.filters.PermissionFilter;
import TaskManager.service.PermissionService;
import TaskManager.utils.jwtUtils.JWTTokenHelper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@EnableWebMvc
public class SecurityConfig {

    private final UserDetailsService myUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String OPTIONS="OPTIONS";
    private final JWTTokenHelper jWTTokenHelper;

    @Autowired
    private  final AuthenticationEntryPoint authenticationEntryPoint;

    private final PermissionService permissionService;

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        return new ProviderManager(authenticationProvider);
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.sessionManagement().sessionCreationPolicy(STATELESS).and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().authorizeRequests((request) -> request.antMatchers( "/auth/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated())
                .addFilterBefore(new JWTAuthenticationFilter(myUserDetailsService, jWTTokenHelper), UsernamePasswordAuthenticationFilter.class).
                 addFilterAfter(new PermissionFilter(requestMappingHandlerMapping(),permissionService), JWTAuthenticationFilter.class);
        http.csrf().disable().headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }





    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods(GET, POST, PUT, DELETE,OPTIONS)
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };
    }
}