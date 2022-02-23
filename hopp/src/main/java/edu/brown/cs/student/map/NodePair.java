package edu.brown.cs.student.map;

public class NodePair {
	
	private String origin;
	private String destination;
	public NodePair(String origin,String destination) {
		this.origin = origin;
		this.destination = destination;
	}
	
	public String getOrigin() {
		return this.origin;
		
	}
	
	public String getDestination() {
		return this.destination;
	}
	
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Override
	public int hashCode() {
		return this.origin.hashCode() + 31*this.destination.hashCode();

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
		    NodePair other = (NodePair) obj;
		    return (other.getOrigin().equals(this.origin) && 
		    		other.getDestination().equals(this.destination));
		  }
	

}
