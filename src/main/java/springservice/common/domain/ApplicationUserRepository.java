package springservice.common.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
	public Optional<ApplicationUser> findByUsername(String username);
	//ApplicationUser findByUsername(String username);
}
