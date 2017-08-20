
package com.choranet.gestiflotte;


/**
 * ReservationService Service pour la gestion des opérations
 * transactionnelles pour l'objet Reservation
 */
class ReservationService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Reservation.class)
    }
        
    /**
     * Fonction pour la validation d'une réservation donnée à la base des réservations déjà passées
     **/
    def validatedReservation(reservation, vehiculeSelected) throws Exception {
        def criteria = Reservation.createCriteria()
        def reservationAlreadyPassed = criteria.list {
            eq("vehicule", vehiculeSelected)
            not {
                eq("numBC", reservation.numBC)
            }
            or {
                between("dateDepart", reservation.dateDepart, reservation.dateRestitution)
                between("dateRestitution", reservation.dateDepart, reservation.dateRestitution)
                and {
                    le("dateDepart", reservation.dateDepart)
                    ge("dateRestitution", reservation.dateRestitution)
                }
            }       
        }
        if (reservationAlreadyPassed != null) {
            return reservationAlreadyPassed.size()<= 0
        }
        else return true
    }
        
    /**
     * Fonction pour la validation d'une réservation donnée à base des interventions déjà passées
     **/
    def validatedReservationForIntervention(reservation, vehiculeSelected)throws Exception {
        def criteria = Intervention.createCriteria()
        def interventionAlreadyPassed = criteria.list {
            eq("vehicule", vehiculeSelected)
            or {
                between("dateDepart", reservation.dateDepart, reservation.dateRestitution)
                between("dateRestitution", reservation.dateDepart, reservation.dateRestitution)
                and {
                    le("dateDepart", reservation.dateDepart)
                    ge("dateRestitution", reservation.dateRestitution)
                }
            }
        }
        if (interventionAlreadyPassed != null) {
            return interventionAlreadyPassed.size()<= 0
        }
        else return true
    }
    
    /**
    Fonction pour la génération d'une intervention à partir de la réservation en cours
     **/
    def generateIntervention(Object object) throws Exception {

        def newIntervention = new com.choranet.gestiflotte.Intervention(numBC : object.numBC, mission : object.mission,
            prixParJour : object.prixParJour,
            dateDepart : object.dateDepart, dateRestitution : object.dateRestitution, nombreDeJours : object.nombreDeJours, 
            kilometrageDepart : object.vehicule.kilometrage, kilometrageRetour : null ,
            kilometrageDisque : null, lieuLivraison : object.lieuLivraison, lieuReprise : object.lieuReprise, 
            observations : object.observations, contratNumeriser : null,
            numFacture : null, conducteur : object.conducteur, vehicule : object.vehicule, 
            agentLoueurResponsable : object.responsableReservations, client : object.client)
        newIntervention.save()     
    }

    def getAlertsReservation() {
        Date now = new Date()
        def criteria = Reservation.createCriteria()
        def result = criteria.list {
            le("dateDepart", now + 7)
        }
        return result
    }
}