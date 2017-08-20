
package com.choranet.gestiflotte;


/**
 * CategorieFraisCirculationService Service pour la gestion des opérations
 * transactionnelles pour l'objet CategorieFraisCirculation
 */
class CategorieFraisCirculationService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(CategorieFraisCirculation.class)
        }
        
}