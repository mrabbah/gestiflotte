
package com.choranet.gestiflotte;


/**
 * VehiculeService Service pour la gestion des opérations
 * transactionnelles pour l'objet Vehicule
 */
class VehiculeService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Vehicule.class)
    }
        
    def getListVehiculesDeService() {
        return Vehicule.finAllByVehiculeDeService(true).sort{it.immatriculation}
    }
    
    def getListVehiculeDeService() {
        def criteria = Vehicule.createCriteria()
        def result = criteria.list {}
        return result.sort{it.immatriculation}
    }
        
    def getListVehiculesPrestatairesExternes() {
        return Vehicule.finAllByVehiculeDeService(false)
    }
        
    def getListVoituresDeService() {
        def criteria = Vehicule.createCriteria()
        def result = criteria.list {
            categorie{
                eq("libelle", "VOITURE")
            }
        }
        return result.sort{it.immatriculation}
    }
    
    def getListCamions(){
        def criteria = Vehicule.createCriteria()
        def result = criteria.list {
            categorie{
                eq("libelle", "CAMION")
            }
        }
        return result.sort{it.immatriculation}
    }
        
    def getListVehiculesDeServiceWithoutVoitures() {
        def criteria = Vehicule.createCriteria()
        def result = criteria.list {
            categorie{
                not {
                    eq("libelle", "VOITURE")
                }
            }
        }
        return result.sort{it.immatriculation}
    }
      
    def getListVehiculesLouees() throws Exception {
        Date now = new Date()
        def criteria = Intervention.createCriteria()
        def result = criteria.list {
            ge("dateRestitution", now)
            le("dateDepart", now)
            vehicule {
                prorprietaireSociete {
                    eq("societeDuGroup", true)
                }
                categorie{
                    not {
                        eq("libelle", "VOITURE")
                    }
                }
            }
            projections {
                    "vehicule"
            }
        }
            
        return result.sort{it.immatriculation}
    }
    
    def getListVehiculesGarree() {
        def locations = getListVehiculesLouees()
        def result
        if(locations.size() == 0) {
            def criteria = Vehicule.createCriteria()
            result = criteria.list {
                prorprietaireSociete {
                    eq("societeDuGroup", true)
                }
                categorie{
                    not {
                        eq("libelle", "VOITURE")
                    }
                }
            }
        } else {
            def criteria = Vehicule.createCriteria()
            result = criteria.list {
                not{"in"("immatriculation", locations.vehicule.immatriculation)}
                prorprietaireSociete {
                    eq("societeDuGroup", true)
                }
                categorie{
                    not {
                        eq("libelle", "VOITURE")
                    }
                }
            }
        }
            
        return result.sort{it.immatriculation}
    }
        
    def getNbVehiculeGarree() {
        def locations = getListVehiculesLouees()
        def result
        if(locations.size() == 0) {
            def criteria = Vehicule.createCriteria()
            result = criteria.count {
                prorprietaireSociete {
                    eq("societeDuGroup", true)
                }
                categorie{
                    not {
                        eq("libelle", "VOITURE")
                    }
                }
            }
        } else {
            def criteria = Vehicule.createCriteria()
            result = criteria.count {
                not{"in"("immatriculation", locations.vehicule.immatriculation)}
                prorprietaireSociete {
                    eq("societeDuGroup", true)
                }
                categorie{
                    not {
                        eq("libelle", "VOITURE")
                    }
                }
            }
        }
            
        return result
    }
        
    def getNbVehiculeLouee() {
        Date now = new Date()
        def criteria = Intervention.createCriteria()
        def result = criteria.count {
            ge("dateRestitution", now)
            le("dateDepart", now)
            vehicule {
                prorprietaireSociete {
                    eq("societeDuGroup", true)
                }
                categorie{
                    not {
                        eq("libelle", "VOITURE")
                    }
                }
            }
            projections {
                    "vehicule"
            }
        }
            
        return result 
    }
}