
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
 * Marque Window Object
 **/
class MarqueWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Marque
     **/
    def marqueService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class MarqueWindow
     **/
    private Log logger = LogFactory.getLog(MarqueWindow.class)
    
    /**
     * Constructeur
     **/
    public MarqueWindow () {
        super(Marque.class)
    }  

    protected SuperService getService() {
        return this.marqueService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Marques"
        def reportDef = new JasperReportDef(name:'rapport_des_Marques.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Marques.pdf"
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
        String titrerapport = "Rapport des Marques"
        def reportDef = new JasperReportDef(name:'rapport_des_Marques.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Marques.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
}

