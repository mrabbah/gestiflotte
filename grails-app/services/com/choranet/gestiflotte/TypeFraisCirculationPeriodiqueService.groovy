
package com.choranet.gestiflotte;

import java.util.Date
import java.text.SimpleDateFormat

/**
 * TypeFraisCirculationPeriodiqueService Service pour la gestion des opérations
 * transactionnelles pour l'objet TypeFraisCirculationPeriodique
 */
class TypeFraisCirculationPeriodiqueService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(TypeFraisCirculationPeriodique.class)
    }
        
    // fonction recursive pour vérifier la portabilité d'une catégorie pour un type d'entretien périodique donné
    def isCategorySupportedForTypeFraisCirculationPeriodique(currentCategorieVehicule, categorieOfCurrentVehicule){ 
        if (categorieOfCurrentVehicule != null){
            if (currentCategorieVehicule.libelle == categorieOfCurrentVehicule.libelle) return true
            isCategorySupportedForTypeFraisCirculationPeriodique(currentCategorieVehicule, categorieOfCurrentVehicule.categoryParente)
        }
        else return false
    }
    
    /**
     * Generer les entretiens periodiques pour chaque véhicule dont la catégorie est prise en compte 
     * pour le type d'entretien périodique en paramètre
     * */
    def genererFraisCirculationPeriodique(TypeFraisCirculationPeriodique typeFCPCourant) {
        
        def aujourdhui = new Date()
        def sdf = new SimpleDateFormat("yyyy/MM/dd")
        
        def vehicules = Vehicule.list()
        vehicules.each { v -> 
            for(Categorie categorieVehicule : typeFCPCourant.categorieVehicules){
                // si la categorie ou l'une des categories parentes de la voiture courante est déjà prise en compte 
                // pour un type de frais de circulation périodique déjà défini
                if (isCategorySupportedForTypeFraisCirculationPeriodique(categorieVehicule, v.categorie)){
                    
                    // System.out.println(v.categorie)            
                    if(typeFCPCourant.libelle.equals("Vignette")) {
                        
                        def anneePeriode = aujourdhui.getYear()
                        if(aujourdhui.getMonth() != 0) {
                            anneePeriode++
                        }
                        def periode = new Date(anneePeriode, 0, 10)
                        def fcp = new FraisCirculationPeriodique('date' : null, 'details' : typeFCPCourant.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : typeFCPCourant.chargeDeBase, 'paye' : false, 'periode' : periode, 'vehicule' : v, 'typeFraisCirculationPeriodique' : typeFCPCourant)
                        save(fcp)
                    } else {
                        if(v.dateMiseEnCirculation != null) {
                            def moisMiseEnCirculation = v.dateMiseEnCirculation.getMonth()
                            def jourMiseEnCirculation = v.dateMiseEnCirculation.getDate()
                            def dateMiseEnCirculationDecalee = new Date(aujourdhui.getYear(), moisMiseEnCirculation, jourMiseEnCirculation)
                
                            if(moisMiseEnCirculation == aujourdhui.getMonth() && jourMiseEnCirculation == aujourdhui.getDate()) {
                                def fcp = new FraisCirculationPeriodique('date' : null, 'details' : typeFCPCourant.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : typeFCPCourant.chargeDeBase, 'paye' : false, 'periode' : aujourdhui, 'vehicule' : v, 'typeFraisCirculationPeriodique' : typeFCPCourant)
                                save(fcp)
                            } else if (aujourdhui.before(dateMiseEnCirculationDecalee)) {
                                def periode = dateMiseEnCirculationDecalee.clone()
                                while(aujourdhui.before(periode - typeFCPCourant.periodicite)) {
                                    periode = periode - typeFCPCourant.periodicite
                                }
                                def fcp = new FraisCirculationPeriodique('date' : null, 'details' : typeFCPCourant.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : typeFCPCourant.chargeDeBase, 'paye' : false, 'periode' : periode, 'vehicule' : v, 'typeFraisCirculationPeriodique' : typeFCPCourant)
                                save(fcp)
                            } else {
                                def periode = dateMiseEnCirculationDecalee.clone()
                                while((periode + typeFCPCourant.periodicite).before(aujourdhui)) {
                                    periode = periode + typeFCPCourant.periodicite
                                }
                                periode = periode + typeFCPCourant.periodicite
                                def fcp = new FraisCirculationPeriodique('date' : null, 'details' : typeFCPCourant.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : typeFCPCourant.chargeDeBase, 'paye' : false, 'periode' : periode, 'vehicule' : v, 'typeFraisCirculationPeriodique' : typeFCPCourant)
                                save(fcp)
                            }
                        }
                    }
                }
                
            }  // End For
        }
    }
    
}