package edu.brown.cs.student.hopp;

import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.brown.cs.student.database.Proxy;
import edu.brown.cs.student.driver.Driver;
import edu.brown.cs.student.driver.Trip;
import edu.brown.cs.student.geocoding.Geocode;
import edu.brown.cs.student.map.Node;
import edu.brown.cs.student.map.NodePair;
import edu.brown.cs.student.rider.Rider;
import edu.brown.cs.student.routing.UserStorage;

/**
 * @author JavaSolutionsGuide
 *
 */
@RestController
public class Hello {



  @Autowired
  TripService tripService;

  
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;


  private final AsyncLogic asyncLogic = new AsyncLogic();


  //rider search for drivers



  //driver canceling trip
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @RequestMapping(value = "/driver_cancel", method = RequestMethod.POST, 
  produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody void cancelDriverTrip(@RequestBody
      Map<String,Object> payload) {
	  try {
	    	Proxy.connect();
	    } catch (SQLException e) {
	    	System.out.println("ERROR: Could not connect to database");
	    	return;
	    } catch (ClassNotFoundException e) {
	    	System.out.println("ERROR: Could not find Redshift Driver");
	    	return;
	    }

    System.out.println("Cancelling driver's trip");
    String tripID = null;

    for (String x : payload.keySet() ) {
      if (x.equals("tripID")) {
        tripID = (String) payload.get(x);
      }
    }

    if (tripID == null) {
      System.out.println("ERROR: Could not get Trip ID");
      return;
    }

    List<String> tIDs = new ArrayList<String>();

    Trip t = Proxy.getTripFromToken(tripID);
    for (Rider r : t.getRiders()) {
      Integer removeInd = null;
      tIDs = r.getTripIDs();
      for (int i = 0; i < tIDs.size();i++) {
        if (tIDs.get(i).equals(tripID)) {
          removeInd = i;
        }
      }
      if (removeInd==null) {
        System.out.println("ERROR: trip not in rider's trip list");
        return;
      }
      
      r.removeTripID(tripID);
      r.removeStop(r.getStops().get(removeInd));
      r.removeNumSeats(removeInd);
      Proxy.updateRider(r);
    }

    Driver d = t.getDriver();
    d.removeTripID(tripID);
    Proxy.updateDriver(d);
    Proxy.removeTrip(t.getToken());


    /** NOTIFICATION LOGIC 
    // get rider token string
    String output = "Your upcoming trip has been cancelled.";
    for (String riderToken : tIDs) {
      boolean exists = UserStorage.getInstance().getUsers().contains(riderToken);
      if (exists) {
        simpMessagingTemplate.convertAndSend("/topic/notifications/" + riderToken, output);
      }
    }
    boolean exists = UserStorage.getInstance().getUsers().contains(d.getToken());
    if (exists) {
      simpMessagingTemplate.convertAndSend("/topic/notifications/" + d.getToken(), output);
    }
    NOTIFICATION LOGIC **/
  
  }



  //TODO: CHECK THIS ENDPOINT

  //driver canceling trip
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @RequestMapping(value = "/driver_remove", method = RequestMethod.POST, 
  produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody void DriverremoveRider(@RequestBody
      Map<String,Object> payload) {
	  
	  try {
	    	Proxy.connect();
	    } catch (SQLException e) {
	    	System.out.println("ERROR: Could not connect to database");
	    	return;
	    } catch (ClassNotFoundException e) {
	    	System.out.println("ERROR: Could not find Redshift Driver");
	    	return;
	    }
	  
	  System.out.println("Driver removing rider from trip");
	  
	  
	  String riderID = null;
	    String tripID = null;
	    for (String x : payload.keySet() ) {
	      switch (x) {
	      case "userID":
	        riderID = (String) payload.get(x);
	      case "tripID":
	        tripID = (String) payload.get(x);
	      }
	    }

	    if (tripID==null || riderID==null) {
	      System.out.println("ERROR: Could not get rider or trip ID");
	      return;
	    }
	    removeHelper(tripID, riderID);


	    /** NOTIFICATION LOGIC 
	    // get driver token string
	    String output = "You have been rmoved from your upcoming trip";
	    boolean exists = UserStorage.getInstance().getUsers().contains(riderID);
	    if (exists) {
	      simpMessagingTemplate.convertAndSend("/topic/notifications/" + riderID, output);
	    }
	    NOTIFICATION LOGIC **/

  }

  public void removeHelper(String tripID, String riderID) {
	  	Trip t = Proxy.getTripFromToken(tripID);
	  	Rider r = Proxy.getRiderFromToken(riderID);


	    List<String> tIDs = r.getTripIDs();
	    Integer removeInd = null;
	    for (int i = 0; i < tIDs.size();i++) {
	      if (tIDs.get(i).equals(tripID)) {
	        removeInd = i;
	      }
	    }
	
	    if (removeInd == null) {
	      System.out.println("ERROR: Rider was not added to this trip");
	      return;
	    }
	
	    NodePair np = r.getStops().get(removeInd);
	    String stop1 = np.getOrigin();
	    String stop2 = np.getDestination();
	    Integer numSeats = r.getNumSeats().get(removeInd);
	
	    t.removeRider(r,stop1, stop2, numSeats);
	
	    r.removeTripID(tripID);
	    r.removeStop(r.getStops().get(removeInd));
	    r.removeNumSeats(removeInd);
	    
	    Proxy.updateRider(r);
	    Proxy.updateTrip(t);
  }
  

//TODO: CHECK THIS ENDPOINT
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @RequestMapping(value = "/rider_remove", method = RequestMethod.POST, 
  produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody void RiderremoveRider(@RequestBody
      Map<String,Object> payload) {
	  
	  try {
	    	Proxy.connect();
	    } catch (SQLException e) {
	    	System.out.println("ERROR: Could not connect to database");
	    	return;
	    } catch (ClassNotFoundException e) {
	    	System.out.println("ERROR: Could not find Redshift Driver");
	    	return;
	    }


    System.out.println("Rider removing themselves from a trip");

    String riderID = null;
    String tripID = null;
    for (String x : payload.keySet() ) {
      switch (x) {
      case "userID":
        riderID = (String) payload.get(x);
      case "tripID":
        tripID = (String) payload.get(x);
      }
    }

    if (tripID==null || riderID==null) {
      System.out.println("ERROR: could not get rider or trip ID");
      return;
    }

    removeHelper(tripID,riderID);


    /** NOTIFICATION LOGIC 
    // get driver token string
    String driverToken = "";
    String output = "A rider has left your upcoming trip.";
    boolean exists = UserStorage.getInstance().getUsers().contains(driverToken);
    if (exists) {
      simpMessagingTemplate.convertAndSend("/topic/notifications/" + driverToken, output);
    }
    NOTIFICATION LOGIC **/
  }


  
  /**
	'riderID' : String
	'tripID': String
 **/

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/rider_join", method = RequestMethod.POST, 
produces = MediaType.APPLICATION_JSON_VALUE)
public @ResponseBody Trip addRidertoTrip(@RequestBody
    Map<String, Object> payload) {
	
	try {
    	Proxy.connect();
    } catch (SQLException e) {
    	System.out.println("Could not connect to database");
    	return null;
    } catch (ClassNotFoundException e) {
    	System.out.println("Could not find Redshift Driver");
    	return null;
    }

  System.out.println("handling rider join");


  String riderID = null;
  String tripID = null;
  LinkedHashMap origin = null;
  LinkedHashMap dest = null;

  for (String x : payload.keySet() ) {
    
    
    if (x.equals("tripID")) {
      tripID = (String) payload.get(x);
    } else if (x.equals("riderID")) {
      riderID = (String) payload.get(x);
    } else if (x.equals("origin")) {
      origin = (LinkedHashMap) payload.get(x);
    } else if (x.equals("dest")) {
      dest = (LinkedHashMap) payload.get(x);
    } 
  }

  if (riderID==null || tripID==null || origin==null || dest==null) {
    System.out.println("ERROR: Did not recieve needed value in payload");
    return null;
  }



  Double sLat = (Double) origin.get("lat");
  Double sLng = (Double) origin.get("lng");
  Double eLat = (Double) dest.get("lat");
  Double eLng = (Double) dest.get("lng");
  
  Trip t = Proxy.getTripFromToken(tripID);
  Rider r =  Proxy.getRiderFromToken(riderID);
  
  
  System.out.println(sLat);
  System.out.println(sLng);
  System.out.println(eLat);
  System.out.println(eLng);
  
  
  
  
  Node start = Proxy.findNearestNode(sLat,sLng);
  Node end = Proxy.findNearestNode(eLat,eLng);
  
  System.out.println("nodes pt 2");
  System.out.println(start.getID());
  System.out.println(end.getID());
  
  
  if (t==null || r==null || start ==null || end==null) {
	    System.out.println("ERROR: Could extract necessary item from DB");
	    return null;
	}
  
  Integer numSeats = t.addRider(r, start, end);
  

  if (numSeats==null) {
    System.out.println("ERROR: Could not add rider to ride");
    return null;
  }

  Proxy.updateTrip(t);
  r.addStop(new NodePair(start.getID(), end.getID()));
  r.addNumSeats(numSeats);
  r.addTripID(tripID);

  Proxy.updateRider(r);


  

  /** NOTIFICATION LOGIC 
//  // get driver token string
  String driverToken = t.getDriver().getToken();
  String output = "A rider has joined your trip.";
  boolean exists = UserStorage.getInstance().getUsers().contains(driverToken);
  if (exists) {
    simpMessagingTemplate.convertAndSend("/topic/notifications/" + driverToken, output);
  }
  
  String output2 = "You have been added to the trip.";
  boolean exists2 = UserStorage.getInstance().getUsers().contains(riderID);
  if (exists2) {
    simpMessagingTemplate.convertAndSend("/topic/notifications/" + riderID, output2);
  }
  NOTIFICATION LOGIC **/


  //return the updated trip
  return t;
}
  
  
  
  /**
	'riderID' : String
	'origin': linkedhashmap
	'dest' : linkedhashmap
	'time' //(has a T in it) 0034-02-28T00:33
	'seats' : String
   * @throws ExecutionException 
   * @throws InterruptedException 
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/rider_post", method = RequestMethod.POST, 
produces = MediaType.APPLICATION_JSON_VALUE)
public @ResponseBody List<SendRiderTrip> getTripsForRider(@RequestBody
    Map<String, Object> payload) throws InterruptedException, ExecutionException {

  System.out.println("handling rider post");
  
  try {
  	Proxy.connect();
  } catch (SQLException e) {
  	System.out.println("ERROR: Could not connect to database");
  	return null;
  } catch (ClassNotFoundException e) {
  	System.out.println("ERROR: Could not find Redshift Driver");
  	return null;
  }
  	

  String id = null;
  Integer numSeats = null;
  String date = null;
  LinkedHashMap<String, Double> origin = null;
  LinkedHashMap<String, Double> dest = null;
  Integer price = null;
  for (String x : payload.keySet() ) {
    if (x.equals("id")) {
      id = (String) payload.get(x);
    } else if (x.equals("time")) {
      date = ((String) payload.get(x)).split("T")[0];
    }  else if (x.equals("origin")) {
      origin = (LinkedHashMap<String, Double>) payload.get(x);
    } else if (x.equals("dest")) {
      dest = (LinkedHashMap<String,Double>) payload.get(x);  
    } else if (x.equals("seats")) {
      numSeats = Integer.parseInt((String) payload.get(x));
    } else if (x.equals("price")) {
    	price = Integer.parseInt((String) payload.get(x));
    }
  }

  if (id==null || numSeats==null || 
      date==null || origin==null || dest==null) {

    System.out.println("ERROR: Did not recieve needed value in payload");
    return null;
  }
  
 
  CompletableFuture<List<SendRiderTrip>> future = 
		  asyncLogic.handleRiderPost(id, origin, dest, numSeats, date, price);
  
  return future.get(); // CancellationException

}
  


	//get all trips where this id is a driver or rider
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/trip_info_post", method = RequestMethod.POST, 
	produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<SendTrip> getTripsforUser(@RequestBody
	    Map<String, Object> payload) {
	
	  System.out.println("Getting driver trip manager info");
	  
	  try {
		  	Proxy.connect();
		  } catch (SQLException e) {
		  	System.out.println("ERROR: Could not connect to database");
		  	return null;
		  } catch (ClassNotFoundException e) {
		  	System.out.println("ERROR: Could not find Redshift Driver");
		  	return null;
		  }
	
	  
	  String driverID = null;
	  for (String x : payload.keySet() ) {
	    if (x.equals("id")) {
	      driverID = (String) payload.get(x);	  
	    }
	  }
	
	  if (driverID==null) {
	    System.out.println("ERROR: Did not give ID in payload");
	    return null;
	  }
	
	  Driver d = Proxy.getDriverFromToken(driverID);
	  Rider r = Proxy.getRiderFromToken(driverID);
	  
	  
	  if (d == null || r==null) {
	    System.out.println("ERROR: User not in database");
	  }
	  
	  List<Trip> output = new ArrayList<>(Proxy.getAllTripsForDriver(d));
	  output.addAll(Proxy.getAllTripsForRider(r));

	  List<SendTrip> transformed = new ArrayList<>();
	  for (Trip t : output) {
		  Node temp = t.getOrigin();
		  Node origin = new Node(temp.getID(), temp.getLatitude(), temp.getLongitude());
		  temp = t.getDestination();
		  Node dest = new Node(temp.getID(), temp.getLatitude(), temp.getLongitude());

		  Set<SendRider> send = new HashSet<>();
	  
		  //find the nodes that this rider uses on this trip
		 for (Rider rider : t.getRiders()) {
			 String or = null;
			 String det = null;
			 Integer tInd = null;
			 List<String> tIDs = rider.getTripIDs();
			 
			 for (int i=0;i <tIDs.size();i++) {
				 if (tIDs.get(i).equals(t.getToken())) {
					 tInd = i;
				 }
			 }
			 
			assert(tInd != null) : "ERROR: rider inserted into DB incorrectly, "
					+ "should not be on trip: " + t.getToken();
			
			NodePair np = rider.getStops().get(tInd);
			or  = np.getOrigin();
			det = np.getDestination();
	
	
			 send.add(new SendRider(rider.getToken(),
			 rider.getFirstName(), 
					 rider.getLastName(), 
					 rider.getPhoneNumber(), or, det));
		 }
	  
	 
		 String tripO = Geocode.reverseGeocode(origin.getLatitude(), 
				 origin.getLongitude());
		 
		 String tripD = Geocode.reverseGeocode(dest.getLatitude(), 
				 dest.getLongitude());
		 
		  SendTrip y = new SendTrip(t.getToken(), t.getDriver().getToken(),
				  tripO, tripD,send, t.getDriver().getFirstName(),
					t.getDriver().getLastName(),t.getDepartureDate(), 
					t.getDriver().getPhoneNumber());

		  //add transformed trip to output list of trips for frontend
		  transformed.add(y);

	  }
	  
	  System.out.print("Number of trips: ");
	  System.out.println(transformed.size());

	  System.out.println("Trip Manager handled");
	  return transformed;
	 
	}
 
  /**
	'id' : String
	'origin': linkedhashmap
	'dest' : linkedhashmap
	'time' : (has a T in it) 0034-02-28T00:33
	'price' : String
	'seats' : String
   * @throws ExecutionException 
   * @throws InterruptedException 

   **/
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @RequestMapping(value = "/driver_post", method = RequestMethod.POST, 
  produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody void getDriverPost(@RequestBody
      Map<String, Object> payload) throws InterruptedException, ExecutionException {


    System.out.println("handling driver post");
    
    try {
      	Proxy.connect();
      } catch (SQLException e) {
      	System.out.println("ERROR: Could not connect to database");
      	return;
      } catch (ClassNotFoundException e) {
      	System.out.println("ERROR: Could not find Redshift Driver");
      	return;
      }

    String driverID = null;
    Integer seats = null;
    Integer price = null;
    String date = null;
    Double detour = null;
    LinkedHashMap<String,Double> origin = null;
    LinkedHashMap<String,Double> dest = null;

    for (String x : payload.keySet()) {
      System.out.println(x);
      if (x.equals("seats")) {
        seats = Integer.parseInt((String) payload.get(x));
      }
      if (x.equals("detour")) {
        detour = Double.parseDouble((String) payload.get(x));
      }

      if (x.equals("driverID")) {
        driverID = (String) payload.get(x);
      }

      if (x.equals("price")) {
        price = Integer.parseInt((String) payload.get(x));
      }

      if (x.equals("time")) {
        date = ((String) payload.get(x)).split("T")[0];
      }

      if (x.equals("origin")) {
        origin = (LinkedHashMap<String, Double>) payload.get(x);
      }
      if (x.equals("dest")) {
        dest = (LinkedHashMap<String, Double>) payload.get(x);
      }

    }

    if (driverID==null || seats==null || 
        price==null || date==null || detour==null
        || origin==null || dest==null) {

      System.out.println("ERROR: Did not recieve needed value in payload");
      return;
    }
    
    
    CompletableFuture<Integer> future = asyncLogic.handleDriverPost(origin, dest, 
        seats, price, driverID, detour, date);
    if (future.get() == 1) {
      System.out.println("sucessfully handled driver post");
    } else {
      System.out.println("driver post failed");
    }

  }


  /**
   * Finished
   * @param payload
   * @return
   */

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @RequestMapping(value = "/sign_up", method = RequestMethod.POST, 
  produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody boolean getRiderfromSignup(@RequestBody
      Map<String, Object> payload) {
	  
	  try {
		  	Proxy.connect();
		  } catch (SQLException e) {
		  	System.out.println("ERROR:Could not connect to database");
		  	return false;
		  } catch (ClassNotFoundException e) {
		  	System.out.println("ERROR:Could not find Redshift Driver");
		  	return false;
		  }

    //create driver and rider objects
	  
    System.out.println("Signing up person, adding new Rider "
        + "and driver to the database");


    String id = null;
    String username = null;
    String birthday = null;
    String address = null;
    String gender = null;
    String phone = null;
    String firstName = null;
    String lastName = null;
    String email = null;


    for (String x : payload.keySet() ) {
      switch (x) {
      case "userID":
        id = (String) payload.get(x);
      case "username":
        username = (String) payload.get(x);
      case "birthday":
        birthday = (String) payload.get(x);
      case "address":
        address = (String) payload.get(x);
      case "gender":
        gender = (String) payload.get(x);
      case "phone":
        phone = (String) payload.get(x);
      case "firstName":
        firstName = (String) payload.get(x);
      case "lastName":
        lastName = (String) payload.get(x);
      case "email":
        email = (String) payload.get(x);  
      }
    }


    if (id==null || username==null || birthday==null || address==null 
        || gender==null || phone==null || 
        firstName==null || lastName==null || email==null) {
      System.out.println("ERROR: One of the fields is null, cannot create account");
      return false;
    }

    Rider r = new Rider(username, firstName, lastName, 
        address, gender, phone, birthday, email,
        id, new ArrayList<String>(),
        new ArrayList<Integer>(), new ArrayList<NodePair>(),
        new ArrayList<String>());

    
    //not currently using license or car information
    
    Driver d = new Driver(username, firstName, lastName, 
        address, gender, phone, birthday, email, "temp","temp", 
        Double.NaN,id,new ArrayList<String>(),new ArrayList<String>());
    

    assert(!Proxy.checkRiderExists(id)) : "Rider already exists in database";
    assert(!Proxy.checkDriverExists(id)) : "Driver already exists in database";
    
    if (Proxy.checkRiderExists(id)) {
    	Proxy.updateRider(r);
    } else {
    	Proxy.insertRider(r);
    }
    
    if (Proxy.checkDriverExists(id)) {
    	Proxy.updateDriver(d);
    } else {
    	Proxy.insertDriver(d);
    }
    
    System.out.println("Signup handled");

    return true;

  }

}
