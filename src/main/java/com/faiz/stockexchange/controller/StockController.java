package com.faiz.stockexchange.controller;

import com.faiz.stockexchange.domain.Index;
import com.faiz.stockexchange.domain.Stock;
import com.faiz.stockexchange.domain.StockValue;
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


@Api(description = "This is Stock API")
@Slf4j
@RestController
public class StockController {

  private static Logger LOGGER = LoggerFactory.getLogger("Logging StockController API");

  @Autowired
  MessageByLocaleService messageByLocaleService;

  @Autowired
  private StockService stockService;

  @Autowired
  private IndexService indexService;

  @ApiOperation(value = "View List of Stocks")
  @RequestMapping(method = RequestMethod.GET, value = "/stocks")
  public List<Stock> getAllStocks() {
    LOGGER.info("Getting All Stocks");
    return stockService.getAllStocks();
  }

  @ApiOperation(value = "Get Stock by StockCode")
  @RequestMapping(method = RequestMethod.GET, value = "/stocks/{stockCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getStock(@PathVariable("stockCode") String stockCode) {
    //System.out.println(stock);
    if (!stockService.isStockCodeExists(stockCode)) {
      String stockCodeNotFound = messageByLocaleService.getMessage("stockcode.notexists");
      LOGGER.info(stockCodeNotFound);
      throw new CustomException(stockCodeNotFound, HttpStatus.NOT_FOUND);
    }
    Stock stock = stockService.getStockByStockCode(stockCode);
    LOGGER.info("Getting Stock with stockCode {}", stockCode);
    return new ResponseEntity<>(stock, HttpStatus.OK);
  }

  @ApiOperation(value = "Create a new Stock")
  @RequestMapping(method = RequestMethod.POST, value = "/stocks")
  public ResponseEntity<Object> createStock(@RequestBody @Valid Stock stock) {
    if (stockService.isStockCodeExists(stock.getStockCode())) {
      String stockCodeAlreadyExists = messageByLocaleService.getMessage("stockcode.exists");
      LOGGER.info(stockCodeAlreadyExists);
      throw new CustomException(stockCodeAlreadyExists, HttpStatus.CONFLICT);
    }
    if (stockService.isStockNameExists(stock.getStockName())) {
      String stockNameAlreadyExists = messageByLocaleService.getMessage("stockname.exists");
      LOGGER.info(stockNameAlreadyExists);
      throw new CustomException(stockNameAlreadyExists, HttpStatus.CONFLICT);
    }
    stockService.createStock(stock);
    LOGGER.info("Stock with stockCode {} created successfully", stock.getStockCode());
    return new ResponseEntity<>(stock, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Update an existing Stock")
  @RequestMapping(method = RequestMethod.PUT, value = "/stocks/{stockCode}")
  public ResponseEntity<Object> updateStock(@Valid @PathVariable("stockCode") String stockCode,
      @Valid @RequestBody Set<StockValue> stockValue) {
    if (!stockService.isStockCodeExists(stockCode)) {
      String stockCodeNotFound = messageByLocaleService.getMessage("stockcode.notexists");
      LOGGER.info(stockCodeNotFound);
      throw new CustomException(stockCodeNotFound, HttpStatus.NOT_FOUND);
    }
    Stock currentStock = stockService.getStockByStockCode(stockCode);
    currentStock.setStockValue(stockValue);
    stockService.updateStock(currentStock);
    LOGGER.info("Stock with stockCode {} updated successfully", stockCode);
    return new ResponseEntity<>(stockService.getStockByStockName(currentStock.getStockName()),
        HttpStatus.OK);
  }

  @ApiOperation(value = "Delete a Stock")
  @RequestMapping(value = "/stocks/{stockCode}", method = RequestMethod.DELETE)
  public ResponseEntity<Object> deleteStock(@PathVariable("stockCode") String stockCode) {
    //Stock currentStock = stockService.getStockByStockCode(stockCode);
    if (!stockService.isStockCodeExists(stockCode)) {
      String stockCodeNotFound = messageByLocaleService.getMessage("stockcode.notexists");
      LOGGER.info(stockCodeNotFound);
      throw new CustomException(stockCodeNotFound, HttpStatus.NOT_FOUND);
    }
    List<Index> currentIndex = indexService.getAllIndices();
    currentIndex.forEach(index -> index.getStocks().forEach(indexStock -> {
      if (indexStock.getStockCode().equals(stockCode)) {
        String stockCodeInIndex = messageByLocaleService.getMessage("stock.present.index");
        LOGGER.info(stockCodeInIndex);
        throw new CustomException(stockCodeInIndex, HttpStatus.METHOD_NOT_ALLOWED);
      }
    }));

    if (!stockService.deleteStockByStockCode(stockCode)) {
      String stockCodeInIndex = messageByLocaleService.getMessage("stockcode.notexists");
      throw new CustomException(stockCodeInIndex, HttpStatus.BAD_REQUEST);
    }
    String stockDeleted = messageByLocaleService.getMessage("stock.deleted");
    LOGGER.info("Stock deleted with stockCode {} successfully", stockCode);
    return new ResponseEntity<>(stockCode + " " + stockDeleted, HttpStatus.OK);
  }

  @ApiOperation(value = "Delete a StockValue")
  @RequestMapping(value = "/stocks/{stockCode}/{stockValueIndex}", method = RequestMethod.DELETE)
  public ResponseEntity<Object> deleteStockValue(@PathVariable("stockCode") String stockCode,
      @PathVariable("stockValueIndex") String stockValueIndex) {

    stockService.deleteStockValue(stockCode, stockValueIndex);
    String stockDeleted = messageByLocaleService.getMessage("stock.deleted");
    LOGGER.info("Stock deleted with stockCode {} successfully", stockCode);
    return new ResponseEntity<>(stockCode + " " + stockDeleted, HttpStatus.OK);
  }

  @ApiOperation(value = "Update a StockValue")
  @RequestMapping(value = "/stocks/{stockCode}/{stockValueIndex}", method = RequestMethod.PUT)
  public ResponseEntity<Object> updateStockValue(@PathVariable("stockCode") String stockCode,
      @PathVariable("stockValueIndex") String stockValueIndex,
      @Valid @RequestBody StockValue stockValue) {

    stockService.deleteStockValue(stockCode, stockValueIndex);
    stockService.updateStockValue(stockCode, stockValueIndex, stockValue);
    System.out.println("StockValue with stockCode {} successfully Updated." + stockValue);
    String stockDeleted = "Updated";
    LOGGER.info("StockValue with stockCode {} successfully Updated.", stockCode);
    return new ResponseEntity<>(stockCode + " " + stockDeleted, HttpStatus.OK);
  }

}