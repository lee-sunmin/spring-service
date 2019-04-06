package springservice.common.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Setter;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Table(name = "APP_USER")
public class ApplicationUser {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String username;

	private String password;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "APP_USER_ID", referencedColumnName = "ID")
	private List<UserRole> roles;

	public ApplicationUser() {
		
	}

	public ApplicationUser(Long id, String username, String password, List<UserRole> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<UserRole> getRoles() {
		return roles;
	}
}
