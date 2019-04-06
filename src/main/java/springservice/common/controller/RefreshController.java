package springservice.common.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import springservice.common.domain.ApplicationUser;
import springservice.common.domain.RawAccessJwtToken;
import springservice.common.domain.UserService;
import springservice.common.security.JwtSettings;
import springservice.common.security.JwtToken;
import springservice.common.security.JwtTokenFactory;
import springservice.common.security.RefreshToken;
import springservice.common.security.UserContext;
import springservice.common.security.auth.jwt.extractor.TokenExtractor;
import springservice.common.security.config.WebSecurityConfig;
import springservice.common.security.exceptions.InvalidJwtToken;

@RestController
public class RefreshController {

	@Autowired
	private JwtTokenFactory tokenFactory;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private JwtSettings jwtSettings;
	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("jwtHeaderTokenExtractor")
	private TokenExtractor tokenExtractor;

	@RequestMapping(value = "/api/auth/token", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

		RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
		RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey())
				.orElseThrow(() -> new InvalidJwtToken());

		String subject = refreshToken.getSubject();
		ApplicationUser user = userService.getByUsername(subject)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
				.collect(Collectors.toList());

		UserContext userContext = UserContext.create(user.getUsername(), authorities);

		JwtToken newAccessToken = tokenFactory.createAccessJwtToken(userContext);
		JwtToken newRefreshToken = tokenFactory.createRefreshToken(userContext);

		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("token", newAccessToken.getToken());
		tokenMap.put("refreshToken", newRefreshToken.getToken());

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		mapper.writeValue(response.getWriter(), tokenMap);

		clearAuthenticationAttributes(request);
	}

	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
