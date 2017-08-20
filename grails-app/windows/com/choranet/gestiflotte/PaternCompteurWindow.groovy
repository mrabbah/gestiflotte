
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
 * PaternCompteur Window Object
 **/
class PaternCompteurWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet PaternCompteur
     **/
    def paternCompteurService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class PaternCompteurWindow
     **/
    private Log logger = LogFactory.getLog(PaternCompteurWindow.class)
    
    
    def isUpdate
    
    /**
     * Constructeur
     **/
    public PaternCompteurWindow () {
        super(PaternCompteur.class)
    }  

    protected SuperService getService() {
        return this.paternCompteurService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des PaternCompteurs"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_PaternCompteurs.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PaternCompteurs.pdf"
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
        String titrerapport = "Rapport des PaternCompteurs"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_PaternCompteurs.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PaternCompteurs.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        
    }
    
    /**
     *  Cette fonction est appeliée lorsque un élement de la liste est selectionné
     **/
    def select() {  
        isUpdate = true
        objet = objetSelected
        afficherValeurAssociation()
        rafraichirField()
    }
    
    def addOrUpdate(){
        if (isUpdate) {
            this.update()
            isUpdate = false
        } else {
            this.add()
        }
    }
    
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
        }
        
    }
    
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
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
        }
        
    }
    
    def delete() {
        getService().delete(objet)
        objet = clazz.newInstance()
        rafraichirField()
        rafraichirList()
        reinitialiserAssociation(true)
    }
}