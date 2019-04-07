package springservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import springservice.domain.Regions;
import springservice.domain.RegionsInf;
import springservice.domain.repository.RegionsInfRepository;
import springservice.domain.repository.RegionsRepository;

@RestController
public class UploadController {
	@Autowired
	RegionsInfRepository regionsInfRepository;

	@Autowired
	RegionsRepository regionsRepository;

	// Post 방식 파일 업로드
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFormPOST(MultipartFile file) throws Exception {

		List<RegionsInf> list = regionsInfRepository.findAll();

		if(list.size() > 0) {
			return "already load";
		}
		
		System.out.println("[uploadFormPOST start]");

		if (file != null) {
			System.out.println("originalName:" + file.getOriginalFilename());
			System.out.println("size:" + file.getSize());
			System.out.println("ContentType:" + file.getContentType());
		}

		// EUC-KR 이 아닐 경우 고려해야 함.
		String input = new String(file.getBytes(), "EUC-KR");

		try {
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

				regionsInfRepository
						.save(RegionsInf.builder().regions(regions).target(sLine[2]).usage(sLine[3]).slimit(sLine[4])
								.rate(sLine[5]).institute(sLine[6]).mgmt(sLine[7]).reception(sLine[8]).build());
			}
		} catch (Exception e) {
			
		}
		return "success";
	}
}
