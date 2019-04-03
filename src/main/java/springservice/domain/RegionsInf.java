package springservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "REGIONS_INF")
public class RegionsInf extends BaseTimeEntity {
	@Id
	@GeneratedValue
	private Long regid;

	// FK
	@OneToOne
	@JoinColumn(name = "code", referencedColumnName = "code")
	private Regions regions;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String target;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String usage;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String slimit;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String rate;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String institute;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String mgmt;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String reception;

//	@Column
//	private LocalDateTime crtTm;
//
//	@Column
//	private LocalDateTime chgTm;

	@Builder
	public RegionsInf(Regions regions, String target, String usage, String slimit, String rate, String institute,
			String mgmt, String reception) {
		// this.region_cd = region_cd;
		this.regions = regions;
		this.target = target;
		this.usage = usage;
		this.slimit = slimit;
		this.rate = rate;
		this.institute = institute;
		this.mgmt = mgmt;
		this.reception = reception;
//		this.crtTm = crtTm;
//		this.chgTm = chgTm;
	}

}
