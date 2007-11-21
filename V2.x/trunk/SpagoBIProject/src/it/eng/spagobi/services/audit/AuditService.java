package it.eng.spagobi.services.audit;

public interface AuditService {

    String log(String token,String user,String id,String start,String end,String state,String message,String errorCode);
}
