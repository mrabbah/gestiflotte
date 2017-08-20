

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Entretien Domain Object 
 */
class Trajet implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String code
    	
    String source	
    
    String destination
    
    Double kilometrage
    
    static mapping = { 
        code index : "trajet_code"
        source index : "trajet_source"
        destination index : "trajet_destination"
        kilometrage index : "trajet_kilometrage"
        batchSize: 15 
    }
    static constraints = {
    
        code(blank : false, nullable : false, unique : true)
        
        source(nullable : false)
        
        destination(nullable : false)
    
        kilometrage(min : 0d, nullable : false)
        
    }
	
    String toString() {
        return source + "=>" + destination
    }
}

