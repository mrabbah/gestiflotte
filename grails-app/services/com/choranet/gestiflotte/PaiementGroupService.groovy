
package com.choranet.gestiflotte;


/**
 * PaiementGroupService Service pour la gestion des opérations
 * transactionnelles pour l'objet PaiementGroup
 */
class PaiementGroupService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(PaiementGroup.class)
        }
        
        def getPaiementGroupBetween(date1, date2) {
        def criteria = PaiementGroup.createCriteria()
        def result = criteria.list {
            between("date", date1, date2)
            societeGroup{
                order("raisonSociale","asc")
            }
            order("numCheque","asc")
        }
        return result
    }
}