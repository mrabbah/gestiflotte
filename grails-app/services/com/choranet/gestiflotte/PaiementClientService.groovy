
package com.choranet.gestiflotte;


/**
 * PaiementClientService Service pour la gestion des opérations
 * transactionnelles pour l'objet PaiementClient
 */
class PaiementClientService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(PaiementClient.class)
        }
}