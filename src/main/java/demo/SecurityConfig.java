package demo;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public ApplicationContext context;

//	@Autowired
//	private UserRepository userRepository;

	@Override
	public void configure(final WebSecurity web) throws Exception {
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String username = (String) authentication.getPrincipal();
				String providedPassword = (String) authentication.getCredentials();

//				User user = userRepository.findUserByUsername( username );

//				if (user == null || !passwordEncoder().matches(providedPassword, user.getPassword())) {
//					throw new BadCredentialsException("Username/Password does not match for " + username);
//				}

				return new UsernamePasswordAuthenticationToken(username, providedPassword, Collections.singleton(new SimpleGrantedAuthority("REGULAR_USER")));
			}

			@Override
			public boolean supports(Class<?> authentication) {
				return true;
			}
		});
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
			.csrf().disable() // TODO - Enable CSRF in PRODUCTION environment!
			.authorizeRequests()
				.antMatchers("/", "/login", "/404").permitAll()
				.antMatchers(HttpMethod.GET, "/events/**", "/agenda/**", "/talks/**").permitAll()
				.antMatchers(HttpMethod.POST, "/registrations/**", "/submissions/**").permitAll()
				// .antMatchers(HttpMethod.POST, "/talks/**").hasAuthority("SPEAKER")
				.antMatchers("/events/**", "/talks/**").hasAuthority("REGULAR_USER")
				// .antMatchers("/user/**").hasAuthority("USER") //will contain schedule and etc
				.anyRequest().authenticated()
				.and().httpBasic();

		// .and()
		// .formLogin()
		// .loginPage("/login")
		// .permitAll();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}