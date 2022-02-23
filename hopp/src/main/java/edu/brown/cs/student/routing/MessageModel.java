package edu.brown.cs.student.routing;

public class MessageModel {
    
  private String tripToken;
  private String riderToken;
  public String getTripToken() {
    return tripToken;
  }
  public void setTripToken(String tripToken) {
    this.tripToken = tripToken;
  }
  public String getRiderToken() {
    return riderToken;
  }
  public void setRiderToken(String riderToken) {
    this.riderToken = riderToken;
  }
}
