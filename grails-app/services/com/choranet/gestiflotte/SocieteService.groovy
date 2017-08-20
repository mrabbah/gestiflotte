
package com.choranet.gestiflotte;


/**
 * SocieteService Service pour la gestion des opérations
 * transactionnelles pour l'objet Societe
 */
class SocieteService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Societe.class).sort{it.raisonSociale}
        }
        
        def getListSocietesDuGroupe() {
            def criteria = Societe.createCriteria()
            def result = criteria.list {
                eq("societeDuGroup", true)
            }
            return result.sort{it.raisonSociale}
        }
        
        def getListSocietesHorsGroup() {
            def criteria = Societe.createCriteria()
            def result = criteria.list {
                eq("societeDuGroup", false)
            }
            return result.sort{it.raisonSociale}
        }
}