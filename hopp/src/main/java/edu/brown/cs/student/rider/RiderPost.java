package edu.brown.cs.student.rider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import edu.brown.cs.student.map.Node;

public class RiderPost {
	private String riderToken;
	private Node startNode;
	private Node endNode;
	private Integer numSeats;
	private LocalDate departureDate;
	private Integer price;
	
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
	
	
	
	public RiderPost(String riderToken,Node start, Node end, Integer numSeats, String date, Integer price) {
		this.riderToken = riderToken;
		this.startNode = start;
		this.endNode = end;
		this.numSeats = numSeats;
		this.departureDate= LocalDate.parse(date, formatter);
		this.price = price;
	}
	
	public String getRiderToken() {
		return this.riderToken;
	}
	
	public Integer getCost() {
		return this.price;
	}
	
	public void setCost(Integer price) {
		this.price = price;
	}
	
	public void setRiderToken(String token) {
		this.riderToken = token;
	}
	
	
	public Node getOrigin() {
		return this.startNode;
	}
	
	public Node getDestination() {
		return this.endNode;
	}
	
	public void setOrigin(Node start) {
		this.startNode = start;
	}
	
	public void setDestination(Node end) {
		this.endNode = end;
	}
	
	public Integer getNumSeats() {
		return this.numSeats;
	}
	
	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}
	
	
	public LocalDate getDepartureDate() {
		return this.departureDate;
	}
	
	public void setDepartureDate(String date) {
		this.departureDate = LocalDate.parse(date, formatter);
	}
	
	//Can only post one ride per day
	@Override
	  public int hashCode() {
		  return this.riderToken.hashCode() + 31*this.departureDate.hashCode();
	 
	  }
	  
	  @Override 
	  public boolean equals(Object obj) {
		    if (this == obj) {
		      return true;
		    }
		    if (obj == null) {
		      return false;
		    }
		    if (getClass() != obj.getClass()) {
		      return false;
		    }
		    RiderPost other = (RiderPost) obj;
		    return (other.getRiderToken().equals(this.riderToken) &&
		    		other.getDepartureDate().equals(this.departureDate));
		  }
	
	

}
