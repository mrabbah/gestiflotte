
package com.choranet.gestiflotte;

import java.util.Date
import java.text.SimpleDateFormat

/**
 * FraisCirculationPeriodiqueService Service pour la gestion des opérations
 * transactionnelles pour l'objet FraisCirculationPeriodique
 */
class FraisCirculationPeriodiqueService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(FraisCirculationPeriodique.class)
    }
        
    def getListFraisCirculationPeriodiquesNonPayes() {
        def criteria = FraisCirculationPeriodique.createCriteria()
        def result = criteria.list {
            eq("paye", false)
        }
        return result
    }
        
    // fonction recursive pour vérifier la portabilité d'une catégorie pour un type de frais de circulation périodique donné
    def isCategorySupportedForTypeFraisCirculationPeriodique(currentCategorieVehicule, categorieOfCurrentVehicule){ 
        if (categorieOfCurrentVehicule != null){
            if (currentCategorieVehicule.libelle == categorieOfCurrentVehicule.libelle) return true
            isCategorySupportedForTypeFraisCirculationPeriodique(currentCategorieVehicule, categorieOfCurrentVehicule.categoryParente)
        }
        else return false
    }
    
    /**
     * Generer frais circulation periodique pour une nouvelle vehicule
     **/
    def genererFraisCirculationPeriodique(Vehicule v) {
        def sdf = new SimpleDateFormat("yyyy/MM/dd")
        def aujourdhui = new Date()
            
            
        def tfcps = TypeFraisCirculationPeriodique.list()
        tfcps.each { tfcp ->
            for(Categorie categorieVehicule : tfcp.categorieVehicules){
                
                if (isCategorySupportedForTypeFraisCirculationPeriodique(categorieVehicule, v.categorie)){
                    
                    if(tfcp.libelle.equals("Vignette") || tfcp.libelle.equals("TAXE A L 'ESSIEUX")) {
                        
                        def anneePeriode = aujourdhui.getYear()
                        if(aujourdhui.getMonth() != 0) {
                            anneePeriode++
                        }
                        def periode = new Date(anneePeriode, 0, 10)
                        def fcp = new FraisCirculationPeriodique('date' : null, 'details' : tfcp.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : tfcp.chargeDeBase, 'paye' : false, 'periode' : periode, 'vehicule' : v, 'typeFraisCirculationPeriodique' : tfcp)
                        save(fcp)
                    } else {
                        
                        def moisMiseEnCirculation = v.dateMiseEnCirculation.getMonth()
                        def jourMiseEnCirculation = v.dateMiseEnCirculation.getDate()
                        def dateMiseEnCirculationDecalee = new Date(aujourdhui.getYear(), moisMiseEnCirculation, jourMiseEnCirculation)
                
                        if(moisMiseEnCirculation == aujourdhui.getMonth() && jourMiseEnCirculation == aujourdhui.getDate()) {
                            def fcp = new FraisCirculationPeriodique('date' : null, 'details' : tfcp.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : tfcp.chargeDeBase, 'paye' : false, 'periode' : aujourdhui, 'vehicule' : v, 'typeFraisCirculationPeriodique' : tfcp)
                            save(fcp)
                        } else if (aujourdhui.before(dateMiseEnCirculationDecalee)) {
                            def periode = dateMiseEnCirculationDecalee.clone()
                            while(aujourdhui.before(periode - tfcp.periodicite)) {
                                periode = periode - tfcp.periodicite
                            }
                            def fcp = new FraisCirculationPeriodique('date' : null, 'details' : tfcp.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : tfcp.chargeDeBase, 'paye' : false, 'periode' : periode, 'vehicule' : v, 'typeFraisCirculationPeriodique' : tfcp)
                            save(fcp)
                        } else {
                            def periode = dateMiseEnCirculationDecalee.clone()
                            while((periode + tfcp.periodicite).before(aujourdhui)) {
                                periode = periode + tfcp.periodicite
                            }
                            periode = periode + tfcp.periodicite
                            def fcp = new FraisCirculationPeriodique('date' : null, 'details' : tfcp.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : tfcp.chargeDeBase, 'paye' : false, 'periode' : periode, 'vehicule' : v, 'typeFraisCirculationPeriodique' : tfcp)
                            save(fcp)
                        }
                
                    }
                }
                
            } // end for
        }
        
    }
        
    def genererProachainFraisCirculationPeriodique(FraisCirculationPeriodique fcpCourant) {
        def sdf = new SimpleDateFormat("yyyy/MM/dd")
        def tfcp = fcpCourant.typeFraisCirculationPeriodique
        def periode = fcpCourant.periode + tfcp.periodicite
        def v = fcpCourant.vehicule
        def fcp = new FraisCirculationPeriodique('date' : null, 'details' : tfcp.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'montant' : tfcp.chargeDeBase, 'paye' : false, 'periode' : periode, 'vehicule' : v, 'typeFraisCirculationPeriodique' : tfcp)
        save(fcp)
    }
        
    def getListeEntretientsPeriodiquesNonPayeParCheque() {
        def cheques = PaiementGroup.list()
        def ids = cheques*.fraisCirculationPeriodiques*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        def criteria2 = FraisCirculationPeriodique.createCriteria()
        def fcpNonPayes = criteria2.list {
            if(idds.size() > 0) {
                not{"in"("id", idds)}
            }
            eq("paye", true)
        }
        return fcpNonPayes
    }
        
    def concatListeEntretientsPeriodiquesNonPayeParCheque(liste) {
        liste <<  getListeEntretientsPeriodiquesNonPayeParCheque()
        return liste
    }
    
    def getAlertsFraisCirculationPeriodiques() {
        Date nextWeek = new Date() + 8;
        def criteria = FraisCirculationPeriodique.createCriteria()
        def result = criteria.list {
            eq("paye", false)
            le("periode", nextWeek)
        }
        return result
    }
    
    def getFraisCirculationPeriodique(date1, date2) {
        def criteria = FraisCirculationPeriodique.createCriteria()
        def result = criteria.get {
            projections {
                sum("montant")
            }
            between("date", date1, date2)
            eq("paye", true)
        }
        if(result == null) {
            result = 0;
        }
        return result
    }
    
    def getDepensesBetween(date1, date2) {
        def criteria = FraisCirculationPeriodique.createCriteria()
        def result = criteria.list {
            between("date", date1, date2)
            eq("payementEffectue", true)
        }
        return result
    }
        
    def getDepensesVehicule(v) {
        def criteria = FraisCirculationPeriodique.createCriteria()
        def result = criteria.list {
            eq("vehicule", v)
            eq("paye", true)
        }
        return result
    }
}