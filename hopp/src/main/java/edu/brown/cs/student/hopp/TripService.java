package edu.brown.cs.student.hopp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.brown.cs.student.driver.Driver;
import edu.brown.cs.student.driver.Trip;
import edu.brown.cs.student.map.Node;
import edu.brown.cs.student.map.NodePair;
import edu.brown.cs.student.rider.Rider;

@Service
public class TripService {
  
  public static List<Trip> giveTrips() {
	  
	  List<Trip> tripLst = new ArrayList<>();
	  List<String> notifications = new ArrayList<>();
	  List<String> tIDs = new ArrayList<>();
	  notifications.add("yolo");
	  notifications.add("you have been owned");
	  notifications.add("im so tired");
	  
	  List<String> tripIDs = new ArrayList<>();
	  List<Integer> numSeats = new ArrayList<>();
	  List<NodePair> rstops = new ArrayList<>();
	  
	  tripIDs.add("trip1");
	  tripIDs.add("trip2");
	  numSeats.add(1);
	  numSeats.add(2);
	  rstops.add(new NodePair("node1", "node2"));
	  rstops.add(new NodePair("node3", "node4"));
	  
	  
	  
	  Driver d1 = new Driver("userName", "firstName",
    	      "lastName", "address", 
    	     "gender",  "phoneNumber", 
    	      "birthday", "email", 
    	      "licenseNumber", "carMakeandModel", "token",tIDs,notifications );
	  
	  Driver d2 = new Driver("userName2", "firstName2",
    	      "lastNam2e", "address2", 
    	     "gender2",  "phoneNumber2", 
    	      "birthday2", "email2", 
    	      "licenseNumbe2r", "carMakeandModel2", "token2",tIDs,notifications );
	  
	  
	  
	  Node origin = new Node("id1",40,50);
	  Node destination = new Node("id1",41,50);
	  Set<Rider> riders = new HashSet<>();
	  
	  Rider r1 = new Rider("userNameR", "firstNameR",
    	      "lastNameR", "addressR", 
    	     "genderR",  "phoneNumberR", 
    	      "birthdayR", "emailR", "tokenR", 
    	      tripIDs, numSeats, rstops, notifications);
	  
	  riders.add(r1);
	  List<String> stops = new ArrayList<>();
	  stops.add("stop1");
	  stops.add("stop2");
	  List<Double> distances = new ArrayList<>();
	  distances.add(5.0);
	  
	  
	  
	  Trip t1 = new Trip("trip token", d1, 5, 3, 20,
		      origin, destination, riders,
		      "2014-5-27", 10.0, 
		       stops,distances);
	  
	  Trip t2 = new Trip("trip token 2", d2, 4, 1, 100,
		      origin, destination, riders,
		      "2024-5-27", 15.0, 
		       stops,distances);
	  
	 
	  tripLst.add(t1);
	  tripLst.add(t2);

	  
	  
	  return tripLst;
    }

}
