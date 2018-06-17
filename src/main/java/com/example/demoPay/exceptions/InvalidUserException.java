/**
 * 
 */
package com.example.demoPay.exceptions;

/**
 * @author Rupesh.Kumar
 *
 */
public class InvalidUserException extends BusinessException {

	/**
	 * @param string
	 */
	public InvalidUserException(String string) {
		super(string);
	}

}
