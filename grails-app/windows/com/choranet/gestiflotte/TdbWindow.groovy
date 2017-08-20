
package com.choranet.gestiflotte


import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.zkoss.zksandbox.Category
import org.springframework.web.context.request.RequestContextHolder
import com.choranet.gestiflotte.util.DateUtil
import com.choranet.gestiflotte.util.*
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import com.choranet.zk.*;
import org.codehaus.groovy.runtime.TimeCategory

/**
 * Tdb Window Object
 **/
class TdbWindow extends Window {
   
    def vehiculeService
    def interventionService
    def bonFraisCirculationService
    def fraisCirculationPeriodiqueService
    def entretienService
    def entretienPeriodiqueService
    def jasperService
    def reservationService
    def paiementGroupService
    def deplacementService
    
    def listeAlertsFcp
    
    def existAlertsFcp = false
    
    def listeAlertsEp
    
    def existAlertsEp  = false
    
    def listeReservations
    
    def existAlertsReservations = false
    
    def listeVG
    
    def existVG = false
    
    def etatparkmdel
    
    def depensesgains
    
    def vehicules
    
    def vehicule
    
    def cheque
    
    def cheques
    
    def dateDebutDepenses
    
    def dateFinDepenses
    
    def dateDebutInterventions
    
    def dateFinInterventions
    
    def dateDebutPaiementGroup
    
    def dateFinPaiementGroup
    
    def dateDebutDeplacement
    
    def dateFinDeplacement
    /**
     * Logger de la class ClientWindow
     **/
    private Log logger = LogFactory.getLog(TdbWindow.class)
	
