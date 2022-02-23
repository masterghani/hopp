package edu.brown.cs.student.map;

import edu.brown.cs.student.heuristic.Heuristic;


public class Haversine  {

	  private double radius;
	  /**
	   * Constructor with radius.
	   * @param radius Radius of the Earth
	   */
	  public Haversine(double radius) {
	    this.radius = radius;
	  }
	  /**
	   * Get the Haversine distance between two Locations.
	   * @param v1 first Location
	   * @param v2 second Loaction
	   */
	  
	  public double getDistance(Node v1, Node v2) {
	    double latitude1 = v1.getLatitude();
	    double longitude1 = v1.getLongitude();
	    double latitude2 = v2.getLatitude();
	    double longitude2 = v2.getLongitude();
	    double latitudeDifference = Math.toRadians(latitude2 - latitude1);
	    double longitudeDifference = Math.toRadians(longitude2 - longitude1);
	    latitude1 = Math.toRadians(latitude1);
	    latitude2 = Math.toRadians(latitude2);
	    return this.radius * 2.0
	        * Math.asin(Math.sqrt(Math.pow(Math.sin(latitudeDifference / 2.0), 2)
	            + Math.pow(Math.sin(longitudeDifference / 2.0), 2.0)
	                * Math.cos(latitude1) * Math.cos(latitude2)));
	  }
	}