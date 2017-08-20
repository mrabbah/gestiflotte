
package com.choranet.gestiflotte

import com.choranet.gestiflotte.util.DateUtil;
import java.util.Date;
/**
 * PaternCompteurService Service pour la gestion des opérations
 * transactionnelles pour l'objet PaternCompteur
 */
class PaternCompteurService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(PaternCompteur.class)
    }
        
    def getProchainNumInterEtIncrementer(type, date) {
        def pc = PaternCompteur.findByType("INTERVENTION");
        def result = constituerPattern(pc, type, date);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumInter(type, date) {
        def pc = PaternCompteur.findByType("INTERVENTION");
        return constituerPattern(pc, type, date);
    }
    
    def incrementerPatternIntervention() {
        incrementerPattern(PaternCompteur.findByType("INTERVENTION"));
    }
    
    def decrementerPatternIntervention(){
        decrementerPattern(PaternCompteur.findByType("INTERVENTION"));
    }
    
    def getDernierNumInter(type, date) {
        def pc = PaternCompteur.findByType("INTERVENTION");
        def result = constituerPattern(pc, type, date);
        decrementerPattern(pc);
        return result;
    }
    
    def getProchainNumReservEtIncrementer() {
        def pc = PaternCompteur.findByType("RESERVATION");
        def result = constituerPattern(pc);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumReserv() {
        def pc = PaternCompteur.findByType("RESERVATION");
        return constituerPattern(pc);
    }
    
    def getProchainNumDeplEtIncrementer() {
        def pc = PaternCompteur.findByType("DEPLACEMENT");
        def result = constituerPattern(pc);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumDepl() {
        def pc = PaternCompteur.findByType("DEPLACEMENT");
        return constituerPattern(pc);
    }
    
    def getProchainNumFacEtIncrementer() {
        def pc = PaternCompteur.findByType("FACTURE");
        def result = constituerPattern(pc);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumFac() {
        def pc = PaternCompteur.findByType("FACTURE");
        return constituerPattern(pc);
    }
    
    def getProchainNumEntrEtIncrementer() {
        def pc = PaternCompteur.findByType("ENTRETIEN");
        def result = constituerPattern(pc);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumEntr() {
        def pc = PaternCompteur.findByType("ENTRETIEN");
        return constituerPattern(pc);
    }
    
    def getProchainNumEntrPerioEtIncrementer() {
        def pc = PaternCompteur.findByType("ENTRETIEN_PERIODIQUE");
        def result = constituerPattern(pc);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumEntrPerio() {
        def pc = PaternCompteur.findByType("ENTRETIEN_PERIODIQUE");
        return constituerPattern(pc);
    }
    
    def getProchainNumPaiemCheqEtIncrementer() {
        def pc = PaternCompteur.findByType("PAIEMENT_CHEQ");
        def result = constituerPattern(pc);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumPaiemCheq() {
        def pc = PaternCompteur.findByType("PAIEMENT_CHEQ");
        return constituerPattern(pc);
    }
    
    def getProchainNumFCPerioEtIncrementer() {
        def pc = PaternCompteur.findByType("FC_PERIODIQUE");
        def result = constituerPattern(pc);
        incrementerPattern(pc);
        return result;
    }
    def getProchainNumFCPerio() {
        def pc = PaternCompteur.findByType("FC_PERIODIQUE");
        return constituerPattern(pc);
    }
        
    private String constituerBasePattern(PaternCompteur pc) throws Exception {
        Date now = new Date()
        def result = pc.prefixe
        String ns = pc.numeroSuivant
        def diff = pc.remplissage - ns.length()
        while(diff > 0) {
            ns = "0" + ns;
            diff--;
        }
        result += ns; 
        result += pc.suffixe
        return result
    }
    
    private String constituerPattern(PaternCompteur pc) throws Exception {
        String result = constituerBasePattern(pc);
        //Remplacer les variables
        result = result.replaceAll("\\{annee\\}", DateUtil.getDateTime("yy", now));
        result = result.replaceAll("\\{mois\\}", DateUtil.getDateTime("MM", now));
        result = result.replaceAll("\\{jour\\}", DateUtil.getDateTime("dd", now));
        return result
    }
    
    private String constituerPattern(PaternCompteur pc, String type, Date date) throws Exception {
        String result = constituerBasePattern(pc);
        
        //Remplacer les variables
        result = result.replaceAll("\\{type\\}", type);
        result = result.replaceAll("\\{jour\\}", DateUtil.getDateTime("dd", date));
        result = result.replaceAll("\\{mois\\}", DateUtil.getDateTime("MM", date));
        result = result.replaceAll("\\{annee\\}", DateUtil.getDateTime("yy", date));
        return result
    }
    
    
    
    private void incrementerPattern(PaternCompteur pc) throws Exception {
        pc.numeroSuivant += pc.pas;
        update(pc);
    }
    
    private void decrementerPattern(PaternCompteur pc) throws Exception {
        pc.numeroSuivant -= pc.pas;
        update(pc);
    }
}
