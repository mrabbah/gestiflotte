
package com.choranet.gestiflotte;


/**
 * SocieteService Service pour la gestion des opérations
 * transactionnelles pour l'objet Fournisseur
 */
class FournisseurService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Fournisseur.class)
        }
        
}