package com.choranet.commun

import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.ByteArrayOutputStream
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.*;

import java.text.SimpleDateFormat;

import com.choranet.gestiflotte.*


class ExcelImporterService {

    private Log logger = LogFactory.getLog(ExcelImporterService.class)    
    
    static transactional = true   
    
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
    
    static String fs = System.getProperty('file.separator');
    static String ls = System.getProperty('line.separator');
    
    def societeService
    def vehiculeService
    def intervenantService
    def fournisseurService
    def trajetService
    def regionService
    def tarificationService
    
    def creerFichierLogImport(String nomFichier) {
        File filelog = new File(System.getProperty('user.home')+fs+nomFichier+dateFormat.format(new Date()))
        filelog.createNewFile();
        loggerInformations(filelog, System.getProperty('os.name'))
        loggerInformations(filelog, System.getProperty('os.arch'))
        loggerInformations(filelog, System.getProperty('user.dir'))
        loggerInformations(filelog, System.getProperty('user.home'))
        loggerInformations(filelog, System.getProperty('user.language'))
        loggerInformations(filelog, System.getProperty('java.version'))
        loggerInformations(filelog, System.getProperty('java.vendor'))
        return filelog;
    }
    
    
    def loggerInformations(File filelog, Object info) {
        filelog << info
        filelog << ls
    }
    
    def chargerDonneesLigne(Row row, types) {
        int taille = types.size()
        Map map = [:]        
        def cles = types.keySet().toArray();
        for(int i = 0 ; i < taille ; i++ )  {
            Cell cell = row.getCell(i)
            def cle = cles[i]
            if(cell != null) {
                if(types[cle].equals("String")) {
                    switch(cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING : 
                        map[cle] = cell.getStringCellValue(); 
                        break;
                        case Cell.CELL_TYPE_NUMERIC : 
                        map[cle] = String.valueOf(cell.getNumericCellValue());
                        break;
                        case Cell.CELL_TYPE_BOOLEAN : 
                        map[cle] = String.valueOf(cell.getBooleanCellValue());
                        break;
                    }
                    if(map[cle] != null) {
                        map[cle] = map[cle].trim()
                        if(map[cle].equals("")) {
                            map[cle] = null
                        }
                    }
                } else if (types[cle].equals("Numeric")) {
                    map[cle] = cell.getNumericCellValue();
                } else if (types[cle].equals("Date")) {
                    map[cle] = cell.getDateCellValue();
                } else if (types[cle].equals("Boolean")) {
                    map[cle] = cell.getBooleanCellValue();
                } else {
                    map[cle] = null
                }
            } else {
                map[cle] = null
            }
        }
        return map
    }
    
    def chargerDonneesParDefaut(Map objet, Map valeursParDefaut) {
        valeursParDefaut.entrySet().each {
            if(objet[it.key] == null) {
                objet[it.key] = it.value
            }
        }
    } 
    
