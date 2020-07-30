package com.mauriciosc.dataprovider;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauriciosc.core.exception.TokenGenerationHeaderManipulationException;
import com.mauriciosc.core.exception.ResponseSchemaMappingException;
import com.mauriciosc.core.exception.UrlNotProvidedException;
import com.mauriciosc.core.model.request.HeaderModelRequest;
import com.mauriciosc.core.model.request.TokenModelRequest;
import com.mauriciosc.core.schema.ResponseSchema;
import com.mauriciosc.core.schema.TokenGenerationSchema;

/**
 * Executes the calls to created a token based on the given schema
 * (headers, request and response).
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-24
 *
 */
@Component
public class TokenGenerationDataProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenGenerationDataProvider.class);
	
	private static final String FALHA_MANIPULACAO_HEADERS = "Falha na manipulação dos headers da requisição";
	private static final String URL_NOT_PROVIDED = "Não foi possível identificar a url de autenticação. Verifique a parametrização da propriedade 'authentication.url'";
	private static final String FALHA_DESSERIALIZACAO_RESPONSE = "Falha ao desserializar o response no schema indicado.";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${token.generation.url:@null}")
	private String tokenGenerationUrl;
	
	/**
	 * Creates and executes the token generation call based on the given schema.
	 * 
	 * @param tokenGenerationSchema - schema of the request/response call.
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
	public ResponseSchema authenticate(TokenGenerationSchema tokenGenerationSchema) 
			throws TokenGenerationHeaderManipulationException, UrlNotProvidedException, ResponseSchemaMappingException {	
		
		this.validateUrl();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		MultiValueMap<String, String> headers = this.createHeaders(tokenGenerationSchema.getHeaderModelRequest());
		
		HttpEntity<TokenModelRequest> httpEntity = 
				new HttpEntity<>(tokenGenerationSchema.getTokenModelRequest(), headers);
		
		ResponseEntity<String> responseEntity = 
				restTemplate.exchange(tokenGenerationUrl, 
										HttpMethod.POST,
										httpEntity, 
										new ParameterizedTypeReference<String>() {});
		
		try {
			return objectMapper.readValue(responseEntity.getBody(), tokenGenerationSchema.getResponseSchema());			
		}
		catch(Exception e) {
			LOGGER.error(FALHA_DESSERIALIZACAO_RESPONSE, e);
			throw new ResponseSchemaMappingException(FALHA_DESSERIALIZACAO_RESPONSE);
		}
	}

	/**
	 * Validates if the authentication url is provided.
	 * 
	 * @throws UrlNotProvidedException thrown when the authentication url is null or
	 *                                 black (filled with spaces).
	 */
	private void validateUrl() throws UrlNotProvidedException {
		if(StringUtils.isBlank(this.tokenGenerationUrl)) {
			LOGGER.error(URL_NOT_PROVIDED);
			throw new UrlNotProvidedException(URL_NOT_PROVIDED);
		}	
	}

	/**
	 * Converts a {@link HeaderModelRequest} instance into a {@link MultiValueMap}.
	 * 
	 * @param headerSchema - object to be converted.
	 * 
	 * @return {@code MultiValueMap} - map containing the headers based on the given
	 *         header schema.
	 * @throws TokenGenerationHeaderManipulationException
	 */
	@SuppressWarnings("unchecked")
	private MultiValueMap<String, String> createHeaders(HeaderModelRequest headerSchema) throws TokenGenerationHeaderManipulationException {
		ObjectMapper mapper = new ObjectMapper();
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		
		if(Objects.nonNull(headerSchema)) {
			try {
				Map<String, Object> objectMap = mapper.convertValue(headerSchema, Map.class);
				
				objectMap.entrySet()
					.forEach(entry -> {
						multiValueMap.add(entry.getKey(), String.valueOf(entry.getValue()));			
					});
			}
			catch (Exception e) {
				LOGGER.error(FALHA_MANIPULACAO_HEADERS, e);
				throw new TokenGenerationHeaderManipulationException(FALHA_MANIPULACAO_HEADERS);
			}
		}
		
		return multiValueMap;
	}
}
