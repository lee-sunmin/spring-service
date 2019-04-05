package springservice.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;

@RestController
public class RecommController {

	@RequestMapping("/recommend")
	public static void testGeoCoding() {
		Float[] coords = geoCoding("충남 보령시");

		//System.out.println("충남 대천" + ": " + coords[0] + ", " + coords[1]);
		coords = geoCoding("충청남도 보령시");
		coords = geoCoding("보령시");
		System.out.println("충남 대천" + ": " + coords[0] + ", " + coords[1]);
	}
	
	public static Float[] geoCoding(String location) {

		if (location == null)

			return null;

		Geocoder geocoder = new Geocoder();

		// setAddress : 변환하려는 주소 (경기도 성남시 분당구 등)

		// setLanguate : 인코딩 설정

		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("ko")
				.getGeocoderRequest();

		GeocodeResponse geocoderResponse;

		try {

			geocoderResponse = geocoder.geocode(geocoderRequest);

			if (geocoderResponse.getStatus() == GeocoderStatus.OK & !geocoderResponse.getResults().isEmpty()) {

				GeocoderResult geocoderResult = geocoderResponse.getResults().iterator().next();

				LatLng latitudeLongitude = geocoderResult.getGeometry().getLocation();

				Float[] coords = new Float[2];

				coords[0] = latitudeLongitude.getLat().floatValue();

				coords[1] = latitudeLongitude.getLng().floatValue();

				return coords;

			}

		} catch (IOException ex) {

			ex.printStackTrace();

		}

		return null;

	}

}
