package com.mauriciosc.core.schema;

import com.mauriciosc.core.model.request.HeaderModelRequest;
import com.mauriciosc.core.model.request.TokenModelRequest;

/**
 * Schema maintaining the structure of the token generation communication. It
 * keeps data related to headers, request and response structure of the token
 * generation calls.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-24
 *
 */
public class TokenGenerationSchema {

	private HeaderModelRequest headerModelRequest;
	private TokenModelRequest tokenModelRequest;
	private Class<? extends ResponseSchema> responseSchema;
	
	/**
	 * All args constructor to set properties on instance setup.
	 * 
	 * @param headerModelRequest - object containing headers for the token
	 *                           generation call.
	 * @param tokenModelRequest  - object containing request body data for the token
	 *                           generation call.
	 * @param responseSchema     - object representing the structure of the response
	 *                           coming from the token generation call.
	 */
	public TokenGenerationSchema(HeaderModelRequest headerModelRequest, TokenModelRequest tokenModelRequest, Class<? extends ResponseSchema> responseSchema) {
		this.headerModelRequest = headerModelRequest;
		this.tokenModelRequest = tokenModelRequest;
		this.responseSchema = responseSchema;
	}
	
	/**
	 * Returns header data.
	 * @return {@code HeaderModelRequest} - headers model field.
	 */
	public HeaderModelRequest getHeaderModelRequest() {
		return headerModelRequest;
	}

	/**
	 * Returns request body data.
	 * @return {@code RequestModelRequest} - request body model field.
	 */
	public TokenModelRequest getTokenModelRequest() {
		return tokenModelRequest;
	}

	/**
	 * Returns response body schema.
	 * 
	 * @return {@code Class} - field representig the structure of the response body.
	 */
	public Class<? extends ResponseSchema> getResponseSchema() {
		return responseSchema;
	}
}
