
package com.choranet.gestiflotte;


/**
 * EnergieService Service pour la gestion des opérations
 * transactionnelles pour l'objet Energie
 */
class EnergieService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Energie.class)
        }
}