
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
 * PaiementClient Window Object
 **/
class PaiementClientWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet PaiementClient
     **/
    def paiementClientService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class PaiementClientWindow
     **/
    private Log logger = LogFactory.getLog(PaiementClientWindow.class)
    
    /**
     * liste de facturation
     **/	
    def facturations	
    /**
     * facturation  selectionn�
     **/
    def facturationSelected
                
    /**
     * Constructeur
     **/
    public PaiementClientWindow () {
        super(PaiementClient.class)
    }  

    protected SuperService getService() {
        return this.paiementClientService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des PaiementClients"
        def reportDef = new JasperReportDef(name:'rapport_des_PaiementClients.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PaiementClients.pdf"
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
        String titrerapport = "Rapport des PaiementClients"
        def reportDef = new JasperReportDef(name:'rapport_des_PaiementClients.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PaiementClients.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        facturations = Facturation.list()		
        if(facturations.size() > 0)
        facturationSelected = facturations.get(0)
        else
        facturationSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            facturations = Facturation.list()
        }	
        if(facturations.size() > 0)
        facturationSelected = facturations.get(0)
        else
        facturationSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.facturation = facturationSelected
        if(facturations.size() > 0) {
            def binderfacturation = new AnnotateDataBinder(this.getFellow("cofacturations"))
            facturationSelected = facturations.get(0)
            binderfacturation.loadAll()
        }
        else
        facturationSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def binderfacturation = new AnnotateDataBinder(this.getFellow("cofacturations"))
        facturationSelected = facturations.find{ it.id == PaiementClient.findById(objet.id).facturation.id }
        binderfacturation.loadAll()
                    
    }
}

