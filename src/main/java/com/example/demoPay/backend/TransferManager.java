package com.example.demoPay.backend;

import java.math.BigDecimal;
import java.util.List;

//To be provided
public interface TransferManager {
	void sendMoney(String senderUsername, String recipientUsername, BigDecimal amount);
	List<BigDecimal> getAllTransactions(String senderUsername, String recipientUsername);
}