
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
 * UniteDeMesure Window Object
 **/
class UniteDeMesureWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet UniteDeMesure
     **/
    def uniteDeMesureService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class CategorieFraisCirculationWindow
     **/
    private Log logger = LogFactory.getLog(UniteDeMesureWindow.class)
    
    /**
     * Constructeur
     **/
    public UniteDeMesureWindow () {
        super(UniteDeMesure.class)
    }  

    protected SuperService getService() {
        return this.uniteDeMesureService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Unités de mesure"
        def reportDef = new JasperReportDef(name:'rapport_Unites_de_Mesure.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Unite_de_Mesure.pdf"
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
        String titrerapport = "Rapport des des Unités de mesure"
        def reportDef = new JasperReportDef(name:'rapport_Unites_de_Mesure.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_Unites_de_Mesure.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
}

