
package com.choranet.gestiflotte;


/**
 * TypeEntretienService Service pour la gestion des opérations
 * transactionnelles pour l'objet TypeEntretien
 */
class TypeEntretienService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(TypeEntretien.class)
        }
}