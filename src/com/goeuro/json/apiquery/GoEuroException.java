package com.goeuro.json.apiquery;

public class GoEuroException extends Exception {

	/**
	 * @author Vinil J General exception class for Go Euro JSON Reader API class
	 *         Lower level exceptions are also wrapped into it
	 */
	private static final long serialVersionUID = -8000067835939820292L;

	private String errMessage = "UNKNOWN_EXCEPTION";

	public GoEuroException(String message, String errMessage) {
		super(message);
		this.errMessage = errMessage;
	}

	public String getErrorMessage() {
		return this.errMessage;
	}

}
