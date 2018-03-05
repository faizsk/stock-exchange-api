package com.faiz.stockexchange.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.faiz.stockexchange.domain.Index;
import com.faiz.stockexchange.domain.IndexStock;
import com.faiz.stockexchange.exception.MessageByLocaleService;
import com.faiz.stockexchange.service.IndexService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(value = IndexController.class, secure = false)
public class IndexControllerTest {

  @InjectMocks
  private IndexController controller;

  @Mock
  private IndexService service;

  @Mock
  private MessageByLocaleService messageByLocaleService;

  @Autowired
  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void shouldGetAllIndexes() throws Exception {
    List<Index> indices = Arrays.asList(new Index("1001", "Sensex", new HashSet<>(
        Arrays
            .asList(new IndexStock("A1", 0.4f),
                new IndexStock("B1", 0.3f),
                new IndexStock("C1", 0.3f)))));

    Mockito.when(service.getAllIndices()).thenReturn(indices);

    this.mockMvc
        .perform(get("/index").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"));
  }

  private String mapToJson(Object object) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(object);
  }
}

