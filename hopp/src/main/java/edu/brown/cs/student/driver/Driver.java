package edu.brown.cs.student.driver;
import java.util.List;

import edu.brown.cs.student.person.Person;

public class Driver extends Person {

  private double rating = Double.NaN;
  private String licenseNumber = "";
  private String carMakeandModel = "";
  private List<String> notifications;
  private List<String> tripIDs;

  public Driver(String userName, String firstName, 
      String lastName, String address, 
      String gender, String phoneNumber, 
      String birthday, String email, 
      String licenseNumber, String carMakeandModel, String token, 
      List<String> tripIDs,
      List<String> notifications) {

    super(token, userName,firstName, lastName,address,
        gender, phoneNumber, birthday, email);
    this.licenseNumber = licenseNumber;
    this.carMakeandModel = carMakeandModel;
    this.notifications = notifications;
    this.tripIDs = tripIDs;
  }

  
  public Driver(String userName, String firstName, 
	      String lastName, String address, 
	      String gender, String phoneNumber, 
	      String birthday, String email, 
	      String licenseNumber, String carMakeandModel, Double rating,
	      String token, List<String> tripIDs,List<String> notifications ) {

	    super(token, userName,firstName, lastName,address,
	        gender, phoneNumber, birthday, email);
	    this.licenseNumber = licenseNumber;
	    this.carMakeandModel = carMakeandModel;
	    this.rating = rating;
	    this.notifications = notifications;
	    this.tripIDs = tripIDs;
	  }
  
  
  public Driver () {}

  public double getRating () {
    return this.rating;
  }
  public String getLicenseNumber() {
    return this.licenseNumber;
  }
  public String getCarMakeandModel () {
    return this.carMakeandModel;
  }

  public void setRating (double rating ) {
    this.rating = rating;
  }

  public void setLicenseNumber (String licenseNumber) {
    this.licenseNumber = licenseNumber;
  }

  public void setCarMakeandModel (String carMakeandModel) {
    this.carMakeandModel = carMakeandModel;
  }
  
  
  public List<String> getNotifications() {
	  return this.notifications;
  }
  
  public void addNotification(String not) {
	  this.notifications.add(not);
  }
  
  public void removeNotificationByIndex(Integer ind) {
		this.notifications.remove(ind);
		return;
	}
  
  public void removeNotificationByValue(String not) {
		this.notifications.remove(not);
		return;
	}
  
  
  public void addTripID(String tripID) {
	  this.tripIDs.add(tripID);
	  return;
  }
  
  public List<String> getTripIDs() {
	  return this.tripIDs;
  }
  
  public void removeTripID(String tripID) {
	  this.tripIDs.remove(tripID);
	  
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
	    Driver other = (Driver) obj;
	    return other.getToken().equals(getToken());
	  }

}
