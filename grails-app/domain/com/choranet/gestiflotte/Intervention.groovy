

package com.choranet.gestiflotte


import java.io.Serializable;
import java.util.Calendar;

/**
 * Intervention Domain Object 
 */
class Intervention implements Serializable {
    
    private static final long serialVersionUID = 1L;
    	
    String numIntervention
    
    String type
    
    String numBC	
    
    String numBL
    	
    String mission	
    	
    Double prixParJour	
    	
    Date dateDepart
    Integer dateDepartMois
    Integer dateDepartAnnee
    	
    Date dateRestitution	
    	
    Integer nombreDeJours	
    
    Double quantite
    	
    Double kilometrageDepart	
    	
    Double kilometrageRetour	
    	
    Double kilometrageDisque
    
    Double consommationGasoil
    
    Double consommationGasoilTheoriqueMax
    
    Double trans_consommationGasoilTheorique
    
    Double surplusConsommationGasoil
    
    Double trans_surplusGasoil
    
    String numBonGasoil
    
    String trans_numBonGasoil
    
    Double totalCharges
    	
    String lieuLivraison	
    	
    String lieuReprise	
    	
    String observations	
    	
    String numFacture	
       		
    Double montantFacture
    
    Double montantDeBase
    
    Double commissionChauffeur
    
    Double pourcentageConsommation
    
    Double trans_pourcentageConsommation
    
    String etatChargement
    
    static transients = ['trans_consommationGasoilTheorique', 'trans_surplusGasoil', 'trans_pourcentageConsommation', 'trans_numBonGasoil']
    
    static belongsTo = [conducteur : Intervenant , vehicule : Vehicule , agentLoueurResponsable : Utilisateur , client : Societe, tarification : Tarification]
    
    static mapping = { 
        numIntervention index : "intervention_num"
        type index : "intervention_type"
        numBC index : "intervention_num_b_c"
        numBL index : "intervention_num_bl"
        mission index : "intervention_mission"
        prixParJour index : "intervention_prix_par_jour"
        dateDepart index : "intervention_date_depart"
        dateDepartMois index : "intervention_date_depart_mois"
        dateDepartAnnee index : "intervention_date_depart_annee"
        dateRestitution index : "intervention_date_restitution"
        nombreDeJours index : "intervention_nombre_de_jours"
        quantite index : "intervention_quantite"
        kilometrageDepart index : "intervention_kilometrage_depart"
        kilometrageRetour index : "intervention_kilometrage_retour"
        kilometrageDisque index : "intervention_kilometrage_disque"
        comsommationGasoil index : "intervention_consommation_gasoil"
        consommationGasoilTheoriqueMax index : "intervention_consommation_gasoil_theorique_max"
        surplusConsommationGasoil index : "intervention_surplus_consommation_gasoil"
        numBonGasoil index : "intervention_num_bon_gasoil"
        totalCharges index : "intervention_total_charges"
        lieuLivraison index : "intervention_lieu_livraison"
        lieuReprise index : "intervention_lieu_reprise"
        observations index : "intervention_observations"
        numFacture index : "intervention_num_facture"
        montantFacture index : "intervention_montant_facture"
        montantDeBase index : "intervention_montant_base"
        commissionChauffeur index : "intervention_commission_chauffeur"
        pourcentageConsommation index : "intervention_pourcentage_consommation"
        etatChargement index : "intervention_etatChargement"
        conducteur lazy : false
        vehicule lazy : false
        agentLoueurResponsable lazy : false
        client lazy : false
        tarification lazy : false
        batchSize: 10 
    }
    static constraints = {
    
        numIntervention(blank : false, nullable : false, unique : true)
        
        type(blank : false, nullable : true)
        
        numBC(nullable : true)
        
        numBL(nullable : true)
    
        mission(blank : false, nullable : false)
    
        prixParJour(min : 0d)
    
        dateDepart(nullable : false)
        dateDepartMois(nullable : false)
        dateDepartAnnee(nullable : false)
    
        dateRestitution(nullable : true)
        
        montantFacture(nullable : true)
        
        montantDeBase(nullable : true)
        
        commissionChauffeur(nullable : true)
    
        nombreDeJours(nullable : true)
        
        quantite(min : 0d, nullable : false)
    
        kilometrageDepart(nullable : true)
    
        kilometrageRetour(nullable : true)
    
        kilometrageDisque(nullable : true)
        
        consommationGasoil(nullable : true)
        
        consommationGasoilTheoriqueMax(nullable : true)
        
        surplusConsommationGasoil(nullable : true)
        
        numBonGasoil(nullable : true)
        
        pourcentageConsommation(nullable : true)
        
        totalCharges(nullable : true)
    
        lieuLivraison(nullable : true)
    
        lieuReprise(nullable : true)
    
        observations(nullable : true)
    
        numFacture(nullable : true)
        
        tarification(nullable : false)
        
        etatChargement(nullable : true)
        
    }
    
