
package com.choranet.gestiflotte;


/**
 * DeplacementService Service pour la gestion des opérations
 * transactionnelles pour l'objet Deplacement
 */
class DeplacementService extends SuperService {

    static transactional = true
        
    /**
     * Service pour la gestion de l'objet vehicule
     **/      
    def vehiculeService

        
    def filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection, ofs, maxNb) throws Exception {
        try {
            def listeObjets 
            def tailleListe = 0
            def requete = clazz.createCriteria()
            def attributToSort
            def selectionnerDistinctResultat = false
            listeObjets = requete.list {
                for(nomAttribut in attributsAFiltrer) {
                    
                    if(sortedHeader == "h"+nomAttribut) {
                        attributToSort = nomAttribut 
                    }
                    
                    println "nomAttribut : " + nomAttribut
                    if(nomAttribut instanceof Date){
                        println "nomAttribut begin name : " + nomAttribut+'_begin'
                        def filtreDateBegin = filtre."$nomAttribut+'_begin'"
                        def filtreDateEnd = filtre."$nomAttribut+'_end'"
                        println "filtreDateBegin : " + filtreDateBegin
                        and{
                            le(filtreDateBegin, nomAttribut)
                            le(nomAttribut, filtreDateEnd)    
                        }
                    }else {
                        def valeurAttribut = filtre."$nomAttribut"
                        if(valeurAttribut != null) {
                            if(valeurAttribut instanceof String) {
                                if(valeurAttribut != "") {
                                    ilike(nomAttribut, valeurAttribut+"%")                                
                                }
                            } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                if(valeurAttribut) {
                                    eq(nomAttribut, true)
                                }
                            } else if(valeurAttribut instanceof java.util.Collection) {
                            "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                selectionnerDistinctResultat = true
                            } else {
                                eq(nomAttribut, valeurAttribut)
                            }
                        }
                    }
                }

                if(attributToSort != null) {
                    order(attributToSort, sortedDirection)
                }
                firstResult(ofs)
                //maxResults(maxNb) 
                if(selectionnerDistinctResultat) {
                    resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    //Cette solution n est pas clean puisque ce travail est fait
                    //cote client et non pas database une deuxieme solution es la
                    //suivante reste a affiner voir: 
                    //https://forum.hibernate.org/viewtopic.php?t=941669
                    //                    projections {
                    //                        clazz.getSimpleName()
                    //                        distinct("id")
                    //                    }
                }
            }
            if(listeObjets != null && listeObjets.size() > 0) {
                def criteria = clazz.createCriteria()
                tailleListe = criteria.count {
                    for(nomAttribut in attributsAFiltrer) {
                        if(nomAttribut instanceof Date){
                            println "nomAttribut begin name : " + nomAttribut+'_begin'
                            def filtreDateBegin = filtre."$nomAttribut+'_begin'"
                            def filtreDateEnd = filtre."$nomAttribut+'_end'"
                            println "filtreDateBegin : " + filtreDateBegin
                            and{
                                le(filtreDateBegin, nomAttribut)
                                le(nomAttribut, filtreDateEnd)    
                            }
                        }else {
                            def valeurAttribut = filtre."$nomAttribut"
                            if(valeurAttribut != null) {
                                if(valeurAttribut instanceof String) {
                                    if(valeurAttribut != "") {
                                        ilike(nomAttribut, valeurAttribut+"%")                                
                                    }
                                } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                    if(valeurAttribut) {
                                        eq(nomAttribut, true)
                                    }
                                }else if(valeurAttribut instanceof java.util.Collection) {
                                "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                } else {
                                    eq(nomAttribut, valeurAttribut)
                                }
                            }
                        }
                    }
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }
                }
            }else {
                tailleListe = 0
                listeObjets = null
            }
            
            return [tailleListe:tailleListe,listeObjets:listeObjets]
            
        }
        catch(Exception e) {
            logger.error(e)
            throw e
        }
    }
        

    
    def list() throws Exception {
        return super.list(Deplacement.class)
    }
        
    def updateVehiculeKilometrage(deplacement) throws Exception {
        if (deplacement.kilometrageRetour != null && deplacement.voitureService.kilometrage < deplacement.kilometrageRetour) {
            deplacement.voitureService.kilometrage = deplacement.kilometrageRetour
            vehiculeService.update(deplacement.voitureService)
        }
    }
    
    /**
     * Fonction pour la validation d'une intervention donnée à la base des interventions déjà passées
     **/
    def validatedDeplacement(deplacement, voitureSelected) throws Exception {
        if (deplacement.dateDepart != null && deplacement.dateRetour != null) {
            def criteria = Deplacement.createCriteria()
            def deplacementAlreadyPassed = criteria.list {
                eq("voitureService", voitureSelected)
                not {
                    eq("numeroDeplacement", deplacement.numeroDeplacement)
                }
                or {
                    between("dateDepart", deplacement.dateDepart, deplacement.dateRetour)
                    between("dateRetour", deplacement.dateDepart, deplacement.dateRetour)
                    and {
                        le("dateDepart", deplacement.dateDepart)
                        ge("dateRetour", deplacement.dateRetour)
                    }
                }       
            }
            if (deplacementAlreadyPassed != null) {
                return deplacementAlreadyPassed.size()<= 0
            }
            else return true
        } else return true
    }
    
    def getDeplacementBetween(date1, date2) {
        def criteria = Deplacement.createCriteria()
        def result = criteria.list {
            between("dateDepart", date1, date2)
        }
        return result
    }
    
}