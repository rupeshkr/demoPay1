/**
 * 
 */
package com.example.demoPay.op;

import com.example.demoPay.backend.TransferManager;
import com.example.demoPay.backend.UserManager;

/**
 * Holder to hold username of currently logged in user, other delegates used for further processing.
 * 
 * @author Rupesh.Kumar
 *
 */
public class Context {

	private final UserManager userManager;
	private final TransferManager transferManager;
	private final String username;
	
	public Context(UserManager userManager, TransferManager transferManager, String username) {
		this.userManager = userManager;
		this.transferManager = transferManager;
		this.username = username;
	}

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @return the transferManager
	 */
	public TransferManager getTransferManager() {
		return transferManager;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
}
