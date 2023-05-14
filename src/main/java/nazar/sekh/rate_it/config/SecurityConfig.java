package nazar.sekh.rate_it.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("**/icon.png", "**/ava.png", "/css/**", "/js/**", "/img/**").permitAll()
                .antMatchers("/", "/home", "/index", "/register", "/login").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
//                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
//                .successForwardUrl("/successURL")
                .failureUrl("/login?error").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
                .and().exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryConfigurer() {
        return new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>();
    }

    //error-----------------
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth,
//                                AuthenticationProvider provider) throws Exception {
//        inMemoryConfigurer()
//                .withUser("admin")
//                .password("{noop}admin")
//                .authorities("ADMIN")
//                .and()
//                .configure(auth);
//        auth.authenticationProvider(provider);
//    }
}

