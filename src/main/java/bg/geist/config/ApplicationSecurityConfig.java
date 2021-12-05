package bg.geist.config;

import bg.geist.exception.CustomAccessDeniedHandler;
import bg.geist.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    public ApplicationSecurityConfig(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, CustomAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.
            authorizeRequests()
                // allow access to static resources to anyone // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/assets/**", "/css/**", "/js/**").permitAll()
                // allow access to cards and quiz by id
                .antMatchers(HttpMethod.GET,"/cards/{id:\\d+}", "/quizzes/{id:\\d+}", "/maps/{id:\\d+}").permitAll()
                // allow access to index, user login and registration to anyone
                .antMatchers("/", "/cards", "/quizzes", "/maps", "/error", "/users/login", "/users/register").permitAll()
                // allow GET requests to api and swagger
                .antMatchers(HttpMethod.GET,"/api", "/v2/api-docs").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/**/certification").authenticated()
//                .antMatchers(HttpMethod.GET,"/api/**").permitAll()
            .and()
                .authorizeRequests()
                .antMatchers("/client", "/home", "/logout").authenticated()
                .antMatchers("/users/profiles/{id}").access("@guard.isAuthorized(authentication,#id)")
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                // protect all other pages
                .antMatchers("/**").authenticated()
            .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
            .and()
                .formLogin()
                // our login page will be served by the controller with mapping /users/login
                .loginPage("/users/login")
                // the name of the user name input field in OUR login form is username (optional)
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                // the name of the user password input field in OUR login form is password (optional)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                // on login success redirect here
                .defaultSuccessUrl("/client", true)
                // on login failure redirect here
                .failureForwardUrl("/users/login-error")
            .and()
                .logout()
                // which endpoint performs logout, e.g. http://localhost:8080/logout (!this should be POST request)
                .logoutUrl("/logout")
                // where to land after logout
                .logoutSuccessUrl("/?username=")
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
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}