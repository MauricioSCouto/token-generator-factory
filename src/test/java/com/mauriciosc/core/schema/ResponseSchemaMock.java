package com.mauriciosc.core.schema;

/**
 * Mock implementation of {@link ResponseSchema} class.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-26
 *
 */
public class ResponseSchemaMock implements ResponseSchema {

	private String token;

	/**
	 * @return token field.
	 */
	public String getToken() {
		return token;
	}
}