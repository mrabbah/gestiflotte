
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
 * FraisCirculationPeriodique Window Object
 **/
class FraisCirculationPeriodiqueWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet FraisCirculationPeriodique
     **/
    def fraisCirculationPeriodiqueService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class FraisCirculationPeriodiqueWindow
     **/
    private Log logger = LogFactory.getLog(FraisCirculationPeriodiqueWindow.class)
    
    /**
     * liste de vehicule
     **/	
    def vehicules	
    /**
     * vehicule  selectionn�
     **/
    def vehiculeSelected
                
    /**
     * liste de typeFraisCirculationPeriodique
     **/	
    def typeFraisCirculationPeriodiques	
    /**
     * typeFraisCirculationPeriodique  selectionn�
     **/
    def typeFraisCirculationPeriodiqueSelected
                
    def fournisseurs
    def fournisseurSelected
    
    /**
     * Constructeur
     **/
    public FraisCirculationPeriodiqueWindow () {
        super(FraisCirculationPeriodique.class,10)
    }  

    protected SuperService getService() {
        return this.fraisCirculationPeriodiqueService
    }
    
    /**
     * Activer ou dï¿½sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        //this.getFellow("btnDelete").visible = visible
        this.getFellow("btnCancel").visible = visible
        this.getFellow("btnPdf").visible = !visible
        this.getFellow("btnExcel").visible = !visible
        //this.getFellow("btnSave").visible = !visible
        //this.getFellow("btnNew").visible = !visible
        this.getFellow("westPanel").open = visible        
    }
    
    def select() {
        super.select()
        if(objet.paye) {
            getFellow("btnUpdate").visible = false
        }
    }
    
    def update() {
        def btnUpdateVisible = getFellow("btnUpdate").visible
        super.updateWithNoRefresh()
        if(btnUpdateVisible && objet.paye) {
            fraisCirculationPeriodiqueService.genererProachainFraisCirculationPeriodique(objet)
        }
        super.refresh()
    }
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des FraisCirculationPeriodiques"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_FraisCirculationPeriodiques.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_FraisCirculationPeriodiques.pdf"
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
        String titrerapport = "Rapport des FraisCirculationPeriodiques"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_FraisCirculationPeriodiques.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_FraisCirculationPeriodiques.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        vehicules = Vehicule.list()		
        if(vehicules.size() > 0)
        vehiculeSelected = vehicules.get(0)
        else
        vehiculeSelected = null
                    
        typeFraisCirculationPeriodiques = TypeFraisCirculationPeriodique.list()		
        if(typeFraisCirculationPeriodiques.size() > 0)
        typeFraisCirculationPeriodiqueSelected = typeFraisCirculationPeriodiques.get(0)
        else
        typeFraisCirculationPeriodiqueSelected = null
        
        fournisseurs = Fournisseur.list().sort{it.raisonSociale}
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            vehicules = Vehicule.list()
        }	
        if(vehicules.size() > 0)
        vehiculeSelected = vehicules.get(0)
        else
        vehiculeSelected = null
                    	
        if(del) {
            typeFraisCirculationPeriodiques = TypeFraisCirculationPeriodique.list()
        }	
        if(typeFraisCirculationPeriodiques.size() > 0)
        typeFraisCirculationPeriodiqueSelected = typeFraisCirculationPeriodiques.get(0)
        else
        typeFraisCirculationPeriodiqueSelected = null
        
        if(del) {
            fournisseurs = Fournisseur.list()
        }
        fournisseurSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.vehicule = vehiculeSelected
        if(vehicules.size() > 0) {
            def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
            vehiculeSelected = vehicules.get(0)
            bindervehicule.loadAll()
        }
        else
        vehiculeSelected = null
                    		
        objet.typeFraisCirculationPeriodique = typeFraisCirculationPeriodiqueSelected
        if(typeFraisCirculationPeriodiques.size() > 0) {
            def bindertypeFraisCirculationPeriodique = new AnnotateDataBinder(this.getFellow("cotypeFraisCirculationPeriodiques"))
            typeFraisCirculationPeriodiqueSelected = typeFraisCirculationPeriodiques.get(0)
            bindertypeFraisCirculationPeriodique.loadAll()
        }
        else
        typeFraisCirculationPeriodiqueSelected = null
        
        objet.fournisseur = fournisseurSelected
        fournisseurSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
        vehiculeSelected = vehicules.find{ it.id == FraisCirculationPeriodique.findById(objet.id).vehicule.id }
        bindervehicule.loadAll()
                    		
        def bindertypeFraisCirculationPeriodique = new AnnotateDataBinder(this.getFellow("cotypeFraisCirculationPeriodiques"))
        typeFraisCirculationPeriodiqueSelected = typeFraisCirculationPeriodiques.find{ it.id == FraisCirculationPeriodique.findById(objet.id).typeFraisCirculationPeriodique.id }
        bindertypeFraisCirculationPeriodique.loadAll()
        
        def binderfournisseur = new AnnotateDataBinder(this.getFellow("cofournisseurs"))
        if(objet.fournisseur != null) {
            fournisseurSelected = fournisseurs.find{ it.id == FraisCirculationPeriodique.findById(objet.id).fournisseur.id }
        } else {
            fournisseurSelected = null
        }
        binderfournisseur.loadAll()
                    
    }
}

