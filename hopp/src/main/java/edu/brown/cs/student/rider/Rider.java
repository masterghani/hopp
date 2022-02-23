package edu.brown.cs.student.rider;
import java.util.List;

import edu.brown.cs.student.map.NodePair;
import edu.brown.cs.student.person.Person;

public class Rider extends Person {
	
//	private double rating = Double.NaN;
	private float rating = 0;
	private List<String> tripIDs;
	private List <Integer> numSeats;
	private List<NodePair> stops;
	private List<String> notifications;
	
	public Rider(String userName, String firstName, 
			String lastName, String address, 
			String gender, String phoneNumber, 
			String birthday, String email, String token, 
			List<String> tripIDs, List <Integer> numSeats, List<NodePair> stops,
			List<String> notifications) {
		
        super(token, userName,firstName, lastName,address,
        		gender, phoneNumber, birthday, email);
        this.tripIDs = tripIDs;
        this.numSeats = numSeats;
        this.stops = stops;
        this.notifications = notifications;
	}
	
	public float getRating () {
		return this.rating;
	}
	
	public List<String> getTripIDs() {
		return this.tripIDs;
	}
	
	public List<Integer> getNumSeats() {
		return this.numSeats;
	}
	
	public List<NodePair> getStops() {
		return this.stops;
	}
	public List<String> getNotifications() {
		return this.notifications;
	}
	
	public void addNotification(String not) {
		  this.notifications.add(not);
	  }
	public void removeNotification(String not) {
		this.notifications.remove(not);
	}

	public void addTripID(String tripID) {
		this.tripIDs.add(tripID);
	}
	
	//removal by index
	public void addNumSeats(Integer n) {
		this.numSeats.add(n);
	}
	//removal by value
	public void addStop(NodePair stop) {
		this.stops.add(stop);
	}
	
	public void removeTripID(String tripID) {
		  this.tripIDs.remove(tripID);
	  }
	
	//removal by index
	public void removeNumSeats(int numSeats) {
		  this.numSeats.remove(numSeats);
	  }
	
	
	public void removeStop(NodePair np) {
		this.stops.remove(np);
	}
	
	
	  @Override
	  public int hashCode() {
		  return getToken().hashCode();
	 
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
		    Rider other = (Rider) obj;
		    return other.getToken().equals(getToken());
		  }
	
}
