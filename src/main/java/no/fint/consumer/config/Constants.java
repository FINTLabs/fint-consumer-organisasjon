
package no.fint.consumer.config;

public enum Constants {
;

    public static final String COMPONENT = "organisasjon";
    public static final String COMPONENT_CONSUMER = COMPONENT + " consumer";
    public static final String CACHE_SERVICE = "CACHE_SERVICE";

    
    public static final String CACHE_INITIALDELAY_ORGANISASJONSELEMENT = "${fint.consumer.cache.initialDelay.organisasjonselement:60000}";
    public static final String CACHE_FIXEDRATE_ORGANISASJONSELEMENT = "${fint.consumer.cache.fixedRate.organisasjonselement:900000}";
    

}
