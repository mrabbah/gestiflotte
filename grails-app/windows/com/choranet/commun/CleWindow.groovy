
package com.choranet.commun


import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import com.choranet.gestiflotte.SuperWindow
import com.choranet.gestiflotte.SuperService
import org.zkoss.zk.ui.Executions

/**
 * Cle Window Object
 **/
class CleWindow extends SuperWindow {
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
    public CleWindow () {
        super(Cle.class)
    }  

    protected SuperService getService() {
        return this.cleService
    }
    
    def add() {
        if(cleService.isCleValide(objet.cleProduit)) {
            objet.dateActivation = choraBarrageService.getDateActivation()
            super.add()
            Executions.sendRedirect("/"); 
        } else {
            Messagebox.show("La clé entrée n'est pas valide", "Erreur",
                Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Activer ou d?sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnCancel").visible = visible
        this.getFellow("btnSave").visible = !visible
        this.getFellow("btnNew").visible = !visible
        this.getFellow("btnDemanderCle").visible = !visible
        this.getFellow("westPanel").open = visible        
    }
    
}

