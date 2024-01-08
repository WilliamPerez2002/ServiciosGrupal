    package com.example.demo.securityconfig;

    import com.example.demo.Controllers.ApiUser;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;


    @Configuration
    @EnableWebSecurity
    public class SecurityConfig  {

        @Autowired
        private ApiUser apiUser;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }



        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsService() {
                @Override
                public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


                    com.example.demo.model.User user = apiUser.searchbyUserName(username);


                    if (user == null) {

                        throw new UsernameNotFoundException("No existe: " + username);

                    }

                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String hashedPassword = encoder.encode(user.getPassword());



                    return org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(hashedPassword)
                            .roles(user.getRol())
                            .build();
                }
            };
        }






        @Bean
        SecurityFilterChain security(HttpSecurity http) throws Exception {
            return http.csrf().disable()

                    .formLogin(form -> form
                            .loginPage("/login")
                            .usernameParameter("username").passwordParameter("password")
                            .permitAll()

                            .defaultSuccessUrl("/usuarios", true)
                            .failureUrl("/login?x")
                            .successHandler((request, response, authentication) -> {

                                String role = authentication.getAuthorities().iterator().next().getAuthority();


                                if ("ROLE_ADMIN".equals(role)) {
                                    response.sendRedirect("/");
                                } else {
                                    response.sendRedirect("/usuarios");
                                }
                            })
                    )

                    .authorizeRequests(authorize ->
                            authorize
                                    .requestMatchers("/").hasAuthority("ROLE_ADMIN")
                                    .requestMatchers("/usuarios").hasAuthority("ROLE_USER")
                                    .anyRequest().authenticated()
                    )
                    .build();
        }










    }
