
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
 * TypeEntretienPeriodique Window Object
 **/
class TypeEntretienPeriodiqueWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet TypeEntretienPeriodique
     **/
    def typeEntretienPeriodiqueService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class TypeEntretienPeriodiqueWindow
     **/
    private Log logger = LogFactory.getLog(TypeEntretienPeriodiqueWindow.class)
    
    /**
     * liste des categories des véhicules
     **/
    def categorieVehicules	
    /**
     * categorie véhicule  selectionné
     **/
    def categorieVehiculesSelected
    
    /**
     * Constructeur
     **/
    public TypeEntretienPeriodiqueWindow () {
        super(TypeEntretienPeriodique.class)
    }  

    protected SuperService getService() {
        return this.typeEntretienPeriodiqueService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des TypeEntretienPeriodiques"
        def reportDef = new JasperReportDef(name:'rapport_des_TypeEntretienPeriodiques.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_TypeEntretienPeriodiques.pdf"
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
        String titrerapport = "Rapport des TypeEntretienPeriodiques"
        def reportDef = new JasperReportDef(name:'rapport_des_TypeEntretienPeriodiques.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_TypeEntretienPeriodiques.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    def add() {
        super.addWithNoRefresh()
        if(objet.categorieVehicules != null) {
            typeEntretienPeriodiqueService.genererEntretienPeriodique(objet)
        }
        super.refresh()
    }
    
    def update() {
        def oldCategorieVehicules = objet.categorieVehicules
        //System.out.println 'oldCategorieVehicules : ' + oldCategorieVehicules
        super.updateWithNoRefresh()
        def newCategorieVehicules = objet.categorieVehicules
        //System.out.println 'newCategorieVehicules : ' + newCategorieVehicules
        if (newCategorieVehicules != null){
            if (oldCategorieVehicules != null){
                newCategorieVehicules = newCategorieVehicules.toList()
                oldCategorieVehicules = oldCategorieVehicules.toList()
                newCategorieVehicules.removeAll(oldCategorieVehicules)
            }
            //System.out.println 'newCategorieVehicules : ' + newCategorieVehicules
            if (newCategorieVehicules.size()>0) {
                //System.out.println 'generation ...'
                objet.categorieVehicules = newCategorieVehicules
                typeEntretienPeriodiqueService.genererEntretienPeriodique(objet)
            }
        }
        super.refresh()
    }
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        categorieVehicules = Categorie.list().sort{it.libelle}
        categorieVehiculesSelected = null// = new ArrayList()
        
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            categorieVehicules = Categorie.list().sort{it.libelle}
        }
        this.getFellow("lstcategorieVehicules").clearSelection()
        categorieVehiculesSelected = null// = new ArrayList()
                    	
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        
        objet.categorieVehicules = categorieVehiculesSelected
        this.getFellow("lstcategorieVehicules").clearSelection()
        categorieVehiculesSelected = null// = new ArrayList()
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        
        def binderinterventions = new AnnotateDataBinder(this.getFellow("lstcategorieVehicules"))
        categorieVehiculesSelected = objetSelected.categorieVehicules
        binderinterventions.loadAll()
                    		
    }
    
}

