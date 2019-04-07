package springservice.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springservice.domain.Regions;
import springservice.domain.RegionsInf;
import springservice.domain.RegionsInfUpdateRequestDto;
import springservice.domain.repository.RegionsInfRepository;
import springservice.domain.repository.RegionsRepository;

@RestController
public class UpdateController {
	@Autowired
	RegionsInfRepository regionsInfRepository;

	@Autowired
	RegionsRepository regionsRepository;

	@PostMapping("/update")
	public String updateRegion(@RequestBody RegionsInfUpdateRequestDto dto) {
		Regions regions = regionsRepository.findByname(dto.getRegion());

		RegionsInf regInf = regionsInfRepository.findByregions(regions);

		dto.setId(regInf.getId());
		dto.setRegions(regions);

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
		if (dto.getReception() == null) {
			dto.setReception(regInf.getReception());
		}
		if (dto.getUsage() == null) {
			dto.setUsage(regInf.getUsage());
		}
		if (dto.getTarget() == null) {
			dto.setTarget(regInf.getTarget());
		}

		regionsInfRepository.save(dto.toEntity());

		String json = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("region", dto.getRegion());
			map.put("target", dto.getTarget());
			map.put("usage", dto.getUsage());
			map.put("limit", dto.getLimit());
			map.put("institute", dto.getInstitute());
			map.put("mgmt", dto.getMgmt());
			map.put("reception", dto.getReception());
			map.put("rate", dto.getRate());

			json = mapper.writeValueAsString(map);
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}
}
