
package com.choranet.commun

import java.io.Serializable;

/**
 * Cle d activation
 **/

class Cle implements Serializable {

    private static final long serialVersionUID = 1L;
    
    String cleProduit
    
    Date dateActivation
    
    String dateFinActivation
    
    static constraints = {
        cleProduit(nullable : false, blank: false, unique: true)
        dateActivation(nullable : false, unique: true)
        dateFinActivation(nullable : false, blank: false)
    }
    
    static mapping = {
        cleProduit index : "cle_cleproduit"
        dateActivation index : "cle_datefinactivation"
        batchSize: 14
    }
    
}
