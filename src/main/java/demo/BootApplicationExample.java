package demo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Configuration
//@ComponentScan
//@EnableAutoConfiguration
//...OR...
@SpringBootApplication
public class BootApplicationExample {

	private static final Logger LOG = LoggerFactory.getLogger(BootApplicationExample.class);


	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BootApplicationExample.class, args);

		LOG.info(" *** Beans provided by Spring Boot:");
 
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			LOG.info(" * " + beanName);
		}
	}

	@Component
	public static class Initializer implements ApplicationRunner {

		@SuppressWarnings("unused")
		private final PasswordEncoder passwordEncoder;

		@Autowired
		public Initializer(PasswordEncoder passwordEncoder) {
			this.passwordEncoder = passwordEncoder;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
			// TODO - This is the place to create new Application records!
		}
	}
}