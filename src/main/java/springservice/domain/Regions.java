package springservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Regions {
	@Id
	@GeneratedValue
	private Long code;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String name;

	@Builder
	public Regions(String name) {
		this.name = name;
	}
}
