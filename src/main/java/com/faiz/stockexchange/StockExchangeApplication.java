package com.faiz.stockexchange;


import com.faiz.stockexchange.service.KafkaService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class StockExchangeApplication {

  @Autowired
  PollableChannel consumerChannel;

  @Autowired
  private IntegrationFlowContext flowContext;

  @Autowired
  private KafkaProperties kafkaProperties;

  @Autowired
  private KafkaService kafkaService;

  public static void main(String[] args) {

    ConfigurableApplicationContext context = new SpringApplicationBuilder(
        StockExchangeApplication.class)
        .run(args);

    List<String> valid_topics = Arrays.asList("StockPrice");
    List<String> topics = new ArrayList();
    if (args.length > 0) {
      for (String arg : args) {
        if (valid_topics.contains(arg)) {
          topics.add(arg);
        }
      }
    }
    context.getBean(StockExchangeApplication.class).run(context, topics);
    context.close();
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.US);
    return slr;
  }

  @Bean
  public javax.validation.Validator validator() {
    final LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
    factory.setValidationMessageSource(messageSource());
    return factory;
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:locale/messages");
    messageSource.setCacheSeconds(3600); //refresh cache once per hour
    return messageSource;
  }

  private void run(ConfigurableApplicationContext context, List<String> topics) {

    System.out.println("Inside ConsumerApplication run method...");
    PollableChannel consumerChannel = context.getBean("consumerChannel", PollableChannel.class);

    for (String topic : topics) {
      addAnotherListenerForTopics(topic);
    }

    Message received = consumerChannel.receive();
    while (received != null) {
      received = consumerChannel.receive();
      System.out.println("Received " + received.getPayload().toString());
      kafkaService.addStock(received);
    }
  }

  public void addAnotherListenerForTopics(String... topics) {
    Map consumerProperties = kafkaProperties.buildConsumerProperties();
    IntegrationFlow flow = IntegrationFlows
        .from(Kafka.messageDrivenChannelAdapter(
            new DefaultKafkaConsumerFactory(consumerProperties), topics))
        .channel("consumerChannel")
        .handle((p, h) -> MessageBuilder.withPayload(new GenericMessage<>(p, h)))
        .transform(Transformers.toJson()).get();
    this.flowContext.registration(flow).register();
  }
}
