package net.devoat.common.exception;

public class AccessDeniedException extends TechnicalException{

	private static final long serialVersionUID = -12069870686023303L;

	public AccessDeniedException(String msg) {
		super(msg);
	}

}
