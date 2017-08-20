
package com.choranet.gestiflotte;

import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

class UtilisateurService extends SuperService {

    private Log logger = LogFactory.getLog(UtilisateurService.class)
    
    static transactional = true

    def authenticateService
    
    def list() throws Exception {
        return super.list(Utilisateur.class)
    }
    
    def update(Object object) throws Exception {
        if(object.pass != '[secret]') {
            object.passwd = authenticateService.encodePassword(object.pass)            
        }
        object.validate()
        if(!object.hasErrors()) {
            try {                
                object.merge(flush:true)
            }
            catch(Exception e) {
                logger.error(e)
                throw e;
            }
        } else {
            def erreurs = "Les erreur(s) suivante(s) son a corriger :"
            object.errors.each { error ->
                erreurs += error
            }
            logger.error(erreurs)
            throw new Exception(erreurs)
        }        
    }

    def save(Object object) throws Exception {        
        object.passwd = authenticateService.encodePassword(object.pass)
        object.validate()
        if(!object.hasErrors()) {
            try {
                object.save(flush:true)
            }
            catch(Exception e) {
                logger.error(e)
                throw e;
            }
        } else {
            def erreurs = "Les erreur(s) suivante(s) son a corriger :"
            object.errors.each { error ->
                erreurs += error
            }
            logger.error(erreurs)
            throw new Exception(erreurs)
        }
    }
}
