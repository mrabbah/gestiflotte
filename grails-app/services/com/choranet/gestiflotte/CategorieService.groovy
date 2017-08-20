
package com.choranet.gestiflotte;


/**
 * CategorieService Service pour la gestion des opérations
 * transactionnelles pour l'objet Categorie
 */
class CategorieService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Categorie.class)
        }
}