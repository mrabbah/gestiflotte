
package com.choranet.gestiflotte;


/**
 * MarqueService Service pour la gestion des opérations
 * transactionnelles pour l'objet Marque
 */
class MarqueService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Marque.class)
        }
}