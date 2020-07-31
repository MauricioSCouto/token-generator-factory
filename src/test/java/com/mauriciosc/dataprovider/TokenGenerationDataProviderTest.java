package com.mauriciosc.dataprovider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mauriciosc.core.exception.ResponseSchemaMappingException;
import com.mauriciosc.core.exception.TokenGenerationHeaderManipulationException;
import com.mauriciosc.core.exception.UrlNotProvidedException;
import com.mauriciosc.core.model.request.HeaderModelRequest;
import com.mauriciosc.core.model.request.HeaderModelRequestMock;
import com.mauriciosc.core.model.request.TokenModelRequest;
import com.mauriciosc.core.model.request.TokenModelRequestMock;
import com.mauriciosc.core.schema.ResponseSchema;
import com.mauriciosc.core.schema.ResponseSchemaMock;
import com.mauriciosc.core.schema.TokenGenerationSchema;

/**
 * Suite of tests for the class  {@link TokenGenerationDataProvider}.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-25
 *
 */
public class TokenGenerationDataProviderTest {

	@Spy
	@InjectMocks
	private TokenGenerationDataProvider tokenGenerationDataProvider;
	
	@Mock
	private RestTemplate restTemplate;
	private static final String MOCK_URL = "mock_url";
	
	/**
	 * Init method of the test suite.
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {		
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(tokenGenerationDataProvider, "tokenGenerationUrl", MOCK_URL);
	}

	/**
	 * This scenario tests the binding process for generate an authentication.
	 * 
	 * @throws TokenGenerationHeaderManipulationException exception thrown in case of
	 *                                                   failure when manipulating
	 *                                                   the headers of the request.
	 * @throws UrlNotProvidedException                   exception thrown when the
	 *                                                   request url is not
	 *                                                   provided.
	 * @throws ResponseSchemaMappingException            exception thrown in case of
	 *                                                   failure when
	 *                                                   unmarshalling the
	 *                                                   response body into the
	 *                                                   response schema.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAuthenticate() throws TokenGenerationHeaderManipulationException, UrlNotProvidedException, ResponseSchemaMappingException {
		TokenGenerationSchema tokenGenerationSchema = new TokenGenerationSchema(this.getHeadersMock(), this.getRequestMock(), ResponseSchemaMock.class);
		
		HeaderModelRequestMock headersSchemaMock = (HeaderModelRequestMock) tokenGenerationSchema.getHeaderModelRequest();
		TokenModelRequestMock requestSchemaMock = (TokenModelRequestMock) tokenGenerationSchema.getTokenModelRequest();
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("headerA", headersSchemaMock.getHeaderA());
		multiValueMap.add("headerB", String.valueOf(headersSchemaMock.getHeaderB()));
		
		HttpEntity<TokenModelRequest> httpEntityMock = new HttpEntity<>(tokenGenerationSchema.getTokenModelRequest(), multiValueMap);
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>(getResponseMock(), HttpStatus.OK);
			
		when(
			restTemplate.exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class)))
		.thenReturn(responseEntity);
		
		ResponseSchemaMock responseSchema = (ResponseSchemaMock) tokenGenerationDataProvider.authenticate(tokenGenerationSchema);
		
		// validating request headers
		assertEquals("x-header-mock", headersSchemaMock.getHeaderA());
		assertEquals("2", String.valueOf(headersSchemaMock.getHeaderB()));		
		
		// validating request values
		assertEquals("mock-login", requestSchemaMock.getCampoA());
		assertEquals(1, requestSchemaMock.getCampoB());
		assertEquals("mock-senha", requestSchemaMock.getCampoC());
				
		// validating response values
		assertEquals("mock_token_retorno", responseSchema.getToken());
		
		// validating url call
		verify(restTemplate).exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class));
	}
	
	/**
	 * This scenario tests the binding process for generate an authentication not
	 * providing the request url.
	 * @throws TokenGenerationHeaderManipulationException exception thrown in case of
	 *                                                   failure when manipulating
	 *                                                   the headers of the request.
	 * @throws UrlNotProvidedException                   exception thrown when the
	 *                                                   request url is not
	 *                                                   provided.
	 * @throws ResponseSchemaMappingException            exception thrown in case of
	 *                                                   failure when
	 *                                                   unmarshalling the
	 *                                                   response body into the
	 *                                                   response schema.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAuthenticateNullUrl() throws TokenGenerationHeaderManipulationException, UrlNotProvidedException {
		ReflectionTestUtils.setField(tokenGenerationDataProvider, "tokenGenerationUrl", null);
		TokenGenerationSchema tokenGenerationSchema = new TokenGenerationSchema(this.getHeadersMock(), this.getRequestMock(), ResponseSchemaMock.class);
		
		HeaderModelRequestMock headersSchemaMock = (HeaderModelRequestMock) tokenGenerationSchema.getHeaderModelRequest();
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("headerA", headersSchemaMock.getHeaderA());
		multiValueMap.add("headerB", String.valueOf(headersSchemaMock.getHeaderB()));
		
		HttpEntity<TokenModelRequest> httpEntityMock = new HttpEntity<>(tokenGenerationSchema.getTokenModelRequest(), multiValueMap);
		
		when(
			restTemplate.exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class)))
		.thenCallRealMethod();
		
		// validating exception when request url is not provided
		assertThrows(UrlNotProvidedException.class, () -> tokenGenerationDataProvider.authenticate(tokenGenerationSchema));
		
		// validating client call is not executed
		verify(restTemplate, never())
			.exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class));
	}

	/**
	 * This scenario tests the binding process for generate an authentication
	 * without providing headers.
	 * 
	 * @throws TokenGenerationHeaderManipulationException exception thrown in case of
	 *                                                   failure when manipulating
	 *                                                   the headers of the request.
	 * @throws UrlNotProvidedException                   exception thrown when the
	 *                                                   request url is not
	 *                                                   provided.
	 * @throws ResponseSchemaMappingException            exception thrown in case of
	 *                                                   failure when
	 *                                                   unmarshalling the
	 *                                                   response body into the
	 *                                                   response schema.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAuthenticateNullHeader() throws TokenGenerationHeaderManipulationException, UrlNotProvidedException, ResponseSchemaMappingException {
		ReflectionTestUtils.setField(tokenGenerationDataProvider, "tokenGenerationUrl", MOCK_URL);
		TokenGenerationSchema tokenGenerationSchema = new TokenGenerationSchema(null, this.getRequestMock(), ResponseSchemaMock.class);

		HeaderModelRequestMock headersSchemaMock = (HeaderModelRequestMock) tokenGenerationSchema.getHeaderModelRequest();
		TokenModelRequestMock requestSchemaMock = (TokenModelRequestMock) tokenGenerationSchema.getTokenModelRequest();
		
		HttpEntity<TokenModelRequest> httpEntityMock = new HttpEntity<>(tokenGenerationSchema.getTokenModelRequest(), new LinkedMultiValueMap<>());
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>(getResponseMock(), HttpStatus.OK);
		
		when(
			restTemplate.exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class)))
		.thenReturn(responseEntity);
		
		ResponseSchemaMock responseSchema = (ResponseSchemaMock) tokenGenerationDataProvider.authenticate(tokenGenerationSchema);
		
		// validating null request headers
		assertNull(headersSchemaMock);		
		
		// validating request values
		assertEquals("mock-login", requestSchemaMock.getCampoA());
		assertEquals(1, requestSchemaMock.getCampoB());
		assertEquals("mock-senha", requestSchemaMock.getCampoC());
				
		// validating response values
		assertEquals("mock_token_retorno", responseSchema.getToken());
		
		// validating url call
		verify(restTemplate).exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class));
	}

	/**
	 * This scenario tests the binding process for generate an authentication
	 * without providing the request schema (body).
	 * 
	 * @throws TokenGenerationHeaderManipulationException exception thrown in case of
	 *                                                   failure when manipulating
	 *                                                   the headers of the request.
	 * @throws UrlNotProvidedException                   exception thrown when the
	 *                                                   request url is not
	 *                                                   provided.
	 * @throws ResponseSchemaMappingException            exception thrown in case of
	 *                                                   failure when
	 *                                                   unmarshalling the
	 *                                                   response body into the
	 *                                                   response schema.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAuthenticateNullRequest() throws TokenGenerationHeaderManipulationException, UrlNotProvidedException, ResponseSchemaMappingException {
		ReflectionTestUtils.setField(tokenGenerationDataProvider, "tokenGenerationUrl", MOCK_URL);
		TokenGenerationSchema tokenGenerationSchema = new TokenGenerationSchema(this.getHeadersMock(), null, ResponseSchemaMock.class);
		
		HeaderModelRequestMock headersSchemaMock = (HeaderModelRequestMock) tokenGenerationSchema.getHeaderModelRequest();
		TokenModelRequestMock requestSchemaMock = (TokenModelRequestMock) tokenGenerationSchema.getTokenModelRequest();
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("headerA", headersSchemaMock.getHeaderA());
		multiValueMap.add("headerB", String.valueOf(headersSchemaMock.getHeaderB()));
		
		HttpEntity<TokenModelRequest> httpEntityMock = new HttpEntity<>(null, multiValueMap);
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>(getResponseMock(), HttpStatus.OK);
		
		when(
			restTemplate.exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class)))
		.thenReturn(responseEntity);
		
		ResponseSchemaMock responseSchema = (ResponseSchemaMock) tokenGenerationDataProvider.authenticate(tokenGenerationSchema);
		
		// validating request headers
		assertEquals("x-header-mock", headersSchemaMock.getHeaderA());
		assertEquals("2", String.valueOf(headersSchemaMock.getHeaderB()));		
		
		// validating null request
		assertNull(requestSchemaMock);
				
		// validating response values
		assertEquals("mock_token_retorno", responseSchema.getToken());
		
		// validating url call		
		verify(restTemplate).exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class));
	}
	
	/**
	 * This scenario tests the binding process for generate an authentication
	 * without providing the response schema (body).
	 * 
	 * @throws TokenGenerationHeaderManipulationException exception thrown in case of
	 *                                                   failure when manipulating
	 *                                                   the headers of the request.
	 * @throws UrlNotProvidedException                   exception thrown when the
	 *                                                   request url is not
	 *                                                   provided.
	 * @throws ResponseSchemaMappingException            exception thrown in case of
	 *                                                   failure when unmarshalling
	 *                                                   the response body into the
	 *                                                   response schema.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAuthenticateNullResponse() throws TokenGenerationHeaderManipulationException, UrlNotProvidedException, ResponseSchemaMappingException {
		ReflectionTestUtils.setField(tokenGenerationDataProvider, "tokenGenerationUrl", MOCK_URL);
		TokenGenerationSchema tokenGenerationSchema = new TokenGenerationSchema(this.getHeadersMock(), this.getRequestMock(), null);
		
		HeaderModelRequestMock headersSchemaMock = (HeaderModelRequestMock) tokenGenerationSchema.getHeaderModelRequest();
		TokenModelRequestMock requestSchemaMock = (TokenModelRequestMock) tokenGenerationSchema.getTokenModelRequest();
		
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("headerA", headersSchemaMock.getHeaderA());
		multiValueMap.add("headerB", String.valueOf(headersSchemaMock.getHeaderB()));
		
		HttpEntity<TokenModelRequest> httpEntityMock = new HttpEntity<>(tokenGenerationSchema.getTokenModelRequest(), multiValueMap);
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>(getResponseMock(), HttpStatus.OK);
			
		when(
			restTemplate.exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class)))
		.thenReturn(responseEntity);
		
		// validating exception when response schema is not provided
		assertThrows(ResponseSchemaMappingException.class, () -> tokenGenerationDataProvider.authenticate(tokenGenerationSchema));
	
		// validating request headers
		assertEquals("x-header-mock", headersSchemaMock.getHeaderA());
		assertEquals("2", String.valueOf(headersSchemaMock.getHeaderB()));		
		
		// validating request values
		assertEquals("mock-login", requestSchemaMock.getCampoA());
		assertEquals(1, requestSchemaMock.getCampoB());
		assertEquals("mock-senha", requestSchemaMock.getCampoC());
					
		// validating url call
		verify(restTemplate).exchange(
				Mockito.eq(MOCK_URL), 
				Mockito.eq(HttpMethod.POST), 
				Mockito.eq(httpEntityMock), 
				Mockito.any(ParameterizedTypeReference.class));
	}
	
	/**
	 * Creates an instance mock of the {@link ResponseSchema} class.
	 * 
	 * @return {@code ResponseSchema} - object mock.
	 */
	private String getResponseMock() {
		return "{\"token\": \"mock_token_retorno\"}";
	}

	/**
	 * Creates an instance mock of the {@link TokenModelRequest} class.
	 * 
	 * @return {@code ResponseSchema} - object mock.
	 */
	private TokenModelRequest getRequestMock() {
		return new TokenModelRequestMock("mock-login", 1, "mock-senha");
	}

	/**
	 * Creates an instance mock of the {@link HeaderModelRequest} class.
	 * 
	 * @return {@code HeaderSchema} - object mock.
	 */
	private HeaderModelRequest getHeadersMock() {
		return new HeaderModelRequestMock("x-header-mock", 2);
	}
}
