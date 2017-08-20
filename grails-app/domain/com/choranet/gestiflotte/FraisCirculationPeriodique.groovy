

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * FraisCirculationPeriodique Domain Object 
 */
class FraisCirculationPeriodique implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numBon
    
    Date date	
    	
    String details	
    	
    Double montant	
    	
    Boolean paye	
    	
    Date periode	
       	
    static belongsTo = [vehicule : Vehicule , typeFraisCirculationPeriodique : TypeFraisCirculationPeriodique, fournisseur : Fournisseur]
    
    static mapping = { 
        numBon index : "fraiscirculationperiodique_numBon"
        date index : "fraiscirculationperiodique_date"
        details index : "fraiscirculationperiodique_details"
        montant index : "fraiscirculationperiodique_montant"
        paye index : "fraiscirculationperiodique_paye"
        periode index : "fraiscirculationperiodique_periode"
        vehicule lazy : false
        typeFraisCirculationPeriodique lazy : false
        fournisseur lazy : false
        batchSize: 10
    }
    static constraints = {
    
        numBon(nullable : true)
        
        date(nullable : true)
    
        details()
    
        montant(min : 0d)
    
        paye()
    
        periode(nullable : false)
        
        fournisseur(nullable : true)
        
    }
	
    String toString() {
        return super.toString()
    }
}

