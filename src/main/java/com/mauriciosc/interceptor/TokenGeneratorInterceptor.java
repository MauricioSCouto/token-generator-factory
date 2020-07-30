package com.mauriciosc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mauriciosc.core.model.response.TokenModelResponse;
import com.mauriciosc.core.schema.TokenGenerationSchema;
import com.mauriciosc.core.schema.ResponseSchema;
import com.mauriciosc.dataprovider.TokenGenerationDataProvider;

/**
 * Interceptor designed to generate a token when requests are received.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-24
 *
 */
@Component
public class TokenGeneratorInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenGenerationSchema tokenGenerationSchema;
	
	@Autowired
	private TokenModelResponse tokenModelResponse;
	
	@Autowired
	private TokenGenerationDataProvider authenticationDataprovider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		ResponseSchema responseSchema = authenticationDataprovider.authenticate(tokenGenerationSchema);
		tokenModelResponse.setResponse(responseSchema);
		
		return true;
	}
}
