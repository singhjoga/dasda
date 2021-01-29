package com.harbois.runnerprotocol;

public class FileRequest {
	public static final int PREFETCH_WINDOW_SIZE = 4;
    private int chunkSize;
    private String fileName;

    public FileRequest() {
	}

	public FileRequest(int chunkSize, String fileName) {
      this.chunkSize = chunkSize;
      this.fileName = fileName;
    }

	public int getChunkSize() {
		return chunkSize;
	}

	public String getFileName() {
		return fileName;
	}
}
