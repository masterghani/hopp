package edu.brown.cs.student.hopp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class SendTrip {
	private String id;
	private String driverID;
	private String tripOrigin;
	private String tripDest;
	private Set<SendRider> riders;
	private String driverFirstName;
	private String driverLastName;
	private String driverPhone;
	private LocalDate date;
	
	private static DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy-M-d");
	
	public SendTrip(String id, String driverID, 
			String tripOrigin, String 
			tripDest,Set<SendRider> riders, 
			String driverFirstName,
			String driverLastName, LocalDate date, String driverPhone) {
		
		this.id = id;
		this.driverID = driverID;
		this.tripOrigin = tripOrigin;
		this.tripDest = tripDest;
		this.riders =riders;
		this.driverFirstName = driverFirstName;
		this.driverLastName = driverLastName;
		this.date = date;
		this.driverPhone = driverPhone;
				
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	
	public String getID() {
		return this.id;
	}
	
	public String getDriverID() {
		return this.driverID;
	}
	
	public String getTripOrigin() {
		return this.tripOrigin;
	}
	public String getTripDest() {
		return this.tripDest;
	}
	
	public String getDriverFirstName() {
		return this.driverFirstName;
	}
	public String getDriverLastName() {
		return this.driverLastName;
	}
	
	public Set<SendRider> getRiders() {
		return this.riders;
	}
	
	public String getPhone() {
		return this.driverPhone;
	}
	
	
	
	

}
