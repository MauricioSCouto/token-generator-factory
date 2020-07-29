package com.mauriciosc.core.exception;

/**
 * Exception that indicates missing of the authentication url parameterization.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-26
 *
 */
public class UrlNotProvidedException extends Exception {

	private static final long serialVersionUID = -8038699365909505201L;

	/**
	 * Constructs a new exception with the specified detail message. Thecause is not
	 * initialized, and may subsequently be initialized bya call to initCause.
	 * 
	 * @param message - the detail message. The detail message is saved forlater
	 *                retrieval by the getMessage() method.
	 */
	public UrlNotProvidedException(String message) {
		super(message);
	}
}
