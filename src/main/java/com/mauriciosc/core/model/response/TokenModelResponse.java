package com.mauriciosc.core.model.response;

import org.springframework.stereotype.Component;

import com.mauriciosc.core.schema.ResponseSchema;

/**
 * Class that handles all data regarding to the token generation process.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-25
 *
 */
@Component
public class TokenModelResponse {
	
	private ResponseSchema tokenModelResponse;

	/**
	 * Returns the response field upheld in the class.
	 * 
	 * @return {@code ResponseSchema} - response field.
	 */
	public ResponseSchema getTokenModelResponse() {
		return tokenModelResponse;
	}

	/**
	 * Sets a {@link ResponseSchema} over the class field.
	 * 
	 * @param response - response schema to set the field response.
	 */
	public void setResponse(ResponseSchema response) {
		this.tokenModelResponse = response;
	}

}
