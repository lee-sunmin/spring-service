package springservice.common.domain;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class DatabaseUserService implements UserService {
	private final ApplicationUserRepository userRepository;

	@Autowired
	public DatabaseUserService(ApplicationUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public ApplicationUserRepository getUserRepository() {
		return userRepository;
	}

	@Override
	public Optional<ApplicationUser> getByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}
}
