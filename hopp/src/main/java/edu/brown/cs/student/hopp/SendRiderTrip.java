package edu.brown.cs.student.hopp;

import java.time.LocalDate;

public class SendRiderTrip {
	
	
	private String id;
	private String driverFirstName;
	private String driverLastName;
	private String driverID;
	private String tripOrigin;
	private Double originLat;
	private Double originLng;
	private Double destLat;
	private Double destLng;
	
	
	private String tripDest;
	private int available;
	private int price;
	private LocalDate date;
	
	public SendRiderTrip(String id, String driverID, 
			String tripOrigin, Double originLat, Double originLng,
			String tripDest, Double destLat, Double destLng,
			String driverFirstName,
			String driverLastName, LocalDate date, 
			int available, int price) {
		
		this.id = id;
		this.driverID = driverID;
		this.tripOrigin = tripOrigin;
		this.tripDest = tripDest;
		this.originLat = originLat;
		this.originLng = originLng;
		this.destLat = destLat;
		this.destLng = destLng;
		
		this.driverFirstName = driverFirstName;
		this.driverLastName = driverLastName;
		this.date = date;
		this.price = price;
		this.available = available;
				
	}
	
	public String getDriverFirstName() {
		return this.driverFirstName;
	}
	public String getDriverLastName() {
		return this.driverLastName;
	}
	
	public String getTripOrigin() {
		return this.tripOrigin;
	}
	public String getTripDest() {
		return this.tripDest;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public Double getOriginLat() {
		return this.originLat;
	}
	
	public Double getOriginLng() {
		return this.originLng;
	}
	
	public Double getDestLat() {
		return this.destLat;
	}
	
	public Double getDestLng() {
		return this.destLng;
	}
	
	public int getPrice() {
		return this.price;
		
	}
	public int getAvailable() {
		return this.available;
	}
	
	
	public String getID() {
		return this.id;
	}
	
	public String getDriverID() {
		return this.driverID;
	}
	
	
}
	