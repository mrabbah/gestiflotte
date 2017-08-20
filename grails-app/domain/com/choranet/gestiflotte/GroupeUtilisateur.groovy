package com.choranet.gestiflotte

import java.io.Serializable;

/**
 * Authority domain class.
 */
class GroupeUtilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    
    static hasMany = [people: Utilisateur]
    static belongsTo = Utilisateur
    
    /** description */
    String description
    /** ROLE String */
    String authority

    static constraints = {
        authority(blank: false, unique: true)
        description()
    }
    
    static mapping = {
        people fetch : 'join'
    }
        
    String toString() {
        return description
    }
}
