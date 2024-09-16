package com.nagarro.online_food_ordering_system.exception;


public class RecordAlreadyExistsException extends RuntimeException{
	private String error;
	private int code;
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
	public RecordAlreadyExistsException(String error, int code) {
		super();
		this.error = error;
		this.code = code;
	}
	public RecordAlreadyExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RecordAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	public RecordAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public RecordAlreadyExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public RecordAlreadyExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}

