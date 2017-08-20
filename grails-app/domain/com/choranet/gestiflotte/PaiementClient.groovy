

package com.choranet.gestiflotte


import java.io.Serializable;

/**
 * PaiementClient Domain Object 
 */
class PaiementClient implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String numTransaction	
    	
    Date date	
    	
    String modePaiement	
    	
    Double montantPaye	
       		   
    
    static belongsTo = [facturation : Facturation]
    
    static mapping = { 
        numTransaction index : "paiementclient_num_transaction"
        date index : "paiementclient_date"
        modePaiement index : "paiementclient_mode_paiement"
        montantPaye index : "paiementclient_montant_paye"
        facturation lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        numTransaction(nullable : false)
    
        date(nullable : false)
    
        modePaiement()
    
        montantPaye(min : 0d, nullable : false)
    
    }
	
    String toString() {
        return super.toString()
    }
}

