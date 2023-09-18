package com.osttra.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.osttra.entity.SourceMongoEntity;
import com.osttra.service.ExceptionManagementService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ExceptionListController {

	 @Autowired
	    ExceptionManagementService exceptionMigrationService; 
	 
	 @Autowired
	  RestTemplate restTemplate;
	    
		@GetMapping("/get")
		public ResponseEntity<?>  getAllFromSource(){
			
			List<SourceMongoEntity> mongoData = exceptionMigrationService. getAllFromSource();	
			if(mongoData.size()>0) {
				return new ResponseEntity<List<SourceMongoEntity>>(mongoData, HttpStatus.OK);
			}
			else 
				return new ResponseEntity<>("data not available", HttpStatus.NOT_FOUND);
		}
		
		@PostMapping("/create")
		public ResponseEntity<?> create(@RequestBody SourceMongoEntity mongoData){
			try {
				exceptionMigrationService.addExceptionInSource(mongoData);	
				return new ResponseEntity<SourceMongoEntity>(mongoData, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}				
		}
		
		
//		@PostMapping("/assign")
//		public ResponseEntity<?> assignUserGroup(@RequestBody Map<String, String> assignGroup){
//			 String externalApiUrl = "https://dummy.restapiexample.com/api/v1/create"; 
//			 String assignGroupJason = exceptionMigrationService.toJson(assignGroup);
//			  ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, assignGroupJason, String.class);
//			System.out.println(assignGroupJason);
//			 
//			  return ResponseEntity.ok("Data sent to Spring Boot and external API successfully");
//		}
		
		
		// assigning user group to exception
		@PostMapping("/assign")
		public ResponseEntity<?> assignUserGroup(@RequestBody Map<String, String> assignGroup) {
		    try {
		        String externalApiUrl = "https://jsonplaceholder.typicode.com/posts";
		        String assignGroupJson = exceptionMigrationService.toJson(assignGroup);
		        ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, assignGroupJson, String.class);

		        if (response.getStatusCode().is2xxSuccessful()) {
		            System.out.println(assignGroupJson);
		            return ResponseEntity.ok("Data sent to Spring Boot and external API successfully");
		        } else {
		            return ResponseEntity.status(response.getStatusCode())
		                .body("External API returned an error: " + response.getBody());
		        }
		    } catch (Exception e) {
		       
		        e.printStackTrace(); 
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("Error occurred while processing the request");
		    }
		}

}
