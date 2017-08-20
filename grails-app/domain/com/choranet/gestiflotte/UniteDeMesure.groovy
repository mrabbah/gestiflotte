package com.choranet.gestiflotte

import java.io.Serializable;

/**
 * UniteDeMesure Domain Object 
 */
class UniteDeMesure implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String idUnite	
    	
    Double valeur	
       		   
    
    static mapping = { 
        idUnite index : "uniteDeMesure_idUnite"
        valeur index : "uniteDeMesure_valeur"
        batchSize: 14 
    }
    static constraints = {
    
        idUnite(blank : false, nullable : false, unique : true)
    
        valeur(nullable : true)
    
    }
	
    String toString() {
        return idUnite
    }
}


