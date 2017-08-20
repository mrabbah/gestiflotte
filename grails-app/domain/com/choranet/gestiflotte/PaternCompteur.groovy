package com.choranet.gestiflotte

import java.io.Serializable;

/**
 * PaternCompteur Domain Object 
 */
class PaternCompteur implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String libelle	
    	
    String prefixe	
    	
    String suffixe	
    	
    Integer pas	
    	
    Integer remplissage	
    	
    Integer numeroSuivant	
    	
    String type	
       		   
    
    static mapping = { 
        libelle index : "paterncompteur_libelle"
        prefixe index : "paterncompteur_prefixe"
        suffixe index : "paterncompteur_suffixe"
        pas index : "paterncompteur_pas"
        remplissage index : "paterncompteur_remplissage"
        numeroSuivant index : "paterncompteur_numero_suivant"
        type index : "paterncompteur_type"
        batchSize: 14 
    }
    static constraints = {
    
        libelle(blank : false, nullable : false, unique : true)
    
        prefixe(blank : true, nullable : false)
    
        suffixe(blank : true, nullable : false)
    
        pas(min:1, nullable : false)
    
        remplissage(min:1, nullable : false)
    
        numeroSuivant(min : 0, nullable : false)
    
        type(blank : false, nullable : false, inList:["INTERVENTION", "RESERVATION", "DEPLACEMENT", "FACTURE", "ENTRETIEN", "ENTRETIEN_PERIODIQUE", "PAIEMENT_CHEQ", "FC_PERIODIQUE"])
    
    }
	
    String toString() {
        return libelle
    }
}