    /**
     * Constructeur
     **/
    public TdbWindow (vehiculeService, interventionService, bonFraisCirculationService,
        fraisCirculationPeriodiqueService, entretienService, entretienPeriodiqueService, reservationService) {
        
        this.vehiculeService = vehiculeService
        this.interventionService = interventionService
        this.bonFraisCirculationService = bonFraisCirculationService
        this.fraisCirculationPeriodiqueService = fraisCirculationPeriodiqueService
        this.entretienService = entretienService
        this.entretienPeriodiqueService = entretienPeriodiqueService
        this.reservationService = reservationService
        
        listeAlertsFcp = fraisCirculationPeriodiqueService.getAlertsFraisCirculationPeriodiques()
        listeAlertsEp = entretienPeriodiqueService.getAlertsEntretienPeriodique()
        listeReservations = reservationService.getAlertsReservation()
        listeVG = vehiculeService.getListVehiculesGarree()
        
        if(listeReservations != null && listeReservations.size() > 0) {
            existAlertsReservations = true
        }
        if(listeAlertsFcp != null && listeAlertsFcp.size() > 0) {
            existAlertsFcp = true
        }
        if(listeAlertsEp != null && listeAlertsEp.size() > 0) {
            existAlertsEp = true
        }
        if(listeVG != null && listeVG.size() > 0) {
            existVG = true
        }
        
        etatparkmdel = new SimplePieModel();
        etatparkmdel.setValue("Garés", vehiculeService.getNbVehiculeGarree());
        etatparkmdel.setValue("Loués", vehiculeService.getNbVehiculeLouee());
        
        
        Date d1 = new Date()
        String mois1 = DateUtil.recupererMoisDate(d1);
        Date[] dateDebutFin1 = DateUtil.recupereDateDebutDateFinMois(d1);
        def benefices1 = interventionService.getSommeBenifices(dateDebutFin1[0], dateDebutFin1[1]);
        def charges1 = bonFraisCirculationService.getFraisCirculation(dateDebutFin1[0], dateDebutFin1[1])  + fraisCirculationPeriodiqueService.getFraisCirculationPeriodique(dateDebutFin1[0], dateDebutFin1[1]) + entretienService.getFraisEntretien(dateDebutFin1[0], dateDebutFin1[1]) + entretienPeriodiqueService.getFraisEntretienPeriodique(dateDebutFin1[0], dateDebutFin1[1]);
        
        Date d2 = DateUtil.recupererDateMoisPrecedent(d1);
        String mois2 = DateUtil.recupererMoisDate(d2);
        Date[] dateDebutFin2 = DateUtil.recupereDateDebutDateFinMois(d2);
        def benefices2 = interventionService.getSommeBenifices(dateDebutFin2[0], dateDebutFin2[1]);
        def charges2 = bonFraisCirculationService.getFraisCirculation(dateDebutFin2[0], dateDebutFin2[1]) + fraisCirculationPeriodiqueService.getFraisCirculationPeriodique(dateDebutFin2[0], dateDebutFin2[1]) + entretienService.getFraisEntretien(dateDebutFin2[0], dateDebutFin2[1]) + entretienPeriodiqueService.getFraisEntretienPeriodique(dateDebutFin2[0], dateDebutFin2[1]);
            
        Date d3 = DateUtil.recupererDateMoisPrecedent(d2);
        String mois3 = DateUtil.recupererMoisDate(d3);
        Date[] dateDebutFin3 = DateUtil.recupereDateDebutDateFinMois(d3);
        def benefices3 = interventionService.getSommeBenifices(dateDebutFin3[0], dateDebutFin3[1]);
        def charges3 = bonFraisCirculationService.getFraisCirculation(dateDebutFin3[0], dateDebutFin3[1]) + fraisCirculationPeriodiqueService.getFraisCirculationPeriodique(dateDebutFin3[0], dateDebutFin3[1]) + entretienService.getFraisEntretien(dateDebutFin3[0], dateDebutFin3[1]) + entretienPeriodiqueService.getFraisEntretienPeriodique(dateDebutFin3[0], dateDebutFin3[1]);  
            
        Date d4 = DateUtil.recupererDateMoisPrecedent(d3);
        String mois4 = DateUtil.recupererMoisDate(d4);
        Date[] dateDebutFin4 = DateUtil.recupereDateDebutDateFinMois(d4);
        def benefices4 = interventionService.getSommeBenifices(dateDebutFin4[0], dateDebutFin4[1]);
        def charges4 = bonFraisCirculationService.getFraisCirculation(dateDebutFin4[0], dateDebutFin4[1]) + fraisCirculationPeriodiqueService.getFraisCirculationPeriodique(dateDebutFin4[0], dateDebutFin4[1]) + entretienService.getFraisEntretien(dateDebutFin4[0], dateDebutFin4[1]) + entretienPeriodiqueService.getFraisEntretienPeriodique(dateDebutFin4[0], dateDebutFin4[1]);
        
        depensesgains = new SimpleCategoryModel();
        depensesgains.setValue("Dépenses", mois4, charges4);
        depensesgains.setValue("Dépenses", mois3, charges3);
        depensesgains.setValue("Dépenses", mois2, charges2);
        depensesgains.setValue("Dépenses", mois1, charges1);
        depensesgains.setValue("Gains", mois4, benefices4);
        depensesgains.setValue("Gains", mois3, benefices3);
        depensesgains.setValue("Gains", mois2, benefices2);
        depensesgains.setValue("Gains", mois1, benefices1);
        
        vehicules = Vehicule.list()
        if(vehicules != null && vehicules.size() > 0) {
            vehicule = vehicules.get(0)
        }
        
        cheques = PaiementGroup.list()
        if(cheques != null && cheques.size() > 0) {
            cheque = cheques.get(0)
        }
    }  
    
