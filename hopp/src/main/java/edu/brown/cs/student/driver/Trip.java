package edu.brown.cs.student.driver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

import java.util.List;

import edu.brown.cs.student.database.Proxy;
import edu.brown.cs.student.map.Node;
import edu.brown.cs.student.rider.Rider;
import edu.brown.cs.student.matching.HoppAlgo;



//IMPORTANT: ALWAYS INITIALIZE TRIP WITH A STOP FOR THE DRIVERS ORIGIN AND THE DRIVERS
//DESTINATION


public class Trip {

  private String token = null;
  private Integer capacity = null;
  private Node origin = null;
  private Node destination = null;
  private Driver driver = null;
  private Set<Rider> riders = null;
  private Integer available = null;
  //per rider cost
  private Integer cost = null;
  private LocalDate departureDate = null;
  private Double maxDetourMileage = null;
  private List<String> stops = new ArrayList<>();
  private List<Double> distances = new ArrayList<>();
  
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

  /**
   * 
   * @param token
   * @param driver
   * @param name
   * @param capacity
   * @param origin
   * @param destination
   * @param date - format : "2017/08/07", "2017/8/7"
   */
  public Trip(String token, Driver driver, Integer capacity, Integer cost,
      Node origin, Node destination, String date, Double maxDetourMileage) {
    this.token = token;
    this.capacity = capacity;
    this.available = this.capacity;
    this.cost = cost;
    this.origin = origin;
    this.destination = destination;
    this.driver = driver;
    this.departureDate = LocalDate.parse(date, formatter);
    this.maxDetourMileage = maxDetourMileage;
  }
  
  public Trip(String token, Driver driver, Integer capacity, Integer available, Integer cost,
	      Node origin, Node destination, Set<Rider> riders,
	      String date, Double maxDetourMileage, 
	      List<String> stops, List<Double> distances) {
	    this.token = token;
	    this.capacity = capacity;
	    this.available =available;
	    this.cost = cost;
	    this.origin = origin;
	    this.destination = destination;
	    this.driver = driver;
	    this.riders = riders;
	    this.departureDate = LocalDate.parse(date, formatter);
	    this.maxDetourMileage = maxDetourMileage;
	    this.stops = stops;
	    this.distances = distances;
	  }
  

  public String getRiderTokens() {
	if (this.riders==null) {
		return "";
	}
	  
	  
    StringBuilder riders = new StringBuilder();
    for (Rider rider : this.getRiders()) {
      riders.append(rider.getToken());
      riders.append(",");
    }
    return riders.toString().trim();
  }

  public Set<Rider> getRiders() {
    return riders;
  }

  public void setRiders(Set<Rider> riders) {
    this.riders = riders;
  }

  public Integer getAvailable() {
    return available;
  }

  public void setAvailable(Integer available) {
    this.available = available;
  }
  
  public Integer getCost() {
	  return this.cost;
  }
  
