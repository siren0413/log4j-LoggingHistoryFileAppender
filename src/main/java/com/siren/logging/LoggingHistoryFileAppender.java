package com.siren.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
		final String SEPARATER = "_";
		AtomicInteger atomicInteger = new AtomicInteger(0);

		
		int lastIndexofDOT = fileName.lastIndexOf(".");
		String fileExtension = fileName.substring(lastIndexofDOT + 1, fileName.length()); // the extension of the log e.g XX.log
		String fileNameNoExtension = fileName.substring(0, lastIndexofDOT);  // the file name without extension e.g XX
		

		if (fileName != null) {
			// test if the extended log file already exist, if exist, increase atomic value by 1 and continue testing until no such file exist.
			while (true) {
				File file = new File(fileNameNoExtension + SEPARATER + String.format("%03d", atomicInteger.get()) + "." + fileExtension);
				if (file.exists())
					atomicInteger.incrementAndGet();
				else
					break;
			}

			String extendedfileName = fileNameNoExtension + SEPARATER + String.format("%03d", atomicInteger.get()) + "." + fileExtension;
			
			// copying file
			File source = new File(fileName);
			File destination = new File(extendedfileName);
			try {
				if(source.exists()) {
					copyFile(source, destination);
				}
			} catch (IOException e) {
				errorHandler.error("Error while copying log files", e,
		                ErrorCode.FILE_OPEN_FAILURE);
			}
			
			try {
				setFile(fileName, fileAppend, bufferedIO, bufferSize);
			} catch (IOException e) {
				errorHandler.error("Error while activating log options", e,
		                ErrorCode.FILE_OPEN_FAILURE);
			}	

		}
	}
	

	
	private static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}

}
