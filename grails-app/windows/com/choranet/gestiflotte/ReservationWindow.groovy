
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
import org.springframework.web.context.request.RequestContextHolder


/**
 * Reservation Window Object
 **/
class ReservationWindow extends SuperWindow {
    
    def vehiculeService
    
    def societeService
    /**
     * Service pour la gestion de l'objet Reservation
     **/
    def reservationService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class ReservationWindow
     **/
    private Log logger = LogFactory.getLog(ReservationWindow.class)
    
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
     * liste de responsableReservations
     **/	
    def responsableReservationss	
    /**
     * responsableReservations  selectionn�
     **/
    def responsableReservationsSelected
                
    /**
     * liste de client
     **/	
    def clients	
    /**
     * client  selectionn�
     **/
    def clientSelected
                
    /**
     * Constructeur
     **/
    public ReservationWindow (vehiculeService, societeService) {
        super(Reservation.class)
        this.vehiculeService = vehiculeService
        this.societeService = societeService
        this.specialeInitialisation()
    }  

    protected SuperService getService() {
        return this.reservationService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Reservations"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Reservations.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Reservations.pdf"
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
        String titrerapport = "Rapport des Reservations"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Reservations.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Reservations.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def specialeInitialisation() {
        
        conducteurs = Intervenant.list()		
        if(conducteurs.size() > 0)
        conducteurSelected = conducteurs.get(0)
        else
        conducteurSelected = null
                    
        vehicules = vehiculeService.getListVehiculesDeServiceWithoutVoitures()	
        if(vehicules.size() > 0) {
            vehiculeSelected = vehicules.get(0) 
            objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        }
        else {
            vehiculeSelected = null
            objet.prixParJour = null
        }
        
                    
        responsableReservationss = Utilisateur.list()		
        if(responsableReservationss.size() > 0) {
            def session = RequestContextHolder.currentRequestAttributes().getSession()
            responsableReservationsSelected = responsableReservationss.find{ it.id == session.utilisateur.id }
        }
                    
        clients = societeService.getListSocietesHorsGroup()		
        if(clients.size() > 0)
        clientSelected = clients.get(0)
        else
        clientSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            conducteurs = Intervenant.list()
        }	
        if(conducteurs.size() > 0)
        conducteurSelected = conducteurs.get(0)
        else
        conducteurSelected = null
                    	
        if(del) {
            vehicules = vehiculeService.getListVehiculesDeServiceWithoutVoitures()	
        }	
        if(vehicules.size() > 0) {
            vehiculeSelected = vehicules.get(0)
            objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        }
        else {
            vehiculeSelected = null
            objet.prixParJour = null
        }
                    	
        if(del) {
            responsableReservationss = Utilisateur.list()
        }	
        if(responsableReservationss.size() > 0) {
            def session = RequestContextHolder.currentRequestAttributes().getSession()
            responsableReservationsSelected = responsableReservationss.find{ it.id == session.utilisateur.id }
        }
      
        if(del) {
            clients = societeService.getListSocietesHorsGroup()	
        }	
        if(clients.size() > 0)
        clientSelected = clients.get(0)
        else
        clientSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.conducteur = conducteurSelected
        if(conducteurs.size() > 0) {
            def binderconducteur = new AnnotateDataBinder(this.getFellow("coconducteurs"))
            conducteurSelected = conducteurs.get(0)
            binderconducteur.loadAll()
        }
        else
        conducteurSelected = null
                    		
        objet.vehicule = vehiculeSelected
        if(vehicules.size() > 0) {
            def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
            vehiculeSelected = vehicules.get(0)
            bindervehicule.loadAll()
        }
        else {
            vehiculeSelected = null
            objet.prixParJour = null
        }
                    		
        objet.responsableReservations = responsableReservationsSelected
                    		
        objet.client = clientSelected
        if(clients.size() > 0) {
            def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
            clientSelected = clients.get(0)
            binderclient.loadAll()
        }
        else
        clientSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def binderconducteur = new AnnotateDataBinder(this.getFellow("coconducteurs"))
        conducteurSelected = conducteurs.find{ it.id == Reservation.findById(objet.id).conducteur.id }
        binderconducteur.loadAll()
                    		
        def bindervehicule = new AnnotateDataBinder(this.getFellow("covehicules"))
        vehiculeSelected = vehicules.find{ it.id == Reservation.findById(objet.id).vehicule.id }
        if(objetSelected == null) {
            objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        }
        bindervehicule.loadAll()
                    		
        def binderresponsableReservations = new AnnotateDataBinder(this.getFellow("coresponsableReservationss"))
        responsableReservationsSelected = responsableReservationss.find{ it.id == Reservation.findById(objet.id).responsableReservations.id }
        binderresponsableReservations.loadAll()
                    		
        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        clientSelected = clients.find{ it.id == Reservation.findById(objet.id).client.id }
        binderclient.loadAll()
                    
    }
    /**
     * Fonction qui se charge de sauveguarder un nouveau �l�ment de article
     **/
    def add() {
        try {  
            def isReservationValid = reservationService.validatedReservation(objet, vehiculeSelected)
            def isResForInterventionValid = reservationService.validatedReservationForIntervention(objet, vehiculeSelected)
            if  (isReservationValid && isResForInterventionValid) {
                // au lieu de super.add()
                actualiserValeurAssociation()
                try {     
                    getService().save(objet)
                } catch(Exception ex) {
                    logger.error "Error: ${ex.message}", ex
                    Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
                } finally {
                    objet = clazz.newInstance()
                    objet.prixParJour = vehiculeSelected.categorie.prixJournalier
                    
                    rafraichirField()
                    rafraichirList()
                    activerBoutons(false)
                }
            }
            else {
                Messagebox.show("Echec lors de l'ajout d'une réservation : véhicule déjà réservé ou mise en service pendant cette période \n[" +objet.dateDepart +" , "+ objet.dateRestitution+"] \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        } finally {
        }
    }
    
    def confirmerReservation () {
        try{
            def isReservationValid = reservationService.validatedReservation(objet, vehiculeSelected)
            def isResForInterventionValid = reservationService.validatedReservationForIntervention(objet, vehiculeSelected)
            if  (isReservationValid && isResForInterventionValid) {
                reservationService.generateIntervention(objet) 
                super.delete()
            } else {
                Messagebox.show("Echec lors de la confirmation de la présente résérvation : véhicule déjà réservé ou mise en service pendant cette période \n[" +objet.dateDepart +" , "+ objet.dateRestitution+"] \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
                objet.dateDepart = null
                objet.dateRestitution = null
                rafraichirField()
                rafraichirList()
            }
        } catch(Exception ex){
            logger.error "Error: Erreur pendant la confirmation de réservation ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
    }
    
    /**
     * Activer ou d�sactiver les boutons d'ajout, suppression, modfication
     **/
    def activerBoutons(visible) {
        this.getFellow("btnUpdate").visible = visible
        this.getFellow("btnDelete").visible = visible
        this.getFellow("btnCancel").visible = visible
        this.getFellow("btnSave").visible = !visible
        this.getFellow("btnConfirm").visible = visible

        //this.getFellow("btnExport").visible = !visible
        this.getFellow("westPanel").open = visible   
        //        this.getFellow("fieldDateDepart").disabled = visible
        //        this.getFellow("fieldDateRestitution").disabled = visible
        // this.getFellow("covehicules").disabled = visible
    }
    
    def update() {
        try {
            def isReservationValid = reservationService.validatedReservation(objet, vehiculeSelected)
            def isResForInterventionValid = reservationService.validatedReservationForIntervention(objet, vehiculeSelected)
            if  (isReservationValid && isResForInterventionValid) {
                super.update()
            } else {
                Messagebox.show("Echec lors de la mise à jour d'une résérvation : véhicule déjà réservé ou mise en service pendant cette période \n[" +objet.dateDepart +" , "+ objet.dateRestitution+"] \n", "Erreur", Messagebox.OK, Messagebox.ERROR)
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
    
    def changerDeVoiture() {
        objet.prixParJour = vehiculeSelected.categorie.prixJournalier
        def coPrixParJour = this.getFellow("fieldPrixParJour")
        def binder3 = new AnnotateDataBinder(coPrixParJour)
        binder3.loadAll()
        
    }
}

