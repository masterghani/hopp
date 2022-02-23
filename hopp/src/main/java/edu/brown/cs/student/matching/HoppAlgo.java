package edu.brown.cs.student.matching;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.MinMaxPriorityQueue;

import edu.brown.cs.student.database.Proxy;
import edu.brown.cs.student.driver.RiderTripWrapper;
import edu.brown.cs.student.driver.Trip;
import edu.brown.cs.student.geocoding.Geocode;
import edu.brown.cs.student.map.Astar;
import edu.brown.cs.student.map.Haversine;
import edu.brown.cs.student.map.Node;
import edu.brown.cs.student.map.NodeDist;
import edu.brown.cs.student.map.TripDist;
import edu.brown.cs.student.rider.RiderPost;



public final class HoppAlgo {

  private final static double earthRadius = (6378.137 + 6356.752) / 2;
  private static final boolean False = false;
  private static final boolean True = false;
  private static Haversine heuristic = new Haversine(earthRadius);
  private static Astar shortestPathSearch = new Astar(heuristic);
  
  
  private static Comparator<TripDist> tripComparator = new Comparator<TripDist>() {

    @Override
    public int compare(TripDist o1, TripDist o2) {
      return o1.getDist().compareTo(o2.getDist());
    }

  };

  public static double getShortestDist(Node start, Node end) {
    return shortestPathSearch.getShortestDistance(start, end);
  }

  
  public static Double handleDriverPost(Trip t) {

    //this the nearest coords to the location that the driver put in
    Node origin = t.getOrigin();
    Node destination = t.getDestination();
    Double shortestDistance = shortestPathSearch.getShortestDistance(origin, destination);
    
    if (shortestDistance==-1) {
    	System.out.println("No path between driver nodes");
    	return -1.0;
    }
    return shortestDistance;

  }


  public static Double MultipleStopDistance(List<NodeDist> nodeDistLst, 
		  String stop1, String stop2) {

    if (stop1.equals(stop2)) {
      return 0.0;
    }
    Double total = 0.0;
    boolean counting = False;
    for (NodeDist nd : nodeDistLst) {
    	
    	
    	//stop2 should always come after stop1
    	
    	if (nd.getNode().getID().equals(stop2)) {
            return total;
          }
    	
    	if (counting) {
        total += nd.getDist();
      }
    	if (nd.getNode().getID().equals(stop1)) {
        counting = True;
        total += nd.getDist();
      }   

    }

    return total;

  }


  //this will return 7 strings which correspond to the following:

  //point in stop list where we should insert rider origin (Point1) after 
  //point in stop list where we should insert rider destination (Point2) after 
  //distance from Point1 to rider origin 
  //distance from rider origin to next closest stop
  //distance from Point2 to rider destination
  //distance from rider destination to next closest stop
  //length of best detour



