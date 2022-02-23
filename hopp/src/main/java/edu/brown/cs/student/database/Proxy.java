package edu.brown.cs.student.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.brown.cs.student.driver.Driver;
import edu.brown.cs.student.driver.RiderTripWrapper;
import edu.brown.cs.student.driver.Trip;
import edu.brown.cs.student.map.Node;
import edu.brown.cs.student.map.NodeDist;
import edu.brown.cs.student.map.NodePair;
import edu.brown.cs.student.map.Way;
import edu.brown.cs.student.rider.Rider;

public final class Proxy {

  static final String dbURL = "jdbc:redshift://hopp-db.c0wipsh0sggr.us-east-2.redshift.amazonaws.com:5440/hopp";
  static final String MasterUsername = "hsingh574";
  static final String MasterUserPassword = "Hopp1234!";
  private static Connection conn = null;
  private static Statement stmt = null;
  private static PreparedStatement prep = null;

  public void doOnceFunction() {
	  setUpRiderTable();
	  setUpDriverTable();
	  setUpTripTable();
	  setUpNodeTable();
	  setUpWayTable();
	  setUpRiderTripWrapperTable();

  }

  public static void connect() throws ClassNotFoundException, SQLException {

      // Dynamically load driver at runtime.
      Class.forName("com.amazon.redshift.jdbc42.Driver");

      // Open a connection and define properties.
      System.out.println("Connecting to database...");

      Properties props = new Properties();

      
      //Using a keystore on the server for security
      props.setProperty("ssl", "true");
      props.setProperty("user", MasterUsername);
      props.setProperty("password", MasterUserPassword);
      conn = DriverManager.getConnection(dbURL, props);
      System.out.println("Finished connectivity test.");
      
  }

