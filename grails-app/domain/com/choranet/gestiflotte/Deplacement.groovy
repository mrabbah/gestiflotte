

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Deplacement Domain Object 
 */
class Deplacement implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numeroDeplacement	
    	
    String mission	
    	
    Date dateDepart	
    	
    Date dateRetour	
    	
    Integer nombreDeJours	
    	
    Integer kilometrageDepart	
    	
    Integer kilometrageRetour	
    	
    Integer distanceParcourue	
    	
    String observations	
    
    //Date dateDepart_begin
    //Date dateDepart_end
    
    //static transients = ['trans_dateDepart_begin', 'trans_dateDepart_end']
       		   
    
    static belongsTo = [personnel : Intervenant , voitureService : Vehicule]
    
    static mapping = { 
        numeroDeplacement index : "deplacement_numero_deplacement"
        mission index : "deplacement_mission"
        dateDepart index : "deplacement_date_depart"
        //dateDepart_begin index : "deplacement_date_depart_begin"
        //dateDepart_end index : "deplacement_date_depart_end"
        dateRetour index : "deplacement_date_retour"
        nombreDeJours index : "deplacement_nombre_de_jours"
        kilometrageDepart index : "deplacement_kilometrage_depart"
        kilometrageRetour index : "deplacement_kilometrage_retour"
        distanceParcourue index : "deplacement_distance_parcourue"
        observations index : "deplacement_observations"
        personnel lazy : false
        voitureService lazy : false
        batchSize: 10 
    }
    static constraints = {
    
        numeroDeplacement()
    
        mission(blank : false, nullable : false)
    
        dateDepart(nullable : true)
        //dateDepart_begin(nullable : true)
        //dateDepart_end(nullable : true)
    
        dateRetour(nullable : true)
    
        nombreDeJours()
    
        kilometrageDepart(nullable : false)
    
        kilometrageRetour(nullable : true)
    
        distanceParcourue(nullable : true)
    
        observations()
    
    }
	
    String toString() {
        return (mission + " " + numeroDeplacement) 
    }
}
