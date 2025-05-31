    package vn.hcmute.appfood.configs;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;

    import java.util.List;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        private static final List<String> PUBLIC_ENDPOINT = List.of("/api/auth/login",
                "/api/auth/sendOtp",
                "/api/auth/verifyOtp",
                "/api/auth/reset-password/request",
                "/api/auth/reset-password/verify",
                "/api/auth/reset-password/resend-otp");

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())// Tắt CSRF vì ứng dụng mobile dùng token
                    .authorizeHttpRequests(auth -> auth
                            // Cho phép truy cập công khai các endpoint
                            .requestMatchers(PUBLIC_ENDPOINT.toArray(new String[0])).permitAll()
                            //Còn lại cần xac thuc
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    );
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
    }
