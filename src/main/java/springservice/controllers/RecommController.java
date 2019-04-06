package springservice.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommController {
	static String KEY = "AIzaSyDwb7v4ZRUlj_Z7AM8y8joxFaR2NXZgg5c";

	@RequestMapping("/recommend")
	public static void testGeoCoding() {
		try {
			// sendGet("https://maps.googleapis.com/maps/api/geocode/json?address=“보령시”&key=AIzaSyDwb7v4ZRUlj_Z7AM8y8joxFaR2NXZgg5c");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private static void sendGet(String targetUrl) throws Exception {
		URL url = new URL(targetUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET"); // optional default is GET
		// con.setRequestProperty("User-Agent", USER_AGENT); // add request header
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close(); // print result
		System.out.println("HTTP 응답 코드 : " + responseCode);
		System.out.println("HTTP body : " + response.toString());
	}

}
