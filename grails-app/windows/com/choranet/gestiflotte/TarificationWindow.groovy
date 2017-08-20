
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
 * Facturation Window Object
 **/
class TarificationWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Tarification
     **/
    def tarificationService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class TarificationWindow
     **/
    private Log logger = LogFactory.getLog(TarificationWindow.class)
    
    /**
     * liste des clients
     **/
    def clients	
    /**
     * clients  selectionnés
     **/
    def clientsSelected
    
    /**
     * mode de tarification selectionné
     **/
    def modeTarificationSelected
    
    /**
     * liste des trajets
     **/
    def trajets
    
    /**
     * le trajet selectionné pour la tarification en cours
     **/
    def trajetSelected
    
    /**
     * liste des régions
     **/
    def regions
    
    /**
     * la region selectionnée pour la tarification en cours
     **/
    def regionSelected
              
    def excelImporterService
    
    /**
     * Constructeur
     **/
    public TarificationWindow () {
        super(Tarification.class)
    }  

    def importation(media) {
        String resultat = excelImporterService.importerTarifications(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    } 
    
    protected SuperService getService() {
        return this.tarificationService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Tarifications"
        def reportDef = new JasperReportDef(name:'rapport_des_Tarifications.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Tarifications.pdf"
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
        String titrerapport = "Rapport des Tarifications"
        def reportDef = new JasperReportDef(name:'rapport_des_Tarifications.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Tarifications.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui gère l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        clients = Societe.list().sort{it.raisonSociale}
        clientsSelected = null// = new ArrayList()
        
        trajets = Trajet.list().sort{it.source}
        trajetSelected = null
        
        regions = Region.list().sort{it.intitule}
        regionSelected = null
    }
    /**
     * Fonction qui permet de ré-initaliser l'association au niveau de l'interface
     * @param del si c'est une réinitionalisation aprés une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            clients = Societe.list().sort{it.raisonSociale}
            trajets = Trajet.list().sort{it.source}
            regions = Region.list().sort{it.intitule}
        }
        this.getFellow("lstclients").clearSelection()
        clientsSelected = null// = new ArrayList()
        
        //        if(trajets.size() > 0) {
        //            trajetSelected = trajets.get(0)
        //        }
        //        else {
        trajetSelected = null
        //        }
        
        //        if(regions.size() > 0) {
        //            regionSelected = regions.get(0)
        //        }
        //        else {
        regionSelected = null
        //        }
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        
        objet.clients = clientsSelected
        this.getFellow("lstclients").clearSelection()
        clientsSelected = null// = new ArrayList() 
        
        objet.modeTarif = modeTarificationSelected
        def bindermode = new AnnotateDataBinder(this.getFellow("comode"))
        modeTarificationSelected = null
        bindermode.loadAll()
        
        trajets = Trajet.list()
        objet.trajet = trajetSelected
        if(trajets.size() > 0) {
            def bindertrajet = new AnnotateDataBinder(this.getFellow("cotrajets"))
            trajetSelected = null //trajets.get(0)
            bindertrajet.loadAll()
        }
        
        regions = Region.list()
        objet.region = regionSelected
        if(regions.size() > 0) {
            def binderregion = new AnnotateDataBinder(this.getFellow("coregions"))
            regionSelected = null //regions.get(0)
            binderregion.loadAll()
        }
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        
        def binderclients = new AnnotateDataBinder(this.getFellow("lstclients"))
        clientsSelected = objetSelected.clients
        binderclients.loadAll()
        
        def bindercomode = new AnnotateDataBinder(this.getFellow("comode"))          
        modeTarificationSelected = objetSelected.modeTarif
        bindercomode.loadAll()
        
        if(objetSelected.modeTarif == "Tonnage/Trajet" || objetSelected.modeTarif == "Forfitaire/Trajet"){
            if(trajets.size() > 0) {
                def bindertrajet = new AnnotateDataBinder(this.getFellow("cotrajets"))
                trajetSelected = trajets.find{ it.id == Tarification.findById(objet.id).trajet.id }
                bindertrajet.loadAll()
            }
        }else {
            if (objetSelected.modeTarif == "Forfitaire/Region"){
                if(regions.size() > 0) {
                    def binderregion = new AnnotateDataBinder(this.getFellow("coregions"))
                    regionSelected = regions.find{ it.id == Tarification.findById(objet.id).region.id }
                    binderregion.loadAll()
                }
            }
        }
            
    }
    
    def newRecord() {
        super.newRecord()
        this.getFellow("btnCancel").visible = true
    }
}

