package com.osttra.repository.database2;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.osttra.entity.SourceMongoEntity;

@Repository
public interface TemaMongoRepository extends MongoRepository<SourceMongoEntity, String> {

}
