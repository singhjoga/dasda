package com.harbois.komrade.runner.execution;

import com.harbois.komrade.runner.exception.StepFailureException;

public interface StepExecutor {
	void execute() throws StepFailureException;
}
