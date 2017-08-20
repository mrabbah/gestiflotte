
package com.choranet.gestiflotte;


/**
 * TypeEntretienPeriodiqueService Service pour la gestion des opérations
 * transactionnelles pour l'objet TypeEntretienPeriodique
 */
class TypeEntretienPeriodiqueService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(TypeEntretienPeriodique.class)
    }
       
    // fonction recursive pour vérifier la portabilité d'une catégorie pour un type d'entretien périodique donné
    def isCategorySupportedForTypeEntretienPeriodique(currentCategorieVehicule, categorieOfCurrentVehicule){ 
        if (categorieOfCurrentVehicule != null){
            if (currentCategorieVehicule.libelle == categorieOfCurrentVehicule.libelle) return true
            isCategorySupportedForTypeEntretienPeriodique(currentCategorieVehicule, categorieOfCurrentVehicule.categoryParente)
        }
        else return false
    }
    
    /**
     * Generer les entretiens periodiques pour chaque véhicule dont la catégorie est prise en compte 
     * pour le type d'entretien périodique en paramètre
     * */
    def genererEntretienPeriodique(TypeEntretienPeriodique typeEPCourant) {
              
        def vehicules = Vehicule.list()
        vehicules.each { v -> 
            for(Categorie categorieVehicule : typeEPCourant.categorieVehicules){
                // si la categorie ou l'une des categories parentes de la voiture courante est déjà prise en compte 
                // pour un type d'entretien périodique déjà défini
                if (isCategorySupportedForTypeEntretienPeriodique(categorieVehicule, v.categorie)){
                    
                    def periode = 0
                    def reste = v.kilometrage % typeEPCourant.periodicite
                    if(reste == 0) {
                        periode = v.kilometrage
                    } else {
                        periode = (((v.kilometrage - reste)/typeEPCourant.periodicite) + 1 ) * typeEPCourant.periodicite
                    }
                    def ep = new EntretienPeriodique(numBon : null, dateEntretien : null, 'details' : typeEPCourant.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'periode' : periode, 'montant' : typeEPCourant.chargeDeBase, 'paye' : false, 'vehicule' : v, 'typeEntretienPeriodique' : typeEPCourant)
                    save(ep)
                    return
                }  
            }  
        }
    }
    
}