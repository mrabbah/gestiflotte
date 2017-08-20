
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
 * Model Window Object
 **/
class ModelWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Model
     **/
    def modelService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class ModelWindow
     **/
    private Log logger = LogFactory.getLog(ModelWindow.class)
    
    /**
     * liste de marque
     **/	
    def marques	
    /**
     * marque  selectionn�
     **/
    def marqueSelected
                
    /**
     * Constructeur
     **/
    public ModelWindow () {
        super(Model.class)
    }  

    protected SuperService getService() {
        return this.modelService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Models"
        def reportDef = new JasperReportDef(name:'rapport_des_Models.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Models.pdf"
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
        String titrerapport = "Rapport des Models"
        def reportDef = new JasperReportDef(name:'rapport_des_Models.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Models.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        marques = Marque.list().sort{it.libelle}		
        if(marques.size() > 0)
        marqueSelected = marques.get(0)
        else
        marqueSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            marques = Marque.list()
        }	
        if(marques.size() > 0)
        marqueSelected = marques.get(0)
        else
        marqueSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.marque = marqueSelected
        if(marques.size() > 0) {
            def bindermarque = new AnnotateDataBinder(this.getFellow("comarques"))
            marqueSelected = marques.get(0)
            bindermarque.loadAll()
        }
        else
        marqueSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindermarque = new AnnotateDataBinder(this.getFellow("comarques"))
        marqueSelected = marques.find{ it.id == Model.findById(objet.id).marque.id }
        bindermarque.loadAll()
                    
    }
}

