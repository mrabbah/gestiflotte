

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * TypeEntretien Domain Object 
 */
class TypeEntretien implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String libelle	
       		   
    
    static mapping = { 
        libelle index : "typeentretien_libelle"
        batchSize: 14 
    }
    static constraints = {
    
        libelle(blank : false, nullable : false, unique : true)
    
    }
	
    String toString() {
        return libelle
    }
}

