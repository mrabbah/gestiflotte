

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * TypeFraisCirculationPeriodique Domain Object 
 */
class TypeFraisCirculationPeriodique implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String libelle	
    	
    Integer periodicite	
    	
    Double chargeDeBase	
       		
    static hasMany = [categorieVehicules : Categorie]
    
    static mapping = { 
        libelle index : "typefraiscirculationperiodique_libelle"
        periodicite index : "typefraiscirculationperiodique_periodicite"
        chargeDeBase index : "typefraiscirculationperiodique_charge_de_base"
        categorieVehicules lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        libelle(blank : false, nullable : false, unique : true)
    
        periodicite(min : 1, nullable : false)
    
        chargeDeBase(min : 0d, nullable : false)
    
    }
	
    String toString() {
        return libelle
    }
}

