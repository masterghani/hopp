package edu.brown.cs.student.routing;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.brown.cs.student.database.Proxy;
import edu.brown.cs.student.rider.Rider;

@RestController
public class MessageController {
  
  
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;
  

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping("/rider_join/{to}")
  public String handleRiderJoin(@PathVariable String to, @RequestBody Map<String, String> payload) {
    System.out.println("handling rider join notification");
   
    
    for (String s : payload.keySet()) {
      System.out.println(s);
      System.out.println(payload.get(s));
    }
    
//    // get rider name from id 
//    String riderToken = payload.get("riderToken");
//    Rider rider = Proxy.getRiderFromToken(riderToken);
//    String riderName = rider.toString(); 
    
    String output = "A rider has joined your trip.";
   
    boolean exists = UserStorage.getInstance().getUsers().contains(to);
    System.out.println(exists);
    if (exists) {
      simpMessagingTemplate.convertAndSend("/topic/notifications/" + to, output);
    }
    return "sent driver a notification";
  }
  
  
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping("/rider_leave/{to}")
  public String handleRiderLeave(@PathVariable String to, @RequestBody Map<String, String> payload) {
    System.out.println("handling rider leave notification");
    
    // get rider name from id 
    String riderToken = payload.get("riderToken");
    Rider rider = Proxy.getRiderFromToken(riderToken);
    String riderName = rider.toString(); 
    
    String output = riderName + " has left your trip.";
   
    boolean exists = UserStorage.getInstance().getUsers().contains(to);
    System.out.println(exists);
    if (exists) {
      simpMessagingTemplate.convertAndSend("/topic/notifications/" + to, output);
    }
    return "sent driver a notification";
  }
  
  
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping("/driver_cancel/{to}")
  public String handleDriverCancel(@PathVariable String to, @RequestBody Map<String, String> payload) {
    System.out.println("handling driver cancel notification");
    String output = "Your trip has been cancelled.";
   
    boolean exists = UserStorage.getInstance().getUsers().contains(to);
    System.out.println(exists);
    if (exists) {
      simpMessagingTemplate.convertAndSend("/topic/notifications/" + to, output);
    }
    return "sent driver a notification";
  }
  
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping("/driver_remove/{to}")
  public String handleDriverRemove(@PathVariable String to, @RequestBody Map<String, String> payload) {
    System.out.println("handling driver remove notification");
    String output = "The driver has removed you from their trip.";
   
    boolean exists = UserStorage.getInstance().getUsers().contains(to);
    System.out.println(exists);
    if (exists) {
      simpMessagingTemplate.convertAndSend("/topic/notifications/" + to, output);
    }
    return "sent driver a notification";
  } 
  
  
  
  
  
  
  
  
  
 
}
