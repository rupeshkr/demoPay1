package com.example.demoPay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demoPay.backend.TransferManager;
import com.example.demoPay.backend.UserManager;
import com.example.demoPay.entry.DefaultSmsHandler;
import com.example.demoPay.entry.SMSHandler;
import com.example.demoPay.op.CommandSplitter;
import com.example.demoPay.op.Operation;
import com.example.demoPay.op.OperationFactory;
import com.example.demoPay.op.UniformlyDelimitedCommandSplitter;
import com.example.demoPay.op.impl.GetBalanceOperation;
import com.example.demoPay.op.impl.TransferHistoryOperation;
import com.example.demoPay.op.impl.TransferOperation;
import com.example.demoPay.op.impl.UnknownOperation;

@SpringBootApplication
public class DemoPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoPayApplication.class, args);
	}

	@Bean
	SMSHandler getSmsHandler(CommandSplitter splitter, UserManager userManager, TransferManager transferManager, OperationFactory opFactory) {
		return new DefaultSmsHandler(splitter, userManager, transferManager, opFactory);
	}

	@Bean
	OperationFactory getOperationFactory(@Qualifier("allOperations") Map<String, Operation> operations, @Qualifier("fallbackOperation") Operation fallbackOperation) {
		return new OperationFactory(operations, fallbackOperation);
	}

	@Bean
	@Qualifier("allOperations")
	Map<String, Operation> getAllOperations() {
		Map<String, Operation> operations = new HashMap<>();
		operations.put("BALANCE", new GetBalanceOperation());
		operations.put("SEND", new TransferOperation());
		operations.put("TOTAL", new TransferHistoryOperation());
		return operations;
	}

	@Bean
	@Qualifier("fallbackOperation")
	Operation getFallbackOperation() {
		return new UnknownOperation();
	}

	@Bean
	CommandSplitter getSplitter() {
		List<String> delimiters = new ArrayList<>();
		delimiters.add("-");
		delimiters.add(";");
		return new UniformlyDelimitedCommandSplitter(delimiters);
	}

	@Bean
	TransferManager getTransferManager() {
		// creating dummy as its not given
		return new TransferManager() {

			@Override
			public void sendMoney(String senderUsername, String recipientUsername, BigDecimal amount) {
				// no-op - dummy implementation
			}

			@Override
			public List<BigDecimal> getAllTransactions(String senderUsername, String recipientUsername) {
				List<BigDecimal> txns = new ArrayList<>();
				;
				switch (senderUsername + "-" + recipientUsername) {
				case "ROCK-FFRITZ":
					txns.add(new BigDecimal(500));
					break;
				case "ROCK-MSMITH":
					txns.add(new BigDecimal(1500));
					break;
				case "MSMITH-FFRITZ":
					txns.add(new BigDecimal(1000));
					break;
				default:
					txns.add(BigDecimal.ZERO);
				}
				return txns;
			}
		};
	}

	@Bean
	UserManager getUserManager() {
		// creating dummy as its not given
		return new UserManager() {

			@Override
			public String getUserNameForDeviceId(String deviceId) {
				return deviceId;
			}

			@Override
			public BigDecimal getBalance(String username) {
				switch (username) {
				case "ROCK":
					return new BigDecimal(500);
				case "FFRITZ":
					return new BigDecimal(1000);
				case "MSMITH":
					return new BigDecimal(1500);
				default:
					return BigDecimal.ZERO;
				}
			}

			@Override
			public boolean existsUser(String username) {
				switch (username) {
				case "ROCK":
				case "FFRITZ":
				case "MSMITH":
					return true;
				default:
					return false;
				}
			}
		};
	}
}
