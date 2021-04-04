package bg.geist.config;

import bg.geist.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.
            authorizeRequests()
                // allow access to static resources to anyone // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/assets/**", "/css/**", "/js/**").permitAll()
                // allow access to cards and quiz by id
                .antMatchers(HttpMethod.GET,"/cards/{id:\\d+}", "/quiz/{id:\\d+}").permitAll()
                // allow access to index, user login and registration to anyone
                .antMatchers("/", "/cards", "/quiz", "/games", "/error", "/users/login", "/users/register").permitAll()
                // allow GET requests to api and swagger
                .antMatchers(HttpMethod.GET,"/api", "/v2/api-docs").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/**/certification").hasAnyAuthority()
//                .antMatchers(HttpMethod.GET,"/api/**").permitAll()
                // protect all other pages
            .and()
                .authorizeRequests()
                .antMatchers("/users/profile").hasRole("USER")
                .antMatchers("/**").authenticated()
//            .and()
//                .exceptionHandling()
//                .accessDeniedPage("/forbidden")
            .and()
                .formLogin()
                // our login page will be served by the controller with mapping /users/login
                .loginPage("/users/login")
                // the name of the user name input field in OUR login form is username (optional)
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                // the name of the user password input field in OUR login form is password (optional)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                // on login success redirect here
                .defaultSuccessUrl("/home", true)
                // on login failure redirect here
                .failureForwardUrl("/users/login-error")
            .and()
                .logout()
                // which endpoint performs logout, e.g. http://localhost:8080/logout (!this should be POST request)
                .logoutUrl("/logout")
                // where to land after logout
                .logoutSuccessUrl("/")
                // remove the session from the server
                .invalidateHttpSession(true)
                // delete the session cookie
                .deleteCookies("JSESSIONID");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers().antMatchers("/api/**");
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}