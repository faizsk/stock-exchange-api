package com.faiz.stockexchange.config;


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

