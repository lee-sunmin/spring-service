package springservice.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import springservice.domain.Regions;
import springservice.domain.RegionsInf;
import springservice.domain.RegionsInfUpdateRequestDto;
import springservice.domain.repository.RegionsInfRepository;
import springservice.domain.repository.RegionsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class UpdateControllerTest {

	@Autowired
	RegionsInfRepository regionsInfRepository;

	@Autowired
	RegionsRepository regionsRepository;

	@After
	public void cleanup() {
		regionsRepository.deleteAll();
		regionsInfRepository.deleteAll();
	}

	@Test
	@Sql("insertAll.sql")
	public void testUpdateRegion() {
		RegionsInfUpdateRequestDto dto = new RegionsInfUpdateRequestDto();
		dto.setRegion("광명시");
		dto.setTarget("선민이");

		Regions regions = regionsRepository.findByname(dto.getRegion());
		RegionsInf regInf = regionsInfRepository.findByregions(regions);

		// null
		if (dto.getInstitute() == null) {
			dto.setInstitute(regInf.getInstitute());
		}
		if (dto.getLimit() == null) {
			dto.setLimit(regInf.getSlimit());
		}
		if (dto.getMgmt() == null) {
			dto.setMgmt(regInf.getMgmt());
		}
		if (dto.getRate() == null) {
			dto.setRate(regInf.getRate());
		}
		if(dto.getReception()==null) {
			dto.setReception(regInf.getReception());
		}
		if(dto.getUsage()==null) {
			dto.setUsage(regInf.getUsage());
		}
		if(dto.getTarget()==null) {
			dto.setTarget(regInf.getTarget());
		}

		dto.setId(regInf.getId());
		dto.setRegions(regions);

		regionsInfRepository.save(dto.toEntity());

		// test
		regInf = regionsInfRepository.findByregions(regions);

		assertThat(regInf.getTarget(), is("선민이"));
	}

}
