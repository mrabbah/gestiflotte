package com.choranet.gestiflotte

class DroitUtilisateurService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(DroitUtilisateur.class)
    }
}
