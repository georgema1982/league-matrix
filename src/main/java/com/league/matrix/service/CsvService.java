package com.league.matrix.service;

import com.league.matrix.exception.CsvParseException;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvService {

  /**
   * Parse and validate uploaded csv file
   *
   * @param file Uploaded csv file
   * @return A list of string arrays representing the matrix
   * @throws IOException I/O exception
   * @throws CsvParseException Csv validation error
   */
  public List<String[]> parseCsv(MultipartFile file) throws IOException, CsvParseException {
    Reader isReader = null;
    CSVReader csvReader = null;
    try {
      isReader = new InputStreamReader(file.getInputStream());
      csvReader = new CSVReader(isReader);
      List<String[]> matrix = new ArrayList<>();
      String[] line;
      int dimension = 0;
      while ((line = csvReader.readNext()) != null) {
        if (dimension == 0) {
          dimension = line.length;
        } else {
          if (dimension != line.length) {
            throw new CsvParseException("There are lines of different size from other lines");
          }
        }
        if (Arrays.stream(line).anyMatch(element -> {
          try {
            Integer.parseInt(element);
            return false;
          } catch (NumberFormatException e) {
            return true;
          }
        })) {
          throw new CsvParseException("There are invalid integers in the matrix");
        };
        matrix.add(line);
      }
      if (matrix.size() == 0) {
        throw new CsvParseException("The matrix is empty");
      }
      if (dimension != matrix.size()) {
        throw new CsvParseException("The number of rows does not equal to the number of columns");
      }
      return matrix;
    } finally {
      if (isReader != null) {
        isReader.close();
      }
      if (csvReader != null) {
        csvReader.close();
      }
    }
  }
}
