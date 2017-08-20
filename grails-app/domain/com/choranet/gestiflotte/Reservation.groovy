

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Reservation Domain Object 
 */
class Reservation implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numReservation
    
    String numBC	
    	
    String mission	
    	
    Double prixParJour	
    	
    Date dateDepart	
    	
    Date dateRestitution	
    	
    Integer nombreDeJours	
    	
    String lieuLivraison	
    	
    String lieuReprise	
    	
    String observations	
       		   
    
    static belongsTo = [conducteur : Intervenant , vehicule : Vehicule , responsableReservations : Utilisateur , client : Societe]
    
    static mapping = {
        numReservation index : "reservation_num"
        numBC index : "reservation_num_b_c"
        mission index : "reservation_mission"
        prixParJour index : "reservation_prix_par_jour"
        dateDepart index : "reservation_date_depart"
        dateRestitution index : "reservation_date_restitution"
        nombreDeJours index : "reservation_nombre_de_jours"
        lieuLivraison index : "reservation_lieu_livraison"
        lieuReprise index : "reservation_lieu_reprise"
        observations index : "reservation_observations"
        conducteur lazy : false
        vehicule lazy : false
        responsableReservations lazy : false
        client lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        numReservation(blank : false, nullable : false, unique : true)
        
        numBC(blank : false, nullable : false)
    
        mission(blank : false, nullable : false)
    
        prixParJour(min : 0d, nullable : false)
    
        dateDepart(nullable : false)
    
        dateRestitution(nullable : false)
    
        nombreDeJours(nullable : false)
    
    }
	
    String toString() {
        return (mission + " " + numReservation) 
    }
}

