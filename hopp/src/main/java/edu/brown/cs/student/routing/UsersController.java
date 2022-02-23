package edu.brown.cs.student.routing;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
  
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;
  
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping("/registration/{userToken}")
  public ResponseEntity<Void> register(@PathVariable String userToken) {
    System.out.println("handling register user request: " + userToken);
    
    try {
      UserStorage.getInstance().setUser(userToken);
    } catch(Exception e) {
      ResponseEntity.badRequest().build();
    }
    
    return ResponseEntity.ok().build();
  }
  
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @GetMapping("/fetchAllUsers")
  public Set<String> fetchAll() {
    return UserStorage.getInstance().getUsers();
  }

}
