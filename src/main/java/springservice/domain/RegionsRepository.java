package springservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionsRepository extends JpaRepository<Regions, Long> {
	Regions findByname(String name);
	Regions findBycode(Long code);
}
