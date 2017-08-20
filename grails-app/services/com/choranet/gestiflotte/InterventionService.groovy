
package com.choranet.gestiflotte;

import org.zkoss.zk.ui.Executions

/**
 * InterventionService Service pour la gestion des opérations
 * transactionnelles pour l'objet Intervention
 */
class InterventionService extends SuperService {

    static transactional = true
        
    /**
     * Service pour la gestion de l'objet vehicule
     **/      
    def vehiculeService
    
    def paternCompteurService

    def list() throws Exception {
        return super.list(Intervention.class)
    }
    
    def save(objet) throws Exception {
        //objet.numIntervention = paternCompteurService.getProchainNumInterEtIncrementer(objet.type, objet.dateDepart)
        super.save(objet)
        paternCompteurService.incrementerPatternIntervention()
    }
    
//    def update(objet) throws Exception {
//        try {                
//            objet.merge(flush:true)
//        }
//        catch(Exception e) {
//            //logger.error(e)
//            throw e;
//        }
//    }
    
    def isLastIntervention(numInter){
        def listIntervention = Intervention.list()
        def isLast = (numInter == listIntervention.get(listIntervention.size()-1).numIntervention)
        return isLast
    }
    
    def delete(objet) throws Exception {
        //        def numInter = objet.numIntervention
        //        objet.numIntervention = objet.type + numInter.substring(1,numInter.length())
        //        println objet.numIntervention
        
        super.delete(objet)
        def isLast = isLastIntervention(objet.numIntervention)
        if(isLast){
            paternCompteurService.decrementerPatternIntervention()
        }
        //        else {
        //            throw new Exception('Seulement la dernière intervention qui peut être supprimée\n')
        //        }
        
    }
        
    //    def updateVehiculeKilometrage(intervention) throws Exception {
    //        if (intervention.kilometrageRetour != null && intervention.vehicule.kilometrage < intervention.kilometrageRetour) {
    //            intervention.vehicule.kilometrage = intervention.kilometrageRetour
    //            vehiculeService.update(intervention.vehicule)
    //        }
    //    }
    
    /**
     * Fonction pour la validation d'une intervention donnée à la base des interventions déjà passées
     **/
    def validatedIntervention(intervention, vehiculeSelected) throws Exception {
        def criteria = Intervention.createCriteria()
        def interventionAlreadyPassed = criteria.list {
            eq("vehicule", vehiculeSelected)
            not {
                eq("numBC", intervention.numBC)
            }
            or {
                between("dateDepart", intervention.dateDepart, intervention.dateRestitution)
                between("dateRestitution", intervention.dateDepart, intervention.dateRestitution)
                and {
                    le("dateDepart", intervention.dateDepart)
                    ge("dateRestitution", intervention.dateRestitution)
                }
            }       
        }
        if (interventionAlreadyPassed != null) {
            return interventionAlreadyPassed.size()<= 0
        }
        else return true
    }
    
    /**
     * Fonction pour la validation d'une intervention donnée à base des réservations déjà passées
     **/
    def validatedInterventionForReservation(intervention, vehiculeSelected)throws Exception {
        def criteria = Reservation.createCriteria()
        def reservationAlreadyPassed = criteria.list {
            eq("vehicule", vehiculeSelected)
            or {
                between("dateDepart", intervention.dateDepart, intervention.dateRestitution)
                between("dateRestitution", intervention.dateDepart, intervention.dateRestitution)
                and {
                    le("dateDepart", intervention.dateDepart)
                    ge("dateRestitution", intervention.dateRestitution)
                }
            }
        }
        if (reservationAlreadyPassed != null) {
            return reservationAlreadyPassed.size()<= 0
        }
        else return true
    }
    
    def getSommeBenifices(date1, date2) {
        def criteria = Intervention.createCriteria()
        def result = criteria.get {
            between("dateRestitution", date1, date2)
            projections {
                sum("montantFacture")
            }
        }
        if(result == null) {
            result = 0;
        }
        return result
    }
    
    def getInterventionsBetween(date1, date2) {
        def criteria = Intervention.createCriteria()
        def result = criteria.list {
            between("dateDepart", date1, date2)
            vehicule{
                categorie{
                    order("libelle","asc")
                }
                order("immatriculation","asc")
            }
            order("numIntervention","asc")
           
        }
        return result
    }
        
    def getInterventionsVehicule(v) {
        def criteria = Location.createCriteria()
        def result = criteria.list {
            eq('vehicule', v)
        }
        return result
    }
    
