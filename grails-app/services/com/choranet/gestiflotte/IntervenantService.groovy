
package com.choranet.gestiflotte;


/**
 * IntervenantService Service pour la gestion des opérations
 * transactionnelles pour l'objet Intervenant
 */
class IntervenantService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Intervenant.class)
        }
        
        def getListIntervenantsParticuliers() {
            def criteria = Intervenant.createCriteria()
            def result = criteria.list {
                eq("sousTraite", true)
                isNull("employer")
            }
            return result.sort{it.prenom}
        }
        
        def getListIntervenantsSocieteDuGroupe() {
            def criteria = Intervenant.createCriteria()
            def result = criteria.list {
                eq("sousTraite", false)
            }
            return result.sort{it.prenom}
        }
        
        def getListIntervenantsSocietesPrestations() {
            def criteria = Intervenant.createCriteria()
            def result = criteria.list {
                eq("sousTraite", true)
                isNotNull("employer")
            }
            return result.sort{it.prenom}
        }
}