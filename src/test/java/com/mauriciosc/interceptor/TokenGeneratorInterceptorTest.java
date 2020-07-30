package com.mauriciosc.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.mauriciosc.configuration.TokenGenerationTestConfig;
import com.mauriciosc.core.model.response.TokenModelResponse;
import com.mauriciosc.core.schema.ResponseSchemaMockWrapper.ResponseSchemaMock;


/**
 * Suite for testing the entire process to generate a token given the schema configuration.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-30
 *
 */
@SpringBootTest(properties = {"token.generation.url=http://localhost:8080/api/token", "spring.main.allow-bean-definition-overriding=true"})
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(TokenGenerationTestConfig.class)
public class TokenGeneratorInterceptorTest {

	@Autowired
	private TokenGeneratorInterceptor tokenGeneratorInterceptor;
	
	@Autowired
	private TokenModelResponse tokenModelResponse;
		
	@MockBean
	private RestTemplate restTemplate;
	
	/**
	 * Tests the component (application) process to generate a token dynamically.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testPreHandle() throws Exception {
		given(
			restTemplate.exchange(
				BDDMockito.eq("http://localhost:8080/api/token"),
				BDDMockito.eq(HttpMethod.POST), 
				BDDMockito.any(HttpEntity.class), 
				BDDMockito.any(ParameterizedTypeReference.class)))
		.willReturn(this.getMockTokenGenerationResponse());
		
		tokenGeneratorInterceptor.preHandle(new MockHttpServletRequest(), new MockHttpServletResponse(), null);

		ResponseSchemaMock responseSchema = (ResponseSchemaMock) tokenModelResponse.getTokenModelResponse();
		
		assertEquals("mock_token", responseSchema.getToken());
		
		then(restTemplate)
			.should()
			.exchange(BDDMockito.eq("http://localhost:8080/api/token"),
					BDDMockito.eq(HttpMethod.POST), 
					BDDMockito.any(HttpEntity.class), 
					BDDMockito.any(ParameterizedTypeReference.class));
	}

	/**
	 * Returns a {@link ResponseEntity} of success with a mock token.
	 * 
	 * @return {@code ResponseEntity} - response entity
	 */
	private ResponseEntity<String> getMockTokenGenerationResponse() {
		return new ResponseEntity<String>("{\"token\":\"mock_token\"}", HttpStatus.OK);
	}

}
