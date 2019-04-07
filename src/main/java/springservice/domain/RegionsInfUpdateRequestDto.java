package springservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegionsInfUpdateRequestDto {

	private Long id;
	private Regions regions;
	// name
	private String region;
	private String target;
	private String usage;
	private String limit;
	private String rate;
	private String institute;
	private String mgmt;
	private String reception;

	public RegionsInf toEntity() {
		return RegionsInf.builder().id(id).regions(regions).target(target).usage(usage).slimit(limit).rate(rate)
				.institute(institute).mgmt(mgmt).reception(reception).build();
	}
}
