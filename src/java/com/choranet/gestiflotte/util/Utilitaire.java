
package com.choranet.gestiflotte.util;

import java.io.File;
import java.io.InputStream;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;

/**
 *
 * @author RABBAH
 */
public class Utilitaire {
           
    private static String pathToLogoImage = "com/choranet/gestiflotte/images/logo.png";
    
    private static String pathToPaiementGroupFcpSubReport = "com/choranet/gestiflotte/subreports/Rapport_paiement_des_frais_fcp.jasper";
    private static String pathToPaiementGroupFcSubReport = "com/choranet/gestiflotte/subreports/Rapport_paiement_des_frais_fc.jasper";
    private static String pathToPaiementGroupEpSubReport = "com/choranet/gestiflotte/subreports/Rapport_paiement_des_frais_ep.jasper";
    private static String pathToPaiementGroupESubReport = "com/choranet/gestiflotte/subreports/Rapport_paiement_des_frais_e.jasper";
    
    

    
    public static Image getLogoImage(Class clazz) throws Exception {
        ClassLoader cl = clazz.getClassLoader();
        InputStream is;
        try {
            is = cl.getResourceAsStream(pathToLogoImage);
        } catch (Exception e) {
            throw new Exception("Impossible d obtenir l image logo", e);
        }
        Image img = new AImage("logo", is);
        return img;
    }
    
        public static File getPaiementGroupESubReport(Class clazz) throws Exception {
        ClassLoader cl = clazz.getClassLoader();
        File is;
        try {
            is = new File(cl.getResource(pathToPaiementGroupESubReport).getPath());
        } catch (Exception e) {
            throw new Exception("Impossible d obtenir le rapport 1", e);
        }
        return is;
    }
    
    public static File getPaiementGroupEpSubReport(Class clazz) throws Exception {
        ClassLoader cl = clazz.getClassLoader();
        File is;
        try {
            is = new File(cl.getResource(pathToPaiementGroupEpSubReport).getPath());
        } catch (Exception e) {
            throw new Exception("Impossible d obtenir le rapport 2", e);
        }
        return is;
    }
    
    public static File getPaiementGroupFcSubReport(Class clazz) throws Exception {
        ClassLoader cl = clazz.getClassLoader();
        File is;
        try {
            is = new File(cl.getResource(pathToPaiementGroupFcSubReport).getPath());
        } catch (Exception e) {
            throw new Exception("Impossible d obtenir le rapport 3", e);
        }
        return is;
    }
    
    public static File getPaiementGroupFcpSubReport(Class clazz) throws Exception {
        ClassLoader cl = clazz.getClassLoader();
        File is;
        try {
            is = new File(cl.getResource(pathToPaiementGroupFcpSubReport).getPath());
        } catch (Exception e) {
            throw new Exception("Impossible d obtenir le rapport 4", e);
        }
        return is;
    }
    

}
