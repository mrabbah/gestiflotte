
package com.choranet.gestiflotte;


/**
 * EntretienService Service pour la gestion des opérations
 * transactionnelles pour l'objet Entretien
 */
class EntretienService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Entretien.class)
    }
    
    def getListeEntretientsNonPayeParCheque() {
        def cheques = PaiementGroup.list()
        def ids = cheques*.entretiens*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        def criteria2 = Entretien.createCriteria()
        def epNonPayes = criteria2.list {
            if(idds.size() > 0) {
                not{"in"("id", idds)}
            }
        }
        return epNonPayes
    }
        
    def concatListeEntretientsNonPayeParCheque(liste) {
        liste << getListeEntretientsNonPayeParCheque()
        return liste
    }
    
    def getFraisEntretien(date1, date2) {
        def criteria = Entretien.createCriteria()
        def result = criteria.get {
            projections {
                sum("montant")
            }
            between("dateEntretien", date1, date2)
        }
        if(result == null) {
            result = 0;
        }
        return result
    }
    
    def getDepensesBetween(date1, date2) {
        def criteria = Entretien.createCriteria()
        def result = criteria.list {
            between("dateEntretien", date1, date2)
        }
        return result
    }
        
    def getDepensesVehicule(v) {
        def criteria = Entretien.createCriteria()
        def result = criteria.list {
            eq("vehicule", v)
        }
        return result
    }
}