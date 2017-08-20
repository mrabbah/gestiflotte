
package com.choranet.gestiflotte;


/**
 * EntretienPeriodiqueService Service pour la gestion des opérations
 * transactionnelles pour l'objet EntretienPeriodique
 */
class EntretienPeriodiqueService extends SuperService {

    static transactional = true
    
    def list() throws Exception {
        return super.list(EntretienPeriodique.class)
    }
        
    def getListEntretiensPeriodiquesNonPayes() {
        def criteria = EntretienPeriodique.createCriteria()
        def result = criteria.list {
            eq("paye", false)
        }
        return result
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
     * Generer entretient periodique pour une nouvelle vehicule
     **/
    def genererEntretienPeriodique(Vehicule v) {
        
        def teps = TypeEntretienPeriodique.list()
        teps.each { tep ->           
            for(Categorie categorieVehicule : tep.categorieVehicules){
                // si la categorie ou l'une des categories parentes de la voiture courante est déjà prise en compte 
                // pour un type d'entretien périodique déjà défini
                if (isCategorySupportedForTypeEntretienPeriodique(categorieVehicule, v.categorie)){
                    
                    def periode = 0
                    def reste = v.kilometrage % tep.periodicite
                    if(reste == 0) {
                        periode = v.kilometrage
                    } else {
                        periode = (((v.kilometrage - reste)/tep.periodicite) + 1 ) * tep.periodicite
                    }
                    def ep = new EntretienPeriodique(numBon : null, dateEntretien : null, 'details' : tep.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'periode' : periode, 'montant' : tep.chargeDeBase, 'paye' : false, 'vehicule' : v, 'typeEntretienPeriodique' : tep)
                    save(ep)
                    return
                }  
            }  
        }
    }
        
    def genererProchainEntretienPeriodique(EntretienPeriodique epCourant) {
        def tep = epCourant.typeEntretienPeriodique
        def periode = epCourant.periode + tep.periodicite
        def v = epCourant.vehicule
        def ep = new EntretienPeriodique(numBon : null, dateEntretien : null, 'details' : tep.libelle + " : " + v.model.libelle + "(" + v.immatriculation + ")", 'periode' : periode, 'montant' : tep.chargeDeBase, 'paye' : false, 'vehicule' : v, 'typeEntretienPeriodique' : tep)
        save(ep)
    }
        
    def getListeEntretientsPeriodiquesNonPayeParCheque() {
        def cheques = PaiementGroup.list()
        def ids = cheques*.entretienPeriodiques*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        def criteria2 = EntretienPeriodique.createCriteria()
        def epNonPayes = criteria2.list {
            if(idds.size() > 0) {
                not{"in"("id", idds)}
            }
            eq("paye", true)
        }
        return epNonPayes
    }
    def concatListeEntretientsPeriodiquesNonPayeParCheque(liste) {
        liste << getListeEntretientsPeriodiquesNonPayeParCheque()
        return liste
    }
    
    def getAlertsEntretienPeriodique() {
        def result = EntretienPeriodique.findAll("from EntretienPeriodique as e where e.paye = false and (e.periode - e.vehicule.kilometrage) < e.typeEntretienPeriodique.periodicite and mod(e.vehicule.kilometrage , e.typeEntretienPeriodique.periodicite) >= (e.typeEntretienPeriodique.periodicite * 90 / 100)")
        return result
    }
    
    def getFraisEntretienPeriodique(date1, date2) {
        def criteria = EntretienPeriodique.createCriteria()
        def result = criteria.get {
            projections {
                sum("montant")
            }
            between("dateEntretien", date1, date2)
            eq("paye", true)
        }
        if(result == null) {
            result = 0;
        }
        return result
    }
    
    def getDepensesBetween(date1, date2) {
        def criteria = EntretienPeriodique.createCriteria()
        def result = criteria.list {
            between("dateEntretien", date1, date2)
            eq("paye", true)
        }
        return result
    }
        
    def getDepensesVehicule(v) {
        def criteria = EntretienPeriodique.createCriteria()
        def result = criteria.list {
            eq("vehicule", v)
            eq("paye", true)
        }
        return result
    }
}