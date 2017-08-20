

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Societe Domain Object 
 */
class Fournisseur implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String code
    
    String raisonSociale
    
    Boolean societeDuGroup	
    	
    String idFiscal	
    	
    String patente	
    	
    String rc	
    	
    String cnss	
    	
    String adresse	
    	
    String ville	
    	
    String tel	
    	
    String fax	
    	
    String email	
    	
    String siteWeb	
    
    static mapping = { 
        code index : "Fournisseur_code"
        raisonSociale index : "Fournisseur_raison_sociale"
        societeDuGroup index : "Fournisseur_societe_du_group"
        idFiscal index : "Fournisseur_id_fiscal"
        patente index : "Fournisseur_patente"
        rc index : "Fournisseur_rc"
        cnss index : "Fournisseur_cnss"
        adresse index : "Fournisseur_adresse"
        ville index : "Fournisseur_ville"
        tel index : "Fournisseur_tel"
        fax index : "Fournisseur_fax"
        email index : "Fournisseur_email"
        siteWeb index : "Fournisseur_site_web"
        batchSize: 14 
    }
    static constraints = {
    
        code(blank : false, nullable : false, unique : true)
        
        raisonSociale(blank : false, nullable : false, unique : true)
        
        societeDuGroup()
    
        idFiscal(nullable : true)
    
        patente(nullable : true)
    
        rc(nullable : true)
    
        cnss(nullable : true)
    
        adresse(nullable : true)
    
        ville(nullable : true)
    
        tel(nullable : true)
    
        fax(nullable : true)
    
        email(nullable : true)
    
        siteWeb(nullable : true)
    
    }
	
    String toString() {
        return raisonSociale
    }
}

