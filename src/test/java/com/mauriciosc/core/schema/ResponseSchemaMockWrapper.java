package com.mauriciosc.core.schema;

/**
 * Class responsible for wrapping a mock schema of the response of the tests.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-30
 *
 */
public class ResponseSchemaMockWrapper {
	
	/**
	 * Mock implementation of {@link ResponseSchema} class.
	 * 
	 * @author Mauricio Souza Couto
	 * @since 2020-07-26
	 *
	 */
	public static class ResponseSchemaMock implements ResponseSchema {
		
		private String token;
	
		/**
		 * @return token field.
		 */
		public String getToken() {
			return token;
		}
	}
}