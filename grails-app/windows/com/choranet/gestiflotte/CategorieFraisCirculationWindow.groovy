
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
 * CategorieFraisCirculation Window Object
 **/
class CategorieFraisCirculationWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet CategorieFraisCirculation
     **/
    def categorieFraisCirculationService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class CategorieFraisCirculationWindow
     **/
    private Log logger = LogFactory.getLog(CategorieFraisCirculationWindow.class)
    
    def uniteMesures
    def uniteMesureSelected
    
    /**
     * Constructeur
     **/
    public CategorieFraisCirculationWindow () {
        super(CategorieFraisCirculation.class)
    }  

    protected SuperService getService() {
        return this.categorieFraisCirculationService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des CategorieFraisCirculations"
        def reportDef = new JasperReportDef(name:'rapport_des_CategorieFraisCirculations.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_CategorieFraisCirculations.pdf"
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
        String titrerapport = "Rapport des CategorieFraisCirculations"
        def reportDef = new JasperReportDef(name:'rapport_des_CategorieFraisCirculations.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_CategorieFraisCirculations.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
     def initialiserAssociation() {
        uniteMesures = UniteDeMesure.list().sort{it.idUnite}
        uniteMesureSelected = null// = new ArrayList()
    }
    /**
     * Fonction qui permet de ré-initaliser l'association au niveau de l'interface
     * @param del si c'est une réinitionalisation aprés une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            uniteMesures = UniteDeMesure.list().sort{it.idUnite}
        }
        //this.getFellow("coUniteMesure").clearSelection()
        uniteMesureSelected = null// = new ArrayList()
        
    }
    
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        
        objet.uniteMesure = uniteMesureSelected
        //this.getFellow("coUniteMesure").clearSelection()
        uniteMesureSelected = null// = new ArrayList() 
        
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        
        def binderuniteMesures = new AnnotateDataBinder(this.getFellow("coUniteMesure"))
        uniteMesureSelected = uniteMesures.find{ it.id == CategorieFraisCirculation.findById(objet.id).uniteMesure.id }
        //objetSelected.uniteMesure
        binderuniteMesures.loadAll()
       
    }
    
}

