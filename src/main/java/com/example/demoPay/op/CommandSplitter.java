/**
 * 
 */
package com.example.demoPay.op;

import java.util.List;

import com.example.demoPay.exceptions.SystemException;

/**
 * Interface for defining command splitter. This class will be used to identify segments in the passed command.
 * 
 * @author Rupesh.Kumar
 *
 */
public interface CommandSplitter {
	List<String> split(String command) throws SystemException;
}
