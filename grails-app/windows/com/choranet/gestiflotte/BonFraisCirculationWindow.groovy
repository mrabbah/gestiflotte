
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
 * BonFraisCirculation Window Object
 **/
class BonFraisCirculationWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet BonFraisCirculation
     **/
    def bonFraisCirculationService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class BonFraisCirculationWindow
     **/
    private Log logger = LogFactory.getLog(BonFraisCirculationWindow.class)
    
    /**
     * liste de categorieFraisCirculation
     **/	
    def categorieFraisCirculations	
    /**
     * categorieFraisCirculation  selectionn�
     **/
    def categorieFraisCirculationSelected
                
    /**
     * liste de deplacement
     **/	
    def deplacements	
    /**
     * deplacement  selectionn�
     **/
    def deplacementSelected
                
    /**
     * liste de intervention
     **/	
    def interventions	
    /**
     * intervention  selectionn�
     **/
    def interventionSelected
    
    def fournisseurs
    def fournisseurSelected
    
    def vehicules
    def vehiculeSelected
    
    def interventionService
                
    /**
     * Constructeur
     **/
    public BonFraisCirculationWindow () {
        super(BonFraisCirculation.class,10)
    }  

    protected SuperService getService() {
        return this.bonFraisCirculationService
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
    }
    
    def select() {
        super.select()
        getFellow("vbchoix").visible = false
        getFellow("grcrud").visible = true
        if(objet.deplacement != null) {
            getFellow("rwd").visible = true
        } else {
            getFellow("rwd").visible = false
        }
        if(objet.intervention != null) {
            getFellow("rwi").visible = true
        } else {
            getFellow("rwi").visible = false
        }
    }
    
    def choixTypeBfc(id) {
        switch(id) {
            case "r1" : 
            interventions = Intervention.list()
            if(interventions.size() > 0) {
                interventionSelected = interventions.get(0)
            }
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwi").visible = true
            getFellow("rwd").visible = false
            new AnnotateDataBinder(this.getFellow("cointerventions")).loadAll()
            
            break
            case "r2" : 
            deplacements = Deplacement.list()
            if(deplacements.size() > 0) {
                deplacementSelected = deplacements.get(0)
            }
                
            getFellow("vbchoix").visible = false
            getFellow("grcrud").visible = true
            getFellow("rwd").visible = true
            getFellow("rwi").visible = false
            new AnnotateDataBinder(this.getFellow("codeplacements")).loadAll()
            
            break
        }
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des BonFraisCirculations"
        String reportName   
        
        if (filtre."vehicule" == null) {
            reportName = 'rapport_des_BonFraisCirculations.jasper'
        }
        else{
            reportName = 'rapport_des_BonFraisCirculations_filtreVehicule.jasper'
        }
        
        def listeObjetsExporter = getObjetsToExport()        //listeObjetsExporter = listeObjetsExporter.sort{it.vehicule.immatriculation}
        
        def reportDef = new JasperReportDef(name:reportName,
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_BonFraisCirculations.pdf"
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
        String titrerapport = "Rapport des BonFraisCirculations"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_BonFraisCirculations.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_BonFraisCirculations.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        categorieFraisCirculations = CategorieFraisCirculation.list().sort{it.libelle}		
        if(categorieFraisCirculations.size() > 0){
            categorieFraisCirculationSelected = categorieFraisCirculations.get(0)
            objet.details = categorieFraisCirculationSelected.libelle
        }
        else
        categorieFraisCirculationSelected = null
                    
        deplacements = Deplacement.list()		
                    
        interventions = Intervention.list().sort{it.toString()}

        fournisseurs = Fournisseur.list().sort{it.raisonSociale}
        
        vehicules = Vehicule.list().sort{it.immatriculation}.immatriculation
	
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            categorieFraisCirculations = CategorieFraisCirculation.list().sort{it.libelle}
        }	
        if(categorieFraisCirculations.size() > 0){
            categorieFraisCirculationSelected = categorieFraisCirculations.get(0)
            objet.details = categorieFraisCirculationSelected.libelle
        }
        else
        categorieFraisCirculationSelected = null
                    	
        if(del) {
            deplacements = Deplacement.list()
        }	
        
        deplacementSelected = null
                    	
        if(del) {
            interventions = Intervention.list().sort{it.toString()}
        }	
        
        interventionSelected = null
        
        if(del) {
            fournisseurs = Fournisseur.list().sort{it.raisonSociale}
        }
        fournisseurSelected = null
        
        if(del) {
            vehicules = Vehicule.list().sort{it.immatriculation}.immatriculation
        }
        vehiculeSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.categorieFraisCirculation = categorieFraisCirculationSelected
        def bindercategorieFraisCirculation = new AnnotateDataBinder(this.getFellow("cocategorieFraisCirculations"))
        if(categorieFraisCirculations.size() > 0) {
            categorieFraisCirculationSelected = categorieFraisCirculations.get(0)
        }
        else 
        categorieFraisCirculationSelected = null
        bindercategorieFraisCirculation.loadAll()
                    		
        objet.deplacement = deplacementSelected
        
        deplacementSelected = null
                    		
        objet.intervention = interventionSelected
        if (interventionSelected != null){
            objet.immatriculation_vehicule = interventionSelected.vehicule.immatriculation
        }
        
        vehiculeSelected = null
        interventionSelected = null
        
        objet.fournisseur = fournisseurSelected
        fournisseurSelected = null
        
        getFellow("vbchoix").visible = true
        getFellow("r1").checked = false
        getFellow("r2").checked = false
        getFellow("grcrud").visible = false
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindercategorieFraisCirculation = new AnnotateDataBinder(this.getFellow("cocategorieFraisCirculations"))
        categorieFraisCirculationSelected = categorieFraisCirculations.find{ it.id == BonFraisCirculation.findById(objet.id).categorieFraisCirculation.id }
        bindercategorieFraisCirculation.loadAll()
              
        def binderdeplacement = new AnnotateDataBinder(this.getFellow("codeplacements"))
        if(objet.deplacement != null) {
            deplacementSelected = deplacements.find{ it.id == BonFraisCirculation.findById(objet.id).deplacement.id }
        } else {
            deplacementSelected = null
        }
        binderdeplacement.loadAll()
        
        
        def binderintervention = new AnnotateDataBinder(this.getFellow("cointerventions"))
        if(objet.intervention != null) {
            interventionSelected = interventions.find{ it.id == BonFraisCirculation.findById(objet.id).intervention.id }
        } else {
            interventionSelected = null
        }
        
        binderintervention.loadAll()
        
        def binderfournisseur = new AnnotateDataBinder(this.getFellow("cofournisseurs"))
        if(objet.fournisseur != null) {
            fournisseurSelected = fournisseurs.find{ it.id == BonFraisCirculation.findById(objet.id).fournisseur.id }
        } else {
            fournisseurSelected = null
        }
        
        binderfournisseur.loadAll()
                    
    }
    
    def calculerChargeTheorique(consommationGasoilHuile){
        def consomSeuil
        def binderChargeTheorique = new AnnotateDataBinder(this.getFellow("fieldChargeTheorique"))
        if(categorieFraisCirculationSelected.libelle.startsWith("Bon gasoil")){
            interventionSelected.consommationGasoilTheoriqueMax = consommationGasoilHuile
        
            if(interventionSelected.etatChargement.equals("CHARGE")){
                consomSeuil = interventionSelected.vehicule.consommationGasoilMax
            }else {
                consomSeuil = interventionSelected.vehicule.consommationGasoilAvideMax
            }
                
            if (consomSeuil == null){
                Messagebox.show("Echec du contrôle de consommation véhicule : la consommation gasoil max (NORMAL / A VIDE) de véhicule n'est pas indiquée \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
            else {
                if (interventionSelected.kilometrageDisque == null){
                    Messagebox.show("Echec lors du calcule de la consommation gasoil de l'intervention choisie : merci d'indiquer tout d'abord le kilométrage disque \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                }else {
                    interventionSelected.pourcentageConsommation = interventionSelected.getTrans_pourcentageConsommation()
                    if (interventionSelected.pourcentageConsommation > consomSeuil){
                        interventionSelected.consommationGasoilTheoriqueMax = (interventionSelected.kilometrageDisque * consomSeuil) / 100
                    }
                }
            }
            objet.chargeTheorique = interventionSelected.consommationGasoilTheoriqueMax
        }
        binderChargeTheorique.loadAll() 
    }
    
    def calculerChargeEnUM(montant) {
        def binderChargeEnUM = new AnnotateDataBinder(this.getFellow("fieldChargeEnUM"))
        objet.chargeEnUM = bonFraisCirculationService.getChargeEnUM(montant, categorieFraisCirculationSelected.uniteMesure.idUnite)
        binderChargeEnUM.loadAll() 
    }
    
    
    def getChauffeurIntervention(numIntervention){
        def conducteur = Intervention.findByNumInterventionIlike(numIntervention).conducteur
        return conducteur.nom + " " + conducteur.prenom
    }
    
    
    def updateIntervention(consommationGasoil, totalCharges){
        //try{
        if(objet.categorieFraisCirculation.libelle.startsWith("Bon gasoil")){
            def consomSeuil
            if(objet.intervention.etatChargement.equals("CHARGE")){
                consomSeuil = objet.intervention.vehicule.consommationGasoilMax
            }else consomSeuil = objet.intervention.vehicule.consommationGasoilAvideMax 
        
            objet.intervention.consommationGasoil = consommationGasoil
       
            //        else{
            //            if(objet.categorieFraisCirculation.libelle.startsWith("Bon huile")){
            //               objet.intervention.consommationHuile -= objet.chargeEnUM 
            //            }
            //        }
            
            objet.intervention.consommationGasoilTheoriqueMax = consomSeuil * objet.intervention.kilometrageDisque / 100
            if (consommationGasoil < objet.intervention.consommationGasoilTheoriqueMax){
                objet.intervention.consommationGasoilTheoriqueMax = consommationGasoil
            }
            //objet.intervention.pourcentageConsommation = objet.intervention.getTrans_pourcentageConsommation()
            //objet.intervention.consommationGasoilTheoriqueMax = objet.intervention.consommationGasoil
            //            if (objet.intervention.pourcentageConsommation > consomSeuil){
            //                objet.intervention.pourcentageConsommation = consomSeuil
            //                objet.intervention.consommationGasoilTheoriqueMax = (objet.intervention.kilometrageDisque * consomSeuil) / 100
            //            }
            objet.intervention.surplusConsommationGasoil = objet.intervention.consommationGasoil - objet.intervention.consommationGasoilTheoriqueMax
            if (objet.intervention.surplusConsommationGasoil < 0) objet.intervention.surplusConsommationGasoil = 0d
         
        }    
        objet.intervention.totalCharges = totalCharges
        interventionService.update(objet.intervention)
        //        } catch(Exception ex) {
        //            logger.error "Error: ${ex.message}", ex
        //            Messagebox.show("Probleme lors de la mise à jour de l'intervention associée", "Erreur", Messagebox.OK, Messagebox.ERROR)
        //        }
    }
    
    def updateInterventionAfterAddBFC(){
        if (objet.intervention != null){
            def consommationGasoilParam = objet.intervention.consommationGasoil + objet.chargeEnUM
            def totalChargesParam = objet.intervention.totalCharges + objet.montant
            updateIntervention(consommationGasoilParam, totalChargesParam)
        }
    }
    
    def annulerUpdateInterventionAfterAddBFCAction(){
        //try {
        if (objet.intervention != null){
            def consommationGasoilParam = objet.intervention.consommationGasoil - objet.chargeEnUM
            def totalChargesParam = objet.intervention.totalCharges - objet.montant
            updateIntervention(consommationGasoilParam, totalChargesParam)
        }
        //        } catch(Exception ex) {
        //            logger.error "Error: ${ex.message}", ex
        //            Messagebox.show("Probleme lors de l'annulation de la mise à jour de l'intervention associée", "Erreur", Messagebox.OK, Messagebox.ERROR)
        //        }
    }
    
    
    def add() {
        actualiserValeurAssociation()
        try {     
            updateInterventionAfterAddBFC()
            getService().save(objet)
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
            annulerUpdateInterventionAfterAddBFCAction()
        } finally {    
            rafraichirField()
            rafraichirList()
            activerBoutons(false)
        }
    }
    
    
    def updateInterventionAfterUpdateBFC(){
        if (objet.intervention != null){
            def bfcBeforeUpdate = BonFraisCirculation.findById(objet.id)
            def consommationGasoilParam = objet.intervention.consommationGasoil - bfcBeforeUpdate.chargeEnUM + objet.chargeEnUM
            def totalChargesParam = objet.intervention.totalCharges - bfcBeforeUpdate.montant + objet.montant
            updateIntervention(consommationGasoilParam, totalChargesParam)
        }
    }
    
    def annulerUpdateInterventionAfterUpdateBFCAction(){
        if (objet.intervention != null){
            def bfcBeforeUpdate = BonFraisCirculation.findById(objet.id)
            def consommationGasoilParam = objet.intervention.consommationGasoil + bfcBeforeUpdate.chargeEnUM - objet.chargeEnUM
            def totalChargesParam = objet.intervention.totalCharges + bfcBeforeUpdate.montant - objet.montant
            updateIntervention(consommationGasoilParam, totalChargesParam)
        }
    }
    
    def update() {
        try {
            updateInterventionAfterUpdateBFC()
            super.update()
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Probleme lors de la mise à jour de ce frais", "Erreur", Messagebox.OK, Messagebox.ERROR)
            annulerUpdateInterventionAfterUpdateBFCAction()
        }
        finally {
            rafraichirField()
            rafraichirList()
        }
        reinitialiserAssociation()
    }
    
    def updateInterventionAfterDeleteBFC(){
        if (objet.intervention != null){
            def consommationGasoilParam = objet.intervention.consommationGasoil - objet.chargeEnUM
            def totalChargesParam = objet.intervention.totalCharges - objet.montant
            updateIntervention(consommationGasoilParam, totalChargesParam)
        }
    }
    
    def annulerUpdateInterventionAfterDeleteBFCAction(){
        if (objet.intervention != null){
            def consommationGasoilParam = objet.intervention.consommationGasoil + objet.chargeEnUM
            def totalChargesParam = objet.intervention.totalCharges + objet.montant
            updateIntervention(consommationGasoilParam, totalChargesParam)
        }
    }
    
    
    def delete() {
        try{
            updateInterventionAfterDeleteBFC()
            super.delete()
        }catch(Exception e){
            Messagebox.show("Echec lors de la transaction\n" + " Suppression annuler : Ce Frais est déjà lié à un paiement !!! ou Probleme lors de la mise à jour de l'intervention associée", "Erreur", Messagebox.OK, Messagebox.ERROR)
            annulerUpdateInterventionAfterDeleteBFCAction()
        }
        
        getFellow("vbchoix").visible = true
        getFellow("grcrud").visible = false
        getFellow("r1").checked = false
        getFellow("r2").checked = false 
    }
    
    def updateListe() {
        this.listeObjets.each {
            if (it.intervention != null){
                it.immatriculation_vehicule = it.intervention.vehicule.immatriculation
                this.bonFraisCirculationService.update(it)
            }
        }
    }
    
    def initIntervention(){
        Intervention.list().each {
            it.consommationGasoil = 0d
            it.consommationGasoilTheoriqueMax = 0d
            it.totalCharges = 0d
            interventionService.update(it)
        }
    }
    
    def updateConsommationsGasoilTheorique() {
        def consomSeuil
       
        def interventionOfBonFraisInter
        def interventionOfBonFraisInterList = new ArrayList()
        //for(BonFraisCirculation it : listeObjets){
        this.listeObjets.each {
            interventionOfBonFraisInter = it.intervention
            if(it.categorieFraisCirculation.libelle.startsWith("Bon gasoil") && interventionOfBonFraisInter != null){
                consomSeuil = interventionOfBonFraisInter.vehicule.consommationGasoilMax
                if(interventionOfBonFraisInter.etatChargement.equals("DECHARGE") && interventionOfBonFraisInter.vehicule.consommationGasoilAvideMax != null)
                consomSeuil = interventionOfBonFraisInter.vehicule.consommationGasoilAvideMax
            
                interventionOfBonFraisInter.consommationGasoil += it.chargeEnUM
                it.chargeTheorique = consomSeuil * interventionOfBonFraisInter.kilometrageDisque / 100
                if (it.chargeEnUM < it.chargeTheorique){
                    it.chargeTheorique = it.chargeEnUM
                }
                interventionOfBonFraisInter.consommationGasoilTheoriqueMax += it.chargeTheorique
                
                interventionOfBonFraisInter.totalCharges += it.chargeEnUM * it.categorieFraisCirculation.uniteMesure.valeur
                //                it.intervention.surplusConsommationGasoil = it.chargeEnUM - it.chargeTheorique
                //                if (it.intervention.surplusConsommationGasoil < 0) it.intervention.surplusConsommationGasoil = 0d
                //                
                //                if (it.intervention.kilometrageDisque != null && it.intervention.kilometrageDisque > 0){
                //                    it.intervention.pourcentageConsommation = (it.chargeTheorique / it.intervention.kilometrageDisque) * 100;
                //                }
                
                //interventionOfBonFraisInterList.add(interventionOfBonFraisInter)
                interventionService.update(interventionOfBonFraisInter)
                //it.intervention = interventionOfBonFraisInter
                //bonFraisCirculationService.update(it)
            }
        }
        //def compte = 0
        //        interventionOfBonFraisInterList.each {
        //            //compte++
        //            interventionService.update(it)
        //            //println compte
        //        }
        //        this.listeObjets.each {
        //            if(it.categorieFraisCirculation.libelle.startsWith("Bon gasoil")&& it.intervention != null){
        //                
        //            }
        //        }
    }
    
}

