package net.devoat.common.auditing;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLog, String>{
	List<AuditLog> findByObjectTypeAndObjectIdOrderByDateDesc(String objectTyüe, String objectId);
}
