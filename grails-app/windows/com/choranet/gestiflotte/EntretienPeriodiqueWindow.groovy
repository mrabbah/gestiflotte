
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
 * EntretienPeriodique Window Object
 **/
class EntretienPeriodiqueWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet EntretienPeriodique
     **/
    def entretienPeriodiqueService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class EntretienPeriodiqueWindow
     **/
    private Log logger = LogFactory.getLog(EntretienPeriodiqueWindow.class)
    
    /**
     * liste de vehicule
     **/	
    def vehicules	
    /**
     * vehicule  selectionn�
     **/
    def vehiculeSelected
                
    /**
     * liste de typeEntretienPeriodique
     **/	
    def typeEntretienPeriodiques	
    /**
     * typeEntretienPeriodique  selectionn�
     **/
    def typeEntretienPeriodiqueSelected
          
    def fournisseurs
    def fournisseurSelected
    
    /**
     * Constructeur
     **/
    public EntretienPeriodiqueWindow () {
        super(EntretienPeriodique.class,10)
    }  

    protected SuperService getService() {
        return this.entretienPeriodiqueService
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
            entretienPeriodiqueService.genererProchainEntretienPeriodique(objet)
        }
        super.refresh()
    }
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des EntretienPeriodiques"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_EntretienPeriodiques.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_EntretienPeriodiques.pdf"
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
        String titrerapport = "Rapport des EntretienPeriodiques"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_EntretienPeriodiques.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_EntretienPeriodiques.xls"
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
                    
        typeEntretienPeriodiques = TypeEntretienPeriodique.list()		
        if(typeEntretienPeriodiques.size() > 0)
        typeEntretienPeriodiqueSelected = typeEntretienPeriodiques.get(0)
        else
        typeEntretienPeriodiqueSelected = null
        
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
            typeEntretienPeriodiques = TypeEntretienPeriodique.list()
        }	
        if(typeEntretienPeriodiques.size() > 0)
        typeEntretienPeriodiqueSelected = typeEntretienPeriodiques.get(0)
        else
        typeEntretienPeriodiqueSelected = null
        
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
                    		
        objet.typeEntretienPeriodique = typeEntretienPeriodiqueSelected
        if(typeEntretienPeriodiques.size() > 0) {
            def bindertypeEntretienPeriodique = new AnnotateDataBinder(this.getFellow("cotypeEntretienPeriodiques"))
            typeEntretienPeriodiqueSelected = typeEntretienPeriodiques.get(0)
            bindertypeEntretienPeriodique.loadAll()
        }
        else
        typeEntretienPeriodiqueSelected = null
        
        objet.fournisseur = fournisseurSelected
        fournisseurSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
        vehiculeSelected = vehicules.find{ it.id == EntretienPeriodique.findById(objet.id).vehicule.id }
        bindervehicule.loadAll()
                    		
        def bindertypeEntretienPeriodique = new AnnotateDataBinder(this.getFellow("cotypeEntretienPeriodiques"))
        typeEntretienPeriodiqueSelected = typeEntretienPeriodiques.find{ it.id == EntretienPeriodique.findById(objet.id).typeEntretienPeriodique.id }
        bindertypeEntretienPeriodique.loadAll()
        
        def binderfournisseur = new AnnotateDataBinder(this.getFellow("cofournisseurs"))
        if(objet.fournisseur != null) {
            fournisseurSelected = fournisseurs.find{ it.id == EntretienPeriodique.findById(objet.id).fournisseur.id }
        } else {
            fournisseurSelected = null
        }
        binderfournisseur.loadAll()
                    
    }
}

