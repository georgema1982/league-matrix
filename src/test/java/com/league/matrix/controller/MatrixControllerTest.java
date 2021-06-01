package com.league.matrix.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.league.matrix.config.RestExceptionHandler;
import com.league.matrix.exception.CsvParseException;
import com.league.matrix.service.CsvService;
import com.league.matrix.service.MatrixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
class MatrixControllerTest {

  @InjectMocks
  private MatrixController matrixController;

  @Mock
  private CsvService csvService;

  @Mock
  private MatrixService matrixService;

  private MockMvc mockMvc;

  private MockMultipartFile multipartFile;

  private List<String[]> matrix = new ArrayList<>();

  @BeforeEach
  void setUp() throws IOException, CsvParseException {
    MockitoAnnotations.openMocks(this);
    mockMvc = standaloneSetup(matrixController).setControllerAdvice(new RestExceptionHandler()).build();
    multipartFile = new MockMultipartFile("file", "matrix.csv", MediaType.TEXT_PLAIN_VALUE, "".getBytes());
    when(csvService.parseCsv(multipartFile)).thenReturn(matrix);
  }

  @Test
  void echo() throws Exception {
    when(matrixService.echo(matrix)).thenReturn("1");
    testPostRequest("/echo", "1");
  }

  @Test
  void invert() throws Exception {
    when(matrixService.invert(matrix)).thenReturn("1");
    testPostRequest("/invert", "1");
  }

  @Test
  void flatten() throws Exception {
    when(matrixService.flatten(matrix)).thenReturn("1");
    testPostRequest("/flatten", "1");
  }

  @Test
  void sum() throws Exception {
    when(matrixService.sum(matrix)).thenReturn(1);
    testPostRequest("/sum", "1");
  }

  @Test
  void multiply() throws Exception {
    when(matrixService.multiply(matrix)).thenReturn(1);
    testPostRequest("/multiply", "1");
  }

  @Test
  void csfParsingError() throws Exception {
    when(csvService.parseCsv(multipartFile)).thenThrow(new CsvParseException("Test error"));
    mockMvc.perform(multipart("/echo").file(multipartFile)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(content().string("Test error"));
  }

  private void testPostRequest(String url, String expectedContent) throws Exception {
    mockMvc.perform(multipart(url).file(multipartFile)).andExpect(status().is(HttpStatus.OK.value())).andExpect(content().string(expectedContent));
  }
}