  public static void closeEverything() {
    // Finally block to close resources.
    try {
      if (stmt != null)
        stmt.close();
    } catch (Exception ex) {
    } // nothing we can do
    try {
      if (conn != null)
        conn.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void setUpRiderTable() {
    try {
      stmt = conn.createStatement();
      String sql;
      sql = "CREATE TABLE riders (" + "ID VARCHAR(100) NOT NULL, "
          + "USERNAME VARCHAR(100) NOT NULL, "
          + "FIRSTNAME VARCHAR(100) NOT NULL, "
          + "LASTNAME VARCHAR(100) NOT NULL, "
          + "ADDRESS VARCHAR(200) NOT NULL, " + "GENDER VARCHAR(45) NOT NULL, "
          + "PHONENUMBER VARCHAR(45) NOT NULL, "

          + "BIRTHDAY VARCHAR(100) NOT NULL, "
          + "EMAIL VARCHAR(100) NOT NULL, "
          + "RATING FLOAT, "
          + "TRIP_IDS VARCHAR(5000), "
          + "NUM_SEATS VARCHAR(300), "
          + "ORIGINS VARCHAR(5000), "
          + "DESTINATIONS VARCHAR(5000), "
          + "NOTIFICATIONS VARCHAR(5000), "
          
          
          + "PRIMARY KEY (ID));";

      stmt.executeUpdate(sql);
      System.out.println("Sucessfully created rider table.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void setUpDriverTable() {
    try {
      stmt = conn.createStatement();
      String sql;

      sql = "CREATE TABLE drivers ("
          + "ID VARCHAR(100) NOT NULL, "
          + "RATING FLOAT, "
          + "LICENSE VARCHAR(30) NOT NULL, "
          + "CAR VARCHAR(200) NOT NULL, "
          + "TRIP_IDS VARCHAR(5000) NOT NULL, "
          
          + "NOTIFICATIONS VARCHAR(5000), "
          + "PRIMARY KEY (ID));";

      stmt.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void setUpTripTable() {
    try {
      stmt = conn.createStatement();
      String sql;
      sql = "CREATE TABLE trips (" + "ID VARCHAR(100) NOT NULL, "
          + "DRIVER_ID VARCHAR(100) NOT NULL, " + "ORIGIN VARCHAR(100) NOT NULL, "
          + "DESTINATION VARCHAR(100) NOT NULL, " + "CAPACITY INT NOT NULL, "
          + "AVAILABLE INT NOT NULL, " + "COST INT NOT NULL, "
          + "RIDERS VARCHAR(1000), " + "DEPARTURE_DATE DATE NOT NULL, "
          + "MAX_DETOUR DOUBLE PRECISION NOT NULL, "

          + "STOPS VARCHAR(10000) NOT NULL, "
          + "DISTANCES VARCHAR(2000) NOT NULL, "

          + "PRIMARY KEY (ID), FOREIGN KEY (DRIVER_ID) "
          + "REFERENCES drivers(ID))";
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void setUpNodeTable() {
    try {
      stmt = conn.createStatement();
      String sql;

      sql = "CREATE TABLE node ("
          + "ID VARCHAR(250) NOT NULL, "
          + "LATITUDE DOUBLE PRECISION NOT NULL, "
          + "LONGITUDE DOUBLE PRECISION NOT NULL, "

          + "PRIMARY KEY (ID));";
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void setUpWayTable() {
    try {
      stmt = conn.createStatement();
      String sql;
      sql = "CREATE TABLE way (" + "ID VARCHAR(100) NOT NULL, "
          + "TYPE VARCHAR(30), " + "STARTING VARCHAR(100) NOT NULL, "
          + "ENDING VARCHAR(100) NOT NULL, "

          + "PRIMARY KEY (ID),FOREIGN KEY (STARTING) "
          + "REFERENCES node(ID),FOREIGN KEY (ENDING) "
          + "REFERENCES node(ID));";
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void setUpRiderTripWrapperTable() {
	    try {
	      stmt = conn.createStatement();
	      String sql;
	      sql = "CREATE TABLE rider_trip_wrapper ("
	          + "ID int NOT NULL identity(1, 1), " + "TRIP_ID VARCHAR(100) NOT NULL, "
	          + "RIDER_ID VARCHAR(100) NOT NULL, " + "FIRST_POINT VARCHAR(100) NOT NULL, "
	          + "SECOND_POINT VARCHAR(100) NOT NULL, "
	          + "DISTANCES VARCHAR(500) NOT NULL, " + "DETOUR DOUBLE PRECISION NOT NULL, "
	          + "NUM_SEATS INT NOT NULL, "
	          + "PRIMARY KEY (ID));";
	      stmt.executeUpdate(sql);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
  

  public static boolean checkExistsHelper(String query, String token) {
    PreparedStatement prep;
    ResultSet rs;
    int output;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, token);
      rs = prep.executeQuery();
      while (rs.next()) {
        output = rs.getInt(1);
        rs.close();
        return (output == 1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  
  

  /** Next four methods are for Rider Logic **/


  // insert rider that does not exist
  public static void insertRider(Rider rider) {
    String query = "INSERT INTO riders (ID, USERNAME, FIRSTNAME, LASTNAME, "
        + "ADDRESS, GENDER, PHONENUMBER, BIRTHDAY, EMAIL, RATING, "
        + "TRIP_IDS, NUM_SEATS, ORIGINS, DESTINATIONS, NOTIFICATIONS) " + 
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, rider.getToken());
      prep.setString(2, rider.getUserName());
      prep.setString(3, rider.getFirstName());
      prep.setString(4, rider.getLastName());
      prep.setString(5, rider.getAddress());
      prep.setString(6, rider.getGender());
      prep.setString(7, rider.getPhoneNumber());
      prep.setString(8, rider.getBirthday());
      prep.setString(9, rider.getEmail());
      prep.setFloat(10, rider.getRating());
      
      StringBuilder tripIDs = new StringBuilder();
      StringBuilder numSeats = new StringBuilder();
      StringBuilder origins = new StringBuilder();
      StringBuilder destinations = new StringBuilder();
      StringBuilder notifications = new StringBuilder();
      
      List<NodePair> rStops = rider.getStops();
      List<String> rTripIDs = rider.getTripIDs();
      List<Integer> rNumSeats = rider.getNumSeats();
      List<String> rNots = rider.getNotifications();
      
      for (int i = 0; i< rStops.size();i++) {
    	  tripIDs.append(rTripIDs.get(i));
    	  tripIDs.append(",");
    	  numSeats.append(Integer.toString(rNumSeats.get(i)));
    	  numSeats.append(",");
    	  NodePair np = rStops.get(i);
    	  origins.append(np.getOrigin());
    	  origins.append(",");
    	  destinations.append(np.getDestination());
    	  destinations.append(",");

      }
      
      
      for (int j = 0; j < rNots.size(); j++) {
    	  notifications.append(rNots.get(j));
    	  notifications.append(","); 
      }
      
      
      
      prep.setString(11, tripIDs.toString().trim());
      prep.setString(12, numSeats.toString().trim());
      prep.setString(13, origins.toString().trim());
      prep.setString(14, destinations.toString().trim());
      prep.setString(15, notifications.toString().trim());
      prep.executeUpdate();
      System.out.println("Sucessfully inserted rider.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // update rider already in the databases
  public static void updateRider(Rider rider) {
    String query = "UPDATE riders SET USERNAME=?, FIRSTNAME=?, LASTNAME=?, "
        + "ADDRESS=?, GENDER=?, PHONENUMBER=?, BIRTHDAY=?, EMAIL=?, RATING=?, "
        + "TRIP_IDS=?, NUM_SEATS=?, ORIGINS=?, DESTINATIONS=?, "
        + "NOTIFICATIONS=? where ID=?;";
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, rider.getUserName());
      prep.setString(2, rider.getFirstName());
      prep.setString(3, rider.getLastName());
      prep.setString(4, rider.getAddress());
      prep.setString(5, rider.getGender());
      prep.setString(6, rider.getPhoneNumber());
      prep.setString(7, rider.getBirthday());
      prep.setString(8, rider.getEmail());
      prep.setFloat(9, rider.getRating());
      
      
      StringBuilder tripIDs = new StringBuilder();
      StringBuilder numSeats = new StringBuilder();
      StringBuilder origins = new StringBuilder();
      StringBuilder destinations = new StringBuilder();
      StringBuilder notifications = new StringBuilder();
      
      List<NodePair> rStops = rider.getStops();
      List<String> rTripIDs = rider.getTripIDs();
      List<Integer> rNumSeats = rider.getNumSeats();
      List<String> rNots = rider.getNotifications();
      
      for (int i = 0; i< rStops.size();i++) {
    	  tripIDs.append(rTripIDs.get(i));
    	  tripIDs.append(",");
    	  numSeats.append(Integer.toString(rNumSeats.get(i)));
    	  numSeats.append(",");
    	  NodePair np = rStops.get(i);
    	  origins.append(np.getOrigin());
    	  origins.append(",");
    	  destinations.append(np.getDestination());
    	  destinations.append(",");
    	  
      }
      
      for (int j = 0; j < rNots.size(); j++) {
    	  notifications.append(rNots.get(j));
    	  notifications.append(","); 
      }
      
      
      prep.setString(10, tripIDs.toString().trim());
      prep.setString(11, numSeats.toString().trim());
      prep.setString(12, origins.toString().trim());
      prep.setString(13, destinations.toString().trim());
      prep.setString(14, notifications.toString().trim());
      prep.setString(15, rider.getToken());
      prep.executeUpdate();
      System.out.println("Sucessfully updated rider.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean checkRiderExists(String token) {
    String query = "SELECT 1 FROM riders WHERE ID = ?;";
    return checkExistsHelper(query, token);
  }

  public static Rider getRiderFromToken(String token) {
    String query = "SELECT * FROM riders WHERE ID = ?;";
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, token);
      rs = prep.executeQuery();
      
      while (rs.next()) {

    	  List<String> tripIDs = new ArrayList<>();
    	  List<Integer> numSeats = new ArrayList<>();
    	  List<NodePair> stops = new ArrayList<>();
    	  List<String> notifications = new ArrayList<>();
    	  
    	  String [] tIDs = rs.getString("trip_ids").split(",");
    	  
    	  String [] ns = rs.getString("num_seats").split(",");
    	  String [] orgs = rs.getString("origins").split(",");
    	  String [] dests = rs.getString("destinations").split(",");

    	  
    	  
    	  if (!tIDs[0].equals("")) {

    	  
    	  for (int i=0 ; i < tIDs.length;i++) {
    		  if (!tIDs[i].equals("")) {
    			  tripIDs.add(tIDs[i]);
    			  if (!ns[i].equals("")) {
    				  numSeats.add(Integer.parseInt(ns[i]));
    			  }
    		stops.add(new NodePair(orgs[i], dests[i]));
    			  
    		  }  
    	  }
    	  
    	  }
    	  
        return new Rider(rs.getString("USERNAME"), rs.getString("FIRSTNAME"), 
            rs.getString("LASTNAME"),rs.getString("ADDRESS"), 
            rs.getString("GENDER"), rs.getString("PHONENUMBER"), 
            rs.getString("BIRTHDAY"), rs.getString("EMAIL"), 
            rs.getString("ID"), tripIDs, numSeats, stops, notifications);

      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Next four methods are for Driver Logic **/

  // insert driver not in database
  public static void insertDriver(Driver driver) {

    String query = "INSERT INTO drivers " + 
        "VALUES (?, ?, ?, ?, ?, ?);";

    PreparedStatement prep;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, driver.getToken());
      prep.setDouble(2, driver.getRating());
      prep.setString(3, driver.getLicenseNumber());
      prep.setString(4, driver.getCarMakeandModel());
      
      
      StringBuilder tIDs = new StringBuilder();
      for (String tripID : driver.getTripIDs()) {
    	  tIDs.append(tripID);
    	  tIDs.append(",");
      }
      
      StringBuilder notifications = new StringBuilder();
      for (String not : driver.getNotifications()) {
    	  notifications.append(not);
    	  notifications.append(",");
      }
      
      prep.setString(5,tIDs.toString().trim());
      prep.setString(6, notifications.toString().trim());
      prep.executeUpdate();
      System.out.println("Sucessfully inserted driver.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // update driver already in the database
  public static void updateDriver(Driver driver) {
    String query = "UPDATE drivers SET RATING=?, LICENSE=?, "
        + "CAR=?, TRIP_IDS=?, NOTIFICATIONS=? where ID=?;";
    try {
      prep = conn.prepareStatement(query);
      prep.setDouble(1, driver.getRating());
      prep.setString(2, driver.getLicenseNumber());
      prep.setString(3, driver.getCarMakeandModel());
      
      StringBuilder notifications = new StringBuilder();
      for (String not : driver.getNotifications()) {
    	  notifications.append(not);
    	  notifications.append(",");
      }
      
      StringBuilder tIDs = new StringBuilder();
      for (String tripID : driver.getTripIDs()) {
    	  tIDs.append(tripID);
    	  tIDs.append(",");
      }
      
      prep.setString(4, tIDs.toString().trim());
      prep.setString(5, notifications.toString().trim());
      prep.setString(6, driver.getToken());
      prep.executeUpdate();
      System.out.println("Sucessfully updated driver.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean checkDriverExists(String token) {
    String query = "SELECT 1 FROM drivers WHERE ID = ?;";
    return checkExistsHelper(query, token);
  }

  public static Driver getDriverFromToken(String token) {
    String query = "SELECT * FROM drivers WHERE ID = ?;";
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, token);
      rs = prep.executeQuery();
      String license = null;
      String car = null;
      Double rating = null;
      List<String> notifications = new ArrayList<>();
      List<String> tIDs = new ArrayList<>();
      while (rs.next()) {
        license = rs.getString("LICENSE");
        car = rs.getString("CAR");
        rating = rs.getDouble("RATING");
        
        String[] temps = rs.getString("trip_ids").split(",");
        
        for (String t : temps) {
        	if (!t.equals("")) {
        		tIDs.add(t);
        	}
        	
        }
        for (String x : rs.getString("notifications").split(",")) {
        	if (!x.equals("")) {
        		notifications.add(x);
        	}
        }
        
        Rider r = getRiderFromToken(token);
        
        if (r != null) {
		return new Driver(r.getUserName(), 
              r.getFirstName(), r.getLastName(),
              r.getAddress(), r.getGender(), 
              r.getPhoneNumber(), 
              r.getBirthday(), r.getEmail(), 
              license, car, rating, token, tIDs,notifications);
        
      }
        } 
    	  rs.close();
    	  return null;
      
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Next four methods are for Trip Logic **/

  // insert trip not in database
  public static void insertTrip(Trip trip) {
    String query = "INSERT INTO trips "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement prep;
    try {
      prep = conn.prepareStatement(query);

      prep.setString(1, trip.getToken());
      prep.setString(2, trip.getDriver().getToken());
      prep.setString(3, trip.getOrigin().getID());
      prep.setString(4, trip.getDestination().getID());
      prep.setInt(5, trip.getCapacity());
      prep.setInt(6, trip.getAvailable());
      prep.setInt(7, trip.getCost());
      prep.setString(8, trip.getRiderTokens());
      prep.setDate(9, java.sql.Date.valueOf(trip.getDepartureDate()));
      prep.setDouble(10, trip.getMaxDetour());
      prep.setString(11, String.join(",", trip.getStops()));
      prep.setString(12, String.join(",", trip.getStringDistances()));
      prep.executeUpdate();

      System.out.println("Sucessfully inserted trip.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // update trip already in the database
  public static void updateTrip(Trip trip) {
    String query = "UPDATE trips " + "SET DRIVER_ID=?,ORIGIN=?,DESTINATION=?, "
        + "CAPACITY=?, AVAILABLE=?, COST=?, RIDERS=?,DEPARTURE_DATE=?, "
        + "MAX_DETOUR=?,STOPS=?,DISTANCES=? " + "WHERE ID = ?;";
    PreparedStatement prep;
    try {
      prep = conn.prepareStatement(query);

      prep.setString(1, trip.getDriver().getToken());
      prep.setString(2, trip.getOrigin().getID());
      prep.setString(3, trip.getDestination().getID());
      prep.setInt(4, trip.getCapacity());
      prep.setInt(5, trip.getAvailable());
      prep.setInt(6, trip.getCost());
      prep.setString(7, trip.getRiderTokens());
      prep.setDate(8, java.sql.Date.valueOf(trip.getDepartureDate()));
      prep.setDouble(9, trip.getMaxDetour());
      prep.setString(10, String.join(",", trip.getStops()));
      prep.setString(11, String.join(",", trip.getStringDistances()));
      prep.setString(12, trip.getToken());
      prep.executeUpdate();
      System.out.println("Sucessfully updated trip.");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean checkTripExists(String token) {
    String query = "SELECT 1 FROM trips WHERE ID = ?;";
    return checkExistsHelper(query, token);
  }

  public static Trip getTripFromToken(String token) {

    String query = "SELECT * FROM trips WHERE ID = ?;";
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, token);
      rs = prep.executeQuery();
      while (rs.next()) {
        String id = rs.getString("ID");
        Driver driver = getDriverFromToken(rs.getString("DRIVER_ID"));
        Node origin = getNodeFromToken(rs.getString("ORIGIN"));
        Node destination = getNodeFromToken(rs.getString("DESTINATION"));

        Integer capacity = rs.getInt("CAPACITY");
        Integer available = rs.getInt("AVAILABLE");
        Double maxDetour = rs.getDouble("MAX_DETOUR");
        Integer cost = rs.getInt("COST");
        Set<Rider> riders = new HashSet<>();

        String[] riderIDs = rs.getString("RIDERS").split(",");
        
        if (riderIDs.length != 0 && !riderIDs[0].equals("") && (riderIDs[0]!=null)) {
        	
            for (String r : riderIDs) {
              riders.add(getRiderFromToken(r));

            }
        	
        }
        

        String departureDate = rs.getString("DEPARTURE_DATE");

        String[] stopIDs = rs.getString("STOPS").split(",");
        String[] stringDistances = rs.getString("DISTANCES").split(",");

        List<Double> distances = new ArrayList<>();
        if (stringDistances.length != 0 && !stringDistances[0].equals("") && (stringDistances[0]!=null)) {
            for (String sd :stringDistances) {
            	distances.add(Double.parseDouble(sd));
            }
        	
        }
        
        
        List<String> stops = new ArrayList<>();
        if (stopIDs.length != 0 && !stopIDs[0].equals("") && (stopIDs[0]!=null)) {
        	
            for (String r :stopIDs) {
              stops.add(r);

            }
        	
        }
        
        return new Trip(id, driver, capacity, available, cost, origin,
            destination, riders, departureDate, maxDetour, stops, distances);

      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static List<Trip> getAllTripsForDriver(Driver d) {
	  List<Trip> output = new ArrayList<>();
	  for (String tripID : d.getTripIDs()) {
		  output.add(getTripFromToken(tripID));
	  }
	  
	  return output;
  }
  
  
  public static List<Trip> getAllTripsForRider(Rider r) {
	  List<Trip> output = new ArrayList<>();
	  for (String tripID : r.getTripIDs()) {
		  output.add(getTripFromToken(tripID));
	  }
	  
	  return output;
  
  }
  
  
  
  
  public static Set<Trip> getAllTrips() {
	  
	    String query = "SELECT * FROM trips;";
	    PreparedStatement prep;
	    ResultSet rs;
	    try {
	      prep = conn.prepareStatement(query);
	      rs = prep.executeQuery();
	      Set<Trip> output = new HashSet<>();
	      while (rs.next()) {
	        String id = rs.getString("ID");
	        Driver driver = getDriverFromToken(rs.getString("DRIVER_ID"));
	        Node origin = getNodeFromToken(rs.getString("ORIGIN"));
	        Node destination = getNodeFromToken(rs.getString("DESTINATION"));

	        Integer capacity = rs.getInt("CAPACITY");
	        Integer available = rs.getInt("AVAILABLE");
	        Double maxDetour = rs.getDouble("MAX_DETOUR");
	        Integer cost = rs.getInt("COST");

	        String[] riderIDs = rs.getString("RIDERS").split(",");
	        
	        Set<Rider> riders = new HashSet<>();
	        if (riderIDs.length != 0 && !riderIDs[0].equals("") && (riderIDs[0]!=null)) {
	            for (String r : riderIDs) {
	              riders.add(getRiderFromToken(r));
	            }	
	        }
	        

	        String departureDate = rs.getString("DEPARTURE_DATE");
	        String[] stopIDs = rs.getString("STOPS").split(",");
	        String[] stringDistances = rs.getString("DISTANCES").split(",");

	        List<Double> distances = new ArrayList<>();
	        if (stringDistances.length != 0 && !stringDistances[0].equals("") 
	        		&& (stringDistances[0]!=null)) {
	            for (String sd :stringDistances) {
	            	distances.add(Double.parseDouble(sd));
	            }
	        }
	        
	        assert(distances.size() == 2*riders.size()-1) : "Sizes of riders and "
	        		+ "distances do not match up in database. Size of Distances: " 
	        + Integer.toString(distances.size()) + ". Size of riders: " + Integer.toString(riders.size());
	        
	        List<String> stops = new ArrayList<>();
	        if (stopIDs.length != 0 && !stopIDs[0].equals("") 
	        		&& (stopIDs[0]!=null)) {
	            for (String r :stopIDs) {
	              stops.add(r);
	            } 	
	        }
	        
	        assert(distances.size() == stops.size()-1) : "Sizes of stops and "
    		+ "distances do not match up in database. Size of Distances: " 
    + Integer.toString(distances.size()) + ". Size of stops: " + Integer.toString(stops.size());

	        output.add(new Trip(id, driver, capacity, available, cost, origin,
	            destination, riders, departureDate, maxDetour, stops, distances));

	      }
	      rs.close();
	      return output;
	      
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	    return null;  
	  
  }
  
  
  
  

  /** Next four methods are for Node Logic **/

  // insert node not in database
  public static void insertNode(Node node) {
    String query = "INSERT INTO node " + "VALUES (?, ?, ?);";
    PreparedStatement prep;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, node.getID());
      prep.setDouble(2, node.getLatitude());
      prep.setDouble(3, node.getLongitude());

      prep.executeUpdate();
      System.out.println("Sucessfully inserted node.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // update node already in the database
  public static void updateNode(Node node) {
    String query = "UPDATE node SET LATITUDE=?, LONGITUDE=? " + "where ID=?;";
    PreparedStatement prep;
    try {
      prep = conn.prepareStatement(query);

      prep.setDouble(1, node.getLatitude());
      prep.setDouble(2, node.getLongitude());
      prep.setString(3, node.getID());
      prep.executeUpdate();
      System.out.println("Sucessfully updated node.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean checkNodeExists(String token) {
    String query = "SELECT 1 FROM node WHERE ID = ?;";
    return checkExistsHelper(query, token);
  }

  public static Node getNodeFromToken(String token) {
    String query = "SELECT * FROM node WHERE ID = ?;";
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, token);
      rs = prep.executeQuery();
      while (rs.next()) {
        String id = rs.getString("ID");
        Double latitude = rs.getDouble("LATITUDE");
        Double longitude = rs.getDouble("LONGITUDE");
        return new Node(id, latitude, longitude);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Next four methods are for Way Logic **/

  // insert node not in database
  public static void insertWay(Way way) {
    String query = "INSERT INTO way " + "VALUES (?, ?, ?, ?);";
    PreparedStatement prep;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, way.getID());
      prep.setString(2, way.getType());
      prep.setString(3, way.getSource().getID());
      prep.setString(4, way.getDestination().getID());
      prep.executeUpdate();
      System.out.println("Sucessfully inserted way.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // update node already in the database
  public static void updateWay(Way way) {
    String query = "UPDATE way SET TYPE=?, STARTING=?, " + "ENDING=? where"
        + " ID=?;";
    PreparedStatement prep;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, way.getType());
      prep.setString(2, way.getSource().getID());
      prep.setString(3, way.getDestination().getID());
      prep.setString(4, way.getID());
      prep.executeUpdate();
      System.out.println("Sucessfully updated way.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean checkWayExists(String token) {
    String query = "SELECT 1 FROM way WHERE ID = ?;";
    return checkExistsHelper(query, token);
  }

  public static Way getWayFromToken(String token) {
    String query = "SELECT * FROM way WHERE ID = ?;";
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, token);
      rs = prep.executeQuery();
      while (rs.next()) {
        String id = rs.getString("ID");
        String type = rs.getString("TYPE");
        Node start = getNodeFromToken(rs.getString("STARTING"));
        Node end = getNodeFromToken(rs.getString("ENDING"));
        return new Way(id, type, start, end);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  
public static void insertRiderTripWrapper(RiderTripWrapper rtw) {
 String query = "INSERT INTO rider_trip_wrapper(trip_id, rider_id, "
 		+ "first_point, second_point, distances, detour, num_seats) " + 
		 "VALUES (?, ?, ?, ?, ?, ?, ?);";
 PreparedStatement prep;
 try {
   prep = conn.prepareStatement(query);
   prep.setString(1, rtw.getTripID());
   prep.setString(2, rtw.getRiderID());
   prep.setString(3, rtw.getPointToInsertRiderOriginAfter());
   prep.setString(4, rtw.getPointToInsertRiderDestAfter());
   
   StringBuilder distances = new StringBuilder();
   for (Double d : rtw.getInsertDistances()) {
	   distances.append(Double.toString(d));
	   distances.append(",");
   }
   prep.setString(5, distances.toString().trim());
   prep.setString(6, Double.toString(rtw.getDetourForThisRider()));
   prep.setInt(7, rtw.getNumSeats());
   prep.executeUpdate();
   System.out.println("Sucessfully inserted rider trip wrapper.");
 } catch (SQLException e) {
   e.printStackTrace();
 }
}

// update node already in the database
public static void updateRiderTripWrapper(RiderTripWrapper rtw) {
 String query = "UPDATE rider_trip_wrapper SET trip_id=?, rider_id=?, first_point=?, " 
		 + "second_point=?,distances=?,detour=?, num_seats=? where trip_id=? AND rider_id=?;";
 PreparedStatement prep;
 try {
   prep = conn.prepareStatement(query);

   prep.setString(1, rtw.getTripID());
   prep.setString(2, rtw.getRiderID());
   prep.setString(3, rtw.getPointToInsertRiderOriginAfter());
   prep.setString(4, rtw.getPointToInsertRiderDestAfter());
   
   StringBuilder distances = new StringBuilder();
   for (Double d : rtw.getInsertDistances()) {
	   distances.append(Double.toString(d));
	   distances.append(",");
   }
   prep.setString(5, distances.toString().trim());
   prep.setString(6, Double.toString(rtw.getDetourForThisRider()));
   
   prep.setInt(7, rtw.getNumSeats());
   prep.setString(8, rtw.getTripID());
   prep.setString(9, rtw.getRiderID());
   prep.executeUpdate();
   System.out.println("Sucessfully updated rider trip wrapper.");
 } catch (SQLException e) {
   e.printStackTrace();
 }
}

public static boolean checkRiderTripWrapperExists(String tripID, String riderID) {
 String query = "SELECT 1 FROM rider_trip_wrapper WHERE trip_id=? AND rider_id=?;";
 
 PreparedStatement prep;
 ResultSet rs;
 int output;
 try {
   prep = conn.prepareStatement(query);
   prep.setString(1, tripID);
   prep.setString(2, riderID);
   rs = prep.executeQuery();
   while (rs.next()) {
     output = rs.getInt(1);
     return (output == 1);
   }
 } catch (SQLException e) {
   e.printStackTrace();
 }
 return false;
}




public static RiderTripWrapper getRiderTripWrapper(String tripID, String riderID) {
 String query = "SELECT * FROM rider_trip_wrapper WHERE trip_id=? AND rider_id=?;";
 PreparedStatement prep;
 ResultSet rs;
 try {
   prep = conn.prepareStatement(query);
   prep.setString(1, tripID);
   prep.setString(2, riderID);
   
   rs = prep.executeQuery();
   while (rs.next()) {
	   
	 String point1 = rs.getString("first_point");
	 String point2 = rs.getString("second_point");
	 String[] d= rs.getString("distances").split(",");
	 List<Double> distances = new ArrayList<>();
	 for (String x : d) {
		 distances.add(Double.parseDouble(x));
	 }
     Double detour = rs.getDouble("detour");
     Integer numSeats = rs.getInt("num_seats");
     
     return new RiderTripWrapper(tripID, riderID,point1, 
 			point2, distances,  detour, numSeats);
   }
 } catch (SQLException e) {
   e.printStackTrace();
 }
 return null;
}
  
 
  public static Set<Way> getForwardWays(Node start) {
    String query = "SELECT way.id, way.type, node.id, node.latitude, "
    		+ "node.longitude FROM way JOIN node ON ENDING = node.ID WHERE way.STARTING = ?;";
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, start.getID());
      rs = prep.executeQuery();

      Set<Way> forwardWays = new HashSet<Way>();

      while (rs.next()) {
    	  String id = rs.getString(1);
          String type = rs.getString(2);
          String endNodeId = rs.getString(3);
          Double endNodeLat = rs.getDouble(4);
          Double endNodeLon = rs.getDouble(5);
        Node end = new Node(endNodeId, endNodeLat, endNodeLon);
        forwardWays.add(new Way(id, type, start, end));
      }
      return forwardWays;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Set<Way> getBackwardWays(Node end) {
    String query = "SELECT way.id, way.type, node.id, node.latitude, node.longitude"
    		+ " FROM way JOIN node ON STARTING = node.ID WHERE ENDING = ?;";
    PreparedStatement prep;
    ResultSet rs;
    try {
      prep = conn.prepareStatement(query);
      prep.setString(1, end.getID());
      rs = prep.executeQuery();

      Set<Way> backwardWays = new HashSet<Way>();

      while (rs.next()) {
        String id = rs.getString(1);
        String type = rs.getString(2);
        String startNodeId = rs.getString(3);
        Double startNodeLat = rs.getDouble(4);
        Double startNodeLon = rs.getDouble(5);
        Node start = new Node(startNodeId, startNodeLat, startNodeLon);
        backwardWays.add(new Way(id, type, start, end));
      }
      return backwardWays;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  
  
  /**
   * finds and creates the nearest node for incoming lat/lon input from frontend
   * 
   * @param lat
   * @param lon
   * @return
   */

  public static Node findNearestNode(Double lat, Double lon) {
    String query = "SELECT id, latitude, longitude, "
        + "(latitude - ?)*(latitude - ?) + (longitude - ?)*(longitude - ?) as distance "
        + "FROM node ORDER BY distance LIMIT 1;";
    try {
      PreparedStatement prep = conn.prepareStatement(query);
      prep.setDouble(1, lat);
      prep.setDouble(2, lat);
      prep.setDouble(3, lon);
      prep.setDouble(4, lon);
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
    	  Double latitude = rs.getDouble("latitude");
          Double longitude = rs.getDouble("longitude");
          String id = rs.getString("id");
          rs.close();
          return new Node(id, latitude, longitude); 
      }
      return null;
      
      
    } catch (SQLException e) {
    	e.printStackTrace();
      return null;
    }
  }

  public static List<NodeDist> getStops(String tripID) {

    String query = "SELECT stops, distances from trips WHERE " + "ID=?;";
    try {
      PreparedStatement prep = conn.prepareStatement(query);
      prep.setString(1, tripID);
      ResultSet rs = prep.executeQuery();
      

      while (rs.next()) {
      List<NodeDist> nodeDistLst = new ArrayList<>();
      String[] nodes = rs.getString("stops").split(",");
      String[] dists = rs.getString("distances").split(",");
      int x = nodes.length;
      
      assert(x >= 2) : "Should have at least two stops in every trip";

      for (int i=0; i < x-1;i++) {
    	  assert (!nodes[i].equals("") && nodes[i] != null && dists[i].equals("")
    			  && dists[i]==null) : "Either node or distance for a stop is null or empty";
    		  Node n = getNodeFromToken(nodes[i]);
              Double d = Double.parseDouble(dists[i]);
              nodeDistLst.add(new NodeDist(n, d)); 
    	  
      }
      assert (!nodes[x-1].equals("") && nodes[x-1] != null && dists[x-1].equals("")
			  && dists[x-1]==null) : "Either last node or distance for a stop is null or empty";

      
      nodeDistLst.add(new NodeDist(getNodeFromToken(nodes[x-1]), 0.0));
      
      return nodeDistLst;
      
      }

    } catch (SQLException e) {
      return null;
    }
	return null;

  }

  
public static void removeHelper(String token, String tableName) {
		String query = "DELETE FROM " + tableName + " WHERE id=?;";
	    try {
	      PreparedStatement prep = conn.prepareStatement(query);
	      prep.setString(1, token);
	      prep.executeUpdate();
	    } catch (SQLException e) {
	    	System.out.println("Could not remove " + tableName+ " from database");
	    	return;
	    }
		
}
  
public static void removeTrip(String token) {
	String query = "DELETE FROM trips WHERE id=?;";
    try {
      PreparedStatement prep = conn.prepareStatement(query);
      prep.setString(1, token);
      prep.executeUpdate();
    } catch (SQLException e) {
    	System.out.println("Could not remove trip from database");
    	return;
    }
	
}

public static void removeRiderTripWrapper(String tripID, String riderID) {
	String query = "DELETE FROM trips WHERE trip_id=? AND rider_id=?;";
    try {
      PreparedStatement prep = conn.prepareStatement(query);
      prep.setString(1, tripID);
      prep.setString(2, riderID);
      prep.executeUpdate();
    } catch (SQLException e) {
    	System.out.println("Could not remove rider trip wrapper from database");
    	return;
    }
	
}

}
