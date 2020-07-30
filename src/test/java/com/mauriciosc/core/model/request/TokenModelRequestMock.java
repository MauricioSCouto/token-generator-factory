package com.mauriciosc.core.model.request;

/**
 * Mock implementation of {@link TokenModelRequest} class.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-26
 *
 */
public class TokenModelRequestMock implements TokenModelRequest {
	
	private String campoA;
	private Integer campoB;
	private String campoC;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param campoA - campoA parameter.
	 * @param campoB - campoB parameter.
	 * @param campoC - campoC parameter.
	 */
	public TokenModelRequestMock(String campoA, Integer campoB, String campoC) {
		this.campoA = campoA;
		this.campoB = campoB;
		this.campoC = campoC;
	}

	/**
	 * @return campoA field.
	 */
	public String getCampoA() {
		return campoA;
	}

	/**
	 * @return campoB field.
	 */
	public Integer getCampoB() {
		return campoB;
	}

	/**
	 * @return campoC field.
	 */
	public String getCampoC() {
		return campoC;
	}
}
