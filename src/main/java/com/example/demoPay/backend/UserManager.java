package com.example.demoPay.backend;

import java.math.BigDecimal;

// To be provided
public interface UserManager {
	boolean existsUser(String username);

	BigDecimal getBalance(String username);

	String getUserNameForDeviceId(String deviceId);
}