    def getCurrentIntervention(inter){
        def criteria = Intervention.createCriteria()
        def currentIntervention = criteria.get{
            and {
                eq("numIntervention", inter.numIntervention)
            }
        }       
        return currentIntervention
    }
    
    
    /**
     *
     **/
    def getTrans_consommationGasoilTheorique(){
        //        
        //        def bonGasoilList = CategorieFraisCirculation.createCriteria().list{
        //            ilike("libelle", 'Bon gasoil%')
        //        }
        //        consommationGasoilTheoriqueMax = 0d
        //        def currentbongasoil
        //        bonGasoilList.each { bongasoil ->
        //            currentbongasoil = BonFraisCirculation.createCriteria().get{ 
        //                and {
        //                    eq("categorieFraisCirculation", bongasoil)
        //                    eq("intervention", getCurrentIntervention(this)) 
        //                }
        //                projections {
        //                    sum("chargeTheorique")
        //                }
        //            }
        //            if (currentbongasoil != null){
        //                consommationGasoilTheoriqueMax += currentbongasoil
        //            }
        //        }
        ////        
        ////        def consomSeuil = vehicule().consommationGasoilMax
        ////        if(etatChargement.equals("DECHARGE"))
        ////            consomSeuil = vehicule.consommationGasoilAvideMax
        //            
        //        if(pourcentageConsommation != null && kilometrageDisque != null){
        //            trans_consommationGasoilTheorique = pourcentageConsommation * kilometrageDisque / 100
        //            //trans_consommationGasoilTheorique = consommationGasoilTheoriqueMax
        //        }
        return consommationGasoilTheoriqueMax
    }
    
//    def getTotalCharges(){
//        if (totalCharges == null)  return 0d
//        else return totalCharges
//    }
    /**
     *
     **/
    def getTrans_surplusGasoil(){
        
        //        println "\n"
        //        println "call getTrans_surplusGasoil"
        //        println "consommationGasoilCalculated before : " + consommationGasoilCalculated
        //        println "consommationGasoilTheoriqueCalculated before : " + consommationGasoilTheoriqueCalculated
        //        
        //        surplusConsommationGasoil = 0d
        //        if(consommationGasoilCalculated == false){
        //            println 'here consommationGasoilCalculated'
        //            trans_consommationGasoil = getTrans_consommationGasoil()
        //        }
        //        if(consommationGasoilTheoriqueCalculated == false){
        //            println 'here consommationGasoilTheoriqueCalculated'
        //            trans_consommationGasoilTheorique = getTrans_consommationGasoilTheorique()
        //        }
        //        println 'trans_consommationGasoil : ' + trans_consommationGasoil
        //        println 'trans_consommationGasoilTheorique : ' + trans_consommationGasoilTheorique
        //        trans_surplusGasoil = trans_consommationGasoil - trans_consommationGasoilTheorique
        //        
        //        if (trans_surplusGasoil < 0) trans_surplusGasoil = 0d
        //        surplusConsommationGasoil = trans_surplusGasoil
        //        consommationGasoilCalculated = false
        //        consommationGasoilTheoriqueCalculated = false
        //        
        //        return surplusConsommationGasoil
        
        //        def bonGasoilList = CategorieFraisCirculation.createCriteria().list{
        //            ilike("libelle", 'Bon gasoil%')
        //        }
        //        
        //        def currentGasoil
        //        consommationGasoil = 0d
        //        def currentGasoilTheorique
        //        consommationGasoilTheoriqueMax = 0d
        
        //        bonGasoilList.each { bongasoil ->
        //            currentGasoilTheorique = BonFraisCirculation.createCriteria().get{ 
        //                and {
        //                    eq("categorieFraisCirculation", bongasoil)
        //                    eq("intervention", getCurrentIntervention(this)) 
        //                }
        //                projections {
        //                    sum("chargeTheorique")
        //                }
        //            }
        //            currentGasoil = BonFraisCirculation.createCriteria().get{ 
        //                and {
        //                    eq("categorieFraisCirculation", bongasoil)
        //                    eq("intervention", getCurrentIntervention(this)) 
        //                }
        //                projections {
        //                    sum("chargeEnUM")
        //                }
        //            }
        //            if (currentGasoil != null){
        //                consommationGasoil += currentGasoil
        //            }
        //            if (currentGasoilTheorique != null){
        //                consommationGasoilTheoriqueMax += currentGasoilTheorique
        //            }
        //        }
        //        trans_consommationGasoil = consommationGasoil
        //this.getTrans_consommationGasoil()
        //trans_consommationGasoilTheorique = 0d
        //        
        //        println "getCurrentIntervention(this) : " + getCurrentIntervention(this)
        //        def consomSeuil = getCurrentIntervention(this).vehicule.consommationGasoilMax
        //        if(etatChargement.equals("DECHARGE"))
        //            consomSeuil = vehicule.consommationGasoilAvideMax
            
        //consommationGasoilTheoriqueMax = 38d * kilometrageDisque / 100
        //        trans_consommationGasoilTheorique = consommationGasoilTheoriqueMax
        //this.getTrans_consommationGasoilTheorique()
        
        //        if(consommationGasoilTheoriqueMax != null){
        //            trans_surplusGasoil = consommationGasoil - consommationGasoilTheoriqueMax
        //            if (trans_surplusGasoil < 0) trans_surplusGasoil = 0d
        //            surplusConsommationGasoil = trans_surplusGasoil
        //        }
        return surplusConsommationGasoil        
    }
        
