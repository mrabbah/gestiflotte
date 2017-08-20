
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
 * Entretien Window Object
 **/
class TrajetWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Trajet
     **/
    def trajetService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    
    def excelImporterService
    
    /**
     * Logger de la class EntretienWindow
     **/
    private Log logger = LogFactory.getLog(TrajetWindow.class)
                
    /**
     * Constructeur
     **/
    public TrajetWindow () {
        super(Trajet.class, 15)
    }  

    def importation(media) {
        String resultat = excelImporterService.importerTrajets(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    } 
    
    protected SuperService getService() {
        return this.trajetService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Trajet"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_trajets.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Trajets.pdf"
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
        String titrerapport = "Rapport des Trajets"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Trajets.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Trajets.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
}

