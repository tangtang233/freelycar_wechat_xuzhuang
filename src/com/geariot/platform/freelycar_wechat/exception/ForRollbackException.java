package com.geariot.platform.freelycar_wechat.exception;

public class ForRollbackException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errorCode;
	
	public ForRollbackException() {
		super();
	}

	public ForRollbackException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ForRollbackException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ForRollbackException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForRollbackException(String message) {
		super(message);
	}

	public ForRollbackException(Throwable cause) {
		super(cause);
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
