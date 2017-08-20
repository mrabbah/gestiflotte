

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Entretien Domain Object 
 */
class Entretien implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numBon
    
    Date dateEntretien	
    	
    String details	
    	
    Double montant	
       		
    static belongsTo = [typeEntretien : TypeEntretien , vehicule : Vehicule, fournisseur : Fournisseur]
    
    static mapping = { 
        numBon index : "entretien_numBon"
        dateEntretien index : "entretien_date_entretien"
        details index : "entretien_details"
        montant index : "entretien_montant"
        typeEntretien lazy : false
        vehicule lazy : false
        fournisseur lazy : false
        batchSize: 10
    }
    static constraints = {
    
        numBon(nullable : false)
        
        dateEntretien(nullable : false)
    
        details()
    
        montant(min : 0d, nullable : false)
            
        fournisseur(nullable : true)
        
    }
	
    String toString() {
        return super.toString()
    }
}

