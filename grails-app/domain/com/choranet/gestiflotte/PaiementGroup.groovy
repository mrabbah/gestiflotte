

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * PaiementGroup Domain Object 
 */
class PaiementGroup implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numCheque	
    	
    Date date	
    	
    Double montantCheque	
       		   
    
    static belongsTo = [societeGroup : Societe, fournisseur : Fournisseur]
    
    static hasMany = [entretienPeriodiques : EntretienPeriodique , entretiens : Entretien , fraisCirculationPeriodiques : FraisCirculationPeriodique , bonFraisCirculations : BonFraisCirculation]
    
    static mapping = { 
        numCheque index : "paiementgroup_num_cheque"
        date index : "paiementgroup_date"
        montantCheque index : "paiementgroup_montant_cheque"
        entretienPeriodiques lazy : false
        entretiens lazy : false
        fraisCirculationPeriodiques lazy : false
        societeGroup lazy : false
        fournisseur lazy : false
        bonFraisCirculations lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        numCheque(blank : false, nullable : false, unique : true)
    
        date(nullable : false)
    
        montantCheque(min : 0d, nullable : false)
        
        entretienPeriodiques(nullable : true)
        
        entretiens(nullable : true)
        
        fraisCirculationPeriodiques(nullable : true)
        
        bonFraisCirculations(nullable : true)
        
        fournisseur(nullable : true)
    
    }
	
    String toString() {
        return numCheque+"- "+societeGroup.raisonSociale
    }
}
