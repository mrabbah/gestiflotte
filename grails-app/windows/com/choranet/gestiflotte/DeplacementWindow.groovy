
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


/**
 * Deplacement Window Object
 **/
class DeplacementWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Deplacement
     **/
    def deplacementService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class DeplacementWindow
     **/
    private Log logger = LogFactory.getLog(DeplacementWindow.class)
    
    /**
     * liste de personnel
     **/	
    def personnels	
    /**
     * personnel  selectionn�
     **/
    def personnelSelected
                
    /**
     * liste de voitureService
     **/	
    def voitureServices	
    
    /**
     * voitureService  selectionn�
     **/
    def voitureServiceSelected
          
    /**
     * Service pour la gestion de l'objet vehicule
     **/      
    def vehiculeService
    
    /**
     * Service pour la gestion de l'objet intervenant
     **/
    def intervenantService
    /**
     * Constructeur
     **/
    public DeplacementWindow (vehiculeService, intervenantService) {
        super(Deplacement.class,10)
        this.vehiculeService = vehiculeService
        this.intervenantService = intervenantService
        specialeInitialisation()
    }  

    protected SuperService getService() {
        return this.deplacementService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Deplacements"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Deplacements.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Deplacements.pdf"
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
        String titrerapport = "Rapport des Deplacements"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Deplacements.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Deplacements.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def specialeInitialisation() {
        
        personnels = intervenantService.getListIntervenantsSocieteDuGroupe()	
        if(personnels.size() > 0)
        personnelSelected = personnels.get(0)
        else
        personnelSelected = null
                    
        voitureServices = vehiculeService.getListVoituresDeService()
        if(voitureServices.size() > 0) {
            voitureServiceSelected = voitureServices.get(0)
            objet.kilometrageDepart = voitureServiceSelected.kilometrage
        }
        else {
            voitureServiceSelected = null
            objet.kilometrageDepart = null
        }
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            personnels = intervenantService.getListIntervenantsSocieteDuGroupe()
        }	
        if(personnels.size() > 0)
        personnelSelected = personnels.get(0)
        else
        personnelSelected = null
                    	
        if(del) {
            voitureServices = vehiculeService.getListVoituresDeService()
        }	
        if(voitureServices.size() > 0) {
            voitureServiceSelected = voitureServices.get(0)
            objet.kilometrageDepart = voitureServiceSelected.kilometrage
        }
        else{
            voitureServiceSelected = null
            objet.kilometrageDepart = null
        }
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.personnel = personnelSelected
        if(personnels.size() > 0) {
            def binderpersonnel = new AnnotateDataBinder(this.getFellow("copersonnels"))
            personnelSelected = personnels.get(0)
            binderpersonnel.loadAll()
        }
        else
        personnelSelected = null
                    		
        objet.voitureService = voitureServiceSelected
        if(voitureServices.size() > 0) {
            def bindervoitureService = new AnnotateDataBinder(this.getFellow("covoitureServices"))
            voitureServiceSelected = voitureServices.get(0)
            bindervoitureService.loadAll()
        }
        else {
            voitureServiceSelected = null
            objet.kilometrageDepart = null
        }
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def binderpersonnel = new AnnotateDataBinder(this.getFellow("copersonnels"))
        personnelSelected = personnels.find{ it.id == Deplacement.findById(objet.id).personnel.id }
        binderpersonnel.loadAll()
                    		
        def bindervoitureService = new AnnotateDataBinder(this.getFellow("covoitureServices"))
        voitureServiceSelected = voitureServices.find{ it.id == Deplacement.findById(objet.id).voitureService.id }
        if(objetSelected == null) {
            objet.kilometrageDepart = voitureServiceSelected.kilometrage
        }
        bindervoitureService.loadAll()
                    
    }
    
    /**
     * Fonction qui se charge de sauveguarder un nouveau �l�ment de article
     **/
    def add() {
        try {
            def isDeplacementValid = deplacementService.validatedDeplacement(objet, voitureServiceSelected)
            if  (isDeplacementValid) {
                if(objet.kilometrageRetour != null && voitureServiceSelected.kilometrage < objet.kilometrageRetour) {
                    voitureServiceSelected.kilometrage = objet.kilometrageRetour
                    vehiculeService.update(voitureServiceSelected)
                }
                // au lieu de super.add()
                actualiserValeurAssociation()
                try {     
                    getService().save(objet)
                } catch(Exception ex) {
                    logger.error "Error: ${ex.message}", ex
                    Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
                } finally {
                    objet = clazz.newInstance()
                    objet.kilometrageDepart = voitureServiceSelected.kilometrage
                    
                    rafraichirField()
                    rafraichirList()
                    activerBoutons(false)
                }
                //fin au lieu super.add()
            } else {
                Messagebox.show("Echec lors de l'ajout d'un déplacement : véhicule déjà mise en service pendant cette période \n[" +objet.dateDepart +" , "+ objet.dateRetour+"] \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
            
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        } finally {
            
        }
        //reinitialiserAssociation()
    }
    
    /**
     * Fonction qui se charge de mettre � jour un �l�ment selectionn� de article
     **/
    def update() {
        try {
            def isDeplacementValid = deplacementService.validatedDeplacement(objet, voitureServiceSelected)
            if  (isDeplacementValid) {
                if(objet.kilometrageRetour != null  && objet.voitureService.kilometrage < objet.kilometrageRetour) {
                    objet.voitureService.kilometrage = objet.kilometrageRetour
                    vehiculeService.update(objet.voitureService)
                }
                super.update()
            }  else {
                Messagebox.show("Echec lors de la mise à jour d'un déplacement : véhicule déjà mise en service pendant cette période \n[" +objet.dateDepart +" , "+ objet.dateRetour+"] \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                objet.dateDepart = null
                objet.dateRetour = null
                rafraichirField()
                rafraichirList()
            }
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Probleme lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
        }
        reinitialiserAssociation()
    }
    /**
     * Fonction qui se charge de supprimer un �l�ment selectinn� de article
     **/
    def delete() {
        if(objet.voitureService.kilometrage == objet.kilometrageRetour) {
            objet.voitureService.kilometrage = objet.kilometrageDepart
            vehiculeService.update(objet.voitureService)
        }
        super.delete()
  
    }
    
    /**
     * Pour annuler la modification ou la supression et pour basculer en mode ajout d'un nouveau �l�ment
     **/
    def cancel() {        
        super.cancel()
        actualiserValeurAssociation()
    }
    
         
    /**
     * Activer ou d�sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        this.getFellow("btnDelete").visible = visible
        this.getFellow("btnCancel").visible = visible
        this.getFellow("btnSave").visible = !visible

        this.getFellow("westPanel").open = visible  

    }
    
    def changerDeVoiture() {
        objet.kilometrageDepart = voitureServiceSelected.kilometrage
        objet.kilometrageRetour = null
        
        def coKilometrageDepart = this.getFellow("fieldKilometrageDepart")
        def coKilometrageRetour = this.getFellow("fieldKilometrageRetour")
        def binder1 = new AnnotateDataBinder(coKilometrageDepart)
        binder1.loadAll()
        def binder2 = new AnnotateDataBinder(coKilometrageRetour)
        binder2.loadAll()
    }
}