    def genererRapportInterventions() {         
        if(dateDebutInterventions == null || dateFinInterventions == null) {
            Messagebox.show("Veuillez indiquez l'interval du rapport","Erreurs",Messagebox.OK, Messagebox.ERROR)
        } else {
            def dateFinInterventionsTemp 
            use(TimeCategory) {
                dateFinInterventionsTemp = dateFinInterventions + 23.hours + 59.minutes + 59.seconds
            }
            def interventions = interventionService.getInterventionsBetween(dateDebutInterventions, dateFinInterventionsTemp)
            String nomsociete = ChoraClientInfo.get(1).nomsociete
            String titrerapport = "Etat des interventions"
            def reportDef = new JasperReportDef(name:'rapport_des_Interventionsparvehicule.jasper',
                fileFormat:JasperExportFormat.PDF_FORMAT,
                reportData : interventions,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'datedebut':dateDebutInterventions, 'datefin':dateFinInterventions]
            )
            def bit = jasperService.generateReport(reportDef).toByteArray()
            def nom_fichier = "rapport_interventions_entre_"+DateUtil.getDateTime("dd_MM_yyyy",dateDebutInterventions)+"_et_"+DateUtil.getDateTime("dd_MM_yyyy",dateFinInterventions)+".pdf"
            Filedownload.save(bit, "application/file",
                nom_fichier);
        }
    }
    
    def genererRapportPaiementGroup() {
        if(dateDebutPaiementGroup == null || dateFinPaiementGroup == null) {
            Messagebox.show("Veuillez indiquez l'interval du rapport","Erreurs",Messagebox.OK, Messagebox.ERROR)
        } else {
            def dateFinPaiementGroupTemp
            use(TimeCategory) {
                dateFinPaiementGroupTemp = dateFinPaiementGroup + 23.hours + 59.minutes + 59.seconds
            }
            def paiementGroups = paiementGroupService.getPaiementGroupBetween(dateDebutPaiementGroup, dateFinPaiementGroupTemp)
            String nomsociete = ChoraClientInfo.get(1).nomsociete
            String titrerapport = "Rapport des paiements"
            def reportDef = new JasperReportDef(name:'rapport_des_PaiementGroupsParSociete.jasper',
                fileFormat:JasperExportFormat.PDF_FORMAT,
                reportData : paiementGroups,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,'datedebut':dateDebutPaiementGroup,'datefin':dateFinPaiementGroup]
            )
            def bit = jasperService.generateReport(reportDef).toByteArray()
            def nom_fichier = "rapport_paiements_entre_"+DateUtil.getDateTime("dd_MM_yyyy",dateDebutPaiementGroup)+"_et_"+DateUtil.getDateTime("dd_MM_yyyy",dateFinPaiementGroup)+".pdf"
            Filedownload.save(bit, "application/file",
                nom_fichier);
        }
    }
    
    def genererRapportDeplacement() {
        if(dateDebutDeplacement == null || dateFinDeplacement == null) {
            Messagebox.show("Veuillez indiquez l'interval du rapport","Erreurs",Messagebox.OK, Messagebox.ERROR)
        } else {
            def dateFinDeplacementTemp
            use(TimeCategory) {
                dateFinDeplacementTemp = dateFinDeplacement + 23.hours + 59.minutes + 59.seconds
            }
            def deplacements = deplacementService.getDeplacementBetween(dateDebutDeplacement, dateFinDeplacementTemp)
            String nomsociete = ChoraClientInfo.get(1).nomsociete
            String titrerapport = "Rapport des déplacements"
            def reportDef = new JasperReportDef(name:'rapport_des_DeplacementsParVehicule.jasper',
                fileFormat:JasperExportFormat.PDF_FORMAT,
                reportData : deplacements,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,'datedebut':dateDebutDeplacement,'datefin':dateFinDeplacement]
            )
            def bit = jasperService.generateReport(reportDef).toByteArray()
            def nom_fichier = "rapport_deplacements_entre_"+DateUtil.getDateTime("dd_MM_yyyy",dateDebutDeplacement)+"_et_"+DateUtil.getDateTime("dd_MM_yyyy",dateFinDeplacement)+".pdf"
            Filedownload.save(bit, "application/file",
                nom_fichier);
        }
    }
    
    def genererRapportPaiementGroupDetails() {
        
        def fraisCirculationsPeriodiques = cheque.fraisCirculationPeriodiques
        def fraisCirculations = cheque.bonFraisCirculations
        def entretienPeriodique = cheque.entretienPeriodiques
        def entretien = cheque.entretiens
                    
        def PaiementGroupFcpSubReport = Utilitaire.getPaiementGroupFcpSubReport(getClass())
        def PaiementGroupFcSubReport = Utilitaire.getPaiementGroupFcSubReport(getClass())
        def PaiementGroupEpSubReport = Utilitaire.getPaiementGroupEpSubReport(getClass())
        def PaiementGroupESubReport = Utilitaire.getPaiementGroupESubReport(getClass())
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport détails du chèque"
        
        def reportDef = new JasperReportDef(name:'rapport_des_PaiementGroupsParSocieteDetails.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : [cheque],
            parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,
                          'PaiementGroupFcpSubReport' : PaiementGroupFcpSubReport,
                          'PaiementGroupFcSubReport' : PaiementGroupFcSubReport,
                          'PaiementGroupEpSubReport' : PaiementGroupEpSubReport,
                          'PaiementGroupESubReport' : PaiementGroupESubReport,
                          'fraisCirculationsPeriodiques' : fraisCirculationsPeriodiques,
                          'fraisCirculations' : fraisCirculations,
                          'entretienPeriodique' : entretienPeriodique,
                          'entretien' : entretien
            ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Details_Cheque_N_" + cheque.toString() + ".pdf"
        Filedownload.save(bit, "application/file", nom_fichier);
    }
    
    //    def genererRapportDepenses() {
    //        if(dateDebutDepenses == null || dateFinDepenses == null) {
    //            Messagebox.show("Veuillez indiquez l'interval du rapport","Erreurs",Messagebox.OK, Messagebox.ERROR)
    //        } else {
    //            String nomsociete = ChoraClientInfo.get(1).nomsociete
    //            String titrerapport = "Rapport des frais"
    //            def bonFraisCirculationsPeriodiques = fraisCirculationPeriodiqueService.getDepensesBetween(dateDebutDepenses, dateFinDepenses)
    //            def bonFraisCirculations = bonFraisCirculationService.getDepensesBetween(dateDebutDepenses, dateFinDepenses)
    //            def entretienPeriodique = entretienPeriodiqueService.getDepensesBetween(dateDebutDepenses, dateFinDepenses)
    //            def entretien = entretienService.getDepensesBetween(dateDebutDepenses, dateFinDepenses)
    //            def FcpSubReport = Utilitaire.getFcpSubReport(getClass())
    //            def FcSubReport = Utilitaire.getFcSubReport(getClass())
    //            def EpSubReport = Utilitaire.getEpSubReport(getClass())
    //            def ESubReport = Utilitaire.getESubReport(getClass())
    //            def reportDef = new JasperReportDef(name:'Rapport_des_frais.jasper',
    //                fileFormat:JasperExportFormat.PDF_FORMAT,
    //                reportData : vehicules,
    //                parameters : ['nomsociete':nomsociete, 
    //                              'titrerapport':titrerapport,
    //                              'bonFraisCirculationsPeriodiques' : bonFraisCirculationsPeriodiques,
    //                              'bonFraisCirculations' : bonFraisCirculations,
    //                              'entretienPeriodique' : entretienPeriodique,
    //                              'entretien' : entretien,
    //                              'FcpSubReport' : FcpSubReport,
    //                              'FcSubReport' : FcSubReport,
    //                              'EpSubReport' : EpSubReport,
    //                              'ESubReport' : ESubReport
    //                ]
    //            )
    //            def bit = jasperService.generateReport(reportDef).toByteArray()
    //            def nom_fichier = "rapport_frais_entre_"+DateUtil.getDateTime("dd_MM_yyyy",dateDebutDepenses)+"_et_"+DateUtil.getDateTime("dd_MM_yyyy",dateFinDepenses)+".pdf"
    //            Filedownload.save(bit, "application/file", nom_fichier);
    //        }
    //    }
    //    
    //    def genererRapportLocations() {
    //        if(dateDebutLocations == null || dateFinLocations == null) {
    //            Messagebox.show("Veuillez indiquez l'interval du rapport","Erreurs",Messagebox.OK, Messagebox.ERROR)
    //        } else {
    //            def interventions = interventionService.getLocationsBetween(dateDebutLocations, dateFinLocations)
    //            String nomsociete = ChoraClientInfo.get(1).nomsociete
    //            String titrerapport = "Rapport des interventions"
    //            def reportDef = new JasperReportDef(name:'Rapport_des_interventions.jasper',
    //                fileFormat:JasperExportFormat.PDF_FORMAT,
    //                reportData : interventions,
    //                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport]
    //            )
    //            def bit = jasperService.generateReport(reportDef).toByteArray()
    //            def nom_fichier = "rapport_interventions_entre_"+DateUtil.getDateTime("dd_MM_yyyy",dateDebutLocations)+"_et_"+DateUtil.getDateTime("dd_MM_yyyy",dateFinLocations)+".pdf"
    //            Filedownload.save(bit, "application/file",
    //                nom_fichier);
    //        }
    //    }
    //    
    //    def genererRapportBenifices() {
    //        
    //        def bonFraisCirculationsPeriodiques = fraisCirculationPeriodiqueService.getDepensesVehicule(vehicule)
    //        def bonFraisCirculations = bonFraisCirculationService.getDepensesVehicule(vehicule)
    //        def entretienPeriodique = entretienPeriodiqueService.getDepensesVehicule(vehicule)
    //        def entretien = entretienService.getDepensesVehicule(vehicule)
    //        def interventions = interventionService.getLocationsVehicule(vehicule)
    //            
    //        def VehiculeFcpSubReport = Utilitaire.getVehiculeFcpSubReport(getClass())
    //        def VehiculeFcSubReport = Utilitaire.getVehiculeFcSubReport(getClass())
    //        def VehiculeEpSubReport = Utilitaire.getVehiculeEpSubReport(getClass())
    //        def VehiculeESubReport = Utilitaire.getVehiculeESubReport(getClass())
    //        def VehiculeRevenusReport = Utilitaire.getVehiculeRevenusReport(getClass())
    //        String nomsociete = ChoraClientInfo.get(1).nomsociete
    //        String titrerapport = "Rapport des frais et revenus"
    //        
    //        def reportDef = new JasperReportDef(name:'Rapport_des_frais_et_revenus.jasper',
    //            fileFormat:JasperExportFormat.PDF_FORMAT,
    //            reportData : [vehicule],
    //            parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,
    //                          'VehiculeFcpSubReport' : VehiculeFcpSubReport,
    //                          'VehiculeFcSubReport' : VehiculeFcSubReport,
    //                          'VehiculeEpSubReport' : VehiculeEpSubReport,
    //                          'VehiculeESubReport' : VehiculeESubReport,
    //                          'VehiculeRevenusReport' : VehiculeRevenusReport,
    //                          'bonFraisCirculationsPeriodiques' : bonFraisCirculationsPeriodiques,
    //                          'bonFraisCirculations' : bonFraisCirculations,
    //                          'entretienPeriodique' : entretienPeriodique,
    //                          'entretien' : entretien,
    //                          'interventions' : interventions
    //            ]
    //        )
    //        def bit = jasperService.generateReport(reportDef).toByteArray()
    //        def nom_fichier = "Frais_et_revenus_vehicule.pdf"
    //        Filedownload.save(bit, "application/file", nom_fichier);
    //    }
    
}

