package edu.brown.cs.student.driver;

import java.util.List;



public class RiderTripWrapper {

	private String tripID;

	private String riderID;
	private String point1;
	private String point2;
	
	private List<Double> distances;
	
	private Integer numSeats;
	
	private Double detour;
	
	
	public RiderTripWrapper(String t, String riderID,String point1, 
			String point2, List<Double> distances, Double detour, Integer numSeats) {
		this.tripID = t;
		this.riderID = riderID;
		this.point1 = point1;
		this.point2 = point2;
		this.distances = distances;
		this.detour = detour;
		this.numSeats = numSeats;
	}
	
	public String getTripID() {
		return this.tripID;
	}
	
	public void setTripID(String tripID) {
		this.tripID = tripID;
	}
	
	public String getRiderID() {
		return this.riderID;
	}
	
	public void setRiderID(String riderID) {
		this.riderID = riderID;
	}
	
	public String getPointToInsertRiderOriginAfter() {
		return this.point1;
	}
	
	public String getPointToInsertRiderDestAfter() {
		return this.point2;
	}
	
	public List<Double> getInsertDistances() {
		return this.distances;
	}
	
	
	public Double getDetourForThisRider() {
		return this.detour;
	}
	
	public Integer getNumSeats() {
		return this.numSeats;
	}
	
	
	@Override
	  public int hashCode() {
		  return this.tripID.hashCode() + 
				  31*this.riderID.hashCode();
	 
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
		    RiderTripWrapper other = (RiderTripWrapper) obj;
		    return (other.getRiderID().equals(this.riderID) &&
		    		other.getTripID().equals(this.tripID));
		  }
	
	
	
	
	
	
	
}
