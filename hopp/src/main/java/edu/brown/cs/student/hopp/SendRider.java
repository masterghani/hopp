package edu.brown.cs.student.hopp;

import edu.brown.cs.student.driver.Trip;

public class SendRider {
	private String riderID;
	private String firstName;
	private String lastName;
	private String phone;
	private String origin;
	private String destination;
	
	
	public SendRider(String r, String f, String l, String p, String o, 
			String d) {
		this.riderID = r;
		this.firstName = f;
		this.lastName = l;
		this.phone = p;
		this.origin = o;
		this.destination = d;
	}
	
	public String getOrigin() {
		return this.origin;
	}
	
	public String getDestination() {
		return this.destination;
	}
	
	public String getRiderID() {
		return this.riderID;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	
	@Override
	  public int hashCode() {
		  return this.riderID.hashCode();
	 
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
		    SendRider other = (SendRider) obj;
		    return (other.getRiderID().equals(this.riderID) &&
		    		other.getOrigin().equals(this.origin) &&
		    		other.getDestination().equals(this.destination));
		    		
		  }
	
	
	
	

}
