

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Facturation Domain Object 
 */
class Facturation implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numFacture	
    	
    Date date	
    	
    String description	
    	
    Double montantHT	
    	
    Boolean paye	
       		   
    
    static belongsTo = [perstataireGroup : Societe]
    
    static hasMany = [interventions : Intervention]
    
    static mapping = { 
        numFacture index : "facturation_num_facture"
        date index : "facturation_date"
        description index : "facturation_description"
        montantHT index : "facturation_montant_h_t"
        paye index : "facturation_paye"
        interventions lazy : false
        perstataireGroup lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        numFacture(blank : false, nullable : false, unique : true)
    
        date(nullable : false)
    
        description()
    
        montantHT(min : 0d, nullable : false)
    
        paye()
    
    }
	
    String toString() {
        return numFacture
    }
}

