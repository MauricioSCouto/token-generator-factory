package com.mauriciosc.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import com.mauriciosc.core.model.request.HeaderModelRequestMock;
import com.mauriciosc.core.model.request.TokenModelRequestMock;
import com.mauriciosc.core.model.response.TokenModelResponse;
import com.mauriciosc.core.schema.ResponseSchemaMockWrapper.ResponseSchemaMock;
import com.mauriciosc.core.schema.TokenGenerationSchema;

/**
 * @author Mauricio Souza Couto
 * @since 2020-07-30
 *
 */
@TestConfiguration
public class TokenGenerationTestConfig {
	
	/**
	 * Defines an instance test bean for the {@link TokenModelResponse} class.
	 * 
	 * @return {@code TokenModelResponse} - bean instance.
	 */
	@Bean
	@Scope("singleton")
	public TokenModelResponse getTokenModelResponse() {
		return new TokenModelResponse();
	}
	
	/**
	 * Defines an instance test bean for the {@link RestTemplate} class.
	 * 
	 * @return {@code RestTemplate} - bean instance.
	 */
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	/**
	 * Defines an instance test bean representing a {@link TokenGenerationSchema}.
	 * 
	 * @return {@code TokenGenerationSchema} - bean instance.
	 */
	@Bean
	@Primary
	public TokenGenerationSchema getTokenGenerationSchema() {
		TokenGenerationSchema tokenGenerationSchema = new TokenGenerationSchema();
		tokenGenerationSchema.setHeaderModelRequest(new HeaderModelRequestMock("headerA", 1));
		tokenGenerationSchema.setTokenModelRequest(new TokenModelRequestMock("campoA", 2, "campoC"));
		tokenGenerationSchema.setResponseSchema(ResponseSchemaMock.class);
		
		return tokenGenerationSchema;
	}
}
