package com.osttra.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osttra.entity.SourceMongoEntity;
import com.osttra.entity.TemaMongoEntity;
import com.osttra.repository.database1.SourceMongoRepository;
import com.osttra.repository.database2.TemaMongoRepository;

@Service
public class ExceptionManagementService {

	@Autowired
	private SourceMongoRepository sourceMongoRepository;

	@Autowired
	private TemaMongoRepository temaMongoRepository;

	@Autowired
	RestTemplate restTemplate;

	public List<SourceMongoEntity> getAllFromSource() {

		return sourceMongoRepository.findAll();
	}

//	public List<TemaMongoEntity> getAllFromTema() {
//
//		return temaMongoRepository.findAll();
//	}

	public SourceMongoEntity addExceptionInSource(SourceMongoEntity sourceData) {

		return sourceMongoRepository.save(sourceData);
	}
//    
//    public void migrateData() {
//        List<SourceMongoEntity> dataToMigrate = sourceMongoRepository.findAll();
//        for (SourceMongoEntity mongoData : dataToMigrate) {
//        	 if (!temaMongoRepository.existsById(mongoData.getExceptionId())) {
//        temaMongoRepository.save(mongoData);
//        }
//        }
//    }
//    
////    public void migrateData() {
////        List<SourceMongoEntity> sourceData = sourceMongoRepository.findAll();
////        List<TemaMongoEntity> destinationData = new ArrayList<>();
////
////        for (SourceMongoEntity sourceEntity : sourceData) {
////            // Check if a document with the same exceptionId exists in the destination database
////           
////
////          //  if (!temaMongoRepository.existsById(sourceEntity.getExceptionId())) {
////                // Document with the same exceptionId doesn't exist in destination, so migrate it
////                TemaMongoEntity destinationEntity = new TemaMongoEntity();
////               // destinationEntity.setAssign("ASSIGN");
////                // Copy fields from sourceEntity to destinationEntity
////                // ...
////                // Save destinationEntity to the destination database
////                destinationData.add(destinationEntity);
////              
////          //  }
////        }
////        temaMongoRepository.saveAll(destinationData);
////    
////    }

//*********************old migration code***************************************8	
//	public void migrateData() {
//		List<SourceMongoEntity> sourceData = sourceMongoRepository.findAll();
//
//		for (SourceMongoEntity sourceEntity : sourceData) {
//			// Check if a document with the same exceptionId exists in the destination
//			// database
//			TemaMongoEntity existingEntity = temaMongoRepository.findByExceptionId(sourceEntity.getExceptionId());
//
//			if (existingEntity == null) {
//
//				TemaMongoEntity destinationEntity = new TemaMongoEntity();
//
//				destinationEntity.setExceptionId(sourceEntity.getExceptionId());
//				destinationEntity.setTradeId(sourceEntity.getTradeId());
//				destinationEntity.setCounterParty(sourceEntity.getCounterParty());
//				destinationEntity.setTradeDate(sourceEntity.getTradeDate());
//				destinationEntity.setExceptionType(sourceEntity.getExceptionType());
//				destinationEntity.setResolutionSteps(sourceEntity.getResolutionSteps());
//				destinationEntity.setStatus(sourceEntity.getStatus());
//				destinationEntity.setPriority(sourceEntity.getPriority());
//				destinationEntity.setDescription(sourceEntity.getDescription());
//				destinationEntity.setCreatedBy(sourceEntity.getCreatedBy());
//				destinationEntity.setCreatedAt(sourceEntity.getCreatedAt());
//				destinationEntity.setUpdatedBy(sourceEntity.getUpdatedBy());
//				destinationEntity.setUpdatedAt(sourceEntity.getUpdatedAt());
//				destinationEntity.setAssign("ASSIGN"); 
//
//			    String exceptionId = destinationEntity.getExceptionId();
//			    
////				String externalApiUrl = "https://example.com/api/products?exceptionId=" + exceptionId;
////				ResponseEntity<String> response = restTemplate.exchange(externalApiUrl, HttpMethod.GET, null, String.class);
//			    
//			    Map<String, String> mapExceptionId = new HashMap<>();
//			    mapExceptionId.put("exceptionId", exceptionId);
//			    
//			    String ExceptionIdJson = maptoJson(mapExceptionId);
//			    
//			    String externalApiUrl = "https://example.com/api/products";
//			    
//				HttpHeaders headers = new org.springframework.http.HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				HttpEntity<String> requestEntity = new HttpEntity<>(ExceptionIdJson, headers);
//				ResponseEntity<String> response = restTemplate.postForEntity(externalApiUrl, requestEntity, String.class);
//				
//				String businessId = extractProductIdFromResponse(response.getBody());
//				destinationEntity.setBusinessId(businessId); 				
//				temaMongoRepository.save(destinationEntity);
//			}
//		}
//	}

