package springservice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import springservice.domain.Regions;
import springservice.domain.RegionsInf;
import springservice.domain.RegionsInfRepository;
import springservice.domain.RegionsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionsInfRepositoryTest {
	@Autowired
	RegionsInfRepository regionsInfRepository;

	@Autowired
	RegionsRepository regionsRepository;

	@After
	public void cleanup() {
		/**
		 * 이후 테스트 코드에 영향을 끼치지 않기 위해 테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
		 **/
		regionsInfRepository.deleteAll();
	}

	@Test
	public void contextLoads() {
		// given
		regionsRepository.save(Regions.builder().name("강릉시").build());
		Regions regions = regionsRepository.findByname("강릉시");

		regionsInfRepository.save(RegionsInf.builder().regions(regions).target("강릉시 소재 중소기업으로서 강릉시장이 추천한 자").usage("운전")
				.slimit("추천금액 이내").rate("3%").institute("강릉시").mgmt("강릉지점").reception("강릉시 소재 영업점").build());

		// when
		List<RegionsInf> regionsInfList = regionsInfRepository.findAll();

		// then
//		RegionsInf regionsInf = regionsInfList.get(0);
//		assertThat(regionsInf.getId(), is(1L));
//		assertThat(regionsInf.getRegions().getCode(), is(1L));
//		assertThat(regionsInf.getTarget(), is("강릉시 소재 중소기업으로서 강릉시장이 추천한 자"));
//		assertThat(regionsInf.getUsage(), is("운전"));
//		assertThat(regionsInf.getSlimit(), is("추천금액 이내"));
//		assertThat(regionsInf.getRate(), is("3%"));
//		assertThat(regionsInf.getInstitute(), is("강릉시"));
//		assertThat(regionsInf.getMgmt(), is("강릉지점"));
//		assertThat(regionsInf.getReception(), is("강릉시 소재 영업점"));
	}

}
