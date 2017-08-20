package com.choranet.gestiflotte


/**
 * RegionService Service pour la gestion des opérations
 * transactionnelles pour l'objet Region
 */
class RegionService extends SuperService {

    static transactional = true

    def list() throws Exception {
            return super.list(Region.class)
        }
}
