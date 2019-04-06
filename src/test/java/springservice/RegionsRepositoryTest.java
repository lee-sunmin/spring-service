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
import springservice.domain.RegionsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionsRepositoryTest {
	@Autowired
	RegionsRepository regionsRepository;

	@After
	public void cleanup() {
		/**
		 * 이후 테스트 코드에 영향을 끼치지 않기 위해 테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
		 **/
		regionsRepository.deleteAll();
	}

	@Test
	public void contextLoads() {
		// given
		regionsRepository.save(Regions.builder().name("수원시").build());

		// when
		List<Regions> regionsList = regionsRepository.findAll();

		// then
//		Regions regions = regionsList.get(0);
//		assertThat(regions.getCode(), is(1L));
//		assertThat(regions.getName(), is("수원시"));
	}
}
