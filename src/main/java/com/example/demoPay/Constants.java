/**
 * 
 */
package com.example.demoPay;

/**
 * Contains all the error codes and messages.
 * 
 * @author Rupesh.Kumar
 *
 */
public enum Constants {
	INFO_OK("OK"),
	ERR_UNKNOW_USER("ERR - NO USER"),
	ERR_INSUFFICIENT_FUNDS("ERR - INSUFFICIENT FUNDS"),
	ERR_UNKNOWN_COMMAND("ERR - UNKNOWN COMMAND"), 
	ERR_MSG_UNKNOW_USER_DEVICE_ID("Unknown device id specified."),
	ERR_INVALID_COMMAND_SYNTAX("Invalid command arguments supplied.")
	;
	
	private String errorMessage;

	private Constants(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return errorMessage;
	}

}
