
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
 * Categorie Window Object
 **/
class CategorieWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Categorie
     **/
    def categorieService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class CategorieWindow
     **/
    private Log logger = LogFactory.getLog(CategorieWindow.class)
    
    /**
     * liste de categoryParente
     **/	
    def categoryParentes	
    /**
     * categoryParente  selectionn�
     **/
    def categoryParenteSelected
                
    /**
     * Constructeur
     **/
    public CategorieWindow () {
        super(Categorie.class)
    }  

    protected SuperService getService() {
        return this.categorieService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.get(1).nomsociete
        String titrerapport = "Rapport des Categories"
        def reportDef = new JasperReportDef(name:'rapport_des_Categories.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Categories.pdf"
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
        String titrerapport = "Rapport des Categories"
        def reportDef = new JasperReportDef(name:'rapport_des_Categories.jasper',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjets,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Categories.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        categoryParentes = Categorie.list()		
    }
    def add() {
        super.add()
        reinitialiserAssociation(true)
    }
    def update() {
        super.update()
        reinitialiserAssociation(true)
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
        categoryParentes = Categorie.list()
        }	
        categoryParenteSelected = null   
        def bindercategoryParente = new AnnotateDataBinder(this.getFellow("cocategoryParentes")) 
        bindercategoryParente.loadAll() 
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.categoryParente = categoryParenteSelected
        if(categoryParentes.size() > 0) {
            def bindercategoryParente = new AnnotateDataBinder(this.getFellow("cocategoryParentes"))
            categoryParenteSelected = categoryParentes.get(0)
            bindercategoryParente.loadAll()
        }
        else
        categoryParenteSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        def bindercategoryParente = new AnnotateDataBinder(this.getFellow("cocategoryParentes"))        		
        if(objet.categoryParente != null) {
            categoryParenteSelected = categoryParentes.find{ it.id == Categorie.findById(objet.id).categoryParente.id }
        } else {
            categoryParenteSelected = null
        }
        bindercategoryParente.loadAll()    

    }
}

