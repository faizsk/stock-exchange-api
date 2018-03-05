package com.faiz.stockexchange.repositories;

import com.faiz.stockexchange.domain.Index;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IndexRepository extends MongoRepository<Index, String> {

  //@Query(value = "{ 'indexName' : ?0 }")
  Index findOneByIndexName(String indexName);


  //@Query(value = "{ 'indexCode' : ?0 }")
  Index findOneByIndexCode(String indexCode);

}

