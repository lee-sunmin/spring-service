package springservice.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import springservice.domain.Regions;
import springservice.domain.RegionsInf;
import springservice.domain.repository.RegionsInfRepository;
import springservice.domain.repository.RegionsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UploadControllerTest {

	@Autowired
	RegionsInfRepository regionsInfRepository;

	@Autowired
	RegionsRepository regionsRepository;

	@Test
	public void testUploadFormPOST() throws IOException {
		File file = new File("test/2019.csv");
		Path path = Paths.get(file.getAbsolutePath());
		byte[] data = Files.readAllBytes(path);

		String input = new String(data, "EUC-KR");

		String[] token = input.split("[\r\n\t]");
		int size = token[0].split(",").length;

		for (int i = 1; i < token.length; i++) {
			String line = token[i];
			String[] sLine = new String[size];
			int idx = 0;
			for (int j = 0; j < line.length(); j++) {
				if (line.charAt(j) == '\"') {
					j++;
					if (sLine[idx] == null) {
						sLine[idx] = "";
					}
					while (line.charAt(j) != '\"') {
						sLine[idx] += line.charAt(j);
						j++;
					}
				} else if (line.charAt(j) == ',') {
					idx++;
				} else {
					if (sLine[idx] == null) {
						sLine[idx] = "";
					}
					sLine[idx] += line.charAt(j);
				}
			}

			regionsRepository.save(Regions.builder().name(sLine[1]).build());
			Regions regions = regionsRepository.findByname(sLine[1]);
			regionsInfRepository.save(RegionsInf.builder().regions(regions).target(sLine[2]).usage(sLine[3])
					.slimit(sLine[4]).rate(sLine[5]).institute(sLine[6]).mgmt(sLine[7]).reception(sLine[8]).build());
		}

		List<RegionsInf> list = regionsInfRepository.findAll();

		assertThat(list.size(), is(98));

	}
}
