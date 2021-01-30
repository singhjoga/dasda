package net.devoat.component.v1;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import net.devoat.common.auth.CrudActions;

public interface ComponentActions extends CrudActions{
	public String BUILD="build";
	public String DEPLOY="deploy";
	public String START="start";
	public String STOP="stop";
	public String UNDEPLOY="undeploy";
	public String DELETE_INSTANCE="delete-instance";
	public String QUERY_STATUS="query-status";
	public String QUERY_LOG="query-log";
	public Set<String> ALL_ACTIONS=new TreeSet<>(Arrays.asList(ADD,UPDATE,DELETE,VIEW,BUILD,DEPLOY,START,STOP,UNDEPLOY,DELETE_INSTANCE,QUERY_STATUS,QUERY_LOG));
}
