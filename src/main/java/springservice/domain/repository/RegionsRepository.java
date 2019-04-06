package springservice.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springservice.domain.Regions;

public interface RegionsRepository extends JpaRepository<Regions, Long> {
	Regions findByname(String name);
	Regions findBycode(Long code);
}