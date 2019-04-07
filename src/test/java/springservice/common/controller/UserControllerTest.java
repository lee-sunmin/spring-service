package springservice.common.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import springservice.common.domain.ApplicationUser;
import springservice.common.domain.ApplicationUserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Test
	public void testSignUp() {
		ApplicationUser user = new ApplicationUser();
		user.setUsername("admin");
		user.setPassword("password");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		applicationUserRepository.save(user);

		List<ApplicationUser> list = applicationUserRepository.findAll();

		assertThat(list.size(), is(1));
	}

}
