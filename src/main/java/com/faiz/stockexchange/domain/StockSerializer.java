package com.faiz.stockexchange.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;


public class StockSerializer implements Serializer, Deserializer {

  @Override
  public List deserialize(String topic, byte[] data) {
    List<Stock> stocks = null;
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    try {
      //return objectMapper.readValue(data, StockValue.class);
      String str = new String(data, StandardCharsets.UTF_8);
      stocks = objectMapper.readValue(str, new TypeReference<List<Stock>>() {
      });
      return stocks;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("problem with object deserialization ");
    }
    return stocks;
  }

  @Override
  public void configure(Map configs, boolean isKey) {

  }

  @Override
  public byte[] serialize(String topic, Object data) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(data).getBytes();
    } catch (JsonProcessingException e) {
      System.out.println("problem with object serialization ");
    }
    return "".getBytes();
  }

  @Override
  public void close() {

  }


}