package edu.brown.cs.student.hopp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import edu.brown.cs.student.database.Proxy;
import edu.brown.cs.student.driver.Driver;
import edu.brown.cs.student.driver.Trip;
import edu.brown.cs.student.map.Node;
import edu.brown.cs.student.matching.HoppAlgo;
import edu.brown.cs.student.rider.Rider;
import edu.brown.cs.student.rider.RiderPost;
import edu.brown.cs.student.routing.UserStorage;


@Service
public class AsyncLogic {
  
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Async("threadPoolTaskExecutor")
  public CompletableFuture<List<SendRiderTrip>> handleRiderPost(String id, LinkedHashMap<String, Double> origin,
      LinkedHashMap<String, Double> dest, Integer numSeats, String date, Integer price) {
    Double sLat = (Double) origin.get("lat");
    Double sLng = (Double) origin.get("lng");
    Double eLat = (Double) dest.get("lat");
    Double eLng = (Double) dest.get("lng");

    
    
    
    Node start = Proxy.findNearestNode(sLat,sLng);
    Node end = Proxy.findNearestNode(eLat,eLng);
    
    if (start.equals(end)) {
    	System.out.println("ERROR: Found the same start and end Node");
    	return null;
    }
    
    System.out.println("nodes");
    System.out.println(start.getID());
    System.out.println(end.getID());
    

    RiderPost rp = new RiderPost(id, start, end, numSeats, date, price);

    int maxTrips = 3; // hard-coded for now

    Set<Trip> allTrips = Proxy.getAllTrips();
    List<Trip> trips = HoppAlgo.getBestTripsforRider(allTrips, rp, maxTrips);


    System.out.println("Number of trips:"+Integer.toString(trips.size()));

    List<SendRiderTrip> sendTrips = new ArrayList<>();
    for (Trip t : trips) {
      sendTrips.add(new SendRiderTrip(t.getToken(), t.getDriver().getToken(),
          t.getOrigin().getID(), t.getOrigin().getLatitude(), 
          t.getOrigin().getLongitude(),t.getDestination().getID(), 
          t.getDestination().getLatitude(), 
          t.getDestination().getLongitude(),t.getDriver().getFirstName(),
          t.getDriver().getLastName(), t.getDepartureDate(),
          t.getAvailable(), t.getCost()));
    }

    return CompletableFuture.completedFuture(sendTrips);
  }

  public CompletableFuture<Integer> handleDriverPost(LinkedHashMap<String, Double> origin, 
      LinkedHashMap<String, Double> dest, Integer seats, Integer price, String driverID, 
      Double detour, String date) {
	  
    Driver d = Proxy.getDriverFromToken(driverID);

    if (d == null) {
      System.out.println("ERROR: Driver not in database");
    }


    /** NOTIFICATION LOGIC
    String output = "you just posted a trip my man.";
    boolean exists = UserStorage.getInstance().getUsers().contains(driverID);
    if (exists) {
      System.out.println("sending notification to driver");
      simpMessagingTemplate.convertAndSend("/topic/notifications/" + driverID, output);
      System.out.println("done with notification");
    }
    NOTIFICATION LOGIC **/

    Double sLat = (Double) origin.get("lat");
    Double sLng = (Double) origin.get("lng");
    Double eLat = (Double) dest.get("lat");
    Double eLng = (Double) dest.get("lng");

    
    
    Node start = Proxy.findNearestNode(sLat,sLng);
    Node end = Proxy.findNearestNode(eLat,eLng);
    
    if (start.equals(end)) {
    	System.out.println("ERROR: Found the same start and end Node");
    	return null;
    }

    //generate unique trip ID
    String tripID = UUID.randomUUID().toString();

    List<String> stops = new ArrayList<>();
    List<Double> distances = new ArrayList<>();


    //create a trip with first two stops
    Trip t = new Trip(tripID, d, seats, seats, price, start, 
        end, new HashSet<Rider>(), date, detour, new ArrayList<String>(), 
        new ArrayList<Double>());

    
    
    stops.add(start.getID());
    stops.add(end.getID());
    t.setStops(stops);
    Proxy.insertTrip(t);
    d.addTripID(tripID);
    Proxy.updateDriver(d);
    
    
    Double out = HoppAlgo.handleDriverPost(t);

    if (out.equals(-1.0)) {
      System.out.println("Could not find a path between "
          + "driver nodes, please pick a new pair");
      return CompletableFuture.completedFuture(-1);
    }

    
    distances.add(out);
    t.setDistances(distances);
    Proxy.updateTrip(t);
    
    return CompletableFuture.completedFuture(1);
  }

}
