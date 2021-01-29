package com.harbois.komrade.runner.processrunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harbois.komrade.runner.exception.CommandExectionException;

public class AbstractCommandlineApp {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractCommandlineApp.class);
	private File processFile;
	private File workingDir;
	private List<CommandParameter<?>> commonArguments = new ArrayList<>();
	private Map<String, String> environment = new HashedMap<>();

	public AbstractCommandlineApp(File processFile) {
		super();
		this.processFile = processFile;
		if (SystemUtils.IS_OS_WINDOWS) {
			commonArguments.add(new StringParameter("CMD"));
			commonArguments.add(new StringParameter("/C"));
		} else {
			commonArguments.add(new StringParameter("/bin/bash"));
			commonArguments.add(new StringParameter("-c"));
		}
		commonArguments.add(new StringParameter(processFile.getAbsolutePath()));
	}

	public File getProcessFile() {
		return processFile;
	}

	public File getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(File workingDir) {
		this.workingDir = workingDir;
	}

	public Map<String, String> getEnvironment() {
		return environment;
	}

	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}

	protected int execute(List<CommandParameter<?>> argList) throws CommandExectionException {
		List<String> strArgList = new ArrayList<>();
		commonArguments.forEach(p->strArgList.add(p.toString()));
		argList.forEach(p->strArgList.add(p.toString()));
		ProcessBuilder pb = new ProcessBuilder(strArgList);
		if (workingDir != null) {
			pb.directory(workingDir);
		}
		pb.environment().putAll(environment);
		LOG.info("Executing " + toDisplayString(argList));
		Process process;
		try {
			process = pb.start();
			// Write stdout and stderr to console
			InputStream out = process.getInputStream();
			InputStream err = process.getErrorStream();
			byte[] buffer = new byte[4000];
			while (isAlive(process)) {
				int no = out.available();
				if (no > 0) {
					int n = out.read(buffer, 0, Math.min(no, buffer.length));
					System.out.println(new String(buffer, 0, n));
				}
				no = err.available();
				if (no > 0) {
					int n = err.read(buffer, 0, Math.min(no, buffer.length));
					System.err.println(new String(buffer, 0, n));
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			int exitCode = process.exitValue();
			return exitCode;
		} catch (IOException e) {
			throw new CommandExectionException(e);
		}
	}
	protected List<CommandParameter<?>> string2StringParameterList(String strArguments) {
		List<CommandParameter<?>> result = new ArrayList<>();
		String[] ary = StringUtils.split(strArguments, " ");
		for (String str: ary) {
			result.add(new StringParameter(str));
		}
		
		return result;
	}

	public String toDisplayString(List<CommandParameter<?>> argList) {
		StringBuilder sb = new StringBuilder();
		for (int i=2;i<commonArguments.size();i++) {
			CommandParameter<?> arg=commonArguments.get(i);
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(arg.toDisplayString());
		}
		for (CommandParameter<?> arg : argList) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(arg.toDisplayString());
		}
		
		return sb.toString();
	}
	private boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

}
