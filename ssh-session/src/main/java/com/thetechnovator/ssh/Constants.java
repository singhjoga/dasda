package com.thetechnovator.ssh;

public interface Constants {
	int SUCCESS=0;
	int FAILURE=1;
	int CR=13;
	int LF=10;
	int GROUP_SEPARATOR = 29;
	int RECORD_SEPARATOR = 30;
	int END_OF_TEXT_UNIT = 31;
	String CMD_SUFFIX = ";printf '\\x1D\\x1E%d\\x1F' $?";
	String CMD_PREFIX = "printf '\\x1D\\x1E';";
}
