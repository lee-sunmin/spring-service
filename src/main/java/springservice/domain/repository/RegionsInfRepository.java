package springservice.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springservice.domain.Regions;
import springservice.domain.RegionsInf;

public interface RegionsInfRepository extends JpaRepository<RegionsInf, Long> {
	RegionsInf findByregions(Regions regions);
}