    def getBonGasoilCategorie(){
        def criteria = CategorieFraisCirculation.createCriteria()
        def bonGasoil = criteria.get{
            eq("libelle", 'Bon gasoil')
        }
        return bonGasoil
    }
    
    //    def getCurrentIntervention(inter){
    //        def criteria = Intervention.createCriteria()
    //        def currentIntervention = criteria.get{
    //            and {
    //                eq("numIntervention", inter.numIntervention)
    //            }
    //            
    //        }       
    //        return currentIntervention
    //    }
    //    
    //    def getTotalFraisCirculation(intervention) {
    //        def criteria = BonFraisCirculation.createCriteria()
    //        def totalCharges = criteria.get {
    //            eq("intervention", getCurrentIntervention(intervention))
    //            projections {
    //                sum("montant")
    //            }
    //        }
    //        if(totalCharges == null) {
    //            totalCharges = 0;
    //        }
    //        return totalCharges
    //    }
    //    
    //    def getTotalConsommationGasoil(intervention){
    //        
    //        def bgasoilCtg = getBonGasoilCategorie()
    //        def currentInter = getCurrentIntervention(intervention)
    //        
    //        def criteria = BonFraisCirculation.createCriteria()
    //        def consommation = criteria.get{ 
    //            and {
    //                eq("categorieFraisCirculation", bgasoilCtg)
    //                eq("intervention", currentInter) 
    //            }
    //            projections {
    //                sum("chargeEnUM")
    //            }
    //        }
    //        if(consommation == null) {
    //            consommation = 0;
    //        }
    //        return consommation
    //    }

    def arrondirePrix(prix, seuilArrondissement, senseArrondissement){
                    
        if (seuilArrondissement == null || senseArrondissement == null) {
            return prix
        }
            
        // prixDeduit = SArd*Q + R 
        // Q = (int)prixDeduit / SArd
        // R = prixDeduit - SArd*Q
        def quotion  = (int)prix/seuilArrondissement
        def reste = prix - seuilArrondissement*quotion
                
        // si le prix est déjà arrondi
        if (reste == 0d) return prix
        // Sinon
        def minoration = 0d
        def majoration = 0d
        def rapprochement = 0d
                
        if (reste < seuilArrondissement){
            minoration = reste
            majoration = seuilArrondissement - reste
        } else {
            minoration = seuilArrondissement
            majoration = 2 * seuilArrondissement - reste  //majoration = SArd - (R - SArd) = 2SArd-R
        }
                 
        if (senseArrondissement.equals("Majoration")) {
            // prixDeduit = prixDeduit + majoration
            // prixDeduit = 12.3 -> Q = 24, R = 0.3
            prix += majoration
        }
        else {
            if (senseArrondissement.equals("Minoration")) {                        
                prix -= minoration
            } else { 
                // rapprochement
                if (minoration < majoration){
                    rapprochement = minoration
                    prix -= rapprochement
                }
                else {
                    rapprochement = majoration
                    prix += rapprochement
                }
            }    
        }
        return prix
    }
    
    def getKilometrageVehicule(vehicule, currentKilometrageRetour){
        def criteria = Intervention.createCriteria()
        def result = criteria.list {
            and {
                eq('vehicule', vehicule)
                isNotNull('kilometrageRetour')
            }
            order('kilometrageRetour', 'desc')
        }
        if (result.size() == 0){
            return currentKilometrageRetour
        }
        return result.get(0).kilometrageRetour
    }
    
    def getKilometrageVehiculeAfterInterventionUpdate(idInter, vehicule){
        def criteria = Intervention.createCriteria()
        def result = criteria.list {
            and {
                not{
                    idEq(idInter)
                }
                eq('vehicule', vehicule)
                isNotNull('kilometrageRetour')
            }
            order('kilometrageRetour', 'desc')
        }
        // si l'intervention courante est la seule executée avec la véhicule en question
        if (result.size() == 0){
            //println 'vehicule.kilometrage : ' + vehicule.kilometrage
            return vehicule.kilometrage
            // s'il y a d'autre interventions à part l'intervention courante, qui sont exécutées avec la véhicule en question
        }else if (result.size() > 0){
            //println 'result : ' + result.kilometrageRetour
            //println 'result.get(0).kilometrageRetour : ' + result.get(0).kilometrageRetour
            return result.get(0).kilometrageRetour
        }
    }
    
    def getBonFraisInterventionList(interventionCourante){
        def criteria = BonFraisCirculation.createCriteria()
        def bonFraisInterventionList = criteria.list {
            eq('intervention', interventionCourante)
        }
        return bonFraisInterventionList
    }
}