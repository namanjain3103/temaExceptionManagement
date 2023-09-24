package com.osttra.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.osttra.entity.TemaMongoEntity;
import com.osttra.service.ExceptionManagementService;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class ExceptionListController {

	@Autowired
	ExceptionManagementService exceptionManagementService;

	@Autowired
	RestTemplate restTemplate;

	
	@GetMapping("/getAllExceptions")
	public ResponseEntity<?> getAllExceptionList() {
		try {
			List<TemaMongoEntity> mongoData = exceptionManagementService.showAllException();
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

	// *****************************************************
	@PostMapping("/assignGroup")
	public ResponseEntity<?> assignUserGroup(@RequestBody Map<String, String> assignGroup) {
		try {
			
			String processId = exceptionManagementService.getProcessId(assignGroup.get("exceptionId"));
			assignGroup.put("type", "candidate");
			assignGroup.remove("exceptionId");
			String externalApiUrl = "http://192.168.18.20:8080/engine-rest/task/" + processId + "/identity-links";
			//String externalApiUrl = "https://jsonplaceholder.typicode.com/posts";
			String assignGroupJson = exceptionManagementService.mapToJson(assignGroup);
			System.out.println("in assignGroup Controller");

//			HttpHeaders headers = new org.springframework.http.HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<String> requestEntity = new HttpEntity<>(assignGroupJson, headers);
//          ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, requestEntity, String.class);
		
			ResponseEntity<String> response = exceptionManagementService.postJsonToExternalApi(externalApiUrl, assignGroupJson);
			
			if (response.getStatusCode().is2xxSuccessful()) {
				System.out.println(assignGroupJson);
				return ResponseEntity.ok("Data sent to Spring Boot and external API successfully");
			} else {
				System.out.println("inside assignGroup Controller If condition");

				return ResponseEntity.status(response.getStatusCode())
						.body("External API returned an error: " + response.getBody());
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while processing the request");
		}
	}
	
	@PostMapping("/assignUser")
	public ResponseEntity<?> assignUser(@RequestBody Map<String, String> assignUser) {
		try {
			// String externalApiUrl = "https://jsonplaceholder.typicode.com/posts";
			String processId = exceptionManagementService.getProcessId(assignUser.get("exceptionId"));
			assignUser.remove("exceptionId");
			String externalApiUrl = "http://192.168.18.20:8080/engine-rest/task/" + processId + "/claim";
			String assignUserJson = exceptionManagementService.mapToJson(assignUser);
			System.out.println("in userGroup Controller");

//			HttpHeaders headers = new org.springframework.http.HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<String> requestEntity = new HttpEntity<>(assignUserJson, headers);
//			ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, requestEntity, String.class);
			
			ResponseEntity<String> response = exceptionManagementService.postJsonToExternalApi(externalApiUrl, assignUserJson);
			
			if (response.getStatusCode().is2xxSuccessful()) {
				System.out.println(assignUserJson);
				return ResponseEntity.ok("Data sent to Spring Boot and external API successfully");
			} else {
				System.out.println("inside assignGroup Controller If condition");

				return ResponseEntity.status(response.getStatusCode())
						.body("External API returned an error: " + response.getBody());
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while processing the request");
		}
	}
	
//    public ResponseEntity<?> getExceptionDetail(@PathVariable String exceptionId) {
//        try {
//            Optional<TemaMongoEntity> exceptionDetails = exceptionManagementService.getExceptionDetails(exceptionId);
//
//            if (exceptionDetails.isPresent()) {
//                return ResponseEntity.ok(exceptionDetails.get());
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }
	
    @GetMapping("/get/{exceptionId}")
    public ResponseEntity<?> getExceptionDetail(@PathVariable String exceptionId) {
        try {
        	System.out.println(" inside get exception details"+ exceptionId);
            TemaMongoEntity exceptionDetails = exceptionManagementService.getExceptionDetails(exceptionId);
            if (exceptionDetails != null) {
                return ResponseEntity.ok(exceptionDetails);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @GetMapping("/getException/{userId}")
    public ResponseEntity<?> getExceptionForUser(@PathVariable String userId) {
        try {
        	System.out.println(" inside get exception details"+ userId);
            List<TemaMongoEntity> exceptionList = exceptionManagementService.getExceptionListForUser(userId);
            if (exceptionList != null) {
                return ResponseEntity.ok(exceptionList);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
     
    @GetMapping("/getHistory/{exceptionId}")
    public ResponseEntity<?> getExceptionHistory(@PathVariable String exceptionId) {
        try {
        	System.out.println(" inside get exception details"+ exceptionId);
        	List<Map<String, Object>> exceptionHistory = exceptionManagementService.getExceptionHistory(exceptionId);
        	if (exceptionHistory.size() > 0) {
				return new ResponseEntity<List<Map<String, Object>>>(exceptionHistory, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Data not available", HttpStatus.NOT_FOUND);
			}
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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

