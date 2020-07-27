package com.rabo.payment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rabo.payment.dto.CustomerStatement;
import com.rabo.payment.dto.ErrorDetails;
import com.rabo.payment.service.CustomerStatementService;
import com.rabo.payment.utils.Constants;

@RestController
@ControllerAdvice
public class CustomerStatementControllerImpl extends ResponseEntityExceptionHandler
		implements CustomerStatementController {

	@Autowired
	private CustomerStatementService customerStatementService;

	@Override
	public ResponseEntity<?> validateCustomerStatement(@Valid List<CustomerStatement> customerStatement) {

		try {
			ErrorDetails errorDetails = customerStatementService.validateCustomerStatement(customerStatement);

			return ResponseEntity.ok(errorDetails);

		} catch (Exception e) {
			ErrorDetails errorDetails = new ErrorDetails();

			errorDetails.setResult(Constants.INTERNAL_SERVER_ERROR);

			return new ResponseEntity<Object>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails();

		errorDetails.setResult(Constants.BAD_REQUEST);

		return new ResponseEntity<Object>(errorDetails, HttpStatus.BAD_REQUEST);

	}
}
