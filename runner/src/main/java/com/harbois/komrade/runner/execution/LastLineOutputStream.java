package com.harbois.komrade.runner.execution;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LastLineOutputStream extends FilterOutputStream{
	private StringBuilder lastLine;
	boolean newLine=false;
	public LastLineOutputStream(OutputStream out) {
		super(out);
		lastLine = new StringBuilder();
	}
	@Override
	public void write(byte[] bytes, int off, int len) throws IOException {
		for (int i=off; i < off+len;i++) {
			write(bytes[i]);
		}
	}
	@Override
	public void write(byte[] bytes) throws IOException {
		for (byte b: bytes) {
			write(b);
		}
	}
	@Override
	public void write(int b) throws IOException {
		out.write(b);
		if (b==13 || b==10) {
			//new line
			//clear the buffer
			newLine=true;
		}else {
			if (newLine) {
				newLine=false;
				lastLine.delete(0, lastLine.length());	
			}
			lastLine.append((char)b);
		}
	}
	
	public String getLastLine() {
		return lastLine.toString();
	}
}
