
package com.choranet.gestiflotte;


/**
 * UniteDeMesureServiceService Service pour la gestion des opérations
 * transactionnelles pour l'objet UniteDeMesureService
 */
class UniteDeMesureService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(UniteDeMesure.class)
        }
}