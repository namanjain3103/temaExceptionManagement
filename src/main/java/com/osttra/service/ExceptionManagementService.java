package com.osttra.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	

	
    public List<SourceMongoEntity> getAllFromSource(){
    	return sourceMongoRepository.findAll();
    }
    
    
    public List<TemaMongoEntity> getAllFromTema(){
    	return temaMongoRepository.findAll();
    }
    
    
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
    
    
    
    public void migrateData() {
        List<SourceMongoEntity> sourceData = sourceMongoRepository.findAll();

        for (SourceMongoEntity sourceEntity : sourceData) {
            // Check if a document with the same exceptionId exists in the destination database
            TemaMongoEntity existingEntity = temaMongoRepository.findByExceptionId(sourceEntity.getExceptionId());

            if (existingEntity == null) {
         
                TemaMongoEntity destinationEntity = new TemaMongoEntity();
            
                destinationEntity.setExceptionId(sourceEntity.getExceptionId());
                destinationEntity.setTradeId(sourceEntity.getTradeId());
                destinationEntity.setCounterParty(sourceEntity.getCounterParty());
                destinationEntity.setTradeDate(sourceEntity.getTradeDate());
                destinationEntity.setExceptionType(sourceEntity.getExceptionType());
                destinationEntity.setResolutionSteps(sourceEntity.getResolutionSteps());
                destinationEntity.setStatus(sourceEntity.getStatus());
                destinationEntity.setPriority(sourceEntity.getPriority());
                destinationEntity.setDescription(sourceEntity.getDescription());
                destinationEntity.setCreatedBy(sourceEntity.getCreatedBy());
                destinationEntity.setCreatedAt(sourceEntity.getCreatedAt());
                destinationEntity.setUpdatedBy(sourceEntity.getUpdatedBy());
                destinationEntity.setUpdatedAt(sourceEntity.getUpdatedAt());
                destinationEntity.setAssign("ASSIGN"); // Set the default value
                
                // Save destinationEntity to the destination database
                temaMongoRepository.save(destinationEntity);
            }
        }
    }
    
    
    
       
	public String MaptoJson(Map<String, String> assignGroup) {
	      try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            return objectMapper.writeValueAsString(assignGroup);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	


public String StringtoJson(String data) {
    try {
          ObjectMapper objectMapper = new ObjectMapper();
          return objectMapper.writeValueAsString(data);
      } catch (JsonProcessingException e) {
          e.printStackTrace();
          return null;
      }
}

}

