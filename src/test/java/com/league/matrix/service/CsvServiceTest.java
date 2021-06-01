package com.league.matrix.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.league.matrix.exception.CsvParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class CsvServiceTest {

  private CsvService csvService;

  @BeforeEach
  void setUp() {
    csvService = new CsvService();
  }

  @Test
  void parseProperCsv() throws IOException, CsvParseException {
    List<String[]> matrix = csvService.parseCsv(generateMultipartFile("1,2,3\n4,5,6\n7,8,9"));
    List<String[]> expectedMatrix = Arrays.asList(new String[] {"1", "2", "3"}, new String[] {"4", "5", "6"}, new String[] {"7", "8", "9"});
    final int dimension = 3;
    assertEquals(dimension, matrix.size());
    IntStream.range(0, dimension).forEach(lineNum -> {
      String[] line = matrix.get(lineNum);
      assertEquals(dimension, line.length);
      assertTrue(IntStream.range(0, dimension).allMatch(colNum -> line[colNum].equals(expectedMatrix.get(lineNum)[colNum])));
    });
  }

  @Test
  void parseEmptyCsv() {
    parseInvalidCsv("", "is empty");
  }

  @Test
  void parseCsvWithLinesOfDifferentSize() {
    parseInvalidCsv("1,2\n3,4,5", "different size");
  }

  @Test
  void parseCsvWithInvalidIntegers() {
    parseInvalidCsv("1,2\n3,four", "invalid integers");
  }

  @Test
  void parseCsvWithRowColumnNumbersUnequal() {
    parseInvalidCsv("1,2,3\n4,5,6", "not equal");
  }

  private MultipartFile generateMultipartFile(String content) {
    return new MockMultipartFile("file", "matrix.csv", MediaType.TEXT_PLAIN_VALUE, content.getBytes());
  }

  private void parseInvalidCsv(String content, String msgKeyword) {
    CsvParseException exception = assertThrows(CsvParseException.class, () -> csvService.parseCsv(generateMultipartFile(content)));
    assertTrue(exception.getMessage().contains(msgKeyword));
  }
}
