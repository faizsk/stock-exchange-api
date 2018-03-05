package com.faiz.stockexchange.controller;


import com.faiz.stockexchange.domain.Index;
import com.faiz.stockexchange.domain.IndexStock;
import com.faiz.stockexchange.exception.CustomException;
import com.faiz.stockexchange.exception.MessageByLocaleService;
import com.faiz.stockexchange.service.IndexService;
import com.faiz.stockexchange.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "This is Index API")
@Slf4j
@RestController
public class IndexController {

  private static Logger LOGGER = LoggerFactory.getLogger("Logging IndexController API");
  @Autowired
  private MessageByLocaleService messageByLocaleService;
  @Autowired
  private IndexService indexService;
  @Autowired
  private StockService stockService;

  @ApiOperation(value = "View List of Indices", notes = "This will get you all the indices")
  @RequestMapping(method = RequestMethod.GET, value = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllIndexes() throws CustomException {
    List<Index> currentIndex = indexService.getAllIndices();
    if (currentIndex.isEmpty()) {
      String noIndexFound = messageByLocaleService.getMessage("index.notfound");
      LOGGER.info(noIndexFound);
      throw new CustomException(noIndexFound, HttpStatus.NOT_FOUND);
    }
    LOGGER.info("Got all indices");
    System.out.println("getting all indices");
    return new ResponseEntity<>(currentIndex, HttpStatus.OK);
  }

  @ApiOperation(value = "Get Index by IndexCode")
  @RequestMapping(method = RequestMethod.GET, value = "/index/{indexCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Index> getIndex(@PathVariable String indexCode) {
    Index currentIndex = indexService.getIndexByIndexCode(indexCode);
    if (null == currentIndex) {
      String noIndexFound = messageByLocaleService.getMessage("index.notfound");
      LOGGER.info(noIndexFound);
      throw new CustomException(noIndexFound, HttpStatus.NOT_FOUND);
    }
    LOGGER.info("Got index with indexCode {}", indexCode);
    return new ResponseEntity<>(currentIndex, HttpStatus.OK);
  }

  @ApiOperation(value = "Create New Index")
  @RequestMapping(method = RequestMethod.POST, value = "/index")
  public ResponseEntity<Object> addIndex(@RequestBody @Valid Index index) {
/*    if (index == null) {
      LOGGER.info("The index object passed is null");
      throw new CustomException("Index Object entered was null", HttpStatus.BAD_REQUEST);
    }*/
    if (null != indexService.getIndexByIndexCode(index.getIndexCode())) {
      String indexCodeExists = messageByLocaleService.getMessage("indexcode.exists");
      LOGGER.info(indexCodeExists);
      throw new CustomException(indexCodeExists, HttpStatus.CONFLICT);
    }
    if (null != indexService.getIndexByIndexName(index.getIndexName())) {
      String indexNameExists = messageByLocaleService.getMessage("indexname.exists");
      LOGGER.info(indexNameExists);
      throw new CustomException(indexNameExists, HttpStatus.CONFLICT);
    }
    indexService.addIndex(index);
    LOGGER.info("Added Index with indexCode {}", index.getIndexCode());
    return new ResponseEntity<>(index, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Update an existing Index")
  @RequestMapping(method = RequestMethod.PUT, value = "/index/{indexCode}")
  public ResponseEntity<Index> updateIndex(@PathVariable("indexCode") @Valid String indexCode,
      @RequestBody @Valid Set<IndexStock> indexStocks) throws CustomException {
    Index currentIndex = indexService.getIndexByIndexCode(indexCode);
    System.out.println("Yes");
    if (null == currentIndex) {
      String noIndexFound = messageByLocaleService.getMessage("indexcode.notexists");
      LOGGER.info(noIndexFound);
      throw new CustomException(noIndexFound, HttpStatus.NOT_FOUND);
    }
    System.out.println("Yes");
    indexStocks.forEach(indexStock -> {
      if (null == stockService.getStockByStockCode(indexStock.getStockCode())) {
        String stockCodeNotFound = messageByLocaleService.getMessage("stockcode.notexists");
        LOGGER.info(stockCodeNotFound);
        throw new CustomException(indexStock.getStockCode() + " " + stockCodeNotFound,
            HttpStatus.NOT_FOUND);
      }
    });
    System.out.println("Yes");
   /* currentIndex.getStocks()
        .forEach(indexStock -> {
          if (stockService.getStockByStockCode(indexStocks.getStockCode()).equals()) {
            System.out.println("YES");
            String stockCodeNotFound = messageByLocaleService.getMessage("stock.no.stockcode");
            throw new CustomException(stockCodeNotFound, HttpStatus.NOT_FOUND);
          }
        });*/
    currentIndex.setStocks(indexStocks);

    double sum = 0;
    for (IndexStock iStock : indexStocks) {
      sum += iStock.getWeight();
    }
    if (1 != sum) {
      String noIndexFound = messageByLocaleService.getMessage("index.weight");
      LOGGER.info(noIndexFound);
      throw new CustomException(noIndexFound, HttpStatus.BAD_REQUEST);
    }
    indexService.updateIndex(currentIndex);
    LOGGER.info("Index with indexCode {} updated successfully", currentIndex.getIndexCode());
    return new ResponseEntity<>(currentIndex, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete an Index")
  @RequestMapping(method = RequestMethod.DELETE, value = "/index/{indexCode}")
  public ResponseEntity<Object> deleteIndexByIndexCode(@PathVariable String indexCode)
      throws CustomException {
    if (null == indexService.getIndexByIndexCode(indexCode)) {
      String noIndexFound = messageByLocaleService.getMessage("index.invalid.indexcode");
      LOGGER.info(noIndexFound);
      throw new CustomException(noIndexFound, HttpStatus.NOT_FOUND);
    }
    indexService.deleteIndexByIndexCode(indexCode);
    String indexDeleted = messageByLocaleService.getMessage("index.deleted");
    LOGGER.info("{} {}", indexCode, indexDeleted);
    return new ResponseEntity<>(indexCode + " " + indexDeleted, HttpStatus.OK);
  }

}