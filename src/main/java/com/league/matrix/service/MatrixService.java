package com.league.matrix.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MatrixService {

  /**
   * Return the matrix as a string in matrix format.
   *
   * @param matrix The matrix
   * @return A string in matrix format
   */
  public String echo(List<String[]> matrix) {
    return convertMatrixToString(matrix, "\n");
  }

  /**
   * Return the matrix as a string in matrix format where the columns and rows are inverted
   *
   * @param matrix The matrix
   * @return A string in matrix format
   */
  public String invert(List<String[]> matrix) {
    return IntStream.range(0, matrix.size()).mapToObj(index -> matrix.stream().map(line -> line[index]).collect(Collectors.joining(",")))
        .collect(Collectors.joining("\n"));
  }

  /**
   * Return the matrix as a 1 line string, with values separated by commas.
   *
   * @param matrix The matrix
   * @return The matrix as a 1 line string
   */
  public String flatten(List<String[]> matrix) {
    return convertMatrixToString(matrix, ",");
  }

  /**
   * Return the sum of the integers in the matrix
   *
   * @param matrix The matrix
   * @return The sum of the integers in the matrix
   */
  public int sum(List<String[]> matrix) {
    return matrix.stream().map(line -> Arrays.stream(line).collect(Collectors.summingInt(Integer::parseInt)))
        .collect(Collectors.summingInt(Integer::intValue));
  }

  /**
   * Return the product of the integers in the matrix
   *
   * @param matrix The matrix
   * @return the product of the integers in the matrix
   */
  public int multiply(List<String[]> matrix) {
    return matrix.stream().map(line -> Arrays.stream(line).mapToInt(Integer::parseInt).reduce(1, MatrixService::multiple))
        .reduce(1, MatrixService::multiple);
  }

  private String convertMatrixToString(List<String[]> matrix, String lineDelimiter) {
    return matrix.stream().map(line -> Arrays.stream(line).collect(Collectors.joining(","))).collect(Collectors.joining(lineDelimiter));
  }

  private static int multiple(int a, int b) {
    return a * b;
  }
}
