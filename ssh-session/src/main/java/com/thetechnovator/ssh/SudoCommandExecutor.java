package com.thetechnovator.ssh;

public class SudoCommandExecutor extends PasswordInputCommand{
	private static final String SUDO_CMD = "sudo echo OK";
	public SudoCommandExecutor(SshSession session, PasswordProvider passwordProvider) {
		
		//Command returns OK on success (we are echoing OK above in the command)
		super(session, "[sudo] password for ", passwordProvider, "OK", null);
	}
	public int exec() throws SshSessionlException {
		String cmdStr=getCommandWithEndMarker(SUDO_CMD);
		SshCommand cmd = new SshCommand(cmdStr);
		int status= super.exec(cmd,cmdStr);
		return status;
	}
	
}
