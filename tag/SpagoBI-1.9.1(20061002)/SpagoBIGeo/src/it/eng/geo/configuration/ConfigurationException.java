/**
 *
 *	LICENSE: see COPYING file
 *
**/

package it.eng.geo.configuration;

public class ConfigurationException extends Exception {
    private String serviceName = null;
    private String description = null;

    /**
     * Costruisce un oggetto di tipo <code>GeoConfigurationException</code>
     * @param severity severity dell'errore.
     * @param code codice di errore.
     */
    public ConfigurationException(String serviceName, String description) {
        super();
        setServiceName(serviceName);
        setServiceName(description);
    }
    public ConfigurationException(String description) {
        super();
        setDescription(description);
    } 

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String toString() {
        return this.description;
    }
}

