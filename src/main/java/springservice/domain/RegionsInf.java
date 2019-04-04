package springservice.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "REGIONS_INF")
public class RegionsInf {// extends BaseTimeEntity {

	// @GeneratedValue
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", insertable = true, updatable = true, unique = true, nullable = false)
	private Long id;

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

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createDateTime;

	@Column
	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	@Builder
	public RegionsInf(Long id, Regions regions, String target, String usage, String slimit, String rate,
			String institute, String mgmt, String reception) {
		this.id = id;
		this.regions = regions;
		this.target = target;
		this.usage = usage;
		this.slimit = slimit;
		this.rate = rate;
		this.institute = institute;
		this.mgmt = mgmt;
		this.reception = reception;
	}
}
