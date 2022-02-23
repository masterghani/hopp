package edu.brown.cs.student.driver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DriverTest {
	private List<String> notifications;
	private Driver driver;
	@BeforeEach
	public void createDriver() {
		this.notifications = new ArrayList<>();
		this.notifications.add("yolo");
		this.driver = new Driver("userName", "firstName", 
			      "lastName", "address", 
			      "gender", "phoneNumber", 
			      "birthday", "email", 
			      "licenseNumber", "carMakeandModel", "123456789", 
			      this.notifications,this.notifications);
	}
	
	
	@Test
	public void testSomething() {
		
	}

}
