package com.siren.logging;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;

public class LoggingHistoryFileAppender extends FileAppender {

	public LoggingHistoryFileAppender() {
	}

	public LoggingHistoryFileAppender(Layout layout, String filename, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
		super(layout, filename, append, bufferedIO, bufferSize);
	}

	public LoggingHistoryFileAppender(Layout layout, String filename, boolean append) throws IOException {
		super(layout, filename, append);
	}

	public LoggingHistoryFileAppender(Layout layout, String filename) throws IOException {
		super(layout, filename);
	}

	@Override
	public void activateOptions() {

		if (fileName == null) {
			throw new NullPointerException("log file name is null");
		}

		final String SEPARATER = "_";
		AtomicInteger atomicInteger = new AtomicInteger(0);
		File source = new File(fileName);
		
		if (source.exists()) {

			int lastIndexofDOT = fileName.lastIndexOf(".");
			String fileExtension = fileName.substring(lastIndexofDOT + 1, fileName.length());
			String fileNameNoExtension = fileName.substring(0, lastIndexofDOT);

			while (true) {
				File file = new File(fileNameNoExtension + SEPARATER + String.format("%03d", atomicInteger.get()) + "." + fileExtension);
				if (file.exists())
					atomicInteger.incrementAndGet();
				else
					break;
			}

			String extendedfileName = fileNameNoExtension + SEPARATER + String.format("%03d", atomicInteger.get()) + "." + fileExtension;

			source.renameTo(new File(extendedfileName));

		}

		try {
			setFile(fileName, fileAppend, bufferedIO, bufferSize);
		} catch (IOException e) {
			errorHandler.error("Error while activating log options", e, ErrorCode.FILE_OPEN_FAILURE);
		}

	}

}
