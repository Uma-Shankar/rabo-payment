package com.rabo.payment.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorDetails {

	private String result;

	private List<ErrorRecords> errorRecords = new ArrayList<ErrorRecords>();

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<ErrorRecords> getErrorRecords() {
		return errorRecords;
	}

	public void setErrorRecords(List<ErrorRecords> errorRecords) {
		this.errorRecords = errorRecords;
	}

}
