

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Tarification Domain Object 
 */
class Tarification implements Serializable {	
    
    private static final long serialVersionUID = 1L;
    	
    String code 
    
    String modeTarif
    
    Integer annee
    
    Integer debutPeriode
    
    Integer finPeriode
    
    Double quantite
    	
    String unite	
    	
    Double prix	
    
    String source
    
    String destination
    
    static belongsTo = [trajet : Trajet, region : Region] 
       		
    static hasMany = [clients : Societe]
    
    static mapping = { 
        code index : "Tarifcation_code"
        modeTarif index : "Tarification_modeTarif"
        annee index : "Tarification_annee"
        debutPeriode index : "Tarification_debut_periode"
        finPeriode index : "Tarification_fin_periode"
        quantite index : "Tarification_quantite"
        unite index : "Tarification_unite"
        prix index : "Tarification_prix"
        source index : "Tarification_source"
        destination index : "Tarification_destination"
        trajet lazy : false
        region lazy : false
        clients lazy : false
        batchSize: 14 
    }
    
    static constraints = {
    
        code(blank : false, nullable : false, unique : true)
        
        modeTarif(nullable : false)
        
        annee(nullable : true)
        
        debutPeriode(min : 1, max : 29, nullable : false)
        
        finPeriode(min : 2, max : 31, nullable : false)
        
        quantite(min : 1d, nullable : false)
    
        unite(nullable : true)
    
        prix(min : 0d, nullable : false)
        
        source(nullable : true)
        
        destination(nullable : true)
        
        trajet(nullable : true)
        
        region(nullable : true)
        
    }
	
    String toString() {
        return code + ' : '+ modeTarif
    }
}

