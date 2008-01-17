package it.eng.spagobi.services.audit;

/**
 * Interface of audit Service
 */
public interface AuditService {
    /**
     * 
     * @param token  String
     * @param user String
     * @param id String
     * @param start String
     * @param end String
     * @param state String
     * @param message String
     * @param errorCode String
     * @return String
     */
    String log(String token,String user,String id,String start,String end,String state,String message,String errorCode);
}
