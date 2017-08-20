package com.choranet.gestiflotte


/**
 * TrajetService Service pour la gestion des opérations
 * transactionnelles pour l'objet Trajet
 */
class TrajetService extends SuperService {

    static transactional = true

    def list() throws Exception {
            return super.list(Trajet.class)
        }
}
