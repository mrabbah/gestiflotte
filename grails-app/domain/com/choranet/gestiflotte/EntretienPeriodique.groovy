

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * EntretienPeriodique Domain Object 
 */
class EntretienPeriodique implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numBon
    
    Date dateEntretien	
    	
    String details	
    	
    Integer periode	
    	
    Double montant	
    	
    Boolean paye	
       		   
    static belongsTo = [vehicule : Vehicule , typeEntretienPeriodique : TypeEntretienPeriodique, fournisseur : Fournisseur]
    
    static mapping = { 
        numBon index : "entretienperiodique_numBon"
        dateEntretien index : "entretienperiodique_date_entretien"
        details index : "entretienperiodique_details"
        periode index : "entretienperiodique_periode"
        montant index : "entretienperiodique_montant"
        paye index : "entretienperiodique_paye"
        vehicule lazy : false
        typeEntretienPeriodique lazy : false
        fournisseur lazy : false
        batchSize: 10 
    }
    static constraints = {
    
        numBon(nullable : true)
        
        dateEntretien(nullable : true)
    
        details()
    
        periode(nullable : false)
    
        montant(min : 0d)
    
        paye()
        
        fournisseur(nullable : true)
        
    }
	
    String toString() {
        return super.toString()
    }
}

