package com.rabo.payment.service;

import java.util.List;

import com.rabo.payment.dto.CustomerStatement;
import com.rabo.payment.dto.ErrorDetails;

public interface CustomerStatementService {

	public ErrorDetails validateCustomerStatement(List<CustomerStatement> customerStatement) throws Exception;
}
