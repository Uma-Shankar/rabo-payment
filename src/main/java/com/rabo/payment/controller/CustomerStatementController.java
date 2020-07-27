package com.rabo.payment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.rabo.payment.dto.CustomerStatement;
import com.rabo.payment.utils.URLConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(value = URLConstants.STATEMENT)
@Api(value = "Validating Rest APIs")
public interface CustomerStatementController {

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	@ApiOperation(value = "Validating customer statment", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "SUCCESSFUL"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "Something went wrong with server. Please try after some times") })
	public ResponseEntity<?> validateCustomerStatement(@Valid @RequestBody List<CustomerStatement> customerStatement);

	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request);

}
