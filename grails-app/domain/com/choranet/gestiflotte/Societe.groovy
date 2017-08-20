

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Societe Domain Object 
 */
class Societe implements Serializable {	
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
        code index : "societe_code"
        raisonSociale index : "societe_raison_sociale"
        societeDuGroup index : "societe_societe_du_group"
        idFiscal index : "societe_id_fiscal"
        patente index : "societe_patente"
        rc index : "societe_rc"
        cnss index : "societe_cnss"
        adresse index : "societe_adresse"
        ville index : "societe_ville"
        tel index : "societe_tel"
        fax index : "societe_fax"
        email index : "societe_email"
        siteWeb index : "societe_site_web"
        batchSize: 11
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

