
package com.choranet.commun


import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import com.choranet.gestiflotte.SuperWindow
import com.choranet.gestiflotte.SuperService

/**
 * Cle Window Object
 **/
class ClePopUpWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Cle
     **/
    def cleService
    
    def choraBarrageService
    
    
    /**
     * Logger de la class CleWindow
     **/
    private Log logger = LogFactory.getLog(CleWindow.class)
	
    /**
     * Constructeur
     **/
    public ClePopUpWindow () {
        super(Cle.class)
    }  

    protected SuperService getService() {
        return this.cleService
    }
    
    def add() {
        if(cleService.isCleValide(objet.cleProduit)) {
            objet.dateActivation = choraBarrageService.getDateActivation()
            super.add()
        } else {
            Messagebox.show("La clé entrée n'est pas valide", "Erreur",
                Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Activer ou d?sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {}
    /**
     * R?initialiser les champs du formulaire
     **/
    def rafraichirField() {}
    
  
    
}

