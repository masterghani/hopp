package edu.brown.cs.student.map;





public class Way {

  private String id;
  private Node start;
  private Node end;
  private Double weight;
  private String type;

  /**
   * @param id - unique id
   * @param start vertex
   * @param end vertex
   */
  public Way(String id, Node start, Node end) {
    this.id = id;
    this.start = start;
    this.end = end;
    this.weight = this.findWeight();
    this.type = "";
  }
  
  public Way(String id, String type, Node start, Node end) {
	    this.id = id;
	    this.start = start;
	    this.end = end;
	    this.weight = this.findWeight();
	    this.type = type;
	  }
  
  

  /**
   * @return weight of way
   */
  private Double findWeight() {
	if ((this.start==null) || (this.end==null)) {
		return Double.MAX_VALUE;
	}  
	
	
	
	
	
    double latitude1 = this.start.getLatitude();
    double latitude2 = this.end.getLatitude();
    double longitude1 = this.start.getLongitude();
    double longitude2 = this.end.getLongitude();
    
    
    
    final double earthRadius = (6378.137 + 6356.752) / 2;
    
	double latitudeDifference = Math.toRadians(latitude2 - latitude1);
	    double longitudeDifference = Math.toRadians(longitude2 - longitude1);
	    latitude1 = Math.toRadians(latitude1);
	    latitude2 = Math.toRadians(latitude2);
	    return earthRadius * 2.0
	        * Math.asin(Math.sqrt(Math.pow(Math.sin(latitudeDifference / 2.0), 2)
	            + Math.pow(Math.sin(longitudeDifference / 2.0), 2.0)
	                * Math.cos(latitude1) * Math.cos(latitude2)));
    
    
  }
  
  
  public String getType() {
	  return this.type;
  }
  
  public void setType(String type) {
	  this.type = type;
  }
  
  
  /**
   * @return weight of way
   */
  public Double getWeight() {
    return this.weight;
  }

  public String getID() {
    return this.id;
  }
  
  public void setID(String id) {
	  this.id = id;
  }


  public Node getSource() {
    return this.start;
  }

  public Node getDestination() {
    return this.end;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
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
	    Way other = (Way) obj;
	    return other.getID().equals(this.id);
	  }
}
