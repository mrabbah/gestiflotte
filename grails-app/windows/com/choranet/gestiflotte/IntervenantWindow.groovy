
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
 * Intervenant Window Object
 **/
class IntervenantWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Societe
     **/
    def societeService
    /**
     * Service pour la gestion de l'objet Intervenant
     **/
    def intervenantService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class IntervenantWindow
     **/
    private Log logger = LogFactory.getLog(IntervenantWindow.class)
    
    /**
     * liste de nationalite
     **/	
    def nationalites	
    /**
     * nationalite  selectionn�
     **/
    def nationaliteSelected
                
    /**
     * liste de employer
     **/	
    def employers	
    /**
     * employer  selectionn�
     **/
    def employerSelected
    
    def excelImporterService
                
    /**
     * Constructeur
     **/
    public IntervenantWindow (intervenantService) {
        super(Intervenant.class,10)
        
//        int compteur = 1
//        this.intervenantService = intervenantService
//        Intervenant.list().each { element ->
//            element.code = element.nom.substring(0,1) + + compteur
//            intervenantService.update(element)
//            compteur += 1
//        }
    }  
    
    def importation(media) {
        String resultat = excelImporterService.importerIntervenants(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }

    protected SuperService getService() {
        return this.intervenantService
    }
    
    /**
     * Activer ou dï¿½sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        this.getFellow("btnDelete").visible = visible
        //this.getFellow("btnCancel").visible = visible
        this.getFellow("btnPdf").visible = !visible
        this.getFellow("btnExcel").visible = !visible
        this.getFellow("btnSave").visible = !visible
        this.getFellow("btnNew").visible = !visible
        this.getFellow("westPanel").open = visible        
    }
    
    def cancel() {
        super.cancel()
        getFellow("vbchoix").visible = true
        getFellow("grcrud").visible = false
        getFellow("r1").checked = false
        getFellow("r2").checked = false
        getFellow("r3").checked = false
    }
    
    def select() {
        super.select()
        getFellow("vbchoix").visible = false
        getFellow("grcrud").visible = true
        if(objet.employer != null) {
            getFellow("rwemp").visible = true
        } else {
            getFellow("rwemp").visible = false
        }
    }
    
    def choixGenreIntervenant(id) {
        switch(id) {
            case "r1" : 
            objet.sousTraite = false
            employers = societeService.getListSocietesDuGroupe()
            if(employers.size() > 0) {
                employerSelected = employers.get(0)
            }
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwemp").visible = true
            new AnnotateDataBinder(this.getFellow("coemployers")).loadAll()
            
            break
            case "r2" : 
            objet.sousTraite = true
            employers = societeService.getListSocietesHorsGroup()
            if(employers.size() > 0) {
                employerSelected = employers.get(0)
            }
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwemp").visible = true
            new AnnotateDataBinder(this.getFellow("coemployers")).loadAll()
            
            break
            case "r3" :
            objet.sousTraite = true
            employers = Societe.list()
            employerSelected = null
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwemp").visible = false
            new AnnotateDataBinder(this.getFellow("coemployers")).loadAll()
            break
        }
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Intervenants"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Intervenants.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Intervenants.pdf"
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
        String titrerapport = "Rapport des Intervenants"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Intervenants.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Intervenants.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        nationalites = Nationalite.list()		
        if(nationalites.size() > 0)
        nationaliteSelected = nationalites.get(0)
        else
        nationaliteSelected = null
                    
        employers = Societe.list().sort{it.raisonSociale}
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            nationalites = Nationalite.list()
        }	
        if(nationalites.size() > 0)
        nationaliteSelected = nationalites.get(0)
        else
        nationaliteSelected = null
                    	
        if(del) {
            employers = Societe.list().sort{it.raisonSociale}
        }	
        
        employerSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.nationalite = nationaliteSelected
        if(nationalites.size() > 0) {
            def bindernationalite = new AnnotateDataBinder(this.getFellow("conationalites"))
            nationaliteSelected = nationalites.get(0)
            bindernationalite.loadAll()
        }
        else
        nationaliteSelected = null
                    		
        objet.employer = employerSelected
        
        employerSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindernationalite = new AnnotateDataBinder(this.getFellow("conationalites"))
        nationaliteSelected = nationalites.find{ it.id == Intervenant.findById(objet.id).nationalite.id }
        bindernationalite.loadAll()
                    		
        def binderemployer = new AnnotateDataBinder(this.getFellow("coemployers"))
        if(objet.employer != null) {
            employerSelected = employers.find{ it.id == Intervenant.findById(objet.id).employer.id }
        } else {
            employerSelected = null
        }
        binderemployer.loadAll()
                    
    }
}

