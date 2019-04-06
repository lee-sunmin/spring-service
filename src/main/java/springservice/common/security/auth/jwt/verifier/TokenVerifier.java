package springservice.common.security.auth.jwt.verifier;

public interface TokenVerifier {
	public boolean verify(String jti);

}
