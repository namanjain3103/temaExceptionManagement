package com.osttra.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osttra.entity.SourceMongoEntity;
import com.osttra.repository.SourceMongoRepository;

@Service
public class ExceptionManagementService {

	@Autowired
	private SourceMongoRepository sourceMongoRepository;
	
    public List<SourceMongoEntity> getAllFromSource(){
    	return sourceMongoRepository.findAll();
    }

    public SourceMongoEntity addExceptionInSource(SourceMongoEntity sourceData) {
    	System.out.println("add exception in source service");
        return sourceMongoRepository.save(sourceData);
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
