package springservice.common.security.model.token;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import springservice.common.domain.RawAccessJwtToken;
import springservice.common.security.auth.JwtAuthenticationToken;
import springservice.common.security.auth.jwt.JwtAuthenticationProvider;
import springservice.common.security.config.JwtSettings;
import springservice.common.security.model.UserContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenFactoryTest {

	@Autowired
	JwtSettings settings;
	@Autowired
	JwtAuthenticationProvider provider;
	@Autowired
	JwtTokenFactory factory;
	
	@Test
	public void testCreateAccessJwtToken() {
		UserContext temp = UserContext.create("sunmin", new ArrayList<GrantedAuthority>());
		
		AccessJwtToken token = factory.createAccessJwtToken(temp);
		RawAccessJwtToken jwtToken = new RawAccessJwtToken(token.getToken());
		Authentication a = provider.authenticate(new JwtAuthenticationToken(jwtToken));
		
		assertTrue(a.isAuthenticated());
	}

	@Test
	public void testCreateRefreshToken() {
		UserContext temp = UserContext.create("sunmin", new ArrayList<GrantedAuthority>());
		
		JwtToken token = factory.createRefreshToken(temp);
		RawAccessJwtToken jwtToken = new RawAccessJwtToken(token.getToken());
		Authentication a = provider.authenticate(new JwtAuthenticationToken(jwtToken));
		
		assertTrue(a.isAuthenticated());
	}

}
