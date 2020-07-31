package com.mauriciosc.core.exception;

/**
 * Exception that indicates problems of headers manipulation.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-26
 *
 */
public class TokenGenerationHeaderManipulationException extends Exception {

	private static final long serialVersionUID = 1232869075921349347L;

	/**
	 * Constructs a new exception with the specified detail message. Thecause is not
	 * initialized, and may subsequently be initialized bya call to initCause.
	 * 
	 * @param message - the detail message. The detail message is saved forlater
	 *                retrieval by the getMessage() method.
	 */
	public TokenGenerationHeaderManipulationException(String message) {
		super(message);
	}
}
