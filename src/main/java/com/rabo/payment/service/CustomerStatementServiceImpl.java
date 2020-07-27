package com.rabo.payment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rabo.payment.dto.CustomerStatement;
import com.rabo.payment.dto.ErrorDetails;
import com.rabo.payment.dto.ErrorRecords;
import com.rabo.payment.utils.Constants;

@Service
public class CustomerStatementServiceImpl implements CustomerStatementService {

	@Override
	public ErrorDetails validateCustomerStatement(List<CustomerStatement> customerStatement) throws Exception {

		Boolean hasDuplicates = false;
		Boolean hasIncorrectEndBalance = false;

		List<ErrorRecords> errorRecords = new ArrayList<ErrorRecords>();

		for (int i = 0; i < customerStatement.size(); i++) {
			for (int j = i + 1; j < customerStatement.size(); j++) {

				if (customerStatement.get(i).getReference().equals(customerStatement.get(j).getReference())) {
					hasDuplicates = true;
					ErrorRecords errorRecord = new ErrorRecords();
					errorRecord.setReference(customerStatement.get(i).getReference());
					errorRecord.setAccountNumber(customerStatement.get(j).getAccountNumber());
					errorRecords.add(errorRecord);

				}

			}

			BigDecimal endBalance = customerStatement.get(i).getStartBalance()
					.add(customerStatement.get(i).getMutation());

			if (endBalance.compareTo(customerStatement.get(i).getEndBalance()) != 0) {
				hasIncorrectEndBalance = true;

				ErrorRecords errorRecord = new ErrorRecords();
				errorRecord.setReference(customerStatement.get(i).getReference());
				errorRecord.setAccountNumber(customerStatement.get(i).getAccountNumber());
				errorRecords.add(errorRecord);
			}
		}

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setErrorRecords(errorRecords);

		if (hasDuplicates && hasIncorrectEndBalance)
			errorDetails.setResult(Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
		else if (hasDuplicates)
			errorDetails.setResult(Constants.DUPLICATE_REFERENCE);
		else if (hasIncorrectEndBalance)
			errorDetails.setResult(Constants.INCORRECT_END_BALANCE);
		else
			errorDetails.setResult(Constants.SUCCESSFUL);

		return errorDetails;
	}

}
