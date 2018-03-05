package com.faiz.stockexchange.config;

/*

@EnableMongoRepositories(basePackageClasses = IndexRepository.class)
@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {

  @Autowired
  private MessageSource messageSource;

  @Override
  public String getDatabaseName() {
    return messageSource.getMessage("spring.data.mongodb.database");
  }

  @Override
  @Bean
  public Mongo mongo() throws Exception {
    return new MongoClient(messageSource.getMessage("spring.data.mongodb.host"),
        Integer.parseInt(messageSource.getMessage("spring.data.mongodb.port")));
  }
}
*/
