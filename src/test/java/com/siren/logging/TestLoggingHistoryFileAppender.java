package com.siren.logging;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestLoggingHistoryFileAppender {

	Logger LOGGER = Logger.getLogger(TestLoggingHistoryFileAppender.class);
	
	@Test
	public void testActivateOptions() {
		LOGGER.info("test");
		LOGGER.info("hahahaha");
		LOGGER.info("fsdflksf");
		LOGGER.info("1111111111");
		LOGGER.info("fsadffffffffffaffffffffffffffff");
		
		LOGGER.debug("+++++++++++++++++++++++++++++++++");
		LOGGER.debug("-----------------");
	}
}
