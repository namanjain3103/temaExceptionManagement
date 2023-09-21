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
import com.osttra.entity.TemaMongoEntity;
import com.osttra.service.ExceptionManagementService;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ExceptionListController {

	 @Autowired
	    ExceptionManagementService exceptionMigrationService; 
	 
	 @Autowired
	  RestTemplate restTemplate;
	    
	 
	 @GetMapping("/get")
	 public ResponseEntity<?> getAllFromSource() {
	     try {
	         List<SourceMongoEntity> mongoData = exceptionMigrationService.getAllFromSource();
	         if (mongoData.size() > 0) {
	             return new ResponseEntity<List<SourceMongoEntity>>(mongoData, HttpStatus.OK);
	         } else {
	             return new ResponseEntity<>("Data not available", HttpStatus.NOT_FOUND);
	         }
	     } catch (Exception e) {
	         e.printStackTrace(); // You can log the exception or handle it as needed
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	             .body("An error occurred while retrieving data from the source: " + e.getMessage());
	     }
	 }
		
		@GetMapping("/getTema")
		public ResponseEntity<?> getAllFromTema() {
		    try {
		        List<TemaMongoEntity> mongoData = exceptionMigrationService.getAllFromTema();
		        if (mongoData.size() > 0) {
		            return new ResponseEntity<List<TemaMongoEntity>>(mongoData, HttpStatus.OK);
		        } else {
		            return new ResponseEntity<>("Data not available", HttpStatus.NOT_FOUND);
		        }
		    } catch (Exception e) {
		        e.printStackTrace(); // You can log the exception or handle it as needed
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("An error occurred while retrieving data from Tema: " + e.getMessage());
		    }
		}
		
		@PostMapping("/create")
		public ResponseEntity<?> create(@RequestBody SourceMongoEntity mongoData) {
		    try {
		        SourceMongoEntity createdData = exceptionMigrationService.addExceptionInSource(mongoData);
		        return ResponseEntity.ok(createdData);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("Failed to create data: " + e.getMessage());
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
		
//		@PostMapping("/assignGroup")
//		public ResponseEntity<?> assignUserGroup(@RequestBody Map<String, String> assignGroup) {
//		    try {
//		       // String externalApiUrl = "https://jsonplaceholder.typicode.com/posts";
//		    	String externalApiUrl = "http://192.168.18.20:8080/engine-rest/task/f6f214d5-57a3-11ee-a4e7-c675abfc1039/claim";
//		        String assignGroupJson = exceptionMigrationService.MaptoJson(assignGroup);
//		        System.out.println("ytytutuyuyiyu");
//			       
//		        HttpHeaders headers = new org.springframework.http.HttpHeaders();
//		        headers.setContentType(MediaType.APPLICATION_JSON);
//		        HttpEntity<String> requestEntity = new HttpEntity<>(assignGroupJson, headers);
//
//		        ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, requestEntity, String.class);  
//		        if (response.getStatusCode().is2xxSuccessful()) {
//		            System.out.println(assignGroupJson);
//		            return ResponseEntity.ok("Data sent to Spring Boot and external API successfully");
//		        } else {
//		        	System.out.println("fdghfdghfdgh");
//		            return ResponseEntity.status(response.getStatusCode())
//		                .body("External API returned an error: " + response.getBody());  
//		        }
//		    } catch (Exception e) {
//		       
//		        e.printStackTrace(); 
//		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//		            .body("Error occurred while processing the request");
//		    }
//		}
		
		
		//       ***************************gaurav wala**************************
		@PostMapping("/assignGroup")
		public ResponseEntity<?> assignUserGroup(@RequestBody Map<String, String> assignGroup) {
		    try {
		       // String externalApiUrl = "https://jsonplaceholder.typicode.com/posts";
		    	String exceptionId = assignGroup.get("exceptionId");
		    	String externalApiUrl = "http://192.168.18.20:8080/engine-rest/task/"+exceptionId+"/claim";
		        
		    	String assignGroupJson = exceptionMigrationService.MaptoJson(assignGroup);
		        System.out.println("ytytutuyuyiyu");
			       
		        HttpHeaders headers = new org.springframework.http.HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_JSON);
		        HttpEntity<String> requestEntity = new HttpEntity<>(assignGroupJson, headers);

		        ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, requestEntity, String.class);  
		        if (response.getStatusCode().is2xxSuccessful()) {
		            System.out.println(assignGroupJson);
		            return ResponseEntity.ok("Data sent to Spring Boot and external API successfully");
		        } else {
		        	System.out.println("fdghfdghfdgh");
		            return ResponseEntity.status(response.getStatusCode())
		                .body("External API returned an error: " + response.getBody());  
		        }
		    } catch (Exception e) {
		       
		        e.printStackTrace(); 
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("Error occurred while processing the request");
		    }
		}
		
//		@PostMapping("/assignGroup")
//		public ResponseEntity<?> assignUserGroup(@RequestBody Map<String, String> assignGroup) {
//		    try {
//		       // String externalApiUrl = "https://jsonplaceholder.typicode.com/posts";
//		    	String externalApiBaseUrl = "http://192.168.18.20:8080/engine-rest/task/";
//		    	String exceptionId = assignGroup.get("exceptionId");
//		    	String userId = assignGroup.get("userId");
//		        System.out.println("ytytutuyuyiyu");
//		        String externalApiUrl = externalApiBaseUrl + exceptionId + "/claim";
//			    System.out.println(externalApiUrl +" " + userId);
//			    String groupIdJson = exceptionMigrationService.StringtoJson(userId);
//			    System.out.println(groupIdJson);
//			    
//		        HttpHeaders headers = new org.springframework.http.HttpHeaders();
//		        headers.setContentType(MediaType.APPLICATION_JSON);
//		        HttpEntity<String> requestEntity = new HttpEntity<>(groupIdJson, headers);
//
//		        ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, requestEntity, String.class);  
//		        if (response.getStatusCode().is2xxSuccessful()) {
//		            System.out.println(userId);
//		            return ResponseEntity.ok("Data sent to Spring Boot and external API successfully");
//		        } else {
//		        	System.out.println("fdghfdghfdgh");
//		            return ResponseEntity.status(response.getStatusCode())
//		                .body("External API returned an error: " + response.getBody());  
//		        }
//		    } catch (Exception e) {
//		       
//		        e.printStackTrace(); 
//		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//		            .body("Error occurred while processing the request");
//		    }
//		}
		
		
		
		
		
		
		@PostMapping("/migrate")
		public ResponseEntity<String> migrate() {
		    try {
		        exceptionMigrationService.migrateData();
		        return ResponseEntity.ok("Data migration completed successfully.");
		    } catch (Exception e) {
		        e.printStackTrace(); // You can log the exception or handle it as needed
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body("An error occurred during data migration: " + e.getMessage());
		    }

		
						    
}
}