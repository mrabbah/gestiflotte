
package com.choranet.gestiflotte;


/**
 * BonFraisCirculationService Service pour la gestion des opérations
 * transactionnelles pour l'objet BonFraisCirculation
 */
class BonFraisCirculationService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(BonFraisCirculation.class)
    }
        
    def getListeBonsFraisCirculationNonPayeParCheque() {
        
        def cheques = PaiementGroup.list()
        def ids = cheques*.bonFraisCirculations*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        def criteria2 = BonFraisCirculation.createCriteria()
        def bfcNonPayes = criteria2.list {
            if(idds.size() > 0) {
                not{"in"("id", idds)}
            }
        }
        return bfcNonPayes
    }
        
    def concatListeBonsFraisCirculationNonPayeParCheque(liste) {
        liste << getListeBonsFraisCirculationNonPayeParCheque()
        return liste
    }
    
    def getFraisCirculation(date1, date2) {
        def criteria = BonFraisCirculation.createCriteria()
        def result = criteria.get {
            projections {
                sum("montant")
            }
            between("date", date1, date2)
        }
        if(result == null) {
            result = 0;
        }
        return result
    }
    
    def getDepensesBetween(date1, date2) {
        def criteria = BonFraisCirculation.createCriteria()
        def result = criteria.list {
            between("date", date1, date2)
        }
        return result
    }
        
    def getDepensesVehicule(v) {
        def criteria = BonFraisCirculation.createCriteria()
        def result = criteria.list {
            or {
                deplacement {
                    eq("voitureService", v)
                }
                intervention {
                    eq("vehicule", v)
                }
            }
        }
        return result
    }
    
    def getChargeEnUM(montantEnDirham, uniteMesure){
        def criteria = UniteDeMesure.createCriteria()
        def result = criteria.get {
            eq("idUnite", uniteMesure)
        }
        if (result.valeur != null && result.valeur != 0d)
            return montantEnDirham /result.valeur
        else return 0d    
    }
}