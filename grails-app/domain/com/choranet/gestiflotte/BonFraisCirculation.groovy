

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * BonFraisCirculation Domain Object 
 */
class BonFraisCirculation implements Serializable {	
    
    private static final long serialVersionUID = 1L;
    	
    String numBon
    
    Date date	
    	
    String details	
    	
    Double montant	
    	
    Double chargeEnUM
    
    Double chargeTheorique
    
    Double trans_surplus
    
    String  immatriculation_vehicule
    
    static transients = ['trans_surplus']
   
    static belongsTo = [categorieFraisCirculation : CategorieFraisCirculation , deplacement : Deplacement , intervention : Intervention, fournisseur : Fournisseur]
    
    static mapping = { 
        numBon index : "bonfraiscirculation_numBon"
        date index : "bonfraiscirculation_date"
        details index : "bonfraiscirculation_details"
        montant index : "bonfraiscirculation_montant"
        chargeEnUM index : "bonfraiscirculation_charge_en_u_m"
        chargeTheorique index : "bonfraiscirculation_charge_theorique"
        immatriculation_vehicule index : "bonfraiscirculation_immatriculation_vehicule"
        categorieFraisCirculation lazy : false
        deplacement lazy : false
        intervention lazy : false
        fournisseur lazy : false
        batchSize: 10
    }
    
    static constraints = {
    
        numBon(nullable : false)
        
        date(nullable : false)
    
        details(nullable : true)
    
        montant(min : 0d, nullable : false)
    
        chargeEnUM(nullable : true)
        
        chargeTheorique(nullable : true)
        
        deplacement(nullable : true)
        
        intervention(nullable : true)
        
        fournisseur(nullable : true)
        
        immatriculation_vehicule(nullable : false)
        
    }
    
//    def getVehicule(){
//        if (this.vehicule == null && this.intervention != null)
//            this.vehicule = this.intervention.vehicule
//    }

    def getTrans_surplus(){
        def surplus = 0d
        if (chargeEnUM != null && chargeTheorique != null && chargeEnUM > chargeTheorique)
            surplus = chargeEnUM - chargeTheorique
        return surplus
    }
	
    String toString() {
        return super.toString()
    }
}

