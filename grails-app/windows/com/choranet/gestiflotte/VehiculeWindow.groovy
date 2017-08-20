
package com.choranet.gestiflotte
    

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Executions

import com.choranet.gestiflotte.SuperWindow
import com.choranet.gestiflotte.SuperService

/**
 * Vehicule Window Object
 **/
class VehiculeWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet FraisCirculationPeriodique
     **/
    def fraisCirculationPeriodiqueService
    /**
     * Service pour la gestion de l'objet EntretienPeriodique
     **/
    def entretienPeriodiqueService
    /**
     * Service pour la gestion de l'objet Intervenant
     **/
    def intervenantService
    /**
     * Service pour la gestion de l'objet Societe
     **/
    def societeService
    /**
     * Service pour la gestion de l'objet Vehicule
     **/
    def vehiculeService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class VehiculeWindow
     **/
    private Log logger = LogFactory.getLog(VehiculeWindow.class)
    
    /**
     * liste de proprietaireParticulier
     **/	
    def proprietaireParticuliers	
    /**
     * proprietaireParticulier  selectionn�
     **/
    def proprietaireParticulierSelected
                
    /**
     * liste de model
     **/	
    def models	
    /**
     * model  selectionn�
     **/
    def modelSelected
                
    /**
     * liste de prorprietaireSociete
     **/	
    def prorprietaireSocietes	
    /**
     * prorprietaireSociete  selectionn�
     **/
    def prorprietaireSocieteSelected
                
    /**
     * liste de categorie
     **/	
    def categories	
    /**
     * categorie  selectionn�
     **/
    def categorieSelected
                
    /**
     * liste de energie
     **/	
    def energies	
    /**
     * energie  selectionn�
     **/
    def energieSelected
    
    def listeSocietesExternes
    
    def excelImporterService
                
    /**
     * Constructeur
     **/
    public VehiculeWindow (intervenantService, societeService, vehiculeService) {
        super(Vehicule.class)
        this.intervenantService = intervenantService
        this.societeService = societeService
        
        initalisationParticuliaireAssoc()
        
        //        int compteur = 1
        //        this.vehiculeService = vehiculeService
        //        Vehicule.list().each { element ->
        //            element.code = 'v' + compteur
        //            vehiculeService.update(element)
        //            compteur += 1
        //        }
    }  

    protected SuperService getService() {
        return this.vehiculeService
    }
    
    def importation(media) {
        String resultat = excelImporterService.importerVehicules(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }
    
    def add() {
        super.addWithNoRefresh()
        if(objet.vehiculeDeService) {
            fraisCirculationPeriodiqueService.genererFraisCirculationPeriodique(objet)
            entretienPeriodiqueService.genererEntretienPeriodique(objet)
        }
        cancel()
        super.refresh()
    }
    
    def delete() {
        try {
            super.delete()
        }catch(Exception ex){
            logger.error "Suppression impossible : la véhicule en cours est liée à certains entretiens périodique ou frais de circulation périodiques ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + "Suppression impossible : la véhicule en cours est liée à certains entretiens périodique ou frais de circulation périodiques", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
    }
    
    /**
     * Activer ou dï¿½sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        this.getFellow("btnDelete").visible = visible
        //this.getFellow("btnCancel").visible = visible
        this.getFellow("btnPdf").visible = !visible
        this.getFellow("btnExcel").visible = !visible
        this.getFellow("btnSave").visible = !visible
        this.getFellow("btnNew").visible = !visible
        this.getFellow("westPanel").open = visible        
    }
    
    def cancel() {
        super.cancel()
        getFellow("vbchoix").visible = true
        getFellow("grcrud").visible = false
        getFellow("r1").checked = false
        try {
            getFellow("r2").checked = false
            getFellow("r3").checked = false
        }catch (Exception e){
            
        }
    }
    
    def select() {
        super.select()
        getFellow("vbchoix").visible = false
        getFellow("grcrud").visible = true
        if(objet.proprietaireParticulier != null) {
            getFellow("rwpp").visible = true
            getFellow("rwps").visible = false
        } else {
            getFellow("rwpp").visible = false
            getFellow("rwps").visible = true
        }
        if(objet.vehiculeDeService) {
            getFellow("rwdmec").visible = true
            //getFellow("rwk").visible = true
        } else {
            getFellow("rwdmec").visible = false
            //getFellow("rwk").visible = false
        }
    }
    
    def choixGenreVehicule(id) {
        switch(id) {
            case "r1" : 
            objet.vehiculeDeService = true
            prorprietaireSocietes = societeService.getListSocietesDuGroupe()
            if(prorprietaireSocietes.size() > 0) {
                prorprietaireSocieteSelected = prorprietaireSocietes.get(0)
            }
            proprietaireParticulierSelected = null
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwpp").visible = false
            getFellow("rwps").visible = true
            getFellow("rwdmec").visible = true
            //getFellow("rwk").visible = true
            new AnnotateDataBinder(this.getFellow("coprorprietaireSocietes")).loadAll()
            
            break
            case "r2" : 
            objet.vehiculeDeService = false
            prorprietaireSocietes = societeService.getListSocietesHorsGroup()
            if(prorprietaireSocietes.size() > 0) {
                prorprietaireSocieteSelected = prorprietaireSocietes.get(0)
            }
            proprietaireParticulierSelected = null
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwpp").visible = false
            getFellow("rwps").visible = true
            getFellow("rwdmec").visible = false
            //getFellow("rwk").visible = false
            new AnnotateDataBinder(this.getFellow("coprorprietaireSocietes")).loadAll()
            
            break
            case "r3" :
            objet.vehiculeDeService = false
            proprietaireParticuliers = intervenantService.getListIntervenantsParticuliers()
            if(proprietaireParticuliers.size() > 0) {
                proprietaireParticulierSelected = proprietaireParticuliers.get(0)
            }
            prorprietaireSocieteSelected = null
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwpp").visible = true
            getFellow("rwps").visible = false
            getFellow("rwdmec").visible = false
            //getFellow("rwk").visible = false
            new AnnotateDataBinder(this.getFellow("coproprietaireParticuliers")).loadAll()
            break
        }
    }
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Vehicules"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Vehicules.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Vehicules.pdf"
        //Filedownload.save(bit, "application/pdf", nom_fichier);
        Media amedia = new AMedia(nom_fichier, "pdf", "application/pdf", bit);
        Map map = new HashMap();
        map.put("content", amedia);
        Executions.createComponents("/zul/util/pdfviewer.zul", null, map).doModal();
    }
    /**
     * Generation du rapport excel
     **/
    def genererRapportExcel() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Vehicules"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Vehicules.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Vehicules.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initalisationParticuliaireAssoc() {
        
        proprietaireParticuliers = intervenantService.getListIntervenantsParticuliers()	
        
        listeSocietesExternes = societeService.getListSocietesHorsGroup()
            
        models = Model.list().sort{it.marque.toString()}		
        if(models.size() > 0)
        modelSelected = models.get(0)
        else
        modelSelected = null
                    
        prorprietaireSocietes = Societe.list().sort{it.raisonSociale}		
                    
        categories = Categorie.list().sort{it.libelle}		
        if(categories.size() > 0)
        categorieSelected = categories.get(0)
        else
        categorieSelected = null
                    
        energies = Energie.list()		
        if(energies.size() > 0)
        energieSelected = energies.get(0)
        else
        energieSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            proprietaireParticuliers = intervenantService.getListIntervenantsParticuliers()	
        }	
        
        proprietaireParticulierSelected = null
                    	
        if(del) {
            models = Model.list().sort{it.marque.toString()}		
        }	
        if(models.size() > 0)
        modelSelected = models.get(0)
        else
        modelSelected = null
                    	
        if(del) {
            prorprietaireSocietes = Societe.list().sort{it.raisonSociale}		
        }	
        
        prorprietaireSocieteSelected = null
                    	
        if(del) {
            categories = Categorie.list().sort{it.libelle}		
        }	
        if(categories.size() > 0)
        categorieSelected = categories.get(0)
        else
        categorieSelected = null
                    	
        if(del) {
            energies = Energie.list()
        }	
        if(energies.size() > 0)
        energieSelected = energies.get(0)
        else
        energieSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.proprietaireParticulier = proprietaireParticulierSelected
        
        proprietaireParticulierSelected = null
                    		
        objet.model = modelSelected
        if(models.size() > 0) {
            def bindermodel = new AnnotateDataBinder(this.getFellow("comodels"))
            modelSelected = models.get(0)
            bindermodel.loadAll()
        }
        else
        modelSelected = null
                    		
        objet.prorprietaireSociete = prorprietaireSocieteSelected
        prorprietaireSocietes = Societe.list()
        
        prorprietaireSocieteSelected = null
                    		
        objet.categorie = categorieSelected
        if(categories.size() > 0) {
            def bindercategorie = new AnnotateDataBinder(this.getFellow("cocategories"))
            categorieSelected = categories.get(0)
            bindercategorie.loadAll()
        }
        else
        categorieSelected = null
                    		
        objet.energie = energieSelected
        if(energies.size() > 0) {
            def binderenergie = new AnnotateDataBinder(this.getFellow("coenergies"))
            energieSelected = energies.get(0)
            binderenergie.loadAll()
        }
        else
        energieSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        if(objet.vehiculeDeService) {
            def binderprorprietaireSociete = new AnnotateDataBinder(this.getFellow("coprorprietaireSocietes"))
            prorprietaireSocieteSelected = prorprietaireSocietes.find{ it.id == Vehicule.findById(objet.id).prorprietaireSociete.id }
            binderprorprietaireSociete.loadAll()
        } else if (objet.proprietaireParticulier == null) {
            def binderprorprietaireSociete = new AnnotateDataBinder(this.getFellow("coprorprietaireSocietes"))
            prorprietaireSocieteSelected = prorprietaireSocietes.find{ it.id == Vehicule.findById(objet.id).prorprietaireSociete.id }
            binderprorprietaireSociete.loadAll()
        } else {
            def binderproprietaireParticulier = new AnnotateDataBinder(this.getFellow("coproprietaireParticuliers"))
            proprietaireParticulierSelected = proprietaireParticuliers.find{ it.id == Vehicule.findById(objet.id).proprietaireParticulier.id }
            binderproprietaireParticulier.loadAll()
        }
                    		
        def bindermodel = new AnnotateDataBinder(this.getFellow("comodels"))
        modelSelected = models.find{ it.id == Vehicule.findById(objet.id).model.id }
        bindermodel.loadAll()
                    				
        def bindercategorie = new AnnotateDataBinder(this.getFellow("cocategories"))
        categorieSelected = categories.find{ it.id == Vehicule.findById(objet.id).categorie.id }
        bindercategorie.loadAll()
                    		
        def binderenergie = new AnnotateDataBinder(this.getFellow("coenergies"))
        energieSelected = energies.find{ it.id == Vehicule.findById(objet.id).energie.id }
        binderenergie.loadAll()
                    
    }
}

