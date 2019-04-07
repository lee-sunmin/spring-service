package springservice.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
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
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.AllArgsConstructor;
import springservice.domain.RegionsInf;
import springservice.domain.RegionsNode;
import springservice.domain.repository.RegionsInfRepository;
import springservice.domain.repository.RegionsRepository;

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
				String region = regionsRepository.findBycode(regionsInf.getId()).getName();

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

	@RequestMapping("/sort")
	public String selectLimitSortedRegions(@RequestParam int num) {
		String json = "";
		try {
			List<RegionsInf> regionsInfList = regionsInfRepository.findAll();

			RegionsNode[] RegionsNodes = new RegionsNode[regionsInfList.size()];

			// for
			for (int i = 0; i < regionsInfList.size(); i++) {

				RegionsInf regionsInf = regionsInfList.get(i);

				RegionsNode node = new RegionsNode();

				String name = regionsRepository.findBycode(regionsInf.getRegions().getCode()).getName();
				node.setName(name);

				String slimit = regionsInf.getSlimit();
				String value = "";
				String unit = "";

				for (int j = 0; j < slimit.length(); j++) {
					char ch = slimit.charAt(j);
					if (ch >= '0' && ch <= '9') {
						value += ch;
					} else if (ch == '원') {
						break;
					} else {
						unit += ch;
					}
				}

				if (value == "") {
					node.setDlimit(-1);
					node.setAveRate(-1);
				} else {
					double dval = Double.parseDouble(value);
					if (unit.equals("백만")) {
						dval = dval / 100;
					}
					node.setDlimit(dval);

					String rate = regionsInf.getRate();
					String[] values = new String[2];
					int idx = 0;
					values[0] = "";
					for (int j = 0; j < rate.length(); j++) {
						char ch = rate.charAt(j);
						if (ch >= '0' && ch <= '9' || ch == '.') {
							values[idx] += ch;
						} else if (ch == '~') {
							idx++;
							values[idx] = "";
						}
					}

					double avgRate;
					if (values[0] == "" || values[0] == null) {
						node.setAveRate(-1);
					} else {
						if (idx == 0) {
							avgRate = Double.parseDouble(values[0]);
						} else {
							avgRate = (Double.parseDouble(values[0]) + Double.parseDouble(values[1])) / 2;
						}
						node.setAveRate(avgRate);
					}
				}
				RegionsNodes[i] = node;
				// end for
			}

			// sort
			System.out.println("load success.");

			Arrays.sort(RegionsNodes, new Comparator<RegionsNode>() {
				public int compare(RegionsNode n1, RegionsNode n2) {
					double n1Dlimit = n1.getDlimit();
					double n2Dlimit = n2.getDlimit();
					if (n1Dlimit == n2Dlimit) {
						return Double.compare(n2.getAveRate(), n1.getAveRate());
					}
					return Double.compare(n2Dlimit, n1Dlimit);
				}
			});

			// print nodes
			 for (int k = 0; k < RegionsNodes.length; k++) {
			 RegionsNode t = RegionsNodes[k];
				System.out.println(t.getName());
				System.out.println(t.getDlimit());
				System.out.println(t.getAveRate());
			 }

			/// json print
			// createArrayNode
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode arrNode = mapper.createArrayNode();
			Map<String, Object> map = new HashMap<String, Object>();

			for (int k = 0; k < num; k++) {
				arrNode.add(RegionsNodes[k].getName());
			}

			map.put("Regions", arrNode);
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

	@RequestMapping("/minRate")
	public String selectMinRate() {
		String json = "";

		try {
			List<RegionsInf> regionsInfList = regionsInfRepository.findAll();
			RegionsNode[] regionsNodes = new RegionsNode[regionsInfList.size()];

			for (int i = 0; i < regionsInfList.size(); i++) {
				RegionsNode node = new RegionsNode();

				//node.setName(regionsRepository.findBycode(regionsInfList.get(i).getRegions().getCode()).getName());
				node.setCode(regionsInfList.get(i).getRegions().getCode());
				
				String rate = regionsInfList.get(i).getRate();
				String value = "";

				for (int j = 0; j < rate.length(); j++) {
					char ch = rate.charAt(j);
					if (ch >= '0' && ch <= '9' || ch == '.') {
						value += ch;
					} else if (ch == '~') {
						break;
					}
				}

				if (value != "") {
					node.setRate(Double.parseDouble(value));
				} else {
					node.setRate(Double.MAX_VALUE);
				}
				regionsNodes[i] = node;

				// end for
			}

			Arrays.sort(regionsNodes, new Comparator<RegionsNode>() {
				public int compare(RegionsNode n1, RegionsNode n2) {
					double n1Dlimit = n1.getRate();
					double n2Dlimit = n2.getRate();

					return Double.compare(n1Dlimit, n2Dlimit);
				}
			});

			ObjectMapper mapper = new ObjectMapper();

			Map<String, Object> map = new HashMap<String, Object>();
			Long code = regionsNodes[0].getCode();
			map.put("institute", regionsInfRepository.findByregions(regionsRepository.findBycode(code)).getInstitute());
			
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
