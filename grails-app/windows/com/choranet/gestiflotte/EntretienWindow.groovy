
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
class EntretienWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Entretien
     **/
    def entretienService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class EntretienWindow
     **/
    private Log logger = LogFactory.getLog(EntretienWindow.class)
    
    /**
     * liste de typeEntretien
     **/	
    def typeEntretiens	
    /**
     * typeEntretien  selectionn�
     **/
    def typeEntretienSelected
                
    /**
     * liste de vehicule
     **/	
    def vehicules	
    /**
     * vehicule  selectionn�
     **/
    def vehiculeSelected
               
    def fournisseurs
    def fournisseurSelected
    
    /**
     * Constructeur
     **/
    public EntretienWindow () {
        super(Entretien.class,10)
    }  

    protected SuperService getService() {
        return this.entretienService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Entretiens"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Entretiens.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Entretiens.pdf"
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
        String titrerapport = "Rapport des Entretiens"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Entretiens.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Entretiens.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        typeEntretiens = TypeEntretien.list().sort{it.libelle}		
        if(typeEntretiens.size() > 0)
        typeEntretienSelected = typeEntretiens.get(0)
        else
        typeEntretienSelected = null
                    
        vehicules = Vehicule.list().sort{it.immatriculation}		
        if(vehicules.size() > 0)
        vehiculeSelected = vehicules.get(0)
        else
        vehiculeSelected = null
        
        fournisseurs = Fournisseur.list().sort{it.raisonSociale}
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            typeEntretiens = TypeEntretien.list().sort{it.libelle}
        }	
        if(typeEntretiens.size() > 0)
        typeEntretienSelected = typeEntretiens.get(0)
        else
        typeEntretienSelected = null
                    	
        if(del) {
            vehicules = Vehicule.list().sort{it.immatriculation}
        }	
        if(vehicules.size() > 0)
        vehiculeSelected = vehicules.get(0)
        else
        vehiculeSelected = null
        
        if(del) {
            fournisseurs = Fournisseur.list().sort{it.raisonSociale}
        }
        fournisseurSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.typeEntretien = typeEntretienSelected
        if(typeEntretiens.size() > 0) {
            def bindertypeEntretien = new AnnotateDataBinder(this.getFellow("cotypeEntretiens"))
            typeEntretienSelected = typeEntretiens.get(0)
            bindertypeEntretien.loadAll()
        }
        else
        typeEntretienSelected = null
                    		
        objet.vehicule = vehiculeSelected
        if(vehicules.size() > 0) {
            def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
            vehiculeSelected = vehicules.get(0)
            bindervehicule.loadAll()
        }
        else
        vehiculeSelected = null
        
        objet.fournisseur = fournisseurSelected
        fournisseurSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindertypeEntretien = new AnnotateDataBinder(this.getFellow("cotypeEntretiens"))
        typeEntretienSelected = typeEntretiens.find{ it.id == Entretien.findById(objet.id).typeEntretien.id }
        bindertypeEntretien.loadAll()
                    		
        def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
        vehiculeSelected = vehicules.find{ it.id == Entretien.findById(objet.id).vehicule.id }
        bindervehicule.loadAll()
        
        def binderfournisseur = new AnnotateDataBinder(this.getFellow("cofournisseurs"))
        if(objet.fournisseur != null) {
            fournisseurSelected = fournisseurs.find{ it.id == Entretien.findById(objet.id).fournisseur.id }
        } else {
            fournisseurSelected = null
        }
        binderfournisseur.loadAll()
                    
    }
}

