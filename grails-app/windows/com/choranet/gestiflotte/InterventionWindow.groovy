
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
import org.zkoss.zk.ui.Executions;

import java.util.Calendar;


/**
 * Intervention Window Object
 **/
class InterventionWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet reservation
     **/  
    def reservationService
    
    /**
     * Service pour la gestion de l'objet vehicule
     **/      
    def vehiculeService
    
    /**
     * Service pour la gestion de l'objet Intervention
     **/
    def interventionService
    
    /**
     * Service pour la gestion de l'objet PaternCompteur
     **/
    def paternCompteurService
    
    /**
     * Service pour la gestion de tarification client
     **/
    def tarificationService
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class InterventionWindow
     **/
    private Log logger = LogFactory.getLog(InterventionWindow.class)
    
    /**
     * liste de conducteur
     **/	
    def conducteurs	
    /**
     * conducteur  selectionn�
     **/
    def conducteurSelected
                
    /**
     * liste de vehicule
     **/	
    def vehicules
    
    /**
     * vehicule  selectionn�
     **/
    def vehiculeSelected
    
    /**
     * type  
     **/
    def typeSelected
    
    /**
     * liste de agentLoueurResponsable
     **/	
    def agentLoueurResponsables	
    /**
     * agentLoueurResponsable  selectionn�
     **/
    def agentLoueurResponsableSelected
                
    /**
     * liste de client
     **/	
    def clients	
    /**
     * client  selectionn�
     **/
    def clientSelected
    
    /**
     * Service pour la gestion de l'objet société
     **/
    def societeService
    
    def recordSelected
    
    /**
     * Liste de tarifications
     **/
    def tarifications
    
    /**
     * tarification selectionnée
     **/
    def tarificationSelected
    
    def trajets
    def trajetSelected
    
    def regions
    def regionSelected
    
    def modeTarifs
    def modeTarifSelected
    
    /**
     * Etat du chargement des véhicules Chargé/non chargé
     **/
    def etatChargementSelected
    
    def interventionsChoisis
    def interventionsChoisieSelected

    def consommationGasoilCoche
    def kilometrageDisqueCoche = 0d
    def pourcentageConsommationCoche
    def totalConsommationGasoilTheoriqueMax
    def totalSurplusConsommationGasoil = 0d
    	
    def nvnumBon
    def nvdate
    def nvmontant
    def nvchargeEnUM
    def nvdetails
    def categorieFraisCirculations
    def categorieFraisCirculationSelected
    
    def interventions	
    def interventionSelected
    
    def fournisseurs
    def fournisseurSelected
    
    def bonFraisCirculationService
    
    def bonFraisCirculationList
    def bonFraisCirculationListSaved
    
    /**
     * Constructeur
     **/
    public InterventionWindow (vehiculeService, societeService, interventionService, paternCompteurService, tarificationService) {
        super(Intervention.class,10)
        this.vehiculeService = vehiculeService
        this.societeService = societeService
        this.interventionService = interventionService
        this.paternCompteurService = paternCompteurService
        this.tarificationService = tarificationService
        
        specialeInitialisation()
        
        //        Intervention.list().each { element ->
        //            //element.type = 'T'
        //            element.numIntervention = paternCompteurService.getProchainNumInterEtIncrementer('T', element.dateDepart)
        //            interventionService.update(element)
        //        }
        
        //updateChargesFiledsElements(super.listeObjets)
        recordSelected = false
    }  

    //    def updateChargesFiledsElements(listObjets){
    //        listObjets.each { element ->
    //            element.consommationGasoil = interventionService.getTotalConsommationGasoil(element)
    //            element.totalCharges = interventionService.getTotalFraisCirculation(element)
    //            interventionService.update(element)
    //        }
    //    }
    
    protected SuperService getService() {
        return this.interventionService
    }
    
    def arrondirePrix(prix, seuilArrondissement, senseArrondissement){
        return interventionService.arrondirePrix(prix, seuilArrondissement, senseArrondissement)
    }
    
    def select() { 
        def bindertype = new AnnotateDataBinder(this.getFellow("cotype"))
        recordSelected = true
        bindertype.loadAll()
        
        def binderDD = new AnnotateDataBinder(this.getFellow("fieldDateDepart"))
        recordSelected = true
        binderDD.loadAll()
        
        super.select()
        
        getNumInterventionFieldStatus(true)
    }
    
    def newRecord() {
        recordSelected = false
        super.newRecord()
        this.getFellow("btnCancel").visible = true
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport simplifié des Interventions "
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Interventions.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_simplifie_des_Interventions.pdf"
        //Filedownload.save(bit, "application/pdf", nom_fichier);
        Media amedia = new AMedia(nom_fichier, "pdf", "application/pdf", bit);
        Map map = new HashMap();
        map.put("content", amedia);
        Executions.createComponents("/zul/util/pdfviewer.zul", null, map).doModal();
    }
    
    def genererRapportPdfVersionComplet() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Interventions"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Interventions_vcomplete.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Interventions.pdf"
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
        String titrerapport = "Rapport des Interventions"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Interventions.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Interventions.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def specialeInitialisation() {
        
        conducteurs = Intervenant.list().sort{it.prenom}		
        if(conducteurs.size() > 0)
        conducteurSelected = conducteurs.get(0)
        else
        conducteurSelected = null
        
        //        if (objet.kilometrageDisque != null && objet.kilometrageDisque > 0)
        //        objet.pourcentageConsommation = (objet.consommationGasoil / objet.kilometrageDisque) * 100;
        
        vehicules = vehiculeService.getListVehiculeDeService()
        //getListVehiculesDeServiceWithoutVoitures()
        
        //        if(vehicules.size() > 0) {
        //            vehiculeSelected = vehicules.get(0)
        //            objet.kilometrageDepart = vehiculeSelected.kilometrage
        //            objet.kilometrageRetour = vehiculeSelected.kilometrage
        //            objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        //        }
        //        else {
        vehiculeSelected = null
        objet.kilometrageDepart = null
        objet.prixParJour = null
        
        
        //        }
                    
        agentLoueurResponsables = Utilisateur.list().sort{it.username}		
        if(agentLoueurResponsables.size() > 0)
        agentLoueurResponsableSelected = agentLoueurResponsables.get(0)
        else
        agentLoueurResponsableSelected = null
                    
        clients = societeService.getListSocietesHorsGroup()
        //        if(clients.size() > 0)
        //        clientSelected = clients.get(0)
        //        else
        clientSelected = null
        
        
        trajets = null 
        regions = null
        trajetSelected = null
        regionSelected = null
        
        //        tarifications = tarificationService.getTarificationsClient(clientSelected, objet.dateDepart)
        //        if(tarifications.size() > 0) {
        //            modeTarifs = tarifications.modeTarif
        //            if (modeTarifs.size() > 1) {
        //                modeTarifSelected = null
        //            }else {
        //                modeTarifSelected = tarifications.get(0).modeTarif
        //                if (modeTarifSelected == "Journalier" || modeTarifSelected == "Kilometrage") {
        //                    tarificationSelected = tarifications.get(0)
        //                }else {
        //                    if (modeTarifSelected == "Forfitaire/Trajet" || modeTarifSelected == "Tonnage/Trajet"){
        //                        trajets = tarificationService.getListTrajets(clientSelected, objet.dateDepart)
        //                    }else // modeTarifClient == "Forfitaire Par Region"
        //                    regions = tarificationService.getListRegions(clientSelected, objet.dateDepart)
        //                }
        //            }
        //        }else
        tarificationSelected = null     
        
        //def bindercoetatChargement = new AnnotateDataBinder(this.getFellow("coetatChargement"))          
        //etatChargementSelected = "CHARGE"
        //bindercoetatChargement.loadAll()
        
        interventionsChoisis = new ArrayList() //Intervention.list()
        interventionsChoisieSelected = null
        
        
        nvnumBon = ""
        nvdate = null
        //        nvmontant = 0
        //        nvchargeEnUM = 0d
        nvdetails = ""
        categorieFraisCirculations = new ArrayList()
        categorieFraisCirculations.addAll(CategorieFraisCirculation.findAllByLibelleIlike('Bon gasoil%'))
        categorieFraisCirculations.addAll(CategorieFraisCirculation.findAllByLibelleIlike('Bon huile%'))
        categorieFraisCirculationSelected = categorieFraisCirculations.get(0)
        nvdetails = categorieFraisCirculationSelected.libelle + " partagé"
        //interventions = Intervention.list()
        fournisseurs = Fournisseur.list().sort{it.raisonSociale}
        
        //updateConsommationsGasoilTheorique()
        
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            conducteurs = Intervenant.list().sort{it.prenom}
        }	
        if(conducteurs.size() > 0)
        conducteurSelected = conducteurs.get(0)
        else
        conducteurSelected = null
                    	
        if(del) {
            vehicules = vehiculeService.getListVehiculeDeService()
            //getListVehiculesDeServiceWithoutVoitures()
        }	
        if(vehicules.size() > 0) {
            vehiculeSelected = vehicules.get(0)
            objet.kilometrageDepart = vehiculeSelected.kilometrage
            objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        }
        else {
            vehiculeSelected = null
            objet.kilometrageDepart = null
            objet.prixParJour = null
        }
                    	
        if(del) {
            agentLoueurResponsables = Utilisateur.list().sort{it.username}
        }	
        if(agentLoueurResponsables.size() > 0)
        agentLoueurResponsableSelected = agentLoueurResponsables.get(0)
        else
        agentLoueurResponsableSelected = null
                    	
        if(del) {
            clients = Societe.list().sort{it.raisonSociale}
        }	
        //        if(clients.size() > 0)
        //        clientSelected = clients.get(0)
        //        else
        clientSelected = null
        
        trajets = null 
        regions = null
        trajetSelected = null
        regionSelected = null
        //        if(del) {
        //            tarifications = tarificationService.getTarificationsClient(clientSelected, objet.dateDepart)
        //        }
        //        if(tarifications.size() > 0) {
        //            modeTarifs = tarifications.modeTarif
        //            if (modeTarifs.size() > 1) {
        //                modeTarifSelected = null
        //            }else {
        //                modeTarifSelected = tarifications.get(0).modeTarif
        //                if (modeTarifSelected == "Journalier" || modeTarifSelected == "Kilometrage") {
        //                    tarificationSelected = tarifications.get(0)
        //                }else {
        //                    if (modeTarifSelected == "Forfitaire/Trajet" || modeTarifSelected == "Tonnage/Trajet"){
        //                        trajets = tarificationService.getListTrajets(clientSelected, objet.dateDepart)
        //                    }else // modeTarifClient == "Forfitaire Par Region"
        //                    regions = tarificationService.getListRegions(clientSelected, objet.dateDepart)
        //                }
        //            }
        //        }
        tarificationSelected = null
        
        if(del) {
            tarifications = Tarification.list().sort{it.code}
        }
        //        if(tarifications != null && tarifications.size() > 0) {
        //            tarificationSelected = tarifications.get(0)
        //        }else
        //        tarificationSelected = null
                    
    }
    
    /**
     * fonction pour la mise à jour de la liste des interventions cochées (interventionsChoisis) par ajout
     **/
    def addElementToList(numIntervention){
        def interventionChecked = Intervention.findByNumIntervention(numIntervention)
        interventionsChoisis.add(interventionChecked)
        kilometrageDisqueCoche += interventionChecked.kilometrageDisque
        def binderListinter = new AnnotateDataBinder(this.getFellow("winFraisIntervention"))
        binderListinter.loadAll()
    }
    
    /**
     * fonction pour la mise à jour de la liste des interventions decochées (interventionsChoisis) par suppression
     **/
    def RemoveElementFromList(numIntervention){
        def interToBeDeleted
        interventionsChoisis.each {
            if (it.numIntervention == numIntervention) {
                interToBeDeleted = it
                return
            }
        }
        interventionsChoisis.remove(interToBeDeleted)
        kilometrageDisqueCoche -= interToBeDeleted.kilometrageDisque
        def binderListinter = new AnnotateDataBinder(this.getFellow("winFraisIntervention"))
        binderListinter.loadAll()
    }
    
    /**
     **
     **/
    def getConsommationsGasoilForCheckedInterventions(){
        try{
            def consomSeuil
            def montantFrais
            def currentConsommation
            def consommationTheorique
            bonFraisCirculationList = new ArrayList()
            interventionsChoisis.each {
                if (it.kilometrageDisque == null){
                    Messagebox.show("Echec lors du calcule de la consommation gasoil de l'intervention choisie : merci d'indiquer tout d'abord le kilométrage disque \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                    it.kilometrageDisque = 0d
                }
                currentConsommation = it.kilometrageDisque * nvchargeEnUM / kilometrageDisqueCoche
                if(categorieFraisCirculationSelected.libelle.startsWith("Bon gasoil")){
                    
                    it.consommationGasoil = Intervention.findById(it.id).consommationGasoil + currentConsommation
                    if(it.etatChargement.equals("CHARGE")){
                        consomSeuil = it.vehicule.consommationGasoilMax
                    }else consomSeuil = it.vehicule.consommationGasoilAvideMax
                
                    if (consomSeuil == null){
                        Messagebox.show("Echec du contrôle de consommation véhicule : la consommation gasoil max (NORMAL / A VIDE) de véhicule n'est pas indiquée \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                        return
                    }
                    it.consommationGasoilTheoriqueMax = it.consommationGasoil
                    it.pourcentageConsommation = it.getTrans_pourcentageConsommation()
                    if (it.pourcentageConsommation > consomSeuil){
                        it.consommationGasoilTheoriqueMax = (it.kilometrageDisque * consomSeuil) / 100
                    }
                    consommationTheorique = it.consommationGasoilTheoriqueMax
                }
                else{
                    Messagebox.show("Gestion des bons de huile partagés n'est pas encore validée !!! \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                    return
                }
                
                //                else{
                //                    if(categorieFraisCirculationSelected.libelle.startsWith("Bon huile")){
                //                        it.consommationHuile +=  it.kilometrageDisque * nvchargeEnUM / kilometrageDisqueCoche
                //                    }
                //                }
                
                montantFrais = currentConsommation * categorieFraisCirculationSelected.uniteMesure.valeur
                bonFraisCirculationList.add( 
                    new BonFraisCirculation(numBon : null, date : null, details : null, montant : montantFrais, 
                        chargeEnUM : currentConsommation, chargeTheorique : consommationTheorique, immatriculation_vehicule : it.vehicule.immatriculation, 
                        categorieFraisCirculation : categorieFraisCirculationSelected, deplacement : null, intervention : it, fournisseur : null) 
                )
                
                if (it.totalCharges == null){
                    it.totalCharges = currentConsommation * categorieFraisCirculationSelected.uniteMesure.valeur
                }else{
                    it.totalCharges = Intervention.findById(it.id).totalCharges + currentConsommation * categorieFraisCirculationSelected.uniteMesure.valeur
                }
            }
        }catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Problème lors la mise à jour des consommations gasoil des interventions de la liste cochée : merci d'indiquer les kilométrages disque \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
        } 

        def winFraisIntervention = this.getFellow("winFraisIntervention")
        def binderListinter = new AnnotateDataBinder(winFraisIntervention.getFellow("lstInterventionsChoisis"))
        binderListinter.loadAll()
    }
    
    /**
     **
     **/
    def updateConsommationsGasoilAfterkilometrageDisqueUpdate(String numIntervention, Double newKilometrageDisque){
        kilometrageDisqueCoche = 0d
        try{
            interventionsChoisis.each {
                if(it.numIntervention != numIntervention){
                    kilometrageDisqueCoche += it.kilometrageDisque
                }else
                kilometrageDisqueCoche += newKilometrageDisque
            }
        
            pourcentageConsommationCoche = nvchargeEnUM / kilometrageDisqueCoche * 100
        
            def interventionAfterUpdate
            interventionsChoisis.each {
                if(it.numIntervention == numIntervention){
                    it.kilometrageDisque = newKilometrageDisque
                    it.consommationGasoil = (newKilometrageDisque * nvchargeEnUM) / kilometrageDisqueCoche
                    interventionAfterUpdate = it
                    return
                }
            }
            interventionsChoisis.each {
                if(it.numIntervention != numIntervention){
                    it.consommationGasoil =  it.kilometrageDisque * (nvchargeEnUM - interventionAfterUpdate.consommationGasoil) / (kilometrageDisqueCoche - interventionAfterUpdate.kilometrageDisque)
                }
            }
            
            updatePourcentageConsomsAfterTotalChargeConsomUpdate()
            //            def winFraisIntervention = this.getFellow("winFraisIntervention")
            //            def binderListinter = new AnnotateDataBinder(winFraisIntervention.getFellow("lstInterventionsChoisis"))
            //            binderListinter.loadAll()
        
        }catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Problème lors la mise à jour des consommations gasoil des interventions de la liste cochée : merci d'indiquer les kilométrages disque \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
    }
    
    /**
     **
     **/
    def updateConsommationGasoilAfterPourcentageConsomUpdate(String numIntervention, Double pourcentageConsommation){
        
        def consomSeuil
        def interventionToBeUpdated
        interventionsChoisis.each {
            if(it.numIntervention == numIntervention){
                interventionToBeUpdated = it
                if(it.etatChargement.equals("CHARGE")){
                    consomSeuil = it.vehicule.consommationGasoilMax
                }else consomSeuil = it.vehicule.consommationGasoilAvideMax
                
                //                println "consomSeuil : " + consomSeuil 
                //                println "pourcentageConsommation: " + pourcentageConsommation
                if (pourcentageConsommation > consomSeuil){
                    it.trans_pourcentageConsommation = consomSeuil
                    it.pourcentageConsommation = consomSeuil
                }else {
                    it.trans_pourcentageConsommation = pourcentageConsommation
                    it.pourcentageConsommation = pourcentageConsommation
                }
                it.consommationGasoilTheoriqueMax = (it.kilometrageDisque * it.pourcentageConsommation) / 100
            }
            return
        }
        totalConsommationGasoilTheoriqueMax = 0d
        interventionsChoisis.each {
            if(!it.numIntervention.equals(interventionToBeUpdated.numIntervention))
            totalConsommationGasoilTheoriqueMax += it.consommationGasoil
            else
            totalConsommationGasoilTheoriqueMax += it.consommationGasoilTheoriqueMax
        }
        pourcentageConsommationCoche = totalConsommationGasoilTheoriqueMax / kilometrageDisqueCoche * 100
        if (nvchargeEnUM > totalConsommationGasoilTheoriqueMax)
        totalSurplusConsommationGasoil = nvchargeEnUM - totalConsommationGasoilTheoriqueMax
        else totalSurplusConsommationGasoil = 0d
        
        if (nvchargeEnUM > totalConsommationGasoilTheoriqueMax){
            if (interventionToBeUpdated.consommationGasoil > interventionToBeUpdated.consommationGasoilTheoriqueMax){
                interventionToBeUpdated.surplusConsommationGasoil = interventionToBeUpdated.consommationGasoil - interventionToBeUpdated.consommationGasoilTheoriqueMax
            }
        }
        //interventionToBeUpdated.consommationGasoil = interventionToBeUpdated.consommationGasoilTheoriqueMax
   
        def winFraisIntervention = this.getFellow("winFraisIntervention")
        def binderListinter = new AnnotateDataBinder(winFraisIntervention.getFellow("lstInterventionsChoisis"))
        binderListinter.loadAll()
        def binderChargeEnUM = new AnnotateDataBinder(winFraisIntervention.getFellow("fieldChargeTheorique"))
        binderChargeEnUM.loadAll()
    }
    
    
    /**
     *
     ***/
    def updatePourcentageConsomsAfterTotalChargeConsomUpdate(){
        
        def consomSeuil
        if(categorieFraisCirculationSelected.libelle.startsWith("Bon gasoil")){
            interventionsChoisis.each {
            
                if(it.etatChargement.equals("CHARGE")){
                    consomSeuil = it.vehicule.consommationGasoilMax
                }else consomSeuil = it.vehicule.consommationGasoilAvideMax 
                if (consomSeuil == null){
                    Messagebox.show("Echec du contrôle de consommation véhicule : la consommation gasoil max (NORMAL / A VIDE) de véhicule n'est pas indiquée \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                    return
                }
            
                if (it.kilometrageDisque == null){
                    Messagebox.show("Echec lors du calcule de la consommation gasoil de l'intervention choisie : merci d'indiquer tout d'abord le kilométrage disque \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                    return
                }
            
                it.consommationGasoilTheoriqueMax = it.consommationGasoil
                it.trans_consommationGasoilTheorique = it.consommationGasoil
                it.surplusConsommationGasoil = 0d
                it.trans_pourcentageConsommation = it.getTrans_pourcentageConsommation()
            
                if (it.trans_pourcentageConsommation > consomSeuil){
                    it.trans_pourcentageConsommation = consomSeuil
                    it.pourcentageConsommation = consomSeuil
                    it.consommationGasoilTheoriqueMax = (it.kilometrageDisque * consomSeuil) / 100
                
                    //                println "consomSeuil : " + consomSeuil 
                    //                println "consommationGasoil normal: " + it.consommationGasoil 
                    //                println "trans_pourcentageConsommation : " + it.trans_pourcentageConsommation 
                    //                println "consommationGasoilTheoriqueMax : " + it.consommationGasoilTheoriqueMax
                
                    totalConsommationGasoilTheoriqueMax = nvchargeEnUM - it.consommationGasoil + it.consommationGasoilTheoriqueMax
                    if (nvchargeEnUM > totalConsommationGasoilTheoriqueMax){
                        if (it.consommationGasoil > it.consommationGasoilTheoriqueMax){
                            it.surplusConsommationGasoil = it.consommationGasoil - it.consommationGasoilTheoriqueMax
                        }
                        else it.surplusConsommationGasoil = 0d
                    }
                    else it.surplusConsommationGasoil = 0d  
                  
                    //it.consommationGasoil = it.consommationGasoilTheoriqueMax
                }
                //            else {
                //                it.consommationGasoilTheoriqueMax = it.consommationGasoil
                //                it.trans_consommationGasoilTheorique = it.consommationGasoil
                //                it.surplusConsommationGasoil = 0d
                //            }
            }
            totalConsommationGasoilTheoriqueMax = 0d
            interventionsChoisis.each {
                //            if(it.trans_pourcentageConsommation > consomSeuil)
                //            totalConsommationGasoilTheoriqueMax += it.consommationGasoilTheoriqueMax
                //            else
                totalConsommationGasoilTheoriqueMax += it.consommationGasoilTheoriqueMax
                //it.consommationGasoilTheoriqueMax = it.consommationGasoil - it.surplusConsommationGasoil
            }
            pourcentageConsommationCoche = totalConsommationGasoilTheoriqueMax / kilometrageDisqueCoche * 100
            if (nvchargeEnUM > totalConsommationGasoilTheoriqueMax)
            totalSurplusConsommationGasoil = nvchargeEnUM - totalConsommationGasoilTheoriqueMax
            else totalSurplusConsommationGasoil = 0d
        }
        else
        Messagebox.show("Gestion des bons de huile partagés n'est pas encore validée !!! \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
        
        def winFraisIntervention = this.getFellow("winFraisIntervention")
        def binderListinter = new AnnotateDataBinder(winFraisIntervention.getFellow("lstInterventionsChoisis"))
        binderListinter.loadAll()
        def binderChargeEnUM = new AnnotateDataBinder(winFraisIntervention.getFellow("fieldChargeTheorique"))
        binderChargeEnUM.loadAll()
    }
    
    /**
     **
     **/
    def updateConsommationsGasoilAfterConsommationUpdate(String numIntervention){
        def intervention
        interventionsChoisis.each {
            if(it.numIntervention == numIntervention){
                intervention = it
                return
            }
        }
        try{
            interventionsChoisis.each {
                if(it.numIntervention != numIntervention){
                    it.consommationGasoil =  it.kilometrageDisque * (nvchargeEnUM - intervention.consommationGasoil) / (kilometrageDisqueCoche - intervention.kilometrageDisque)
                }
            } 
        }catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Problème lors la mise à jour des consommations gasoil des interventions de la liste cochée : merci d'indiquer les kilométrages disque \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        
        def winFraisIntervention = this.getFellow("winFraisIntervention")
        def binderListinter = new AnnotateDataBinder(winFraisIntervention.getFellow("lstInterventionsChoisis"))
        binderListinter.loadAll()
    }    
    
    /**
     **
     **/
    def calculerChargeEnUM(montant) {
        def binderChargeEnUM = new AnnotateDataBinder(this.getFellow("winFraisIntervention").getFellow("fieldChargeEnUM"))
        nvchargeEnUM = bonFraisCirculationService.getChargeEnUM(montant, categorieFraisCirculationSelected.uniteMesure.idUnite)
        binderChargeEnUM.loadAll()      
    }
    
    def getCategorieFraisCirculationByLibelle(String libelle){
        return CategorieFraisCirculation.findByLibelleIlike(libelle);
    }
    
    
    def saveFraisCirculation(){
        bonFraisCirculationListSaved = new ArrayList()
        //try{
        bonFraisCirculationList.each {
            //montantFrais = it.consommationGasoil * categorieFraisCirculationSelected.uniteMesure.valeur
            //                println "nvnumBon : " + nvnumBon
            //                println "nvdate : " + nvdate
            //                println "nvdetails : " + nvdetails
            //                println "montantFrais : " + montantFrais
            //                println "it.consommationGasoil : " + it.consommationGasoil
            //                println "categorieFraisCirculationSelected : " + categorieFraisCirculationSelected
            //                println "fournisseurSelected : " + fournisseurSelected
            
            it.numBon = nvnumBon
            it.date = nvdate
            it.details = nvdetails
            it.fournisseur = fournisseurSelected
            println "nvnumBon : " + nvnumBon
            println "nvdate : " + nvdate
            println "nvdetails : " + nvdetails
            println "montantFrais : " + it.montant
            println "it.consommationGasoil : " + it.chargeEnUM
            println "categorieFraisCirculationSelected : " + categorieFraisCirculationSelected
            println "fournisseurSelected : " + fournisseurSelected
            println "vehicule immatriculation : " + it.immatriculation_vehicule
            
            bonFraisCirculationService.save(it)
            
            //bonFraisCirculationListSaved.add(it)
        }
        //        }catch(Exception ex){
        //            logger.error "Error: ${ex.message}", ex
        //            Messagebox.show("Probleme lors du sauvegarde de frais de circulation", "Erreur", Messagebox.OK, Messagebox.ERROR)
        //            annulerSaveFraisCirculationAction()
        //        }
    }
    
    def annulerSaveFraisCirculationAction(){
        //try{
        bonFraisCirculationListSaved.each {
            bonFraisCirculationService.delete(it)
        }
        //        }catch(Exception ex){
        //            logger.error "Error: ${ex.message}", ex
        //            Messagebox.show("Probleme lors de l'annulation du sauvegarde de frais de circulation", "Erreur", Messagebox.OK, Messagebox.ERROR)
        //        }
    }

    
    def ajouterFraisCirculation() {
        try {
            saveFraisCirculation()
            def currentIntervention
            interventionsChoisis.each {
                currentIntervention = Intervention.findByNumInterventionIlike(it.numIntervention)
                currentIntervention.consommationGasoil = it.consommationGasoil
                //currentIntervention.consommationHuile = it.consommationHuile
                currentIntervention.totalCharges = it.totalCharges
                currentIntervention.kilometrageDisque = it.kilometrageDisque
                
                currentIntervention.consommationGasoilTheoriqueMax = it.consommationGasoilTheoriqueMax
                currentIntervention.surplusConsommationGasoil = it.consommationGasoil - it.consommationGasoilTheoriqueMax
                if (currentIntervention.surplusConsommationGasoil < 0) currentIntervention.surplusConsommationGasoil = 0d
                
                println 'prob avant interventionService.update'
                interventionService.update(currentIntervention)
                println 'prob apres interventionService.update'
            }
            cancelWinFraisIntervention()
            rafraichirField()
            rafraichirList()
        } catch(Exception ex) {
            //logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + "Merci d'indiquer tous les champs obligatoires", "Erreur", Messagebox.OK, Messagebox.ERROR)
            annulerSaveFraisCirculationAction()
        } finally {  
        }
    }
        
    def actualiserTarificationByTrajetSelected(dateDepart){
        //println "call actualiserTarificationByTrajetSelected"
        tarificationSelected = tarificationService.getTarificationByTrajet(clientSelected, trajetSelected, modeTarifSelected, dateDepart)
        objet.tarification = tarificationSelected.get(0)
        //println "tarificationSelected : " + tarificationSelected
    }
    
    def actualiserTarificationByRegionSelected(dateDepart){
        //println "call actualiserTarificationByRegionSelected"
        tarificationSelected = tarificationService.getTarificationByRegion(clientSelected, regionSelected, dateDepart)
        objet.tarification = tarificationSelected.get(0)
        //println "tarificationSelected : " + tarificationSelected
    }
    
    def actualiserTarificationByModeTarif(dateDepart){
        //println "call actualiserTarificationByModeTarif"
        tarifications = tarificationService.getTarificationsByModeTarifAndClient(clientSelected, modeTarifSelected, dateDepart)
        //println "tarifications : " + tarifications
        
        regions = null
        regionSelected = null
        trajets = null
        trajetSelected = null
        tarificationSelected = null
        objet.tarification = null
        
        //def binderModeTarif = new AnnotateDataBinder(this.getFellow("coModeTarifs"))
        if(tarifications.size() > 0) {
            if (modeTarifSelected == "Journalier" || modeTarifSelected == "Kilometrage") {
                tarificationSelected = tarifications.get(0)
                objet.tarification = tarificationSelected
                //objet.tarification
            }else {
                if (modeTarifSelected == "Forfitaire/Trajet" || modeTarifSelected == "Tonnage/Trajet"){
                    def binderTrajet = new AnnotateDataBinder(this.getFellow("coTrajets"))
                    trajets = tarificationService.getListTrajets(clientSelected, modeTarifSelected, dateDepart)
                    binderTrajet.loadAll()
                    //objet.tarification.trajet = trajetSelected
                    //                    if (trajets.size() == 1) {
                    //                        trajetSelected = trajets.get(0)
                    //                        tarificationSelected = tarificationService.getTarificationByTrajet(clientSelected, trajetSelected, modeTarifSelected, dateDepart)
                    //                        objet.tarification = tarificationSelected.get(0)
                    //                    }
                }else {// modeTarifClient == "Forfitaire Par Region"
                    def binderRegion = new AnnotateDataBinder(this.getFellow("coRegions"))
                    regions = tarificationService.getListRegions(clientSelected, modeTarifSelected, dateDepart)
                    binderRegion.loadAll()
                    //objet.tarification.region = regionSelected
                    //                    if (regions.size() == 1) {
                    //                        regionSelected = regions.get(0)
                    //                        tarificationSelected = tarificationService.getTarificationByRegion(clientSelected, regionSelected, dateDepart)
                    //                        objet.tarification = tarificationSelected.get(0)
                    //                    }
                }
            }
        }
        else tarificationSelected = null
        //binderModeTarif.loadAll()
    }
    
    def actualiserTarification(dateDepart){
        //println "call actualiserTarification"	 
        tarifications = tarificationService.getTarificationsClient(clientSelected, dateDepart)
        //println "tarifications : " + tarifications
        tarificationSelected = null
        objet.tarification = null
        
        regions = null
        def binderRegion = new AnnotateDataBinder(this.getFellow("coRegions"))
        regionSelected = null
        binderRegion.loadAll()
        
        trajets = null
        def binderTrajet = new AnnotateDataBinder(this.getFellow("coTrajets"))
        trajetSelected = null
        binderTrajet.loadAll()
        
        modeTarifs = null
        def binderModeTarif = new AnnotateDataBinder(this.getFellow("coModeTarifs"))
        modeTarifSelected = null
        if(tarifications.size() > 0) {
            modeTarifs = tarificationService.getModeTarifs(clientSelected, dateDepart)//tarifications.modeTarif
            //println "modeTarifs : " + modeTarifs
            if (modeTarifs.size() == 1){
                def modeTarifClient = modeTarifs.get(0)
                if (modeTarifClient == "Journalier" || modeTarifClient == "Kilometrage") {
                    tarificationSelected = tarifications.get(0)
                    modeTarifSelected = modeTarifClient
                    objet.tarification = tarificationSelected
                }else {
                    if (modeTarifClient == "Forfitaire/Trajet" || modeTarifClient == "Tonnage/Trajet"){
                        binderTrajet = new AnnotateDataBinder(this.getFellow("coTrajets"))
                        trajets = tarificationService.getListTrajets(clientSelected, modeTarifClient, dateDepart)
                        //println 'trajets client : ' + trajets
                        //objet.tarification.trajet = trajetSelected
                        //                        if (trajets.size() == 1) {
                        //                            //trajetSelected = trajets.get(0)
                        //                            println 'trajetSelected : ' + trajetSelected
                        //                            tarificationSelected = tarificationService.getTarificationByTrajet(clientSelected, trajetSelected, modeTarifClient, dateDepart)
                        //                            println 'tarificationSelected : ' + tarificationSelected
                        //                            objet.tarification = tarificationSelected.get(0)
                        //                        }
                        binderTrajet.loadAll()
                    }else {// modeTarifClient == "Forfitaire Par Region"
                        binderRegion = new AnnotateDataBinder(this.getFellow("coRegions"))
                        regions = tarificationService.getListRegions(clientSelected, modeTarifClient, dateDepart)
                        //objet.tarification.region = regionSelected
                        //                        if (regions.size() == 1) {
                        //                            //regionSelected = regions.get(0)
                        //                            tarificationSelected = tarificationService.getTarificationByRegion(clientSelected, regionSelected, dateDepart)
                        //                            objet.tarification = tarificationSelected.get(0)
                        //                        }
                        binderRegion.loadAll()
                    }
                }
            }
        }
        binderModeTarif.loadAll()
    }
    
    def actualiserAssociationClient() {
        //println "call actualiserAssociationClient"
        objet.client = clientSelected
        //objet.tarification = tarificationSelected
        
        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        clientSelected = null
        binderclient.loadAll()
        
        def binderModeTarif = new AnnotateDataBinder(this.getFellow("coModeTarifs"))
        def binderTrajet = new AnnotateDataBinder(this.getFellow("coTrajets"))
        def binderRegion = new AnnotateDataBinder(this.getFellow("coRegions"))
        
        regions = null
        regionSelected = null
        trajets = null
        trajetSelected = null
        tarificationSelected = null
        modeTarifs = null
        modeTarifSelected = null
                
        //        if(clients.size() > 0) {
        //            //clientSelected = clients.get(0)
        //            tarifications = tarificationService.getTarificationsClient(clientSelected, dateDepart)
        //            if(tarifications != null && tarifications.size() > 0) {
        //                modeTarifs = tarifications.modeTarif
        //                if (modeTarifs.size() > 0) {
        //                    modeTarifSelected = tarifications.get(0).modeTarif
        //                    if (modeTarifSelected == "Journalier" || modeTarifSelected == "Kilometrage") {
        //                        tarificationSelected = tarifications.get(0)
        //                    }else {
        //                        if (modeTarifSelected == "Forfitaire/Trajet" || modeTarifSelected == "Tonnage/Trajet"){
        //                            
        //                            trajets = tarificationService.getListTrajets(clientSelected, modeTarifSelected, dateDepart)
        //                            //objet.tarification.trajet = trajetSelected
        //                            if (trajets.size() == 1) {
        //                                trajetSelected = trajets.get(0)
        //                                tarificationSelected = tarificationService.getTarificationByTrajet(clientSelected, trajetSelected, modeTarifSelected, dateDepart)
        //                                objet.tarification = tarificationSelected.get(0)
        //                            }
        //                        }else {// modeTarifClient == "Forfitaire Par Region"
        //                            regions = tarificationService.getListRegions(clientSelected, modeTarifSelected, dateDepart)
        //                            //objet.tarification.region = regionSelected
        //                            if (regions.size() == 1) {
        //                                regionSelected = regions.get(0)
        //                                tarificationSelected = tarificationService.getTarificationByRegion(clientSelected, regionSelected, dateDepart)
        //                                objet.tarification = tarificationSelected.get(0)
        //                            }
        //                        }
        //                    }
        //                } 
        //            }
        //        }
        binderRegion.loadAll()
        binderTrajet.loadAll()
        binderModeTarif.loadAll()
    }
    
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        //println "call actualiserValeurAssociation"		
        objet.conducteur = conducteurSelected
        if(conducteurs.size() > 0) {
            def binderconducteur = new AnnotateDataBinder(this.getFellow("coconducteurs"))
            conducteurSelected = conducteurs.get(0)
            binderconducteur.loadAll()
            //objet.commissionChauffeur = conducteurSelected.commission
            new AnnotateDataBinder(this.getFellow("fieldPourcentageCommission")).loadAll()
        }
        else
        conducteurSelected = null
                    		
        objet.vehicule = vehiculeSelected
        vehicules = vehiculeService.getListVehiculeDeService()
        if(vehicules.size() > 0) {
            def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
            vehiculeSelected = vehicules.get(0)
            bindervehicule.loadAll()
        }
        else {
            vehiculeSelected = null
            objet.kilometrageDepart = null
            objet.prixParJour = null
        }
                    		
        objet.agentLoueurResponsable = agentLoueurResponsableSelected
        if(agentLoueurResponsables.size() > 0) {
            def binderagentLoueurResponsable = new AnnotateDataBinder(this.getFellow("coagentLoueurResponsables"))
            agentLoueurResponsableSelected = agentLoueurResponsables.get(0)
            binderagentLoueurResponsable.loadAll()
        }
        else
        agentLoueurResponsableSelected = null
                    		
        actualiserAssociationClient()
        
        objet.type = typeSelected
        def bindertype = new AnnotateDataBinder(this.getFellow("cotype"))
        bindertype.loadAll()
        
        objet.etatChargement = etatChargementSelected
        def binderetatChargement = new AnnotateDataBinder(this.getFellow("coetatChargement"))
        binderetatChargement.loadAll()
                   
    }
    
    def getNumInterventionFieldStatus(visible){
        this.getFellow("labelNumInter").visible = visible
        this.getFellow("fieldNumIntervention").visible = visible
        new AnnotateDataBinder(this.getFellow("labelNumInter")).loadAll()
        new AnnotateDataBinder(this.getFellow("fieldNumIntervention")).loadAll()  
    } 
    
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
   
        //println "call afficherValeurAssociation"	 
        
        def binderconducteur = new AnnotateDataBinder(this.getFellow("coconducteurs"))
        conducteurSelected = conducteurs.find{ it.id == Intervention.findById(objet.id).conducteur.id }
        binderconducteur.loadAll()
                    		
        def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
        vehiculeSelected = vehicules.find{ it.id == Intervention.findById(objet.id).vehicule.id }
        if(objetSelected != null) {
            //objet.kilometrageDepart = vehiculeSelected.kilometrage
            //            if(trajetSelected != null)
            //            objet.kilometrageRetour = objet.kilometrageDepart + trajetSelected.kilometrage
            objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        }
        bindervehicule.loadAll()
                    		
        def binderagentLoueurResponsable = new AnnotateDataBinder(this.getFellow("coagentLoueurResponsables"))
        agentLoueurResponsableSelected = agentLoueurResponsables.find{ it.id == Intervention.findById(objet.id).agentLoueurResponsable.id }
        binderagentLoueurResponsable.loadAll()
                    		
        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        clientSelected = clients.find{ it.id == Intervention.findById(objet.id).client.id }
        binderclient.loadAll()
        
        //actualiserTarification()
        if(objetSelected != null) {
            tarificationSelected = objetSelected.tarification
            //println " tarificationSelected : " + tarificationSelected
            
            def bindermodeTarif = new AnnotateDataBinder(this.getFellow("coModeTarifs"))
            modeTarifSelected = objetSelected.tarification.modeTarif
            bindermodeTarif.loadAll()
            
            def binderTrajet = new AnnotateDataBinder(this.getFellow("coTrajets"))
            trajetSelected = objetSelected.tarification.trajet
            binderTrajet.loadAll()
            
            def binderRegion = new AnnotateDataBinder(this.getFellow("coRegions"))
            regionSelected = objetSelected.tarification.region
            binderRegion.loadAll()
        }        
        //actualiserTarificationByTrajetSelected(objetSelected.dateDepart)    
        //actualiserTarificationByRegionSelected(objet.dateDepart)
                    
        def bindercotype = new AnnotateDataBinder(this.getFellow("cotype"))          
        typeSelected = objet.type
        bindercotype.loadAll()
        
        def bindercoetatChargement = new AnnotateDataBinder(this.getFellow("coetatChargement"))          
        etatChargementSelected = objet.etatChargement
        bindercoetatChargement.loadAll()
        
        //        def binderTarification = new AnnotateDataBinder(this.getFellow("coTarifications"))
        //        tarificationSelected = tarifications.find{ it.id == Tarification.findById(objet.id).tarification.id }
        //        binderTarification.loadAll()
    }
    
    /**
     * Fonction qui se charge de sauveguarder un nouveau �l�ment de article
     **/
    def add() {
        try {
            def isInterventionValid =interventionService.validatedIntervention(objet, vehiculeSelected)
            def isInterventionForReservationValid = interventionService.validatedInterventionForReservation(objet, vehiculeSelected)
            if (isInterventionValid && isInterventionForReservationValid) {
                if(objet.kilometrageRetour != null && objet.kilometrageDepart < objet.kilometrageRetour) {
                    vehiculeSelected.kilometrage = interventionService.getKilometrageVehicule(vehiculeSelected, objet.kilometrageRetour) 
                    //objet.kilometrageRetour
                    vehiculeService.update(vehiculeSelected)
                }
                if(objet.consommationGasoil == null){
                    objet.consommationGasoil = 0d
                    objet.totalCharges = 0d
                }   

                // au lieu de super.add()
                actualiserValeurAssociation()
                try {     
                    getService().save(objet)
                } catch(Exception ex) {
                    logger.error "Error: ${ex.message}", ex
                    Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
                } finally {
                    objet = clazz.newInstance()
                    objet.kilometrageDepart = vehiculeSelected.kilometrage
                    objet.prixParJour = vehiculeSelected.categorie.prixJournalier
                    
                    rafraichirField()
                    rafraichirList()
                    activerBoutons(false)
                    getNumInterventionFieldStatus(false)
                }
                //fin au lieu super.add()
            } else {
                Messagebox.show("Echec lors de l'ajout d'une intervention : véhicule déjà mise en service pendant cette période \n[" +objet.dateDepart +" , "+ objet.dateRestitution+"] \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
            
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        } finally {
            cancel()
            super.refresh()
        }
        //reinitialiserAssociation()
    }
    
    /**
     * Fonction qui se charge de mettre à jour un élément selectionné
     **/
    def update() {
        try {
            def numInter = objet.numIntervention
            objet.numIntervention = typeSelected + numInter.substring(1,numInter.length())
            //println objet.numIntervention
            
            def isInterventionValid = interventionService.validatedIntervention(objet, vehiculeSelected)
            def isInterventionForReservationValid = interventionService.validatedInterventionForReservation(objet, vehiculeSelected)
            if (isInterventionValid && isInterventionForReservationValid) {
                if(objet.kilometrageRetour != null  && objet.kilometrageDepart < objet.kilometrageRetour) {
                    // si la véhicule est changée
                    println 'est ce que la véhicule est changée ?? '
                    println 'véhicule avant : ' + Intervention.findById(objet.id).vehicule.immatriculation
                    println 'véhicule aprés : ' + vehiculeSelected
                    def oldVehicule = null
                    if(objet.vehicule.id != vehiculeSelected){
                        println 'la véhicule est changée : '
                        
                        // Mise à jour du kilometrage de l'ancienne véhicule affectée
                        oldVehicule = objet.vehicule
                        //objet.vehicule.kilometrage = interventionService.getKilometrageVehiculeAfterInterventionUpdate(objet.id, objet.vehicule) 
                        //vehiculeService.update(objet.vehicule)
                        
                        // Mise à jour du kilometrage de la nouvelle véhicule affectée
                        vehiculeSelected.kilometrage = interventionService.getKilometrageVehicule(vehiculeSelected, objet.kilometrageRetour) 
                        vehiculeService.update(vehiculeSelected)
                         
                        // s'il existe des frais d'interventions qui sont liés à l'intervention courante
                        // alors il faut faire la mise à jour du champ immatriculation_vehicule pour chaque frais d'intervention
                        def bonFraisInterventionList = interventionService.getBonFraisInterventionList(objet)
                        println 'bonFraisInterventionList avant : ' + bonFraisInterventionList.immatriculation_vehicule
                        if (bonFraisInterventionList.size() > 0){
                            bonFraisInterventionList.each {
                                it.immatriculation_vehicule = vehiculeSelected.immatriculation
                                bonFraisCirculationService.update(it)
                            }
                        }
                        println 'bonFraisInterventionList après : ' + bonFraisInterventionList.immatriculation_vehicule
                    }
                    def interventionCourante = objet
                    super.update()
                    
                    if (oldVehicule != null){
                        // Mise à jour du kilometrage de l'ancienne véhicule affectée
                        oldVehicule.kilometrage = interventionService.getKilometrageVehiculeAfterInterventionUpdate(interventionCourante.id, oldVehicule) 
                        vehiculeService.update(oldVehicule)
                    }
                } 
                else Messagebox.show("Echec lors de la mise à jour d'une intervention : merci d'indiquer une valeur valide pour le champ kilometrageRetour \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                
            } else {
                Messagebox.show("Echec lors de la mise à jour d'une intervention : véhicule déjà mise en service pendant cette période \n[" +objet.dateDepart +" , "+ objet.dateRestitution+"] \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                objet.dateDepart = null
                objet.dateRestitution = null
                rafraichirField()
                rafraichirList()
            }
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Probleme lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
        }
        reinitialiserAssociation()
    }
    /**
     * Fonction qui se charge de supprimer un �l�ment selectinn� de article
     **/
    def delete() {
        
        try {
            //            if(objet.vehicule.kilometrage == objet.kilometrageRetour) {
            //                objet.vehicule.kilometrage = objet.kilometrageDepart
            //                vehiculeService.update(objet.vehicule)
            //            }
            def currentVehicule = Vehicule.findById(objet.vehicule.id)
            //            if(interventionService.isLastIntervention(objet.numIntervention)) {
            super.delete()
            currentVehicule.kilometrage = interventionService.getKilometrageVehicule(vehiculeSelected, objet.kilometrageRetour)
            vehiculeService.update(currentVehicule)
            //            }else 
            //            Messagebox.show("Seulement la dernière intervention qui peut être supprimée\n", "Erreur", Messagebox.OK, Messagebox.ERROR)
            
        }catch(Exception ex){
            logger.error "L'intervention en cours est liée à certains frais d'intevention ${ex.message}", ex
            Messagebox.show("L'intervention en cours est liée à certains frais d'intevention\n", "Attention!!", Messagebox.OK, Messagebox.ERROR)            
        }
    }
    
    def isFIAddedForCurrentIntervention(){
        def result = BonFraisCirculation.createCriteria().list{ 
            eq("intervention", objet)
        }
        if (result.size()>0) return true
        else                 return false
    }
    
    def deleteFIIntervention(){
        try{ 
            def fraisInterventions = BonFraisCirculation.createCriteria().list{ 
                eq("intervention", objet)
            }
            fraisInterventions.each {
                it.delete()
            }
        } catch(Exception ex){
            logger.error "Echec de suppression des frais d'intervetions liés à l'intervention (" + objet.numIntervention + ") ${ex.message}", ex
            Messagebox.show("Echec de suppression des frais d'intervetions liés à l'intervention (" + objet.numIntervention + ")\n", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        delete()
    }
    
    def confirm() {
        try{
            reservationService.generateIntervention(objet)
            super.delete()
        } catch(Exception ex){
            logger.error "Error: Erreur pendant la confirmation de réservation ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
    }
    
    /**
     * Pour annuler la modification ou la supression et pour basculer en mode ajout d'un nouveau �l�ment
     **/
    def cancel() {        
        super.cancel()
        actualiserValeurAssociation()
        recordSelected = false
        getNumInterventionFieldStatus(false)
    }
        
    /**
     * Activer ou d�sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        this.getFellow("btnDelete").visible = visible
        this.getFellow("btnCancel").visible = visible
        this.getFellow("btnSave").visible = !visible

        this.getFellow("westPanel").open = visible  

    }
    
    def changerDeVoiture() {
        objet.kilometrageDepart = vehiculeSelected.kilometrage
        objet.kilometrageRetour = null
        //objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        //def coPrixParJour = this.getFellow("fieldPrixParJour")
        //def binder3 = new AnnotateDataBinder(coPrixParJour)
        //binder3.loadAll()
        
        if (modeTarifSelected.toString().equals("Forfitaire/Trajet") || modeTarifSelected.toString().equals("Tonnage/Trajet")) {
            objet.kilometrageRetour = objet.kilometrageDepart + trajetSelected.kilometrage;                                                                                                                    
            objet.KilometrageDisque = trajetSelected.kilometrage;
        }
        
        def coKilometrageDepart = this.getFellow("fieldKilometrageDepart")
        def coKilometrageRetour = this.getFellow("fieldKilometrageRetour")
        def coKilometrageDisque = this.getFellow("fieldKilometrageDisque")
        def coDistanceParcourue = this.getFellow("fieldDistanceParcourue")
        def binder1 = new AnnotateDataBinder(coKilometrageDepart)
        binder1.loadAll()
        def binder2 = new AnnotateDataBinder(coKilometrageRetour)
        binder2.loadAll()
        def binder3 = new AnnotateDataBinder(coKilometrageDisque)
        binder3.loadAll()
        def binder4 = new AnnotateDataBinder(coDistanceParcourue)
        binder4.loadAll()
    }
    
    //    def getTotalFraisCirculation(){
    //        def binderTotalFraisCirculation = new AnnotateDataBinder(this.getFellow("fieldTotalFraisCirculation"))
    //        totalFraisCirculationCalculated = interventionService.getTotalFraisCirculation(objet)
    //        binderTotalFraisCirculation.loadAll()
    //    }
    //   
    //    def getTotalConsommationGasoil(){
    //        def binderConsommationGasoil = new AnnotateDataBinder(this.getFellow("fieldConsomGasoil"))
    //        consommationGasoilCalculated = interventionService.getTotalConsommationGasoil(objet)
    //        binderConsommationGasoil.loadAll()
    //    }
    
    
    def getNumIntervention(typeSelected, dateDepart){
        if (typeSelected != null && dateDepart != null) {
            objet.dateDepart = dateDepart
            objet.numIntervention = paternCompteurService.getProchainNumInter(typeSelected, dateDepart)
            new AnnotateDataBinder(this.getFellow("fieldNumIntervention")).loadAll()
            new AnnotateDataBinder(this.getFellow("fieldDateDepart")).loadAll()
            return true
        }
        
        objet.numIntervention = null
        new AnnotateDataBinder(this.getFellow("fieldNumIntervention")).loadAll()
        return false
    }
    
    def doModalDialogFC(){
        this.getFellow("winFraisIntervention").visible = true
        this.getFellow("winFraisIntervention").doModal() 
    }
    
    def cancelWinFraisIntervention(){
        def winFInter = this.getFellow("winFraisIntervention")
        if (interventionsChoisis != null ) {
            nvnumBon = null
            nvdate = null
            nvmontant = null
            nvchargeEnUM = null
            nvdetails = "Bon gasoil partagé"
            fournisseurSelected = null
            consommationGasoilCoche = null
            kilometrageDisqueCoche = 0d
            pourcentageConsommationCoche = null
            totalConsommationGasoilTheoriqueMax = null
            totalSurplusConsommationGasoil = 0d
            interventionsChoisis = new ArrayList()
            def binderListinter = new AnnotateDataBinder(winFInter)
            binderListinter.loadAll()
        }else {
            def binderwinFC = new AnnotateDataBinder(winFInter.getFellow("winFC"))
            binderwinFC.loadAll()
        }
        winFInter.visible = false
        
        def binderListobj = new AnnotateDataBinder(this.getFellow("lstObjet"))
        binderListobj.loadAll()   
    }
        
    def updateConsommationIntervention(intervention){   
        def categorieFraisCirculationList = CategorieFraisCirculation.createCriteria().list{
            ilike("libelle", 'Bon gasoil%')
        }
        def bonGasoilList = new ArrayList()
        categorieFraisCirculationList.each { catgfraisCirc ->
            bonGasoilList.addAll(BonFraisCirculation.createCriteria().list{ 
                    and {
                        eq("categorieFraisCirculation", catgfraisCirc)
                        eq("intervention", intervention) 
                    }
                })
        }
        if (bonGasoilList != null){
            intervention.totalCharges = 0d
            intervention.consommationGasoil = 0d
            //intervention.consommationGasoilTheoriqueMax = 0d
            bonGasoilList.each {
                intervention.consommationGasoil += it.chargeEnUM
                //intervention.consommationGasoilTheoriqueMax += it.chargeTheorique
                intervention.totalCharges += it.chargeEnUM * it.categorieFraisCirculation.uniteMesure.valeur
            }
        }
        return intervention
    }
    
    def updateConsommationsGasoilTheorique() {
        def consomSeuil
        this.listeObjets.each {
            //if (it.consommationGasoilTheoriqueMax == null || it.consommationGasoilTheoriqueMax == 0d){
            consomSeuil = it.vehicule.consommationGasoilMax
            if(it.etatChargement.equals("DECHARGE") && it.vehicule.consommationGasoilAvideMax != null)
            consomSeuil = it.vehicule.consommationGasoilAvideMax
            
            it = updateConsommationIntervention(it)
            it.consommationGasoilTheoriqueMax = consomSeuil * it.kilometrageDisque / 100
            if (it.consommationGasoil < it.consommationGasoilTheoriqueMax){
                it.consommationGasoilTheoriqueMax = it.consommationGasoil
            }
            it.surplusConsommationGasoil = it.consommationGasoil - it.consommationGasoilTheoriqueMax
            if (it.surplusConsommationGasoil < 0) it.surplusConsommationGasoil = 0d
                
            if (it.kilometrageDisque != null && it.kilometrageDisque > 0){
                it.pourcentageConsommation = (it.consommationGasoil / it.kilometrageDisque) * 100;
            }
            
            interventionService.update(it)
            // }
            //            it.trans_consommationGasoilTheorique = it.consommationGasoilTheoriqueMax
            //            it.trans_surplusGasoil = it.surplusConsommationGasoil
            //            it.trans_pourcentageConsommation = it.pourcentageConsommation
        }
    }
    
    def setDateDepartMois_Annee(){
        objet.setDateDepartMois()
        objet.setDateDepartAnnee()
    }
    
    def updateDateDepartMois_Annee(){
        this.listeObjets.each {
            it.dateDepartMois = it.dateDepart[Calendar.MONTH]+1
            it.dateDepartAnnee = it.dateDepart[Calendar.YEAR]
            interventionService.update(it)
        }
    }
    
    
}
