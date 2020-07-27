package com.rabo.payment.restassured;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabo.payment.RaboPaymentApplication;
import com.rabo.payment.dto.CustomerStatement;
import com.rabo.payment.dto.ErrorDetails;
import com.rabo.payment.utils.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaboPaymentApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerStatementControllerIntegrationTest {

	private final static String VALIDATE_URI = "/api/v1/statement/validate";

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	@DisplayName("Integration Testing: Validating customer statement with Successful record")
	public void customerStatementSuccessfulTest() throws URISyntaxException {

		List<CustomerStatement> customerStatement = getCustomerStatementForSuccessfulTest();

		ResponseEntity<ErrorDetails> result = this.restTemplate.postForEntity(getURI(VALIDATE_URI), customerStatement,
				ErrorDetails.class);

		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertEquals(Constants.SUCCESSFUL, result.getBody().getResult());
		Assertions.assertEquals(0, result.getBody().getErrorRecords().size());

	}

	@Test
	@DisplayName("Integration Testing: Validating customer statement with Duplicate record")
	public void customerStatementDuplicateTest() throws URISyntaxException {

		List<CustomerStatement> customerStatement = getCustomerStatementForDuplicateTest();

		ResponseEntity<ErrorDetails> result = this.restTemplate.postForEntity(getURI(VALIDATE_URI), customerStatement,
				ErrorDetails.class);

		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertEquals(Constants.DUPLICATE_REFERENCE, result.getBody().getResult());
		Assertions.assertEquals(1, result.getBody().getErrorRecords().size());
		Assertions.assertEquals(194261L, result.getBody().getErrorRecords().get(0).getReference());

	}

	@Test
	@DisplayName("Integration Testing: Validating customer statement with Incorrect balance record")
	public void customerStatementIncorrectTest() throws URISyntaxException {

		List<CustomerStatement> customerStatement = getCustomerStatementForIncorrectTest();

		ResponseEntity<ErrorDetails> result = this.restTemplate.postForEntity(getURI(VALIDATE_URI), customerStatement,
				ErrorDetails.class);

		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertEquals(Constants.INCORRECT_END_BALANCE, result.getBody().getResult());
		Assertions.assertEquals(1, result.getBody().getErrorRecords().size());
		Assertions.assertEquals(194261L, result.getBody().getErrorRecords().get(0).getReference());

	}

	@Test
	@DisplayName("Integration Testing: Validating customer statement with Duplicate and Incorrect balance record")
	public void customerStatementDuplicateAndInCorrectTest() throws URISyntaxException {

		List<CustomerStatement> customerStatement = getCustomerStatementForDuplicateAndInCorrectTest();

		ResponseEntity<ErrorDetails> result = this.restTemplate.postForEntity(getURI(VALIDATE_URI), customerStatement,
				ErrorDetails.class);

		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertEquals(Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, result.getBody().getResult());
		Assertions.assertEquals(2, result.getBody().getErrorRecords().size());
		Assertions.assertEquals(194261L, result.getBody().getErrorRecords().get(0).getReference());

	}

	@Test
	@DisplayName("Integration Testing: Validating customer statement with Bad Request")
	public void customerStatementBadRequestTest() throws URISyntaxException {

		ResponseEntity<ErrorDetails> result = this.restTemplate.postForEntity(getURI(VALIDATE_URI), getEntity(""),
				ErrorDetails.class);

		Assertions.assertEquals(400, result.getStatusCodeValue());
		Assertions.assertEquals(Constants.BAD_REQUEST, result.getBody().getResult());

	}

	private URI getURI(String urlString) throws URISyntaxException {

		final String baseUrl = "http://localhost:" + port + VALIDATE_URI;
		URI uri = new URI(baseUrl);

		return uri;
	}

	private HttpEntity<String> getEntity(Object body) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(body.toString(), headers);

		return entity;
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

}
