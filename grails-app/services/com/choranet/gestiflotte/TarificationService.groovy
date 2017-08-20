package com.choranet.gestiflotte


/**
 * TarificationService Service pour la gestion des opérations
 * transactionnelles pour l'objet Tarification
 */
class TarificationService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Tarification.class)
    }
        
    def getModeTarifs(clientSelected, dateDepart){
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
            projections {
                distinct("modeTarif")
            }
        }
        return result
    }
    
    def getTarificationsClient(clientSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result
    }    
    
    def getListTrajets(clientSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {     
                or{
                    eq("modeTarif", "Forfitaire/Trajet")
                    eq("modeTarif", "Tonnage/Trajet")
                }
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result.trajet 
    }
    
    def getListTrajets(clientSelected, modeTarifSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {                
                eq("modeTarif", modeTarifSelected)
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result.trajet 
    }
    
    def getListRegions(clientSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                eq("modeTarif", "Forfitaire/Region")
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result.region
    }
    
    def getListRegions(clientSelected, modeTarifSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                eq("modeTarif", modeTarifSelected)
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result.region
    }
    
    def getTarificationByTrajet(clientSelected, trajetSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                or{
                    eq("modeTarif", "Forfitaire/Trajet")
                    eq("modeTarif", "Tonnage/Trajet")
                }
                eq("trajet", trajetSelected)
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result
    }
    
    def getTarificationByTrajet(clientSelected, trajetSelected, modeTarifSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                eq("modeTarif", modeTarifSelected)
                eq("trajet", trajetSelected)
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result
    }
    
    def getTarificationByRegion(clientSelected, regionSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                eq("modeTarif", "Forfitaire/Region")
                eq("region", regionSelected)
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result
    }
    
    def getTarificationsByModeTarifAndClient(clientSelected, modeTarifSelected, dateDepart) {
        def criteria = Tarification.createCriteria()
        def result = criteria.list {
            and {
                eq("modeTarif", modeTarifSelected)
                clients{
                    eq('code', clientSelected.code)
                }
                if(dateDepart != null) {
                    le("debutPeriode", dateDepart.date)
                    ge("finPeriode", dateDepart.date)
                }
            }
        }
        return result
    }
}
