    package vn.hcmute.appfood.configs;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import vn.hcmute.appfood.security.JwtAuthenticationFilter;

    import java.util.List;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        private static final List<String> PUBLIC_ENDPOINT = List.of("/api/auth/**", "/api/reviews/product", "/api/order/payment/vn-pay-callback",
                "/api/foods/**", "/api/categories/**", "/api/cart/**",
                "/api/order/*/shipping", "/api/order/*/delivered"//Này để test nên để public chứ admin
                , "/api/auth/user/shipping-address", "/api/order/payment/vn-pay" //Này tính sau
                );

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())// Tắt CSRF vì ứng dụng mobile dùng token
                    .authorizeHttpRequests(auth -> auth
                            //Phân quyền
                            .requestMatchers("/api/slider/**", "/api/foods/add", "/api/foods/update/*", "/api/categories/add", "/api/categories/update/*").hasRole("ADMIN")
                            .requestMatchers("/api/reviews/submit", "/api/order/create-order", "/api/order/*/details", "/api/order",  "/api/order/*/confirm",
                                    "/api/order/*/cancel",  "/api/cart/get", "/api/cart/add", "/api/cart/update", "/api/auth/cart/delete", "/api//user/add-address", "/api/auth/user/shipping-address",
                                    "/api/auth/update").hasAnyRole("ADMIN", "USER")
                            // Cho phép truy cập công khai các endpoint
                            .requestMatchers(PUBLIC_ENDPOINT.toArray(new String[0])).permitAll()
                            //Còn lại cần xac thuc
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);// Thêm filter ở đây
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
