package net.devoat.common.auditing;

public interface Auditable<ID> {
	String getObjectType();
	String getName();
	ID getId();
}