    def getTrans_pourcentageConsommation() {
        pourcentageConsommation = 0d
        if (consommationGasoilTheoriqueMax != null && consommationGasoilTheoriqueMax > 0 && kilometrageDisque != null && kilometrageDisque > 0){
            pourcentageConsommation = consommationGasoilTheoriqueMax * 100 / kilometrageDisque;
        }
        trans_pourcentageConsommation = pourcentageConsommation
        
        return pourcentageConsommation
    }
	
    /**
     * récupérer le numéro de bon de gasoil lié à l'intervention
     **/
    def getTrans_numBonGasoil(){   
        def categorieFraisCirculationList = CategorieFraisCirculation.createCriteria().list{
            ilike("libelle", 'Bon gasoil%')
        }
        def bonGasoilList = new ArrayList()
        categorieFraisCirculationList.each { catgfraisCirc ->
            bonGasoilList.addAll(BonFraisCirculation.createCriteria().list{ 
                    and {
                        eq("categorieFraisCirculation", catgfraisCirc)
                        eq("intervention", getCurrentIntervention(this)) 
                    }
                })
        }
        if (bonGasoilList == null) return 'N/A'
        return bonGasoilList.numBon.unique()
    }
    
    def getChargeConsommationGasoil(chargesEnLitre, totalKilometrage){
        this.consommationGasoil = (kilometrageDisque * chargesEnLitre) / totalKilometrage;
        this.trans_consommationGasoil = this.consommationGasoil
        return trans_consommationGasoil
    }
    
//    def getTrans_mois(){
//        return dateDepart[MONTH]
//    }
//    
//    def getTrans_annee(){
//        return dateDepart[YEAR]
//    }

    def setDateDepartMois(){
        this.dateDepartMois = dateDepart[Calendar.MONTH]+1
    }
    
    def setDateDepartAnnee(){
        this.dateDepartAnnee = dateDepart[Calendar.YEAR]
    }
    
    String toString() { 
        def inter = client.toString() + ' , '
        if (tarification.trajet != null)
        inter = inter + tarification.trajet.toString()
        else 
        if (tarification.region != null)
        inter = inter + tarification.region.toString()
        return ( inter + ' , ' + vehicule.toString() + ' , ' + conducteur.toString() + ' : ' + numIntervention + ' , ' + consommationGasoil) 
    }
}
