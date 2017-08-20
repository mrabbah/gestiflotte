

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Marque Domain Object 
 */
class Marque implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String libelle	
       		   
    
    static mapping = { 
        libelle index : "marque_libelle"
        batchSize: 14 
    }
    static constraints = {
    
        libelle(blank : false, nullable : false, unique : true)
    
    }
	
    String toString() {
        return libelle
    }
}

