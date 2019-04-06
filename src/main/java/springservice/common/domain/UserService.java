package springservice.common.domain;

import java.util.Optional;


public interface UserService {
	  public Optional<ApplicationUser> getByUsername(String username);
}
