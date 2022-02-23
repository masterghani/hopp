package edu.brown.cs.student.database;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.brown.cs.student.driver.Driver;
import edu.brown.cs.student.driver.RiderTripWrapper;
import edu.brown.cs.student.driver.Trip;
import edu.brown.cs.student.map.Node;
import edu.brown.cs.student.map.NodePair;
import edu.brown.cs.student.map.Way;
import edu.brown.cs.student.rider.Rider;

public class ProxyTest {
	
	//Note that the tests for the insert methods were the successful creation of the tables
	
	private Rider rider = null;
	private List<String> notifications = null;
	private Driver driver = null;
	private Node origin = null;
	private Node destination = null;
	private Trip trip = null;
	private Way wayz = null;
	private RiderTripWrapper rtw = null;
	
	@BeforeEach 
	public void setUp() {
		try {
	      	Proxy.connect();
	      } catch (SQLException e) {
	      	System.out.println("ERROR: Could not connect to database");
	      	return;
	      } catch (ClassNotFoundException e) {
	      	System.out.println("ERROR: Could not find Redshift Driver");
	      	return;
	      }
	}
	
	@BeforeEach
	public void createRider() {
		this.notifications = new ArrayList<>();
		this.notifications.add("yolo");
		this.notifications.add("you have been owned");
		this.notifications.add("im so tired");
		List<String> tripIDs = new ArrayList<>();
		List<Integer> numSeats = new ArrayList<>();
		List<NodePair> rstops = new ArrayList<>();
		tripIDs.add("trip1");
		tripIDs.add("trip2");
		numSeats.add(1);
		numSeats.add(2);
		rstops.add(new NodePair("node1", "node2"));
		rstops.add(new NodePair("node3", "node4"));
		this.rider = new Rider("userNameTestor", "firstNameTest", "lastNameTest", 
				"address   address Testing 123", "binary 467", "+17830992399888", 
				"345/12/2122", "hjjh@smail.com","123456789", tripIDs,
				numSeats, rstops, this.notifications);	
	}
	@BeforeEach
	public void createDriver() {
		this.notifications = new ArrayList<>();
		this.notifications.add("yolo");
		this.driver = new Driver("userName", "firstName", 
			      "lastName", "address", 
			      "gender", "phoneNumber", 
			      "birthday", "email", 
			      "licenseNumber", "carMakeandModel", "123456789", this.notifications,this.notifications);
	}
	
	@BeforeEach
	public void createNodes() {
		this.origin = new Node("origin", 1.0, 2.0);
		this.destination = new Node("dest", 4.0, 5.0);
	}
	
	@BeforeEach
	public void createTrip() {
		this.trip = new Trip("token", this.driver, 5, 10, this.origin,
				this.destination, "2020-04-27", 10.0);
		
	}

	@BeforeEach
	public void createWay() {
		this.wayz = new Way("id", this.origin, this.destination);
		
	}
	
	@BeforeEach
	public void createRiderTripWrapper() {
		List<Double> distances = new ArrayList<>();
		distances.add(1.0);
		distances.add(2.0);
		distances.add(3.0);
		distances.add(4.0);
		this.rtw = new RiderTripWrapper("t", "riderID","point1", 
				"point2",distances , 10.0,5);
		
	}
	
	
	
	@AfterEach 
	public void tearDown() {
		
		try {
		
		Proxy.removeHelper(this.rider.getToken(), "riders");
		Proxy.removeHelper(this.driver.getToken(), "drivers");
		Proxy.removeHelper(this.origin.getID(), "node");
		Proxy.removeTrip(this.trip.getToken());
		Proxy.removeHelper(this.wayz.getID(), "way");
		Proxy.removeRiderTripWrapper(this.rtw.getTripID(), this.rtw.getRiderID());
		
		Proxy.closeEverything();
		} catch (NullPointerException e) 
		{}
		this.driver = null;
		this.rider = null;
		this.origin = null;
		this.destination = null;
		this.trip = null;
		this.wayz = null;
		this.rtw = null;

		
	}
	
	
	@Test
	public void testRiderExists() {
		createRider();
		assertFalse(Proxy.checkRiderExists("123456789"));
		tearDown();
	}
	
