package com.choranet.commun

import cr.co.arquetipos.crypto.Blowfish
import com.choranet.gestiflotte.SuperService
import java.text.SimpleDateFormat

class CleService extends SuperService {

    static transactional = true

    def save(Object object) throws Exception {
        def cb = getChoraBarrage()
        def cleEnClaire = Blowfish.decryptBase64(object.cleProduit, cb.idInstance)
        def valeurs = cleEnClaire.split("#")
        def valeur = new Integer(valeurs[5])
        if(valeur == -1) {
            object.dateFinActivation = "Jamais"
        } else {
            def dfa = object.dateActivation + valeur
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            object.dateFinActivation = sdf.format(dfa)
        }
        super.save(object)
        cb.demo = false
        update(cb)
    }
    
    def isCleValide(String cle) {
        def valeur = cle.trim()
        def dbCle = Cle.findByCleProduit(valeur)
        //Pas de cle identique trouve
        if(dbCle == null) {
            def cb = getChoraBarrage()
            def valeurEnClaire = Blowfish.decryptBase64(valeur, cb.idInstance)
            if(valeurEnClaire == null) {
                return false
            }
            def valeurs = valeurEnClaire.split("#")
            if(valeurs.size() == 7) {
                def idInstance = valeurs[0]
                def raisonsociale = valeurs[1]
                def produit = valeurs[2]
                def version = valeurs[3]
                if(cb.idInstance.equals(idInstance) && cb.produit.equals(produit) && cb.versionProduit.equals(version) && cb.raisonSocial.equals(raisonsociale)) {
                    return true
                }
            }
        }
        return false
    }
   
    private ChoraBarrage getChoraBarrage() {
        return ChoraBarrage.list()[0]
    }
}
