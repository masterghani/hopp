package edu.brown.cs.student.geocoding;
import java.io.IOException;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import edu.brown.cs.student.map.Node;

public final class Geocode {
	
	private static GeoApiContext context = new GeoApiContext.Builder()
		    .apiKey("AIzaSyB2TQLYPOwIirDHlej-gH5FOrG6dsmkxqY")
		    .build();
	
	
	public static Node geocode(String location) {
		
		GeocodingResult[] results;
		try {
			results = GeocodingApi.geocode(context,
				    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
			double lat = results[0].geometry.location.lat;
			double lng = results[0].geometry.location.lng;
			System.out.println(lat);
			System.out.println(lng);
			
			return new Node("", lat, lng);
			
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
		
	}
	
	
public static String reverseGeocode (Double lat, Double lng) {
		
		GeocodingResult[] results;
		try {
			results = GeocodingApi.reverseGeocode(context, new LatLng(lat, lng)).await();
			
			String formattedAddress = results[0].formattedAddress;
			
			return formattedAddress;
			
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
		
	}
	
	
		

}
