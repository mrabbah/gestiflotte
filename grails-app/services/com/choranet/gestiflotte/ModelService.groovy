
package com.choranet.gestiflotte;


/**
 * ModelService Service pour la gestion des opérations
 * transactionnelles pour l'objet Model
 */
class ModelService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Model.class)
        }
}