package com.osttra.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osttra.entity.SourceMongoEntity;
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
    
    
    public List<SourceMongoEntity> getAllFromTema(){
    	return temaMongoRepository.findAll();
    }
    
    
    public SourceMongoEntity addExceptionInSource(SourceMongoEntity sourceData) {
    	System.out.println("add exception in source service");
        return sourceMongoRepository.save(sourceData);
    }
    
    
    public void migrateData() {
        List<SourceMongoEntity> dataToMigrate = sourceMongoRepository.findAll();
        for (SourceMongoEntity mongoData : dataToMigrate) {
        	 if (!temaMongoRepository.existsById(mongoData.getExceptionId())) {
        temaMongoRepository.save(mongoData);
        }
        }
    }
       
	public String toJson(Map<String, String> assignGroup) {
	      try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            return objectMapper.writeValueAsString(assignGroup);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	
}
