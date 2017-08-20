
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
 * Societe Window Object
 **/
class SocieteWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Societe
     **/
    def societeService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    
    def excelImporterService
    
    /**
     * Logger de la class SocieteWindow
     **/
    private Log logger = LogFactory.getLog(SocieteWindow.class)
    
    /**
     * Constructeur
     **/
    public SocieteWindow (societeService) {
        super(Societe.class,11)
        
//        int compteur = 1
//        this.societeService = societeService
//        Societe.list().each { element ->
//            element.code = 'S' + compteur
//            societeService.update(element)
//            compteur += 1
//        }
    }  
    
    def importation(media) {
        String resultat = excelImporterService.importerSocietes(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }


    protected SuperService getService() {
        return this.societeService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Societes"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Societes.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Societes.pdf"
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
        String titrerapport = "Rapport des Societes"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Societes.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Societes.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
}

