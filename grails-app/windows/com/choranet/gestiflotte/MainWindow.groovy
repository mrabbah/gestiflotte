
package com.choranet.gestiflotte

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.zkoss.zksandbox.Category
import org.springframework.web.context.request.RequestContextHolder
//import org.zkoss.zk.ui.Executions


/**
 *
 * @author RABBAH
 */
class MainWindow extends Window {
    
    /**
     * Logger de la class ArticleWindow
     **/
    private Log logger = LogFactory.getLog(MainWindow.class)
    
    def authenticateService
    
    def session
    
    def nomsociete = ChoraClientInfo.get(1).nomsociete
    def site = ChoraClientInfo.get(1).site
    def trans_logo  = ChoraClientInfo.get(1).trans_logo
    
    //Page inclue par d�faut pour l'utilisateur selon son role
    def currentinclude
    
    public MainWindow(authenticateService) {                
        if(!this.authenticateService) {
            this.authenticateService = authenticateService
            session = RequestContextHolder.currentRequestAttributes().getSession()
        
            if(!session.utilisateur) {
                def utilisateur = authenticateService.userDomain()
                session.utilisateur = utilisateur
                session.isroot = false
                session.isgestion = false
                session.isadmin = false
                for(GroupeUtilisateur gu : utilisateur.authorities) {
                    if(gu.authority == 'ROLE_ROOT') {
                        session.isroot = true;
                    } else if(gu.authority == 'ROLE_ADMIN') {
                        session.isadmin = true
                    } else if(gu.authority == 'ROLE_GESTION') {
                        session.isgestion = true
                    }
                }
            }
        }
    }
    
    def getCurrentinclude() {
        if(session) {
            def currentUser = session.utilisateur 
            def gp_admin = false;
            def gp_root = false;
            def gp_gestion = false;
            if(currentUser) {
                for(GroupeUtilisateur gu : currentUser.authorities) {
                    if(gu.authority == 'ROLE_ROOT') {
                        gp_root = true;
                    } else if (gu.authority == 'ROLE_ADMIN') {
                        gp_admin = true;
                    } else if (gu.authority == 'ROLE_GESTION') {
                        gp_gestion = true;
                    }
                } 
                if(gp_gestion) {
                    this.currentinclude = "/zul/gestion/tdb.zul"
                } else if (gp_admin) {
                    this.currentinclude = "/zul/admin/categorie.zul"
                } else if (gp_root) {
                    this.currentinclude = "/zul/root/clientinfo.zul"
                }
            }
        }
        return this.currentinclude
    }
    
}