  public static List<String> findBestDetour(Trip t, RiderPost rp) {
    Double minDetour = Double.MAX_VALUE;
    String bestPoint1 = null;
    String bestPoint2 = null;
    String Point1 = null;
    String Point2 = null;
    Double bestd1 = null;
    Double bestd2 = null;
    Double bestd3 = null;
    Double bestd4 = null;
    Double d1 = null;
    Double d2 = null;
    Double d3 = null;
    Double d4 = null;

    List<String> output = new ArrayList<>();


    Node driverOrigin = t.getOrigin();
    Node driverDestination = t.getDestination();


    Node riderOrigin = rp.getOrigin();
    Node riderDestination = rp.getDestination();


    shortestPathSearch.clear(riderOrigin, riderDestination);
    Double shortestRiderDist = shortestPathSearch.
        getShortestDistance(riderOrigin, riderDestination);
    
    if (shortestRiderDist==-1) {
    	System.out.println("No path between rider nodes");
    	return null;
    }
    
    
    //No riders on the trip yet
    if (t.getRiders().size()==0) {
      
      d1 = shortestPathSearch.getShortestDistance(driverOrigin, riderOrigin);
      d4 = shortestPathSearch.getShortestDistance(riderDestination, driverDestination);

      //start -> O(r) -> D(r) -> end
      
      List<NodeDist> NodeDistLst = Proxy.getStops(t.getToken());
      
      Double currentDist = null;
      
      if (NodeDistLst.isEmpty()) {
    	  currentDist = shortestPathSearch.
    		        getShortestDistance(driverOrigin, driverDestination);
      } else {
    	  currentDist = NodeDistLst.get(0).getDist();
      }
      
      //first index in the list of stops to insert the rider
      output.add(driverOrigin.getID());
      
      //empty to convey that the rider destination should be put after the rider origin
      output.add(riderOrigin.getID());
      output.add(Double.toString(d1));
      output.add(Double.toString(0.0));
      output.add(Double.toString(shortestRiderDist));
      output.add(Double.toString(d4));
      output.add(Double.toString(d1+d4+shortestRiderDist-currentDist));
      return output;

    } else {
    	
      //case 1 si -> O(r) -> O(d) -> si+1 

      List<NodeDist> NodeDistLst = Proxy.getStops(t.getToken());
      
      
      for (int i=0; i < NodeDistLst.size()-1; i++) {
        NodeDist nd = NodeDistLst.get(i);
        NodeDist nd1 = NodeDistLst.get(i+1);

        Double currentDist = nd.getDist();
        if (currentDist==null) {
        	currentDist = shortestPathSearch.
    		        getShortestDistance(nd.getNode(), nd1.getNode());
        }
        
        d1 = shortestPathSearch.getShortestDistance(nd.getNode(), riderOrigin);
        d4 = shortestPathSearch.getShortestDistance(riderDestination, nd1.getNode());
        
        Point1 = nd.getNode().getID();
        Point2 = riderOrigin.getID();

        Double detour = (d1+shortestRiderDist+d4)- currentDist;
        if (detour <= 0) {
        	output.add(Point1);
            output.add(Point2);
            output.add(Double.toString(d1));
            output.add(Double.toString(0.0));
            output.add(Double.toString(shortestRiderDist));
            output.add(Double.toString(d4));
            output.add(Double.toString(detour));
            return output;
        }
        
        if (detour < minDetour) {
          minDetour = detour;
          bestd1 = d1;
          bestd2 = 0.0;
          bestd3 = shortestRiderDist;
          bestd4 = d4;
          bestPoint1 = Point1;
          bestPoint2 = Point2;
          
        }

      }

      //case 2 si -> O(r) -> si+1 -> sk -> O(d) -> sk+1
      if (NodeDistLst.size() >=4) {

      for (int j=0; j < NodeDistLst.size()-2; j++) {
        for (int k=j+2; k < NodeDistLst.size()-1; k++) {
          NodeDist sj = NodeDistLst.get(j);
          NodeDist sj1 = NodeDistLst.get(j+1);
          NodeDist sk = NodeDistLst.get(k);
          NodeDist sk1 = NodeDistLst.get(k+1);


          Double currentTotal = MultipleStopDistance(NodeDistLst, 
              sj.getNode().getID(),sk1.getNode().getID());

          
          d1 = shortestPathSearch.getShortestDistance(sj.getNode(), riderOrigin);
          d2 = shortestPathSearch.getShortestDistance(riderOrigin, sj1.getNode());

          Double d5 = MultipleStopDistance(NodeDistLst, 
              sj1.getNode().getID(),sk.getNode().getID());
          
          
          d3 = shortestPathSearch.getShortestDistance(sk.getNode(), riderDestination);
          d4 = shortestPathSearch.getShortestDistance(riderDestination, sk1.getNode());

          Point1 = sj.getNode().getID();
          Point2 = sk.getNode().getID();

          Double newTotal = (d1+d2+d3+d4+d5);

          Double detour = newTotal - currentTotal;

          if (detour < minDetour) {
            minDetour = detour;
            bestd1 = d1;
            bestd2 = d2;
            bestd3 = d3;
            bestd4 = d4;
            bestPoint1 = Point1;
            bestPoint2 = Point2;
            
            
            
          }
        }

      }
      
      }


    }
    output.add(bestPoint1);
    output.add(bestPoint2);
    output.add(Double.toString(bestd1));
    output.add(Double.toString(bestd2));
    output.add(Double.toString(bestd3));
    output.add(Double.toString(bestd4));
    output.add(Double.toString(minDetour));
    return output;
  }


  public static List<Trip> getBestTripsforRider(Set<Trip> trips, RiderPost rp, int maxTrips) {


    MinMaxPriorityQueue<TripDist> queue = MinMaxPriorityQueue.orderedBy(tripComparator)
        .maximumSize(maxTrips)
        .create();

    for (Trip t : trips) {
    	
    	//cannot suggest trip if it does not have enough spots, or if cost is greater 
    	//than what rider is willing to pay
    	if (t.getAvailable() < rp.getNumSeats() || t.getCost() > rp.getCost()) {
    		continue;
    	}
    	
      List<String> output = findBestDetour(t, rp);
      Double bestDetour = Double.parseDouble(output.get(6));
      
      System.out.println("got a best detour, adding to queue");
      queue.add(new TripDist(t, bestDetour));

      
      
      String point1 = output.get(0);
      String point2 = output.get(1);
      List<Double> distances = new ArrayList<>();
      distances.add(Double.parseDouble(output.get(2)));
      distances.add(Double.parseDouble(output.get(3)));
      distances.add(Double.parseDouble(output.get(4)));
      distances.add(Double.parseDouble(output.get(5)));
      
      
      System.out.println("before Create rider trip wrapper");
      RiderTripWrapper rtw = new RiderTripWrapper(t.getToken(),rp.getRiderToken(),
          point1, point2, distances, bestDetour, rp.getNumSeats());
      System.out.println("after Create rider trip wrapper");

      if (Proxy.checkRiderTripWrapperExists(t.getToken(), rp.getRiderToken())==false) {
        Proxy.insertRiderTripWrapper(rtw);
        System.out.println("insert rider trip wrapper");
      } else {
    	  System.out.println("update Create rider trip wrapper");
        Proxy.updateRiderTripWrapper(rtw);
      }
    }
    

    List<Trip> orderedTrips = new ArrayList<>();

    int size = queue.size();
    
    
    //return geocoded origins and destinations for display purposes
    
    for (int i = 0; i < size; i++) {
      Trip t = queue.pollFirst().getTrip();
      Double sLat = t.getOrigin().getLatitude();
      Double sLng = t.getOrigin().getLongitude();
      t.setOrigin(new Node(Geocode.reverseGeocode(sLat, sLng), sLat, sLng));
      Double eLat = t.getDestination().getLatitude();
      Double eLng = t.getDestination().getLongitude();
      t.setDestination(new Node(Geocode.reverseGeocode(eLat, eLng), eLat, eLng));
      orderedTrips.add(t);
    }
    

    return orderedTrips;

  }
  }
