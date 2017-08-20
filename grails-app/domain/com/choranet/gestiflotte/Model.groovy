

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Model Domain Object 
 */
class Model implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String libelle	
       		   
    
    static belongsTo = [marque : Marque]
    
    static mapping = { 
        libelle index : "model_libelle"
        marque lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        libelle(blank : false, nullable : false, unique : true)
    
    }
	
    String toString() {
        return marque.toString() + " " + libelle
    }
}

