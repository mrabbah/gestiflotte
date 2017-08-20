
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
 * PaiementGroup Window Object
 **/
class PaiementGroupWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet FraisCirculationPeriodique
     **/
    def fraisCirculationPeriodiqueService
    /**
     * Service pour la gestion de l'objet EntretienPeriodique
     **/
    def entretienPeriodiqueService
    /**
     * Service pour la gestion de l'objet Entretien
     **/
    def entretienService
   
    /**
     * Service pour la gestion de l'objet BonFraisCirculationService
     **/
    def bonFraisCirculationService
    /**
     * Service pour la gestion de l'objet Societe
     **/
    def societeService
    
    /**
     * Service pour la gestion de l'objet PaiementGroup
     **/
    def paiementGroupService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class PaiementGroupWindow
     **/
    private Log logger = LogFactory.getLog(PaiementGroupWindow.class)
    
    /**
     * liste de entretienPeriodiques
     **/
    def entretienPeriodiques	
    /**
     * entretienPeriodiques  selectionn�
     **/
    def entretienPeriodiquesSelected = []
                
    /**
     * liste de entretiens
     **/
    def entretiens	
    /**
     * entretiens  selectionn�
     **/
    def entretiensSelected = []
    
    def enSelected = []
    
    def enPayesSelected = []
    
    def bfcSelected = []
    
    def bfcPayesSelected = []
    
    def epSelected = []
    
    def epPayesSelected = []
    
    def fcpSelected = []
    
    def fcpPayesSelected = []
                
    /**
     * liste de fraisCirculationPeriodiques
     **/
    def fraisCirculationPeriodiques	
    /**
     * fraisCirculationPeriodiques  selectionn�
     **/
    def fraisCirculationPeriodiquesSelected = []
                
    /**
     * liste de societeGroup
     **/	
    def societeGroups	
    /**
     * societeGroup  selectionn�
     **/
    def societeGroupSelected
                
    /**
     * liste de bonFraisCirculations
     **/
    def bonFraisCirculations	
    /**
     * bonFraisCirculations  selectionn�
     **/
    def bonFraisCirculationsSelected = []
    
    def fournisseurs
    def fournisseurSelected
                
    /**
     * Constructeur
     **/
    public PaiementGroupWindow (societeService, fraisCirculationPeriodiqueService, entretienPeriodiqueService, entretienService, bonFraisCirculationService) {
        super(PaiementGroup.class)
        this.societeService = societeService
        this.fraisCirculationPeriodiqueService = fraisCirculationPeriodiqueService
        this.entretienPeriodiqueService = entretienPeriodiqueService
        this.entretienService = entretienService
        this.bonFraisCirculationService = bonFraisCirculationService
        initalisationParticuliaireAssoc()
    }  

    /**
     * Pour annuler la modification ou la supression et pour basculer en mode ajout d'un nouveau ï¿½lï¿½ment
     **/
    def cancel() {    
        if(bonFraisCirculationsSelected.size() > 0) {
            bonFraisCirculations = bonFraisCirculationService.getListeBonsFraisCirculationNonPayeParCheque()
            bonFraisCirculationsSelected = []
        }
        if(fraisCirculationPeriodiquesSelected.size() > 0) {
            fraisCirculationPeriodiques = fraisCirculationPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
            fraisCirculationPeriodiquesSelected = []
        }
        if(entretiensSelected.size() > 0) {
            entretiens = entretienService.getListeEntretientsNonPayeParCheque()
            entretiensSelected = []
        }
        if(entretienPeriodiquesSelected.size() > 0) {
            entretienPeriodiques = entretienPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
            entretienPeriodiquesSelected = []
        }
        super.cancel()
    }
    
    /**
     * Activer ou dï¿½sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        this.getFellow("btnDelete").visible = visible
        this.getFellow("btnPdf").visible = !visible
        this.getFellow("btnExcel").visible = !visible
        this.getFellow("btnSave").visible = !visible
        this.getFellow("btnNew").visible = !visible
        this.getFellow("westPanel").open = visible   
        actualiserListes(1)
        actualiserListes(2)
        actualiserListes(3)
        actualiserListes(4)
    }
    
    def actualiserListes(id) {
        switch(id) {
            case 1 : 
            bfcSelected = []
            bfcPayesSelected = []
            def l1 = getFellow("lstbonFraisCirculations")
            def binder1 = new AnnotateDataBinder(l1)
            binder1.loadAll()
            l1.clearSelection()
            def l2 = getFellow("lstbonFraisCirculationsPayes")
            def binder2 = new AnnotateDataBinder(l2)
            binder2.loadAll()
            l2.clearSelection()
                
            break
            case 2 :
            fcpSelected = []
            fcpPayesSelected = []
            def l1 = getFellow("lstfraisCirculationPeriodiques")
            def binder1 = new AnnotateDataBinder(l1)
            binder1.loadAll()
            l1.clearSelection()
            def l2 = getFellow("lstfraisCirculationPeriodiquesPayes")
            def binder2 = new AnnotateDataBinder(l2)
            binder2.loadAll()
            l2.clearSelection()
            break
            case 3 :
            enSelected = []
            enPayesSelected = []
            def l1 = getFellow("lstentretiens")
            def binder1 = new AnnotateDataBinder(l1)
            binder1.loadAll()
            l1.clearSelection()
            def l2 = getFellow("lstentretiensPayes")
            def binder2 = new AnnotateDataBinder(l2)
            binder2.loadAll()
            l2.clearSelection()
            break
            case 4 :
            epSelected = []
            epPayesSelected = []
            def l1 = getFellow("lstentretienPeriodiques")
            def binder1 = new AnnotateDataBinder(l1)
            binder1.loadAll()
            l1.clearSelection()
            def l2 = getFellow("lstentretienPeriodiquesPayes")
            def binder2 = new AnnotateDataBinder(l2)
            binder2.loadAll()
            l2.clearSelection()
            break
        }
        
        objet.montantCheque = 0d
        
        if(bonFraisCirculationsSelected != null) {
            bonFraisCirculationsSelected.each { it ->
                objet.montantCheque += it.montant
            }
        }
        if(fraisCirculationPeriodiquesSelected != null) {
            fraisCirculationPeriodiquesSelected.each { it ->
                objet.montantCheque += it.montant
            }
        }
        if(entretiensSelected != null) {
            entretiensSelected.each { it ->
                objet.montantCheque += it.montant
            }
        }
        if(entretienPeriodiquesSelected != null) {
            entretienPeriodiquesSelected.each { it ->
                objet.montantCheque += it.montant
            }
        }
        def binder = new AnnotateDataBinder(getFellow("fieldMontantCheque"))
        binder.loadAll()
    }
    def choisirTout(id) {
        if(id == 1 && bonFraisCirculations.size() == 0)
        return
        if(id == 2 && fraisCirculationPeriodiques.size() == 0)
        return
        if(id == 3 && entretiens.size() == 0)
        return
        if(id == 4 && entretienPeriodiques.size() == 0)
        return
            
        switch(id) {
            case 1 : 
            bonFraisCirculationsSelected.addAll(bonFraisCirculations)
            bonFraisCirculations = []
            break
            case 2 :
            fraisCirculationPeriodiquesSelected.addAll(fraisCirculationPeriodiques)
            fraisCirculationPeriodiques = []
            break
            case 3 :
            entretiensSelected.addAll(entretiens)
            entretiens = []
            break
            case 4 :
            entretienPeriodiquesSelected.addAll(entretienPeriodiques)
            entretienPeriodiques = []
            break
        }
        actualiserListes(id)
    }
    def choisirDesElements(id) {
        if(id == 1 && bfcSelected.size() == 0)
        return
        if(id == 2 && fcpSelected.size() == 0)
        return
        if(id == 3 && enSelected.size() == 0)
        return
        if(id == 4 && epSelected.size() == 0)
        return
            
        switch(id) {
            case 1 : 
            bonFraisCirculationsSelected.addAll(bfcSelected)
            bonFraisCirculations.removeAll(bfcSelected)
            break
            case 2 :
            fraisCirculationPeriodiquesSelected.addAll(fcpSelected)
            fraisCirculationPeriodiques.removeAll(fcpSelected)
            break
            case 3 :
            entretiensSelected.addAll(enSelected)
            entretiens.removeAll(enSelected)
            break
            case 4 :
            entretienPeriodiquesSelected.addAll(epSelected)
            entretienPeriodiques.removeAll(epSelected)
            break
        }
        actualiserListes(id)
    }
    def enleverDesElements(id) {
        if(id == 1 && bfcPayesSelected.size() == 0)
        return
        if(id == 2 && fcpPayesSelected.size() == 0)
        return
        if(id == 3 && enPayesSelected.size() == 0)
        return
        if(id == 4 && epPayesSelected.size() == 0)
        return
            
        switch(id) {
            case 1 : 
            bonFraisCirculations.addAll(bfcPayesSelected )
            bonFraisCirculationsSelected.removeAll(bfcPayesSelected)
            break
            case 2 :
            fraisCirculationPeriodiques.addAll(fcpPayesSelected)
            fraisCirculationPeriodiquesSelected.removeAll(fcpPayesSelected)
            break
            case 3 :
            entretiens.addAll(enPayesSelected)
            entretiensSelected.removeAll(enPayesSelected)
            break
            case 4 :
            entretienPeriodiques.addAll(epPayesSelected)
            entretienPeriodiquesSelected.removeAll(epPayesSelected)
            break
        }
        actualiserListes(id)
    }
    def enleverTout(id) {
        if(id == 1 && bonFraisCirculationsSelected.size() == 0)
        return
        if(id == 2 && fraisCirculationPeriodiquesSelected.size() == 0)
        return
        if(id == 3 && entretiensSelected.size() == 0)
        return
        if(id == 4 && entretienPeriodiquesSelected.size() == 0)
        return
            
        switch(id) {
            case 1 : 
            bonFraisCirculations.addAll(bonFraisCirculationsSelected )
            bonFraisCirculationsSelected = []
            break
            case 2 :
            fraisCirculationPeriodiques.addAll(fraisCirculationPeriodiquesSelected)
            fraisCirculationPeriodiquesSelected = []
            break
            case 3 :
            entretiens.addAll(entretiensSelected)
            entretiensSelected = []
            break
            case 4 :
            entretienPeriodiques.addAll(entretienPeriodiquesSelected)
            entretienPeriodiquesSelected = []
            break
        }
        actualiserListes(id)
    }
    protected SuperService getService() {
        return this.paiementGroupService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des PaiementGroups"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_PaiementGroups.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PaiementGroups.pdf"
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
        String titrerapport = "Rapport des PaiementGroups"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_PaiementGroups.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PaiementGroups.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initalisationParticuliaireAssoc() {
        
        entretienPeriodiques = entretienPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
        entretienPeriodiquesSelected = []// = new ArrayList()
                    
        entretiens = entretienService.getListeEntretientsNonPayeParCheque()
        entretiensSelected = []// = new ArrayList()
                    
        fraisCirculationPeriodiques = fraisCirculationPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
        fraisCirculationPeriodiquesSelected = []// = new ArrayList()
                    
        societeGroups = societeService.getListSocietesDuGroupe()		
        if(societeGroups.size() > 0)
        societeGroupSelected = societeGroups.get(0)
        else
        societeGroupSelected = null
                    
        bonFraisCirculations = bonFraisCirculationService.getListeBonsFraisCirculationNonPayeParCheque()
        bonFraisCirculationsSelected = []// = new ArrayList()
        
        fournisseurs = Fournisseur.list().sort{it.raisonSociale}
        fournisseurSelected = null
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            entretienPeriodiques = entretienPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
        }
        this.getFellow("lstentretienPeriodiques").clearSelection()
        entretienPeriodiquesSelected = []// = new ArrayList()
                    
        if(del) {
            entretiens = entretienService.getListeEntretientsNonPayeParCheque()
        }
        this.getFellow("lstentretiens").clearSelection()
        entretiensSelected = []// = new ArrayList()
                    
        if(del) {
            fraisCirculationPeriodiques = fraisCirculationPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
        }
        this.getFellow("lstfraisCirculationPeriodiques").clearSelection()
        fraisCirculationPeriodiquesSelected = []// = new ArrayList()
                    	
        if(del) {
            societeGroups = societeService.getListSocietesDuGroupe()
        }	
        if(societeGroups.size() > 0)
        societeGroupSelected = societeGroups.get(0)
        else
        societeGroupSelected = null
                    
        if(del) {
            bonFraisCirculations = bonFraisCirculationService.getListeBonsFraisCirculationNonPayeParCheque()
        }
        this.getFellow("lstbonFraisCirculations").clearSelection()
        bonFraisCirculationsSelected = []// = new ArrayList()
        
        if(del) {
            fournisseurs = Fournisseur.list().sort{it.raisonSociale}
        }
        fournisseurSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        
        objet.entretienPeriodiques = entretienPeriodiquesSelected
        this.getFellow("lstentretienPeriodiques").clearSelection()
        entretienPeriodiquesSelected = []// = new ArrayList()
                    
        objet.entretiens = entretiensSelected
        this.getFellow("lstentretiens").clearSelection()
        entretiensSelected = []// = new ArrayList()
                    
        objet.fraisCirculationPeriodiques = fraisCirculationPeriodiquesSelected
        this.getFellow("lstfraisCirculationPeriodiques").clearSelection()
        fraisCirculationPeriodiquesSelected = []// = new ArrayList()
                    		
        objet.societeGroup = societeGroupSelected
        if(societeGroups.size() > 0) {
            def bindersocieteGroup = new AnnotateDataBinder(this.getFellow("cosocieteGroups"))
            societeGroupSelected = societeGroups.get(0)
            bindersocieteGroup.loadAll()
        }
        else
        societeGroupSelected = null
                    
        objet.bonFraisCirculations = bonFraisCirculationsSelected
        this.getFellow("lstbonFraisCirculations").clearSelection()
        bonFraisCirculationsSelected = []// = new ArrayList()
        
        objet.fournisseur = fournisseurSelected
        fournisseurSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        
        def binderentretienPeriodiques = new AnnotateDataBinder(this.getFellow("lstentretienPeriodiques"))       
        entretienPeriodiquesSelected = objetSelected.entretienPeriodiques
        entretienPeriodiques = entretienPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
        binderentretienPeriodiques.loadAll()
                    
        def binderentretiens = new AnnotateDataBinder(this.getFellow("lstentretiens"))
        entretiensSelected = objetSelected.entretiens
        entretiens = entretienService.getListeEntretientsNonPayeParCheque()
        binderentretiens.loadAll()
                    
        def binderfraisCirculationPeriodiques = new AnnotateDataBinder(this.getFellow("lstfraisCirculationPeriodiques"))
        fraisCirculationPeriodiquesSelected = objetSelected.fraisCirculationPeriodiques
        fraisCirculationPeriodiques = fraisCirculationPeriodiqueService.getListeEntretientsPeriodiquesNonPayeParCheque()
        binderfraisCirculationPeriodiques.loadAll()
                    		
        def bindersocieteGroup = new AnnotateDataBinder(this.getFellow("cosocieteGroups"))
        societeGroupSelected = societeGroups.find{ it.id == PaiementGroup.findById(objet.id).societeGroup.id }
        bindersocieteGroup.loadAll()
                    
        def binderbonFraisCirculations = new AnnotateDataBinder(this.getFellow("lstbonFraisCirculations"))
        bonFraisCirculationsSelected = objetSelected.bonFraisCirculations
        bonFraisCirculations = bonFraisCirculationService.getListeBonsFraisCirculationNonPayeParCheque()
        binderbonFraisCirculations.loadAll()
        
        def binderfournisseur = new AnnotateDataBinder(this.getFellow("cofournisseurs"))
        if(objet.fournisseur != null) {
            fournisseurSelected = fournisseurs.find{ it.id == Entretien.findById(objet.id).fournisseur.id }
        } else {
            fournisseurSelected = null
        }
        binderfournisseur.loadAll()
                    
    }
    
}

