package springservice.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import springservice.domain.RegionsInf;
import springservice.domain.RegionsNode;
import springservice.domain.repository.RegionsInfRepository;
import springservice.domain.repository.RegionsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class SelectControllerTest {

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
	public void testSelectList() {
		List<RegionsInf> list = regionsInfRepository.findAll();

		System.out.println(list.size());

		assertThat(list.size(), is(98));
	}

	@Test
	@Sql("insertAll.sql")
	public void testSelectRegionInf() {
		Long code = regionsRepository.findByname("광명시").getCode();

		RegionsInf regionsInf = regionsInfRepository.findOne(code);

		assertThat(code, is(9L));
		assertThat(regionsInf.getRegions().getName(), is("광명시"));
		assertThat(regionsInf.getSlimit(), is("3억원 이내"));
		assertThat(regionsInf.getMgmt(), is("광명지점"));
		assertThat(regionsInf.getRate(), is("2.00%"));
	}

	@Test
	@Sql("insertAll.sql")
	public void testSelectLimitSortedRegions() {
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

			System.out.println(node.getName());
			RegionsNodes[i] = node;
			// end for
		}

		// sort
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

		// if cnt = 3
		assertThat(RegionsNodes[0].getName(), is("경기도"));
		assertThat(RegionsNodes[1].getName(), is("제주도"));
		assertThat(RegionsNodes[2].getName(), is("국토교통부"));
	}

	@Test
	@Sql("insertAll.sql")
	public void testSelectMinRate() {
		List<RegionsInf> regionsInfList = regionsInfRepository.findAll();
		RegionsNode[] regionsNodes = new RegionsNode[regionsInfList.size()];

		for (int i = 0; i < regionsInfList.size(); i++) {
			RegionsNode node = new RegionsNode();

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

		Long code = regionsNodes[0].getCode();
		
		String institute = regionsInfRepository.findByregions(regionsRepository.findBycode(code)).getInstitute();
		assertThat(institute, is("금천구"));
	}

}
