

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * CategorieFraisCirculation Domain Object 
 */
class CategorieFraisCirculation implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String libelle	
       		   
    static belongsTo = [uniteMesure : UniteDeMesure]
        
    static mapping = { 
        libelle index : "categoriefraiscirculation_libelle"
        uniteMesure lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        libelle(blank : false, nullable : false, unique : true)
    
        uniteMesure(nullable : false)
    
    }
	
//    @Override
//    public boolean equals(Object o) {
//        return (this.libelle == o.libelle)
//    }

    String toString() {
        return libelle
    }
}

