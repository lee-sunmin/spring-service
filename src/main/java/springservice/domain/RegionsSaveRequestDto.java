package springservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegionsSaveRequestDto {
	private String name;

	public Regions toEntity() {
		return Regions.builder().name(name).build();
	}
}