	// new migration method

	public void migrateData() {
		List<SourceMongoEntity> sourceData = sourceMongoRepository.findAll();
        System.out.println(sourceData);
		
		for (SourceMongoEntity sourceEntity : sourceData) {
			System.out.println(sourceEntity);
			
			System.out.println(sourceEntity.getExceptionId());
			
			//TemaMongoEntity existingEntity = temaMongoRepository.findById(sourceEntity.getExceptionId()).get();
			
			
			Optional<TemaMongoEntity> existingEntityOptional = temaMongoRepository.findById(sourceEntity.getExceptionId());
			if (!existingEntityOptional.isPresent()) {
				TemaMongoEntity temaExceptionEntity = createTemaExceptionEntity(sourceEntity);
				String exceptionId  = temaExceptionEntity.getExceptionId();
				String exceptionType = temaExceptionEntity.getExceptionType();
				System.out.println(exceptionId +" "+ exceptionType);
				String processId = fetchProcessId(exceptionId, exceptionType);
				System.out.println(processId);
				temaExceptionEntity.setProcessId(processId);
				temaMongoRepository.save(temaExceptionEntity);
			} else {
			    System.out.println(" data already present");
			}

//			if (existingEntity == null) {
//				
//				TemaMongoEntity temaExceptionEntity = createTemaExceptionEntity(sourceEntity);
//				String exceptionId  = temaExceptionEntity.getExceptionId();
//				String exceptionType = temaExceptionEntity.getExceptionType();
//				System.out.println(exceptionId +" "+ exceptionType);
//				String processId = fetchProcessId(exceptionId, exceptionType);
//				temaExceptionEntity.setProcessId(processId);
//				temaMongoRepository.save(temaExceptionEntity);
//			}
		}
	}

	private TemaMongoEntity createTemaExceptionEntity(SourceMongoEntity sourceEntity) {
		TemaMongoEntity temaExceptionEntity = new TemaMongoEntity();
		temaExceptionEntity.setExceptionId(sourceEntity.getExceptionId());
		temaExceptionEntity.setTradeId(sourceEntity.getTradeId());
		temaExceptionEntity.setCounterParty(sourceEntity.getCounterParty());
		temaExceptionEntity.setTradeDate(sourceEntity.getTradeDate());
		temaExceptionEntity.setExceptionType(sourceEntity.getExceptionType());
		temaExceptionEntity.setResolutionSteps(sourceEntity.getResolutionSteps());
		temaExceptionEntity.setStatus(sourceEntity.getStatus());
		temaExceptionEntity.setPriority(sourceEntity.getPriority());
		temaExceptionEntity.setDescription(sourceEntity.getDescription());
		temaExceptionEntity.setCreatedBy(sourceEntity.getCreatedBy());
		temaExceptionEntity.setCreatedAt(sourceEntity.getCreatedAt());
		temaExceptionEntity.setUpdatedBy(sourceEntity.getUpdatedBy());
		temaExceptionEntity.setUpdatedAt(sourceEntity.getUpdatedAt());
		temaExceptionEntity.setAssign("ASSIGN");
		return temaExceptionEntity;
	}

