package com.choranet.gestiflotte

import java.io.Serializable;

/**
 * Request Map domain class.
 */
class DroitUtilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    
	String url
	String configAttribute

	static constraints = {
		url(blank: false, unique: true)
		configAttribute(blank: false)
	}
        
    String toString() {
        return (url . " : " . configAttribute)
    }
}
