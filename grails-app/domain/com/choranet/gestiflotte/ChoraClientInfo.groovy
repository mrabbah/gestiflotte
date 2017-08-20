package com.choranet.gestiflotte

import java.io.IOException
import org.zkoss.image.AImage
import org.zkoss.image.Image
import com.choranet.gestiflotte.util.Utilitaire
import java.io.Serializable;

class ChoraClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    String nomsociete	
   
    String raisonSocial
    
    String telephone
    
    String fax
    
    String email
    
    String patente
    
    String rc
    
    String idF
    
    String cnss
    
    String site

    String repertoirBackup
    
    String adresse
    
    String codePostale
    
    String ville
    
    String pays
    
    byte[] logoData
    
    Image trans_logo
    
    static transients = ['trans_logo']
    
    static constraints = {
        nomsociete(blank:false, nullable : false)
        email(email:true, nullable: true)
        raisonSocial(nullable: true)
        telephone(nullable: true)
        fax(nullable: true)
        patente(nullable: true)
        rc(nullable: true)
        idF(nullable: true)
        cnss(nullable: true)
        site(nullable: true)
        repertoirBackup(nullable: true)
        adresse (nullable: true)
        codePostale (nullable: true)
        ville (nullable: true)
        pays (nullable: true)
    }
    
    public void setLogoData(byte[] bd) {
        logoData = bd
        if(logoData != null) {
            //immatriculation = immatriculation1+immatriculation2+immatriculation3
            trans_logo = new AImage("logo", bd);
        }
    }

    public void setTrans_logo(Image img) {        
        if(img == null) {            
            img = Utilitaire.getLogoImage(getClass())
        }
        trans_logo = img
        logoData = trans_logo.getByteData()     
    }
    
    public String toString() {
        return nomsociete
    }
}
