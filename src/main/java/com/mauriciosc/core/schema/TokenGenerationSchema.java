package com.mauriciosc.core.schema;

import org.springframework.stereotype.Component;

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
@Component
public class TokenGenerationSchema {

	private HeaderModelRequest headerModelRequest;
	private TokenModelRequest tokenModelRequest;
	private Class<? extends ResponseSchema> responseSchema;
	
	/**
	 * No args constructor to initiate the class.
	 */
	public TokenGenerationSchema() {
		// no-args constructor
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

	/**
	 * @param headerModelRequest the headerModelRequest to set
	 */
	public void setHeaderModelRequest(HeaderModelRequest headerModelRequest) {
		this.headerModelRequest = headerModelRequest;
	}

	/**
	 * @param tokenModelRequest the tokenModelRequest to set
	 */
	public void setTokenModelRequest(TokenModelRequest tokenModelRequest) {
		this.tokenModelRequest = tokenModelRequest;
	}

	/**
	 * @param responseSchema the responseSchema to set
	 */
	public void setResponseSchema(Class<? extends ResponseSchema> responseSchema) {
		this.responseSchema = responseSchema;
	}	
}
