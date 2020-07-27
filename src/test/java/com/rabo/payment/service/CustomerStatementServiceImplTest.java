package com.rabo.payment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.rabo.payment.dto.CustomerStatement;
import com.rabo.payment.dto.ErrorDetails;
import com.rabo.payment.utils.Constants;

@SpringBootTest
@ActiveProfiles("test")
public class CustomerStatementServiceImplTest {

	@Autowired
	private CustomerStatementService customerStatementService;

	@Test
	@DisplayName("Test Validating customer statement with successful record")
	public void validateCustomerStatementSuccessfulRecord() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForSuccessfulTest();

		ErrorDetails errorDetails = customerStatementService.validateCustomerStatement(customerStatement);

		Assertions.assertEquals(Constants.SUCCESSFUL, errorDetails.getResult());
		Assertions.assertEquals(0, errorDetails.getErrorRecords().size());
	}

	@Test
	@DisplayName("Test Validating customer statement with Duplicate record")
	public void validateCustomerStatementDuplicateRecord() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForDuplicateTest();

		ErrorDetails errorDetails = customerStatementService.validateCustomerStatement(customerStatement);

		Assertions.assertEquals(Constants.DUPLICATE_REFERENCE, errorDetails.getResult());
		Assertions.assertEquals(1, errorDetails.getErrorRecords().size());
		Assertions.assertEquals(194261L, errorDetails.getErrorRecords().get(0).getReference());
		Assertions.assertEquals("NL91RABO0315273637", errorDetails.getErrorRecords().get(0).getAccountNumber());
	}

	@Test
	@DisplayName("Test Validating customer statement with Incorrect balance record")
	public void validateCustomerStatementInCorrectBalance() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForIncorrectTest();

		ErrorDetails errorDetails = customerStatementService.validateCustomerStatement(customerStatement);

		Assertions.assertEquals(Constants.INCORRECT_END_BALANCE, errorDetails.getResult());
		Assertions.assertEquals(1, errorDetails.getErrorRecords().size());
		Assertions.assertEquals(194261L, errorDetails.getErrorRecords().get(0).getReference());
		Assertions.assertEquals("NL91RABO0315273637", errorDetails.getErrorRecords().get(0).getAccountNumber());
	}

	@Test
	@DisplayName("Test Validating customer statement with Duplicate and Incorrect balance record")
	public void validateCustomerStatementDuplicateAndInCorrectBalance() throws Exception {

		List<CustomerStatement> customerStatement = getCustomerStatementForDuplicateAndInCorrectTest();

		ErrorDetails errorDetails = customerStatementService.validateCustomerStatement(customerStatement);

		Assertions.assertEquals(Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, errorDetails.getResult());
		Assertions.assertEquals(2, errorDetails.getErrorRecords().size());
		Assertions.assertEquals(194261L, errorDetails.getErrorRecords().get(0).getReference());
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
