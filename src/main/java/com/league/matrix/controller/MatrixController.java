package com.league.matrix.controller;

import com.league.matrix.exception.CsvParseException;
import com.league.matrix.service.CsvService;
import com.league.matrix.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@RestController
public class MatrixController {

  @Autowired
  private CsvService csvService;

  @Autowired
  private MatrixService matrixService;

  @PostMapping("echo")
  public String echo(@RequestParam("file") MultipartFile file) throws IOException, CsvParseException {
    return process(matrixService::echo, file);
  }

  @PostMapping("invert")
  public String invert(@RequestParam("file") MultipartFile file) throws IOException, CsvParseException {
    return process(matrixService::invert, file);
  }

  @PostMapping("flatten")
  public String flatten(@RequestParam("file") MultipartFile file) throws IOException, CsvParseException {
    return process(matrixService::flatten, file);
  }

  @PostMapping("sum")
  public int sum(@RequestParam("file") MultipartFile file) throws IOException, CsvParseException {
    return process(matrixService::sum, file);
  }

  @PostMapping("multiply")
  public int multiply(@RequestParam("file") MultipartFile file) throws IOException, CsvParseException {
    return process(matrixService::multiply, file);
  }

  /**
   * Generic method to process http requests
   *
   * @param function A function that takes a matrix and returns a result
   * @param file The uploaded file
   * @param <R> Return type of the function
   * @return Result
   * @throws IOException I/O exception
   * @throws CsvParseException CSV validation error
   */
  private <R> R process(Function<List<String[]>, R> function, MultipartFile file) throws IOException, CsvParseException {
    return function.apply(csvService.parseCsv(file));
  }
}