	public String fetchProcessId(String exceptionId, String exceptionType) {

		//Map<String, String> mapExceptionId = Collections.singletonMap("businessKey", exceptionId);
		
		
		Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("businessKey", exceptionId);
        Map<String, Object> variablesMap = new HashMap<>();
        Map<String, Object> exceptionTypeMap = new HashMap<>();    
        exceptionTypeMap.put("value", exceptionType);
        exceptionTypeMap.put("type", "String");
        variablesMap.put("ExceptionType", exceptionTypeMap);
        requestMap.put("variables", variablesMap);
        // Log the JSON request data
        System.out.println("JSON Request: " + requestMap);			
		
    	ObjectMapper objectMapper = new ObjectMapper();		
		String exceptionIdJson = null;
		try {
			exceptionIdJson = objectMapper.writeValueAsString(requestMap);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String externalApiUrl = "http://10.196.20.65:8080/engine-rest/process-definition/key/ApprovalProcess/start";
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<String> requestEntity = new HttpEntity<>(exceptionIdJson, headers);
		ResponseEntity<String> response = postJsonToExternalApi(externalApiUrl, exceptionIdJson);
		return extractProcessssIdFromResponse(response.getBody());
	}

	public ResponseEntity<String> postJsonToExternalApi(String externalApiUrl, String requestBodyJson) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);
		return restTemplate.postForEntity(externalApiUrl, requestEntity, String.class);
	}

// new migration code ends here	

	public String extractProcessssIdFromResponse(String responseBody) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonResponse;
		try {
		    jsonResponse = objectMapper.readTree(responseBody);
		} catch (Exception e) {
		    // Handle JSON parsing exception
		    e.printStackTrace();
		    return null;
		}

		String processId = jsonResponse.get("id").asText();
		return processId;
	}

	public String mapToJson(Map<String, String> map) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting map to JSON", e);
		}
	}

//	public String StringtoJson(String data) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			return objectMapper.writeValueAsString(data);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

//	public Optional<TemaMongoEntity> getExceptionDetails(String exceptionId) {
//		try {
//			return temaMongoRepository.findById(exceptionId);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Optional.empty();
//		}
//	}

	public TemaMongoEntity getExceptionDetails(String exceptionId) {
		System.out.println("inside serive " + exceptionId);
		TemaMongoEntity exception = temaMongoRepository.findById(exceptionId).get();
		System.out.println(exception);
		updateExceptionStatus(exception);
		return exception;
	}

	public List<TemaMongoEntity> showAllException() {
		List<TemaMongoEntity> exceptions = temaMongoRepository.findAll();
		for (TemaMongoEntity exception : exceptions) {
			updateExceptionStatus(exception);
		}
		return exceptions;
	}

	public void updateExceptionStatus(TemaMongoEntity exception) {
		String businessId = exception.getProcessId();
		Map<String, String> mapBusinessId = Collections.singletonMap("businessId", businessId);
		String businessIdJson = mapToJson(mapBusinessId);
		String externalApiUrl = "https://example.com/api/products";
		ResponseEntity<String> response = postJsonToExternalApi(externalApiUrl, businessIdJson);
		String status = getExceptionStatus(response);
		exception.setStatus(status);
		temaMongoRepository.save(exception);
	}

	public String getExceptionStatus(ResponseEntity<String> response) {
		String status = "Open";
		try {
			String responseBody = response.getBody();
			if (responseBody != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(responseBody);
				JsonNode assigneeNode = rootNode.get("assignee");
				JsonNode activityNameNode = rootNode.get("activity_name");
				String activity = activityNameNode.asText();

				if (assigneeNode != null) {
					if (activity == "FourEyeCheck")
						status = "Resolved";
					else if (activity == "Complete")
						status = "Closed";
					else
						status = "Pending";
				}
			}
		} catch (Exception e) {
			System.out.println("inside getStatus of service");
			e.printStackTrace();
		}
		return status;
	}

}
