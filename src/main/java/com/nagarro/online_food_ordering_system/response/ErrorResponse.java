package com.nagarro.online_food_ordering_system.response;

import java.time.LocalDateTime;


public class ErrorResponse {
	private String error;
	
	private int code;
	
	public ErrorResponse(String error, int code, LocalDateTime timestamp) {
		super();
		this.error = error;
		this.code = code;
		this.timestamp = timestamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	private LocalDateTime timestamp;

	public ErrorResponse() {
		super();
	}

	public ErrorResponse(String error, int code) {
		this.error = error;
		this.code = code;
		this.timestamp = LocalDateTime.now();
	}

}
