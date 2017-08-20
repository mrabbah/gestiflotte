

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Entretien Domain Object 
 */
class Region implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String code
    	
    String intitule	
    
    Double kilometrageMin
    
    Double kilometrageMax
    
    static mapping = { 
        code index : "region_code"
        intitule index : "region_intitule"
        kilometrageMin index : "region_kilometrageMin"
        kilometrageMax index : "region_kilometrageMax"
        batchSize: 15 
    }
    static constraints = {
    
        code(nullable : false)
        
        intitule(nullable : false)
        
        kilometrageMin(min : 0d, nullable : false)
    
        kilometrageMax(min : 0d, nullable : false)
        
    }
	
    String toString() {
        return intitule
    }
}

