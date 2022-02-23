package edu.brown.cs.student.map;

import edu.brown.cs.student.driver.Trip;

public class TripDist {
	
	private Trip trip;
	private Double dist;
	
	
	public TripDist(Trip trip, Double dist) {
		this.trip = trip;
		this.dist = dist;
	}


	public Trip getTrip() {
		return this.trip;
	}
	
	
	public void setTrip(Trip t) {
		this.trip = t;
	}


	public Double getDist() {
		return this.dist;
	}
	
	public void setDist(Double dist) {
		this.dist = dist;
	}
	
	@Override
	public int hashCode() {
		return this.trip.hashCode() + 31*dist.hashCode();

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
		    TripDist other = (TripDist) obj;
		    return (other.getTrip().equals(this.trip) && 
		    		other.getDist().equals(this.dist));
		  }
	

}
