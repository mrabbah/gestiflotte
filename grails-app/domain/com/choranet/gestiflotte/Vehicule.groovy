

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * Vehicule Domain Object 
 */
class Vehicule implements Serializable {	
    
    private static final long serialVersionUID = 1L;
    	
    String code
    
    String immatriculation
    
    String numeroRemorque
    	
    Date dateMiseEnCirculation	
    	
    Boolean vehiculeDeService	
    	
    Integer kilometrage	
    
    Double consommationGasoilAvideMin
    
    Double consommationGasoilAvideMax
    
    Double consommationGasoilMin
    
    Double consommationGasoilMax
    
    Double consommationHuileMin
    
    Double consommationHuileMax
    	
    static belongsTo = [proprietaireParticulier : Intervenant , model : Model , prorprietaireSociete : Societe , categorie : Categorie , energie : Energie]
    
    static mapping = { 
        code index : "vehicule_code"
        immatriculation index : "vehicule_immatriculation"
        numeroRemorque index :  "vehicule_numero_remorque"
        dateMiseEnCirculation index : "vehicule_date_mise_en_circulation"
        vehiculeDeService index : "vehicule_vehicule_de_service"
        kilometrage index : "vehicule_kilometrage"
        consommationGasoilAvideMin index : "vehicule_consommation_gasoil_Avide_min"
        consommationGasoilAvideMax index : "vehicule_consommation_gasoil_Avide_max"
        consommationGasoilMin index : "vehicule_consommation_gasoil_min"
        consommationGasoilMax index : "vehicule_consommation_gasoil_max"
        consommationHuileMin index : "vehicule_consommation_huile_min"
        consommationHuileMax index : "vehicule_consommation_huile_max"
        proprietaireParticulier lazy : false
        model lazy : false
        prorprietaireSociete lazy : false
        categorie lazy : false
        energie lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        code(blank : false, nullable : false, unique : true)
        
        immatriculation(blank : false, nullable : false, unique : true)
        
        numeroRemorque(nullable : true)
    
        dateMiseEnCirculation(nullable : true)
    
        vehiculeDeService()
    
        kilometrage(min : 0, nullable : true)
        
        proprietaireParticulier(nullable : true)
        
        prorprietaireSociete(nullable : true)
        
        consommationGasoilAvideMin(min : 0d, max : 100d, nullable : true)
        
        consommationGasoilAvideMax(min : 0d, max : 100d, nullable : true)
        
        consommationGasoilMin(min : 0d, max : 100d, nullable : true)
        
        consommationGasoilMax(min : 0d, max : 100d, nullable : true)
        
        consommationHuileMin(min : 0d, max : 100d, nullable : true)
        
        consommationHuileMax(min : 0d, max : 100d, nullable : true)
    
    }
	
    String toString() {
        return immatriculation
    }
}

