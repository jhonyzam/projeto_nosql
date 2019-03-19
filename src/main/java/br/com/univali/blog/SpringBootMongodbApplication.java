package br.com.univali.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.univali.blog.models.User;
import br.com.univali.blog.services.UserService;

@SpringBootApplication
public class SpringBootMongodbApplication implements CommandLineRunner {

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMongodbApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		User user = userService.findByUsername("admin");
		if (user == null) {
			userService.save(new User("admin", passwordEncoder.encode("admin"), "admin@gmail.com", "DEUS", "ADMIN"));
		}
	}
}
