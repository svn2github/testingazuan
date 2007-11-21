package it.eng.spagobi.services.event;

public interface EventService {
    
    String fireEvent(String token,String user,String description,String parameters,String rolesHandler,String presentationHandler);

}
