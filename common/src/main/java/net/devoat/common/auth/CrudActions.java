package net.devoat.common.auth;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public interface CrudActions {
	public String ADD="add";
	public String UPDATE="update";
	public String DELETE="delete";
	public String VIEW="view";
	public Set<String> CRUD_ACTIONS=new TreeSet<>(Arrays.asList(ADD,UPDATE,DELETE,VIEW));
}
