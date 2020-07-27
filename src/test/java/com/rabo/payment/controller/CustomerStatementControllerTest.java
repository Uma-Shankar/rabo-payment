package com.rabo.payment.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabo.payment.dto.CustomerStatement;
import com.rabo.payment.dto.ErrorDetails;
import com.rabo.payment.service.CustomerStatementService;
import com.rabo.payment.utils.Constants;

@WebMvcTest
@RunWith(SpringRunner.class)
public class CustomerStatementControllerTest {

	private final static String VALIDATE_URI = "/api/v1/statement/validate";

	@Autowired
	private MockMvc mockMvc;

	@MockBean(name = "customerStatement")
	private CustomerStatementService customerStatementService;

	@Test
	@DisplayName("Test Validating customer statement with Successful record")
	public void customerStatementSuccessfulTest() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForSuccessfulTest();

		String requestJson = getObjectAsString(customerStatement);

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setResult(Constants.SUCCESSFUL);

		Mockito.when(customerStatementService.validateCustomerStatement(Mockito.any())).thenReturn(errorDetails);

		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VALIDATE_URI).contentType("application/json").content(requestJson))
				.andExpect(status().is(200)).andReturn();

		String content = result.getResponse().getContentAsString();

		JsonNode outputResult = readTree(content);

		Assertions.assertEquals(Constants.SUCCESSFUL, outputResult.get("result").asText());

	}

	@Test
	@DisplayName("Test Validating customer statement with Duplicate record")
	public void customerStatementDuplicateTest() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForDuplicateTest();

		String requestJson = getObjectAsString(customerStatement);

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setResult(Constants.DUPLICATE_REFERENCE);

		Mockito.when(customerStatementService.validateCustomerStatement(Mockito.any())).thenReturn(errorDetails);

		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VALIDATE_URI).contentType("application/json").content(requestJson))
				.andExpect(status().is(200)).andReturn();

		String content = result.getResponse().getContentAsString();

		JsonNode outputResult = readTree(content);

		Assertions.assertEquals(Constants.DUPLICATE_REFERENCE, outputResult.get("result").asText());

	}

	@Test
	@DisplayName("Test Validating customer statement with Incorrect balance record")
	public void customerStatementIncorrectTest() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForIncorrectTest();

		String requestJson = getObjectAsString(customerStatement);

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setResult(Constants.INCORRECT_END_BALANCE);

		Mockito.when(customerStatementService.validateCustomerStatement(Mockito.any())).thenReturn(errorDetails);

		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VALIDATE_URI).contentType("application/json").content(requestJson))
				.andExpect(status().is(200)).andReturn();

		String content = result.getResponse().getContentAsString();

		JsonNode outputResult = readTree(content);

		Assertions.assertEquals(Constants.INCORRECT_END_BALANCE, outputResult.get("result").asText());

	}

	@Test
	@DisplayName("Test Validating customer statement with Duplicate and Incorrect balance record")
	public void customerStatementDuplicateAndInCorrectTest() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForDuplicateAndInCorrectTest();

		String requestJson = getObjectAsString(customerStatement);

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setResult(Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);

		Mockito.when(customerStatementService.validateCustomerStatement(Mockito.any())).thenReturn(errorDetails);

		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VALIDATE_URI).contentType("application/json").content(requestJson))
				.andExpect(status().is(200)).andReturn();

		String content = result.getResponse().getContentAsString();

		JsonNode outputResult = readTree(content);

		Assertions.assertEquals(Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE,
				outputResult.get("result").asText());

	}

	@Test
	@DisplayName("Test Validating customer statement, Exception scenario")
	public void customerStatementExceptionTest() throws Exception {

		List<CustomerStatement> customerStatement = new ArrayList<>();

		String requestJson = getObjectAsString(customerStatement);

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setResult(Constants.INTERNAL_SERVER_ERROR);

		Mockito.when(customerStatementService.validateCustomerStatement(Mockito.any()))
				.thenThrow(NullPointerException.class);

		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VALIDATE_URI).contentType("application/json").content(requestJson))
				.andExpect(status().is(500)).andReturn();

		String content = result.getResponse().getContentAsString();

		JsonNode outputResult = readTree(content);

		Assertions.assertEquals(Constants.INTERNAL_SERVER_ERROR, outputResult.get("result").asText());

	}

	@Test
	@DisplayName("Test Validating customer statement with Bad Request")
	public void customerStatementBadRequestTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setResult(Constants.BAD_REQUEST);

		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.post(VALIDATE_URI).contentType("application/json"))
				.andExpect(status().is(400)).andReturn();

		String content = result.getResponse().getContentAsString();

		JsonNode outputResult = mapper.readTree(content);

		Assertions.assertEquals(Constants.BAD_REQUEST, outputResult.get("result").asText());

	}

	private List<CustomerStatement> getCustomerStatementForSuccessfulTest() {

		List<CustomerStatement> customerStatement = new ArrayList<>();

		CustomerStatement statement1 = new CustomerStatement();
		statement1.setReference(194261L);
		statement1.setAccountNumber("NL91RABO0315273637");
		statement1.setDescription("Clothes from Jan Bakker");
		statement1.setStartBalance(BigDecimal.valueOf(21.6));
		statement1.setMutation(BigDecimal.valueOf(-41.83));
		statement1.setEndBalance(BigDecimal.valueOf(-20.23));
		customerStatement.add(statement1);

		return customerStatement;
	}

	private List<CustomerStatement> getCustomerStatementForDuplicateTest() {

		List<CustomerStatement> customerStatement = new ArrayList<>();

		// Customer statement 1
		CustomerStatement statement1 = new CustomerStatement();
		statement1.setReference(194261L);
		statement1.setAccountNumber("NL91RABO0315273637");
		statement1.setDescription("Clothes from Jan Bakker");
		statement1.setStartBalance(BigDecimal.valueOf(21.6));
		statement1.setMutation(BigDecimal.valueOf(-41.83));
		statement1.setEndBalance(BigDecimal.valueOf(-20.23));
		customerStatement.add(statement1);

		// Customer statement 2 Duplicate Record
		CustomerStatement statement2 = new CustomerStatement();
		statement2.setReference(194261L);
		statement2.setAccountNumber("NL91RABO0315273637");
		statement2.setDescription("Clothes from Jan Bakker");
		statement2.setStartBalance(BigDecimal.valueOf(21.6));
		statement2.setMutation(BigDecimal.valueOf(-41.83));
		statement2.setEndBalance(BigDecimal.valueOf(-20.23));

		customerStatement.add(statement2);

		return customerStatement;
	}

	private List<CustomerStatement> getCustomerStatementForIncorrectTest() {

		List<CustomerStatement> customerStatement = new ArrayList<>();

		// Customer statement 1 Incorrect Record
		CustomerStatement statement1 = new CustomerStatement();
		statement1.setReference(194261L);
		statement1.setAccountNumber("NL91RABO0315273637");
		statement1.setDescription("Clothes from Jan Bakker");
		statement1.setStartBalance(BigDecimal.valueOf(21.6));
		statement1.setMutation(BigDecimal.valueOf(-41.83));
		statement1.setEndBalance(BigDecimal.valueOf(-21.23));
		customerStatement.add(statement1);

		return customerStatement;
	}

	private List<CustomerStatement> getCustomerStatementForDuplicateAndInCorrectTest() {

		List<CustomerStatement> customerStatement = new ArrayList<>();

		// Customer statement 1
		CustomerStatement statement1 = new CustomerStatement();
		statement1.setReference(194261L);
		statement1.setAccountNumber("NL91RABO0315273637");
		statement1.setDescription("Clothes from Jan Bakker");
		statement1.setStartBalance(BigDecimal.valueOf(21.6));
		statement1.setMutation(BigDecimal.valueOf(-41.83));
		statement1.setEndBalance(BigDecimal.valueOf(-21.23));
		customerStatement.add(statement1);

		// Customer statement 2 Duplicate Record
		CustomerStatement statement2 = new CustomerStatement();
		statement2.setReference(194261L);
		statement2.setAccountNumber("NL91RABO0315273637");
		statement2.setDescription("Clothes from Jan Bakker");
		statement2.setStartBalance(BigDecimal.valueOf(21.6));
		statement2.setMutation(BigDecimal.valueOf(-41.83));
		statement2.setEndBalance(BigDecimal.valueOf(-20.23));

		customerStatement.add(statement2);

		return customerStatement;
	}

	private String getObjectAsString(List<CustomerStatement> customerStatement) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

		String requestJson = ow.writeValueAsString(customerStatement);

		return requestJson;
	}

	private JsonNode readTree(String content) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode outputResult = mapper.readTree(content);
		return outputResult;

	}
}
