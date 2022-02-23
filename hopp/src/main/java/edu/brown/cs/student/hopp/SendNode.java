package edu.brown.cs.student.hopp;



public class SendNode {
	private String id;
	private Double latitude;
	private Double longitude;
	
	
	
	public SendNode(String id, double latitude, double longitude) {
	    this.id = id;
	    this.latitude = latitude;
	    this.longitude = longitude;
	  }
	
	/**
	   * @return latitude
	   */
	  public double getLatitude() {
	    return this.latitude;
	  }
	  /**
	   * @return longitude
	   */
	  public double getLongitude() {
	    return this.longitude;
	  }
	  /**
	   * @return id
	   */
	  public String getID() {
	    return this.id;
	  }
	
	

}
