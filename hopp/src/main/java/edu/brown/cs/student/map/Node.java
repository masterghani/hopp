package edu.brown.cs.student.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.student.database.Proxy;
import edu.brown.cs.student.geocoding.Geocode;

public class Node  {

  private String id;
  private Double latitude;
  private Double longitude;
  private Map<NodePair, SearchSpace> searchSpaceMap;
  private Way bestIncoming = null;

  /**
   * @param id : unique id
   * @param latitude : lat pos
   * @param longitude : lon pos
   */
  public Node(String id, double latitude, double longitude) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    searchSpaceMap = new HashMap<NodePair, SearchSpace>();
  }
  
  public Node(String id, double latitude, double longitude, 
		  Map<NodePair, SearchSpace> searchSpaceMap) {
	  this.id = id;
	  this.latitude = latitude;
	  this.longitude = longitude;
	  this.searchSpaceMap = searchSpaceMap;
  }
  
  
 public Node(double latitude, double longitude) {
	 this.id = Geocode.reverseGeocode(latitude, longitude);
	 this.latitude = latitude;
	 this.longitude = longitude;
	 this.searchSpaceMap = new HashMap<NodePair, SearchSpace>();
	 
	 
	 
 }
  

  public Map<NodePair, SearchSpace> getSearchSpaceMap() {
	  return this.searchSpaceMap;
  }
  
  public void setSearchSpaceMap(Map<NodePair, SearchSpace> searchSpaceMap) {
	  this.searchSpaceMap=searchSpaceMap;
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
  
  public void setID(String id) {
	  this.id = id;
  }

  public Set<Way> getForwardNeighbors() {
    return Proxy.getForwardWays(this);
  }

  public Set<Way> getBackwardNeighbors() {
    return Proxy.getBackwardWays(this);
  }

public Way getBestIncoming() {
	return this.bestIncoming;
}


public void setBestIncoming(Way edge) {
	this.bestIncoming = edge;
	
}

@Override
public int hashCode() {
	  return this.id.hashCode();

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
	    Node other = (Node) obj;
	    return other.getID().equals(this.id);
	  }
  
}