    def validerChampsRequis(Map objet, champsrequis) {
        for(champs in champsrequis) {
            if(objet[champs] == null) {
                return champs
            }
        }
        return null
    }
    /**
     *  Import des des véhicules
     **/
    def importerVehicules(Object media) {
        File fileLog = creerFichierLogImport("log_import_vehicules")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["code": "String", "marque" : "String", "model" : "String", "categorie" : "String", "energie" : "String", "immatriculation" : "String"
                , "numeroRemorque" : "String", "dateMiseEnCirculation" : "Date", "vehiculeDeService" : "Boolean", "kilometrage" : "Numeric" , "consommationGasoilMin" : "Numeric"
                , "consommationGasoilMax" : "Numeric", "consommationHuileMin" : "Numeric", "consommationHuileMax" : "Numeric", "prorprietaireSociete" : "String"];
        
            def defaultSociete = societeService.getListSocietesDuGroupe().get(0);
            
            def valeursParDefaut = ["vehiculeDeService" : true, "kilometrage" : 0d, "consommationGasoilMin" : 0d
                , "consommationGasoilMax" : 0d, "consommationHuileMin" : 0d, "consommationHuileMax" : 0d]
            
            def valeursObligatoire = ["code", "marque", "model", "immatriculation", "kilometrage", "consommationGasoilMin"
                , "consommationGasoilMax" ,"consommationHuileMin", "consommationHuileMax"]
            
            loggerInformations(fileLog, "Début import des produits : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                    
                    if(map.prorprietaireSociete != null && !map.prorprietaireSociete.equals("")) {
                        map.prorprietaireSociete = Societe.findByRaisonSocialeIlike(map.prorprietaireSociete)
                        if(map.prorprietaireSociete == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " société inexistante")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    if(map.energie != null && !map.energie.equals("")) {
                        map.energie = Energie.findByLibelleIlike(map.energie)
                        if(map.energie == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " mode Energie inexistant")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    if(map.categorie != null && !map.categorie.equals("")) {
                        map.categorie = Categorie.findByLibelleIlike(map.categorie)
                        if(map.categorie == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " categorie inexistante")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    if(map.model != null && !map.model.equals("")) {
                        map.model = Model.findByLibelleIlike((String)map.model)
                        if(map.model == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " model inexistant")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    
                    if(map.marque != null && !map.marque.equals("")) {
                        map.marque = Marque.findByLibelleIlike(map.marque)
                        if(map.marque == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " marque inexistant")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    
                    chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldPdt1 = Vehicule.findByCodeIlike(map.code);
                        def oldPdt2 = Vehicule.findByImmatriculationIlike(map.immatriculation);
                        if(oldPdt1 != null || oldPdt2 != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " la vehicule : " + map.immatriculation + " existe déjà")
                            nbEchec++
                        } else {
                            def newVhl = new Vehicule(code: map.code, marque : map.marque, model : map.model, categorie : map.categorie, energie : map.energie, immatriculation : map.immatriculation
                                , numeroRemorque : map.numeroRemorque, dateMiseEnCirculation : map.dateMiseEnCirculation, vehiculeDeService : map.vehiculeDeService, kilometrage : map.kilometrage
                                , consommationGasoilMin : map.consommationGasoilMin, consommationGasoilMax : map.consommationGasoilMax
                                , consommationHuileMin : map.consommationHuileMin, consommationHuileMax : map.consommationHuileMax, prorprietaireSociete : map.prorprietaireSociete);
                                    
                            vehiculeService.save(newVhl)
                            nbImport++
                        }
                    }
                    
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import véhicules : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    /**
     *  Import des des véhicules
     **/
    def importerIntervenants(Object media) {
        File fileLog = creerFichierLogImport("log_import_intervenants")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["code": "String", "cin" : "String", "nom" : "String", "prenom" : "String", "typePermis" : "String"
                , "numeroPermis" : "String", "sousTraite" : "Boolean", "lieuNaissance" : "String", "dateNaissance" : "Date", "adresse" : "String", "gsm" : "String"
                , "numeroCNSS" : "String", "email" : "String", "commission" : "Numeric" , "nationalite" : "String", "employer" : "String"];
            
            def valeursParDefaut = ["typePermis" : "N/A", "sousTraite" : false, "gsm" : "0000000000" , "commission" : 1d, "nationalite" : "Marocain"]
            
            def valeursObligatoire = ["code", "cin", "nom", "prenom", "typePermis" , "numeroPermis", "gsm", "commission"]
            
            loggerInformations(fileLog, "Début import des intervenants : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                    
                    if(map.nationalite != null && !map.nationalite.equals("")) {
                        map.nationalite = Nationalite.findByLibelleIlike(map.nationalite)
                        if(map.nationalite == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " nationalité inexistante")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    if(map.employer != null && !map.employer.equals("")) {
                        map.employer = Societe.findByRaisonSocialeIlike(map.employer)
                        if(map.employer == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " Employer inexistant")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    
                    chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldPdt1 = Intervenant.findByCodeIlike(map.code);
                        def oldPdt2 = Intervenant.findByCinIlike(map.cin);
                        if(oldPdt1 != null || oldPdt2 != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " l''intervenant avec le N° CIN : " + map.cin + " existe déjà")
                            nbEchec++
                        } else {
                            def newInt = new Intervenant(code: map.code, cin : map.cin, nom : map.nom, prenom : map.prenom, typePermis : map.typePermis
                                , numeroPermis : map.numeroPermis, sousTraite : map.sousTraite, lieuNaissance : map.lieuNaissance, dateNaissance : map.dateNaissance, adresse : map.adresse, 
                                gsm : map.gsm, numeroCNSS : map.numeroCNSS, email : map.email, commission : map.commission, nationalite : map.nationalite, employer : map.employer);
                                    
                            intervenantService.save(newInt)
                            nbImport++
                        }
                    }
                    
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import Intervenants : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    /**
     *  Import des trajets
     **/
    def importerTrajets(Object media) {
        File fileLog = creerFichierLogImport("log_import_trajets")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["code" : "String", "source" : "String", "destination" : "String", "kilometrage" : "Numeric"];
            
            //def valeursParDefaut = ["kilometrage" : 0d]
            
            def valeursObligatoire = ["code", "source", "destination", "kilometrage"]
            
            loggerInformations(fileLog, "Début import des trajets : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                                        
                    //chargerDonneesParDefaut(map, valeursParDefaut)
                    def map = chargerDonneesLigne(row, types);
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldPdt1 = Trajet.findByCodeIlike(map.code);
                        //def oldPdt2 = Trajet.findBySourceIlike(map.source);
                        if(oldPdt1 != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le trajet " + map.code + ":" + map.source + " exist déjà")
                            nbEchec++
                        } else {
                            def newTrajet = new Trajet(code: map.code, source : map.source, destination : map.destination, kilometrage : map.kilometrage);
                                    
                            trajetService.save(newTrajet)
                            nbImport++
                        }
                    }
                    
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import Trajets : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    /**
     *  Import des trajets
     **/
    def importerRegions(Object media) {
        File fileLog = creerFichierLogImport("log_import_regions")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["code" : "String", "intitule" : "String", "kilometrageMin" : "Numeric", "kilometrageMax" : "Numeric"];
            
            def valeursParDefaut = ["kilometrageMin" : 0d, "kilometrageMax" : 0d]
            
            def valeursObligatoire = ["code", "intitule"]
            
            loggerInformations(fileLog, "Début import des regions : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types)
                                        
                    chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldPdt1 = Region.findByCodeIlike(map.code);
                        //def oldPdt2 = Region.findByIntituleIlike(map.intitule);
                        if(oldPdt1 != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " la region : " + map.code + " exist déjà")
                            nbEchec++
                        } else {
                            def newRegion = new Region(code: map.code, intitule : map.intitule, kilometrageMin : map.kilometrageMin, kilometrageMax : map.kilometrageMax);
                                    
                            regionService.save(newRegion)
                            nbImport++
                        }
                    }
                    
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import Regions : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    /**
     *  Import des des véhicules
     **/
    def importerTarifications(Object media) {
        File fileLog = creerFichierLogImport("log_import_tarifications")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["code": "String", "modeTarif" : "String", "annee" : "Numeric", "debutPeriode" : "Numeric"
                , "finPeriode" : "Numeric", "quantite" : "Numeric", "unite" : "String", "prix" : "Numeric"
                , "trajet" : "String", "region" : "String", "client" : "String"];
            
            def valeursParDefaut = ["quantite" : 1.0, "prix" : 0d]
            
            def valeursObligatoire = ["code", "modeTarif", "unite", "prix"]
            
            loggerInformations(fileLog, "Début import des tarifs : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                    
                    if(map.modeTarif == "Forfitaire/Trajet" || map.modeTarif == "Tonnage/Trajet"){
                        if(map.trajet != null && !map.trajet.equals("")) {
                            map.trajet = Trajet.findByCodeIlike(map.trajet)
                            if(map.trajet == null) {
                                loggerInformations(fileLog, "Echec import ligne " + compteur + " trajet inexistant")
                                nbEchec++
                                compteur++
                                continue;
                            }else {
                                map.source = map.trajet.source
                                map.destination = map.trajet.destination
                            }
                        }
                    }else {
                        if (map.modeTarif == "Forfitaire/Region"){
                            if(map.region != null && !map.region.equals("")) {
                                map.region = Region.findByCodeIlike(map.region)
                                if(map.region == null) {
                                    loggerInformations(fileLog, "Echec import ligne " + compteur + " region inexistante")
                                    nbEchec++
                                    compteur++
                                    continue;
                                }
                            }
                        }
                    }
                    
                    if(map.client != null && !map.client.equals("")) {
                        map.client = Societe.findByCodeIlike(map.client)
                        if(map.client == null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " Client inexistant")
                            nbEchec++
                            compteur++
                            continue;
                        }
                    }
                    
                    chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldPdt1 = Tarification.findByCodeIlike(map.code);
                        if(oldPdt1 != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " la tarification : " + map.code + " existe déjà")
                            nbEchec++
                        } else {
                            def newTarif = new Tarification(code: map.code, modeTarif : map.modeTarif, annee : map.annee, debutPeriode : map.debutPeriode
                                , finPeriode : map.finPeriode, quantite : map.quantite, unite : map.unite, prix : map.prix, trajet : map.trajet
                                , region : map.region, clients : [map.client]);
                                    
                            tarificationService.save(newTarif)
                            nbImport++
                        }
                    }
                    
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import Tarificaton : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    /**
     *  Import des sociétés
    Le fichier doit être sous la forme :
    code    raisonSociale  idFiscal   patente   registreCommerce   cnss    adresse   codePostal   telephone   fax   email   siteWeb    adresseFacturation    adresseLivraison   estClient  estFournisseur  estParticulier   ville  FormeJuridique  categorie    
    .
     */  
    def importerSocietes(Object media) {
       
        File fileLog = creerFichierLogImport("log_import_societe")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            def types = ["code" : "String", "raisonSociale" : "String", "societeDuGroup" : "Boolean", "idFiscal" : "String",
            "patente" : "String",  "registreCommerce"  : "String", "cnss"  : "String",  "adresse" : "String", "ville" : "String", 
            "tel"  : "String", "fax"  : "String", "email"  : "String", "siteWeb"  : "String"]
            
            //def valeursParDefaut = ["categoriePartenaires", [defaultCategorie]]
            def valeursObligatoire = ["code", "raisonSociale"]
            
            loggerInformations(fileLog, "Début import sociétés : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                    //                    
                    //                    if(map.ville != null && !map.ville.equals("")) {
                    //                        map.ville = Ville.findByIntituleIlike(map.ville)
                    //                    }
                    
                    //chargerDonneesParDefaut(map, valeursParDefaut)

                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldPar1 = Societe.findByCodeIlike(map.code)
                        def oldPar2 = Societe.findByRaisonSocialeIlike(map.raisonSociale)
                        if(oldPar2 != null || oldPar1 != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " la société : " + map.raisonSociale + " exsite déjà")
                            nbEchec++
                        } else {
                            def newSociete = new Societe(code : map.code, raisonSociale : map.raisonSociale, societeDuGroup : map.societeDuGroup, idFiscal : map.idFiscal ,
                                patente : map.patente, registreCommerce : map.registreCommerce, cnss  : map.cnss, adresse : map.adresse, ville : map.ville, tel : map.tel , fax  : map.fax , email  : map.email , 
                                siteWeb : map.siteWeb)
                            societeService.save(newSociete)
                            nbImport++      
                        }                        
                    }
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import sociétés : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
            logger.error(ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    def importerFournisseurs(Object media) {
       
        File fileLog = creerFichierLogImport("log_import_fournisseurs")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            def types = ["code" : "String", "raisonSociale" : "String", "societeDuGroup" : "Boolean", "idFiscal" : "String",
            "patente" : "String",  "registreCommerce"  : "String", "cnss"  : "String", "adresse" : "String", "ville" : "String", 
            "tel"  : "String", "fax"  : "String", "email"  : "String", "siteWeb"  : "String"]
            
            //def valeursParDefaut = ["categoriePartenaires", [defaultCategorie]]
            def valeursObligatoire = ["code", "raisonSociale"]
            
            loggerInformations(fileLog, "Début import sociétés : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                    
                    //                    if(map.ville != null && !map.ville.equals("")) {
                    //                        map.ville = Ville.findByIntituleIlike(map.ville)
                    //                    }
                    
                    //chargerDonneesParDefaut(map, valeursParDefaut)

                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldPar1 = Fournisseur.findByCodeIlike(map.code)
                        def oldPar2 = Fournisseur.findByRaisonSocialeIlike(map.raisonSociale)
                        if(oldPar2 != null || oldPar1 != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le fournisseur : " + map.raisonSociale + " exsite déjà")
                            nbEchec++
                        } else {
                            def newFournisseur = new Fournisseur(code : map.code, raisonSociale : map.raisonSociale, societeDuGroup : map.societeDuGroup
                                ,idFiscal : map.idFiscal ,patente : map.patente, registreCommerce : map.registreCommerce, cnss : map.cnss
                                ,adresse : map.adresse ,ville : map.ville, tel : map.tel, fax  : map.fax, email : map.email, siteWeb : map.siteWeb)
                            fournisseurService.save(newFournisseur)
                            nbImport++      
                        }                        
                    }
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import Fournisseurs : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
            logger.error(ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
   
}
