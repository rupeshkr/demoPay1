package com.example.demoPay;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demoPay.backend.TransferManager;
import com.example.demoPay.backend.UserManager;
import com.example.demoPay.entry.DefaultSmsHandler;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoPayApplicationTests {

	@Autowired // uses existing bean wiring to create this bean object.
	private DefaultSmsHandler smsHandlerToTest;

	@MockBean // inject mock
	private UserManager userManagerMock;

	@MockBean // inject mock
	private TransferManager transferManagerMock;

	@Test
	public void whenInvalidUser_thenError() {
		String aUser = "invalidUser";
		when(userManagerMock.getUserNameForDeviceId(aUser)).thenReturn(null);
		String smsCommand = "BALANCE";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, aUser);
		String expectedResponse = "ERR - NO USER";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
	}

	@Test
	public void whenValidUserBalanceCheck_thenBalance() {
		String aUser = "ValidUser";
		when(userManagerMock.getUserNameForDeviceId(aUser)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(aUser)).thenReturn(true);
		when(userManagerMock.getBalance(aUser)).thenReturn(new BigDecimal(500));
		
		String smsCommand = "BALANCE";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, aUser);
		String expectedResponse = "500";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
	}

	@Test
	public void whenInvalidBalanceCheckCommand_thenError() {
		String aUser = "ValidUser";
		when(userManagerMock.getUserNameForDeviceId(aUser)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(aUser)).thenReturn(true);
		when(userManagerMock.getBalance(aUser)).thenReturn(new BigDecimal(500));
		
		String smsCommand = "BALANCE-testing";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, aUser);
		String expectedResponse = "Invalid command arguments supplied.";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
	}

	@Test
	public void whenSendValidAmount_thenOk() {
		String sender = "ValidUser";
		String receiver = "FFRITZ";

		when(userManagerMock.getUserNameForDeviceId(sender)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(sender)).thenReturn(true);
		when(userManagerMock.existsUser(receiver)).thenReturn(true);
		when(userManagerMock.getBalance(sender)).thenReturn(new BigDecimal(500));
		
		String smsCommand = "SEND-100-FFRITZ";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, sender);
		String expectedResponse = "OK";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
		verify(transferManagerMock).sendMoney(eq(sender), eq(receiver), eq(new BigDecimal(100)));
	}

	@Test
	public void whenInvalidSendCommand_thenError() {
		String sender = "ValidUser";
		String receiver = "FFRITZ";

		when(userManagerMock.getUserNameForDeviceId(sender)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(sender)).thenReturn(true);
		when(userManagerMock.existsUser(receiver)).thenReturn(true);
		when(userManagerMock.getBalance(sender)).thenReturn(new BigDecimal(500));
		
		String smsCommand = "SEND-100-FFRITZ-test";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, sender);
		String expectedResponse = "Invalid command arguments supplied.";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
		verify(transferManagerMock, never()).sendMoney(eq(sender), eq(receiver), eq(new BigDecimal(100)));
	}

	@Test
	public void whenSendMoreAmt_thenErrorFunds() {
		String sender = "ValidUser";
		String receiver = "FFRITZ";

		when(userManagerMock.getUserNameForDeviceId(sender)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(sender)).thenReturn(true);
		when(userManagerMock.existsUser(receiver)).thenReturn(true);
		when(userManagerMock.getBalance(sender)).thenReturn(new BigDecimal(500));
		
		String smsCommand = "SEND-1000-FFRITZ";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, sender);
		String expectedResponse = "ERR - INSUFFICIENT FUNDS";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
		verify(transferManagerMock, never()).sendMoney(eq(sender), eq(receiver), eq(new BigDecimal(1000)));
	}

	@Test
	public void whenTotalTo1User_thenAmountSent() {
		String sender = "ValidUser";
		String receiver = "FFRITZ";
		List<BigDecimal> txn1 = new ArrayList<>();
		txn1.add(new BigDecimal(200));

		when(userManagerMock.getUserNameForDeviceId(sender)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(sender)).thenReturn(true);
		when(userManagerMock.existsUser(receiver)).thenReturn(true);
		when(transferManagerMock.getAllTransactions(sender, receiver)).thenReturn(txn1);
		
		String smsCommand = "TOTAL-SENT-FFRITZ";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, sender);
		String expectedResponse = "200";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
	}

	@Test
	public void whenTotalTo2Users_thenAmountSent() {
		String sender = "ValidUser";
		String receiver1 = "FFRITZ";
		String receiver2 = "MSMITH";
		List<BigDecimal> txn1 = new ArrayList<>();
		txn1.add(new BigDecimal(200));
		List<BigDecimal> txn2 = new ArrayList<>();
		txn2.add(new BigDecimal(700));

		when(userManagerMock.getUserNameForDeviceId(sender)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(sender)).thenReturn(true);
		when(userManagerMock.existsUser(receiver1)).thenReturn(true);
		when(userManagerMock.existsUser(receiver2)).thenReturn(true);
		when(transferManagerMock.getAllTransactions(sender, receiver1)).thenReturn(txn1);
		when(transferManagerMock.getAllTransactions(sender, receiver2)).thenReturn(txn2);
		
		String smsCommand = "TOTAL-SENT-FFRITZ-MSMITH";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, sender);
		String expectedResponse = "200,700";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
	}

	@Test
	public void whenTotalToInvalidUser_thenError() {
		String sender = "ValidUser";
		String receiver1 = "FFRITZ";
		List<BigDecimal> txn1 = new ArrayList<>();
		txn1.add(new BigDecimal(200));

		when(userManagerMock.getUserNameForDeviceId(sender)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(sender)).thenReturn(true);
		when(userManagerMock.existsUser(receiver1)).thenReturn(true);
		when(transferManagerMock.getAllTransactions(sender, receiver1)).thenReturn(txn1);
		
		String smsCommand = "TOTAL-SENT-FFRITZ-MSMITH";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, sender);
		String expectedResponse = "ERR - NO USER";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
	}

	@Test
	public void whenUnknownCommand_thenError() {
		String sender = "ValidUser";

		when(userManagerMock.getUserNameForDeviceId(sender)).thenReturn("ValidUser");
		when(userManagerMock.existsUser(sender)).thenReturn(true);
		
		String smsCommand = "XYZ";
		String actualResponse = smsHandlerToTest.handleSmsRequest(smsCommand, sender);
		String expectedResponse = "ERR - UNKNOWN COMMAND";
		assertEquals("Response mismatch.", expectedResponse, actualResponse);
	}
}
