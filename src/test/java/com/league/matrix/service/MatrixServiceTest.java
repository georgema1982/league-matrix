package com.league.matrix.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class MatrixServiceTest {

  private MatrixService matrixService;

  final private List<String[]> matrix = Arrays.asList(new String[] {"1", "2", "3"}, new String[] {"4", "5", "6"}, new String[] {"7", "8", "9"});

  @BeforeEach
  void setUp() {
    matrixService = new MatrixService();
  }

  @Test
  void echo() {
    assertEquals("1,2,3\n4,5,6\n7,8,9", matrixService.echo(matrix));
  }

  @Test
  void invert() {
    assertEquals("1,4,7\n2,5,8\n3,6,9", matrixService.invert(matrix));
  }

  @Test
  void flatten() {
    assertEquals("1,2,3,4,5,6,7,8,9", matrixService.flatten(matrix));
  }

  @Test
  void sum() {
    assertEquals(45, matrixService.sum(matrix));
  }

  @Test
  void multiply() {
    assertEquals(362880, matrixService.multiply(matrix));
  }
}
