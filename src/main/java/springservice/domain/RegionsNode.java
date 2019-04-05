package springservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegionsNode {
	private String name;
	private double dlimit;
	private double aveRate;
	
	private String code;
	private double rate;
}