  public void setCost(Integer cost) {
	  this.cost = cost;
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  
  
  
  
  //need to pass in the relevant distances 
  public Integer addRider(Rider rider, Node origin, Node destination) {
	  
	  System.out.println("getting rider wrapper");
	  RiderTripWrapper rtw = Proxy.getRiderTripWrapper(this.token, rider.getToken());
	  System.out.println("got rider wrapper");
	  
	  if (rtw == null) {
		  System.out.println("Ride not added to wrapper DB");
		  return null;
	  }
	  
	  String point1 = rtw.getPointToInsertRiderOriginAfter();
	  String point2= rtw.getPointToInsertRiderDestAfter();
	  List<Double> distances = rtw.getInsertDistances();
	  Double detour = rtw.getDetourForThisRider();
	  Integer numSeats = rtw.getNumSeats();
	  
    assert(this.available >= numSeats) : "This trip does not have "
    									+ "enough spots for this rider";
    
    
    
    riders.add(rider);
    this.available = this.available - numSeats;
    
    
    int firstInsert = -1;
    int secondInsert = -1;
    
    
    
    //only driver on the trip, distances[1] is 0.0 
    if (this.stops.size()==2) {
    	
    	System.out.println("should skip");
    	
    	this.stops.add(1,origin.getID());
    	this.stops.add(2, destination.getID());
    	List<Double> newDist = new ArrayList<>();
    	newDist.add(distances.get(0));
    	newDist.add(distances.get(2));
    	newDist.add(distances.get(3));
    	this.setDistances(newDist);
    	this.maxDetourMileage -= detour;
        return numSeats;
    	
    }
    
    
    System.out.println("Stop List");
    for (String s: this.stops) {
    	System.out.println(s);
    } 
    
  
    
    
    //more than one rider on the trip, therefore at least four stops
    
    //find insertPoints
    for (int i = 0; i < this.stops.size()-1; i++) {
    	if (this.stops.get(i).equals(point1)) {
    		firstInsert= i+1;
    		break;
    	}
    }
    
    System.out.println(point1);
    System.out.print("firstInsert: ");
    System.out.println(firstInsert);
    
    assert(firstInsert != -1) : "Could not find first insert point in stop list";
    
    System.out.println(point2);
    System.out.println(this.stops.size()-1);
    
    for (int j =firstInsert; j < this.stops.size()-1; j++) {
    	if (stops.get(j).equals(point2)) {
    		secondInsert = j+2;
    		break;
    	}
    }
    
    
    if (point2.equals(origin.getID())) {
    	secondInsert = firstInsert+1;
    }
    
    assert(firstInsert != -1) : "Could not find second insert point in stop list";
    
    System.out.print("secondInsert: ");
    System.out.println(secondInsert);
    
    
    if (distances.get(1).equals(0.0)) {
    	
    	System.out.print("connect rider points case");
    	
    	assert(firstInsert +1==secondInsert) : "Insert points not calculated correctly";
    	this.stops.add(firstInsert, origin.getID());
    	this.stops.add(secondInsert, destination.getID());
    	this.distances.remove(firstInsert-1);
    	this.distances.add(firstInsert-1, distances.get(0));
    	this.distances.add(firstInsert, distances.get(2));
    	this.distances.add(secondInsert, distances.get(3));
	
    } else {
    	
    	System.out.print("disconnect rider points case");
    	
    	this.stops.add(firstInsert, origin.getID());
    	this.stops.add(secondInsert, destination.getID());
    	this.distances.remove(firstInsert-1);
    	this.distances.add(firstInsert-1, distances.get(0));
    	this.distances.add(firstInsert, distances.get(1));
    	this.distances.remove(secondInsert-1);
    	this.distances.add(secondInsert-1, distances.get(2));
    	this.distances.add(secondInsert, distances.get(3));
    	
    }
    
    this.maxDetourMileage -= detour;
    return numSeats;
    
    
  }

  public void removeRider(Rider rider, String stop1, String stop2, int numSeats) {
    riders.remove(rider);
    this.available = this.available + numSeats;
    
    
    //note there must be at least four nodes in the stops list
    
    //find indices to remove
    Integer firstRemove = -1;
    Integer secondRemove = -1;
    int i;
    
    for (i = 1; i< this.stops.size()-1;i++) {
    	if (this.stops.get(i).equals(stop1)) {
    		firstRemove = i;
    		break;
    	}
    }
    
    assert(firstRemove != -1) : "Could not find first stop";
    
    for (int j = i+1; j< this.stops.size()-1;j++) {
    	if (this.stops.get(j).equals(stop2)) {
    		secondRemove = j-1;
    		break;
    	}
    }
    
    assert(secondRemove != -1) : "Could not find second stop";
    
    
    
    if (firstRemove==secondRemove) {
    	String ConnectA1 = this.stops.get(firstRemove-1);
    	String ConnectA2 = this.stops.get(firstRemove+2);
    	this.stops.remove(firstRemove);
    	this.stops.remove(secondRemove);
    	this.distances.remove(firstRemove);
    	this.distances.remove(firstRemove);
    	this.distances.remove(firstRemove-1);
    	
    	Double prevDist = HoppAlgo.getShortestDist(Proxy.getNodeFromToken(ConnectA1), Proxy.getNodeFromToken(ConnectA2));
    	this.distances.add(firstRemove-1,prevDist);
    } else {
    	String ConnectA1 = this.stops.get(firstRemove-1);
    	String ConnectA2 = this.stops.get(firstRemove+1);
    	String ConnectB1 = this.stops.get(secondRemove);
    	String ConnectB2 = this.stops.get(secondRemove+2);
    	
    	Double prevDistA = HoppAlgo.getShortestDist(Proxy.getNodeFromToken(ConnectA1), Proxy.getNodeFromToken(ConnectA2));
    	Double prevDistB = HoppAlgo.getShortestDist(Proxy.getNodeFromToken(ConnectB1), Proxy.getNodeFromToken(ConnectB2));
    	this.stops.remove(firstRemove);
    	this.stops.remove(secondRemove);
    	
    	
    	this.distances.remove(secondRemove);
    	this.distances.remove(secondRemove);
    	
    	this.distances.remove(firstRemove);
    	this.distances.remove(firstRemove-1);
    	
    	
    	this.distances.add(firstRemove-1, prevDistA);
    	this.distances.add(secondRemove-1, prevDistB);
    }
    //have to re-calculate maxDetour
    
  	double sum = 0;
  	for(Double d : this.distances)
  	    sum += d;
  	
  	Node firstNode  = Proxy.getNodeFromToken(this.stops.get(0));
  	Node lastNode  = Proxy.getNodeFromToken(this.stops.get(this.stops.size()-1));
  	
  	Double origDist = HoppAlgo.getShortestDist(firstNode, lastNode);
  	
  	this.maxDetourMileage = sum - origDist;
    
  }

  public Driver getDriver() {
    return this.driver;
  }

  public void setDriver(Driver driver) {
    this.driver = driver;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Node getOrigin() {
    return this.origin;
  }

  public void setOrigin(Node origin) {
    this.origin = origin;
  }

  public Node getDestination() {
    return this.destination;
  }

  public void setDestination(Node destination) {
    this.destination = destination;
  }
  
  public List<String> getStops() {
	  return this.stops;
  }
  
  public void setStops(List<String> stops) {
	  this.stops = stops;
  }
  
  public List<Double> getDistances() {
	  return this.distances;
  }
  
  public List<String> getStringDistances() {
	  List<String> strings = new ArrayList<String>();
	  for (Double d : this.distances) {
	      strings.add(Double.toString(d));
	  }
	  return strings;
  }
  
  
  public void setDistances(List<Double> distances) {
	  assert(this.stops.size() == distances.size()-1) : "Stops do not match distances";
	  this.distances = distances;
  }
  
  public Double getMaxDetour() {
	  return this.maxDetourMileage;
  }
  
  public void setMaxDetour(Double maxDetour) {
	  this.maxDetourMileage = maxDetour;
  }
  
  @Override
  public int hashCode() {
	  return this.token.hashCode();
 
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
	    Trip other = (Trip) obj;
	    return other.getToken().equals(this.token);
	  }
  
  
}
