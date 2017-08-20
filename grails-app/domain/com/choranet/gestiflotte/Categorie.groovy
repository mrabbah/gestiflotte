

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Categorie Domain Object 
 */
class Categorie implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String libelle	
    	
    Double prixJournalier	
       		   
    
    static belongsTo = [categoryParente : Categorie]
    
    static mapping = { 
        libelle index : "categorie_libelle"
        prixJournalier index : "categorie_prix_journalier"
        categoryParente lazy : false
        categoryParente cascade:"save-update"
        batchSize: 14 
    }
    static constraints = {
    
        libelle(blank : false, nullable : false, unique : true)
    
        prixJournalier(min : 0d, nullable : false)
    
    }
    
    @Override
    public boolean equals(Object o) {
        return this.libelle == o.libelle
    }

    String toString() {
        return libelle
    }
}

