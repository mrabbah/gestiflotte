

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Intervenant Domain Object 
 */
class Intervenant implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String code
    
    String cin	
    	
    String nom	
    	
    String prenom	
    	
    String typePermis	
    	
    String numeroPermis	
    	
    Boolean sousTraite	
    	
    String lieuNaissance	
    	
    Date dateNaissance	
    	
    String adresse
    
    String numeroCNSS
    	
    String gsm	
    	
    String email
    
    Double commission
       		   
    
    static belongsTo = [nationalite : Nationalite , employer : Societe]
    
    static mapping = { 
        code index : "intervenant_code"
        cin index : "intervenant_cin"
        nom index : "intervenant_nom"
        prenom index : "intervenant_prenom"
        typePermis index : "intervenant_type_permis"
        numeroPermis index : "intervenant_numero_permis"
        sousTraite index : "intervenant_sous_traite"
        lieuNaissance index : "intervenant_lieu_naissance"
        dateNaissance index : "intervenant_date_naissance"
        adresse index : "intervenant_adresse"
        numeroCNSS index : "intervenant_numero_cnss"
        gsm index : "intervenant_gsm"
        email index : "intervenant_email"
        commission index : "intervenant_commission"
        nationalite lazy : false
        employer lazy : false
        batchSize: 10
    }
    static constraints = {
    
        code(blank : false, nullable : false, unique : true)
        
        cin(blank : false, nullable : false, unique : true)
    
        nom(blank : false, nullable : false)
    
        prenom(blank : false, nullable : false)
    
        typePermis(blank : false, nullable : false)
    
        numeroPermis(unique : true)
    
        sousTraite()
    
        lieuNaissance(nullable : true)
    
        dateNaissance(nullable : true)
    
        adresse(nullable : true)
        
        numeroCNSS(nullable : true)
    
        gsm(blank : false, nullable : false)
    
        email(email : true, nullable : true)
        
        commission(min : 1d, nullable : false)
    
        employer(nullable : true)
    }
	
    String toString() {
        return (prenom + ' ' + nom)
    }
}

