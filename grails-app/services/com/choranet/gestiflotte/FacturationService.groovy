
package com.choranet.gestiflotte;


/**
 * FacturationService Service pour la gestion des opérations
 * transactionnelles pour l'objet Facturation
 */
class FacturationService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Facturation.class)
        }
}