package com.mauriciosc.core.model.request;

/**
 * Mock implementation of {@link HeaderModelRequest} class.
 * 
 * @author Mauricio Souza Couto
 * @since 2020-07-26
 *
 */
public class HeaderModelRequestMock implements HeaderModelRequest {
	
	private String headerA;
	private Integer headerB;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param headerA - headerA parameter
	 * @param headerB - headerB parameter
	 */
	public HeaderModelRequestMock(String headerA, Integer headerB) {
		super();
		this.headerA = headerA;
		this.headerB = headerB;
	}

	/**
	 * @return headerA field.
	 */
	public String getHeaderA() {
		return headerA;
	}
	
	/**
	 * @return headerB field.
	 */
	public Integer getHeaderB() {
		return headerB;
	}
}

