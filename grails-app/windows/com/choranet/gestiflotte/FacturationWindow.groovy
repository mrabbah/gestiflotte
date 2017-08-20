
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
 * Facturation Window Object
 **/
class FacturationWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Facturation
     **/
    def facturationService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class FacturationWindow
     **/
    private Log logger = LogFactory.getLog(FacturationWindow.class)
    
    /**
     * liste de interventions
     **/
    def interventions	
    /**
     * interventions  selectionn�
     **/
    def interventionsSelected
                
    /**
     * liste de perstataireGroup
     **/	
    def perstataireGroups	
    /**
     * perstataireGroup  selectionn�
     **/
    def perstataireGroupSelected
                
    /**
     * Constructeur
     **/
    public FacturationWindow () {
        super(Facturation.class)
    }  

    protected SuperService getService() {
        return this.facturationService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Facturations"
        def reportDef = new JasperReportDef(name:'rapport_des_Facturations.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Facturations.pdf"
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
        String titrerapport = "Rapport des Facturations"
        def reportDef = new JasperReportDef(name:'rapport_des_Facturations.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Facturations.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        interventions = Intervention.list()
        interventionsSelected = null// = new ArrayList()
                    
        perstataireGroups = Societe.list()		
        if(perstataireGroups.size() > 0)
        perstataireGroupSelected = perstataireGroups.get(0)
        else
        perstataireGroupSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            interventions = Intervention.list()
        }
        this.getFellow("lstinterventions").clearSelection()
        interventionsSelected = null// = new ArrayList()
                    	
        if(del) {
            perstataireGroups = Societe.list()
        }	
        if(perstataireGroups.size() > 0)
        perstataireGroupSelected = perstataireGroups.get(0)
        else
        perstataireGroupSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        
        objet.interventions = interventionsSelected
        this.getFellow("lstinterventions").clearSelection()
        interventionsSelected = null// = new ArrayList()
                    		
        objet.perstataireGroup = perstataireGroupSelected
        if(perstataireGroups.size() > 0) {
            def binderperstataireGroup = new AnnotateDataBinder(this.getFellow("coperstataireGroups"))
            perstataireGroupSelected = perstataireGroups.get(0)
            binderperstataireGroup.loadAll()
        }
        else
        perstataireGroupSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        
        def binderinterventions = new AnnotateDataBinder(this.getFellow("lstinterventions"))
        interventionsSelected = facturationSelected.interventions
        binderinterventions.loadAll()
                    		
        def binderperstataireGroup = new AnnotateDataBinder(this.getFellow("coperstataireGroups"))
        perstataireGroupSelected = perstataireGroups.find{ it.id == Facturation.findById(objet.id).perstataireGroup.id }
        binderperstataireGroup.loadAll()
                    
    }
}

