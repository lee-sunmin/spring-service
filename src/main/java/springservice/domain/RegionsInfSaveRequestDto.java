package springservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegionsInfSaveRequestDto {
	private Regions regions;
	private String target;
	private String usage;
	private String slimit;
	private String rate;
	private String institute;
	private String mgmt;
	private String reception;

	public RegionsInf toEntity() {
		return RegionsInf.builder().regions(regions).target(target).usage(usage).slimit(slimit).rate(rate)
				.institute(institute).mgmt(mgmt).reception(reception).build();
	}
}
