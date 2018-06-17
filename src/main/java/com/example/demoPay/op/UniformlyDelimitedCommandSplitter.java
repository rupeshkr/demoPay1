/**
 * 
 */
package com.example.demoPay.op;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.demoPay.exceptions.SystemException;

/**
 * Splits a sms command into segments using pre-defined delimiter. Each segment is returned in order in List.
 * At any give time, only one of the delimiters should be used in the command, else exception would be generated.
 * 
 * @author Rupesh.Kumar
 *
 */
public class UniformlyDelimitedCommandSplitter implements CommandSplitter {

	private static final Logger logger = LoggerFactory.getLogger(UniformlyDelimitedCommandSplitter.class);

	private List<String> delimiters;

	@Autowired
	public UniformlyDelimitedCommandSplitter(@Qualifier("delimiters") List<String> delimiters) {
		logger.debug("Predefined delimiters: " + delimiters);
		this.delimiters = delimiters;
	}

	public List<String> split(String command) throws SystemException {
		Optional<String> delimiter = lookupDelimiter(command);
		
		if (delimiter.isPresent()) {
			return Arrays.asList(command.split(delimiter.get()));
		} else {
			// no delimiter in the command, return as is
			return Arrays.asList(command);
		}
	}

	/*
	 * Using pre-defined <code>delimiter</code> list, it tries to resolve the delimiter being used in current command by checking for presence of only one of the delimiters.
	 * 
	 * @param smsCommand
	 * @return resolved delimiter or null to indicate that there is just one segment in the smsCommand.
	 * @throws SystemException if more than one pre-defined delimiters are found the passed command.
	 */
	private Optional<String> lookupDelimiter(String smsCommand) throws SystemException {
		List<String> resolvedDelims = new ArrayList<>();
		for (String delim : delimiters) {
			if (smsCommand.contains(delim)) {
				resolvedDelims.add(delim);
			}
		}

		if (resolvedDelims.size() > 1) {
			throw new SystemException("Multiple delimiters found in the command: " + resolvedDelims);
		}
		
		
		if (resolvedDelims.size() == 1) {
			return Optional.of(resolvedDelims.get(0));
		}
		
		// no delimiter, probably its a standalone command - like BALANCE
		return Optional.empty();
	}
}