	@Test
	public void testRiderInsert() {
		createRider();
		Proxy.insertRider(this.rider);
		assertTrue(Proxy.checkRiderExists("123456789"));
		tearDown();
	}
	
	@Test
	public void testRiderUpdate() {
		createRider();
		Proxy.insertRider(this.rider);
		this.rider.setToken("newtoken");
		Proxy.updateRider(this.rider);
		Rider r = Proxy.getRiderFromToken("newtoken");
		assertFalse(r != null);
		tearDown();
	}
	
	
	@Test
	public void testDriverExists() {
		createDriver();
		assertFalse(Proxy.checkDriverExists("123456789"));
		tearDown();
	}
	
	@Test
	public void testDriverInsert() {
		createDriver();
		Proxy.insertDriver(this.driver);
		assertTrue(Proxy.checkDriverExists("123456789"));
		tearDown();
	}
	
	@Test
	public void testDriverUpdate() {
		createDriver();
		Proxy.insertDriver(this.driver);
		this.driver.setToken("newtoken");
		Proxy.updateDriver(this.driver);
		Driver d = Proxy.getDriverFromToken("newtoken");
		assertFalse(d != null);
		tearDown();
	}
	
	
	@Test
	public void testNodeExists() {
		createNodes();
		assertFalse(Proxy.checkNodeExists("origin"));
		tearDown();
	}
	
	@Test
	public void testNodeInsert() {
		createNodes();
		Proxy.insertNode(this.origin);
		assertTrue(Proxy.checkNodeExists("origin"));
		tearDown();
	}
	
	@Test
	public void testNodeUpdate() {
		createNodes();
		Proxy.insertNode(this.origin);
		this.origin.setID("neworigin");
		Proxy.updateNode(this.origin);
		Node n = Proxy.getNodeFromToken("neworigin");
		assertFalse(n != null);
		tearDown();
	}
	
	
	@Test
	public void testTripExists() {
		createTrip();
		assertFalse(Proxy.checkTripExists("token"));
		tearDown();
	}
	
	@Test
	public void testTripInsert() {
		createNodes();
		Proxy.insertTrip(this.trip);
		assertTrue(Proxy.checkTripExists("token"));
		tearDown();
	}
	
	@Test
	public void testTripUpdate() {
		createTrip();
		Proxy.insertTrip(this.trip);
		this.trip.setToken("newtoken");

		Proxy.updateTrip(this.trip);
		Trip t = Proxy.getTripFromToken("newtoken");
		assertFalse(t != null);
		tearDown();
	}
	
	
	@Test
	public void testWayExists() {
		createWay();
		assertFalse(Proxy.checkTripExists("id"));
		tearDown();
	}
	
	@Test
	public void testWayInsert() {
		createWay();
		Proxy.insertWay(this.wayz);
		assertTrue(Proxy.checkWayExists("id"));
		tearDown();
	}
	
	@Test
	public void testWayUpdate() {
		createWay();
		Proxy.insertWay(this.wayz);
		this.wayz.setID("newid");
		
		Proxy.updateWay(this.wayz);
		Way w = Proxy.getWayFromToken("newid");

		assertFalse(w != null);
		tearDown();
	}
	
	@Test
	public void testRTWExists() {
		createRiderTripWrapper();
		assertTrue(Proxy.checkRiderTripWrapperExists("t", "riderID"));
		tearDown();
	}
	
	@Test
	public void testRTWInsert() {
		createRiderTripWrapper();
		Proxy.insertRiderTripWrapper(this.rtw);
		assertTrue(Proxy.checkRiderTripWrapperExists(this.rtw.getTripID(), this.rtw.getRiderID()));
		tearDown();
	}
	
	@Test
	public void testRTWUpdate() {
		createRiderTripWrapper();
		Proxy.insertRiderTripWrapper(this.rtw);
		this.rtw.setTripID("newt");
		Proxy.updateRiderTripWrapper(this.rtw);
		RiderTripWrapper x = Proxy.getRiderTripWrapper(this.rtw.getTripID(), this.rtw.getRiderID());
		assertFalse(x != null);
		tearDown();
	}
	
}
	


	
	