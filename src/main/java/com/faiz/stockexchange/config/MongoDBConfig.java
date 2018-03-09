package com.faiz.stockexchange.config;


import com.faiz.stockexchange.exception.MessageByLocaleService;
import com.faiz.stockexchange.repositories.IndexRepository;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/*
@EnableMongoRepositories(basePackageClasses = IndexRepository.class)
@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {

  @Autowired
  private MessageByLocaleService messageByLocaleService;

  @Override
  public String getDatabaseName() {
    return messageByLocaleService.getMessage("spring.data.mongodb.database");
  }

  @Override
  @Bean
  public Mongo mongo() throws Exception {
    return new MongoClient(messageByLocaleService.getMessage("spring.data.mongodb.host"),
        Integer.parseInt(messageByLocaleService.getMessage("spring.data.mongodb.port")));
  }

}
*/

