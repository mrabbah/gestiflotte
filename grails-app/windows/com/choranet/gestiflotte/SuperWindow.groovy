package com.choranet.gestiflotte

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

//import org.codehaus.groovy.grails.plugins.springsecurity.Secured

/**
 * Super Window Object
 **/
//@Secured(['ROLE_ROOT'])
abstract class SuperWindow extends Window {
    
    /**
     * la classe fille de la super window :)
     * */
    def clazz
    /**
     * Nom de la classe fille
     **/
    def nomClassFille
    /**
     * Objet qui va contenir le filtre a utiliser
     * */
    def filtre
    /**
     * En cas ou on veut appliquer le filtre seulement sur certains attributs
     * */
    def attributsAFiltrer
    /**
     * Logger de la class ArticleWindow
     **/
    private Log logger = LogFactory.getLog(SuperWindow.class)

    /**
     * liste des objets
     **/
    def listeObjets
    /**
     * article selectionnï¿½
     **/
    def objetSelected
    /**
     * un nouveau element objet  
     **/
    def objet

    /**
     * Taille de liste extraite de la BdD
     * */
    def tailleListe
    
    /**
     * offset de la requete
     * */
    def ofs
    
    /**
     * nombre maximum a extraire de la BdD
     * */
    def maxNb
    /**
     * l element sur lequel va s effectuer le trie
     * */
    def sortedHeader = ""
    
    /**
     * direction du trie
     * */
    def sortedDirection = ""
    
    /**
     * Constructeur
     **/
    public SuperWindow (clazz, maxNb, attributsAFiltrer) throws Exception {
        initialisation(clazz, maxNb, attributsAFiltrer)
    }   
    
    /**
     * Constructeur
     **/
    public SuperWindow (clazz, maxNb) throws Exception {
        initialisation(clazz, maxNb, null)
    }   
    
    /**
     * Constructeur
     **/
    public SuperWindow (clazz) throws Exception {
        initialisation(clazz, 14, null)
    }   
    
    private void initialisation(clazz, maxNb, attributsAFiltrer) throws Exception {
        try {            
            this.clazz = clazz
            this.maxNb = maxNb
            this.attributsAFiltrer = attributsAFiltrer
            this.nomClassFille = clazz.getSimpleName()
            ofs = 0            
            listeObjets = clazz.list(offset:ofs, max:maxNb)
            def criteria = clazz.createCriteria()
            tailleListe = criteria.count{} 
            objetSelected = null   
            objet = clazz.newInstance()
            filtre = clazz.newInstance()
            initialiserAssociation()
        } catch(Exception e) {
            logger.error(e)
            throw e
        }
    }
    def filtrer() {
        try {
            def map
            if(attributsAFiltrer == null) {
                map = getService().filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb)
            } else {
                map = getService().filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection, ofs, maxNb)
            }
            tailleListe = map["tailleListe"]
            listeObjets = map["listeObjets"]
            def binder = new AnnotateDataBinder(this.getFellow("lstObjet"))
            binder.loadAll()
        
            //def binder2 = new AnnotateDataBinder(this.getFellow("paging"))
            //binder2.loadAll()    
        } catch (Exception ex) {
            logger.error(ex)
        }        
    }
    
    def getObjetsToExport() {
        try {
            if(attributsAFiltrer == null) {
                return getService().filtrer(clazz, filtre, sortedHeader, sortedDirection)
            } else {
                return getService().filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection)
            }
        } catch (Exception ex) {
            logger.error(ex)
        }        
    }
    
    def sort(event) {
        sortedHeader = event.getTarget().getId()
        def sortDirection =  event.getTarget().getSortDirection()
        sortedDirection = "desc"

        if(sortDirection == "natural" || sortDirection == "descending") {
            sortedDirection = "asc"
        }
        
        filtrer()
    }
    
    def getNextElements(event) {
        def pgno = event.getActivePage()
        ofs = pgno * event.getPageable().getPageSize()
        
        filtrer()
    }
    
    /**
     *  Cette fonction est appelï¿½e lorsque un ï¿½lï¿½ment de la liste est selectionnï¿½
     **/
    def select() {                    
        objet = objetSelected	
        afficherValeurAssociation()
        //article.lock()  //Ne peut etre utilisï¿½ que pour le base de donnï¿½e qui accepte le veruillage des enregisterments
        rafraichirField()
        activerBoutons(true)
    }
    /**
     * Fonction qui se charge de sauveguarder un nouveau ï¿½lï¿½ment de article
     **/
    def add() {
	actualiserValeurAssociation()
        try {     
            getService().save(objet)
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        } finally {
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
            activerBoutons(false)
        }
    }
    
    def addWithNoRefresh() {
	actualiserValeurAssociation()
        try {
            getService().save(objet)
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        } 
    }
    
    def refresh() {
        objet = clazz.newInstance()
        rafraichirField()
        rafraichirList()
        activerBoutons(false)
    }
    
    /**
     * Pour annuler la modification ou la supression et pour basculer en mode ajout d'un nouveau ï¿½lï¿½ment
     **/
    def cancel() {        
        objet = clazz.newInstance()
	reinitialiserAssociation(false)
        rafraichirField()
        activerBoutons(false)
        annulerSelection()
    }
    /**
     * Fonction qui se charge de la mise à jour
     **/
    def update() {
	actualiserValeurAssociation()
        try {
            getService().update(objet)
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
            activerBoutons(false)
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
        }
    }
    
    def updateWithNoRefresh() {
	actualiserValeurAssociation()
        try {
            getService().update(objet)
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        } 
    }
    
    /**
     * Fonction qui se charge de supprimer un ï¿½lï¿½ment selectinnï¿½ de article
     **/
    def delete() {
        getService().delete(objet)
        activerBoutons(false)
        objet = clazz.newInstance()
        rafraichirField()
        rafraichirList()
        reinitialiserAssociation(true)
    }
    /**
     * Permet d'afficher l'anglet d'ajout d'un nouveau article
     **/
    def newRecord(){
        this.getFellow("westPanel").open = visible        
    }
    /**
     * Activer ou dï¿½sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        this.getFellow("btnDelete").visible = visible
        this.getFellow("btnCancel").visible = visible
        this.getFellow("btnPdf").visible = !visible
        this.getFellow("btnExcel").visible = !visible
        this.getFellow("btnSave").visible = !visible
        this.getFellow("btnNew").visible = !visible
        this.getFellow("westPanel").open = visible        
    }
    /**
     * Rï¿½initialiser les champs du formulaire
     **/
    def rafraichirField() {
        this.getFellows().each { co ->
            if(co.getId() != null && co.getId().startsWith("field")) {
                def binder = new AnnotateDataBinder(co)
                binder.loadAll()
            }
        }               
    }
    
    /**
     * Rafrichier la liste des article
     **/
    def rafraichirList() {
        filtrer()		
        annulerSelection()
    }
    /**
     * Basculer en mode saisi d'un nouveau ï¿½lï¿½ment
     **/
    def annulerSelection() {
        this.getFellow("lstObjet").clearSelection()
        objetSelected = null
    }

    protected abstract SuperService getService();
    
    def initialiserAssociation() {}
    def afficherValeurAssociation() {}
    def reinitialiserAssociation(test) {}
    def actualiserValeurAssociation() {}
}

