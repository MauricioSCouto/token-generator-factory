package com.mauriciosc.core.exception;

/**
 * Exception that indicates problems of response schema handling (e.g. response
 * data unmarshalling).
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-26
 *
 */
public class ResponseSchemaMappingException extends Exception {

	private static final long serialVersionUID = 2438259996751531650L;

	/**
	 * Constructs a new exception with the specified detail message. Thecause is not
	 * initialized, and may subsequently be initialized bya call to initCause.
	 * 
	 * @param message - the detail message. The detail message is saved forlater
	 *                retrieval by the getMessage() method.
	 */
	public ResponseSchemaMappingException(String message) {
		super(message);
	}
}
