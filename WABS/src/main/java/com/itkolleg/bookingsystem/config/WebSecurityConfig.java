package com.itkolleg.bookingsystem.config;

import com.itkolleg.bookingsystem.service.employee.EmployeeService;
import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;

/**
 * Konfigurationsklasse für die Sicherheitseinstellungen.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final EmployeeService employeeService;

    /**
     * Konstruktor der WebSecurityConfig-Klasse.
     *
     * @param employeeService     Der EmployeeService.
     */
    public WebSecurityConfig(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Bean zum Verschlüsseln des Passworts mit dem bcrypt-Algorithmus.
     *
     * @return das BCryptPasswordEncoder-Objekt.
     */
    @Bean
    public BCryptPasswordEncoder bCryptpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfiguriert die Sicherheitseinstellungen für die Http-Requests.
     *
     * @param http Die HttpSecurity-Instanz.
     * @return die SecurityFilterChain-Instanz.
     * @throws Exception wenn Fehler bei der Konfiguration auftreten.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers()
                .contentTypeOptions()
                .disable()
                .and()
                .authorizeHttpRequests(authConfig -> {

                    authConfig.requestMatchers(HttpMethod.GET, "/web/login","/error", "/web/login-error", "/web/logout", "/static/**", "/templates/**","/webjars/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/web/login","/static/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/web/deskbookings/mydeskbookings", "/web/deskbookings/view/**", "/web/deskbookings/new/**","/web/deskbookings/update/**","/web/deskbookings/deskbookinghistory/**","/web/deskbookings/cancel/**","/web/user/start", "/web/ressourceBooking/allBookingsEmployee", "/web/ressourceBooking/createBookingEmployee/**", "/web/ressourceBooking/deleteBookingEmployee/**","/web/deskbookings/mydeskbookings","/web/ressource/allRessourcesEmployee", "/web/rooms/allRoomsEmployee", "/web/roomBooking/allBookingsEmployee","/web/roomBooking/createBookingEmployee/**").hasAnyRole("ADMIN", "OPERATOR", "N_EMPLOYEE", "P_EMPLOYEE");
                    authConfig.requestMatchers(HttpMethod.GET,"/web/**","/web/desks/**","web/deskbookings/**","web/deskbookings/admin/**").hasAnyRole("ADMIN", "OPERATOR");
                    authConfig.requestMatchers(HttpMethod.POST,  "web/deskbookings/**","/web/deskbookings/add","/web/deskbookings/new","/web/deskbookings/update","/web/deskbookings/cancel/**","/web/ressource/**", "/web/roomBooking/**", "/web/ressourceBooking/**", "/web/rooms/**","/web/roomBooking/createBookingEmployee/**","/web/roomBooking/updateBooking/**").hasAnyRole( "ADMIN", "OPERATOR","N_EMPLOYEE", "P_EMPLOYEE");
                    authConfig.requestMatchers(HttpMethod.POST, "/web/**","/web/desks/**","web/deskbookings/admin/**").hasAnyRole("ADMIN", "OPERATOR");

                })
                .formLogin(login -> login.loginPage("/web/login")
                        .failureHandler((request, response, exception) -> {
                            String errorMessage = "Falsche Anmeldeinformationen.";
                            response.sendRedirect("/web/login-error" + errorMessage);
                        })
                        .successHandler((request, response, authentication) -> {
                            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                            if (roles.contains("ROLE_ADMIN")) {
                                response.sendRedirect("/web/admin/admin-start");
                            } else if (roles.contains("ROLE_OPERATOR")) {
                                response.sendRedirect("/web/admin/admin-start");
                            } else if (roles.contains("ROLE_N_EMPLOYEE")) {
                                response.sendRedirect("/web/user/start");
                            } else if (roles.contains("ROLE_P_EMPLOYEE")) {
                                response.sendRedirect("/web/user/start");
                            } else {
                                response.sendRedirect("/web/login-error");
                            }
                        }))
                .logout(logout -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                    logout.logoutSuccessUrl("/web/login");
                    logout.deleteCookies("JSESSIONID");
                    logout.invalidateHttpSession(true);
                });
        return http.build();
    }

    /**
     * Bean für den AuthenticationManager.
     *
     * @param httpSecurity          Die HttpSecurity-Instanz.
     * @param userDetailsService    Das UserDetailsService-Objekt.
     * @param bCryptPasswordEncoder Das BCryptPasswordEncoder-Objekt.
     * @return die AuthenticationManager-Instanz.
     * @throws Exception wenn Fehler bei der Konfiguration auftreten.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Bean für den UserDetailsService.
     *
     * @param bCryptPasswordEncoder Das BCryptPasswordEncoder-Objekt.
     * @return das UserDetailsService-Objekt.
     */
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return username -> {
            Employee employee = employeeService.getEmployeeByNick(username);
            if (employee == null) {
                throw new UsernameNotFoundException("Mitarbeiter mit dem Nick " + username + " nicht gefunden!");
            }
            return User.builder()
                    .username(employee.getNick())
                    .password(bCryptPasswordEncoder.encode(employee.getPassword()))
                    .authorities(employee.getAuthorities())
                    .build();
        };
    }
}