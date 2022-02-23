package edu.brown.cs.student.hopp;

import java.sql.SQLException;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import edu.brown.cs.student.database.Proxy;


@ComponentScan({"edu.brown.cs.student.hopp","edu.brown.cs.student.controller", "edu.brown.cs.student.routing"})
@SpringBootApplication
@EnableAsync
public class HoppApplication {
  
  @Bean("threadPoolTaskExecutor")
  public TaskExecutor getAsyncExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(20);
      executor.setMaxPoolSize(1000);
      executor.setWaitForTasksToCompleteOnShutdown(true);
      executor.setThreadNamePrefix("Async-");
      return executor;
  }
 

  public static void main(String[] args) {
    SpringApplication.run(HoppApplication.class, args);

    // connect to AWS database
    
	    try {
	    	Proxy.connect();
	    } catch (SQLException e) {
	    	System.out.println("Could not connect to database");
	    	return;
	    } catch (ClassNotFoundException e) {
	    	System.out.println("Could not find Redshift Driver");
	    	return;
	    }
  }

}
