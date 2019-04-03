package springservice.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import springservice.domain.RegionsInf;
import springservice.domain.RegionsInfRepository;
import springservice.domain.RegionsRepository;

@RestController
@AllArgsConstructor
public class SelectController {
	@Autowired
	RegionsInfRepository regionsInfRepository;

	@Autowired
	RegionsRepository regionsRepository;

	@RequestMapping("/list")
	public String selectList() {
		List<RegionsInf> regionsInfList = regionsInfRepository.findAll();
		String result = "[";

		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = "";

			for (int i = 0; i < regionsInfList.size(); i++) {

				Map<String, Object> map = new HashMap<String, Object>();
				RegionsInf regionsInf = regionsInfList.get(i);
				String region = regionsRepository.findBycode(regionsInf.getRegid()).getName();

				map.put("region", region);
				map.put("target", regionsInf.getTarget());
				map.put("usage", regionsInf.getUsage());
				map.put("limit", regionsInf.getSlimit());
				map.put("institute", regionsInf.getInstitute());
				map.put("mgmt", regionsInf.getMgmt());
				map.put("reception", regionsInf.getReception());
				map.put("rate", regionsInf.getRate());

				json = mapper.writeValueAsString(map);
				json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

				result += json + ",\n";
			}
			result = result.substring(0, result.length() - 2);
			result += "]";

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/select")
	public String selectRegionInf(@RequestParam String region) {
		String json = "";
		try {
			Long code = regionsRepository.findByname(region).getCode();

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = new HashMap<String, Object>();

			RegionsInf regionsInf = regionsInfRepository.findOne(code);

			map.put("region", region);
			map.put("target", regionsInf.getTarget());
			map.put("usage", regionsInf.getUsage());
			map.put("limit", regionsInf.getSlimit());
			map.put("institute", regionsInf.getInstitute());
			map.put("mgmt", regionsInf.getMgmt());
			map.put("reception", regionsInf.getReception());
			map.put("rate", regionsInf.getRate());

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
