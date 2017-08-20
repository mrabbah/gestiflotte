
	
// Place your Spring DSL code here

beans = { 
    utilisateurWindow(com.choranet.gestiflotte.UtilisateurWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    droitUtilisateurWindow(com.choranet.gestiflotte.DroitUtilisateurWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    mainWindow(com.choranet.gestiflotte.MainWindow, ref("authenticateService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    choraClientInfoWindow(com.choranet.gestiflotte.ChoraClientInfoWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    backupRestoreWindow(com.choranet.gestiflotte.BackupRestoreWindow, ref("backupRestoreService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    cleWindow(com.choranet.commun.CleWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    clePopUpWindow(com.choranet.commun.ClePopUpWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    
    intervenantWindow(com.choranet.gestiflotte.IntervenantWindow, ref("intervenantService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    modelWindow(com.choranet.gestiflotte.ModelWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    entretienPeriodiqueWindow(com.choranet.gestiflotte.EntretienPeriodiqueWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    entretienWindow(com.choranet.gestiflotte.EntretienWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    paiementClientWindow(com.choranet.gestiflotte.PaiementClientWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    fraisCirculationPeriodiqueWindow(com.choranet.gestiflotte.FraisCirculationPeriodiqueWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    categorieFraisCirculationWindow(com.choranet.gestiflotte.CategorieFraisCirculationWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    uniteDeMesureWindow(com.choranet.gestiflotte.UniteDeMesureWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    deplacementWindow(com.choranet.gestiflotte.DeplacementWindow, ref("vehiculeService"), ref("intervenantService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    typeEntretienWindow(com.choranet.gestiflotte.TypeEntretienWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    vehiculeWindow(com.choranet.gestiflotte.VehiculeWindow, ref("intervenantService"), ref("societeService"), ref("vehiculeService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    interventionWindow(com.choranet.gestiflotte.InterventionWindow, ref("vehiculeService"), ref("societeService"), ref("interventionService"), ref("paternCompteurService"), ref("tarificationService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    energieWindow(com.choranet.gestiflotte.EnergieWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    typeEntretienPeriodiqueWindow(com.choranet.gestiflotte.TypeEntretienPeriodiqueWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    reservationWindow(com.choranet.gestiflotte.ReservationWindow, ref("vehiculeService"), ref("societeService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    nationaliteWindow(com.choranet.gestiflotte.NationaliteWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    marqueWindow(com.choranet.gestiflotte.MarqueWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    societeWindow(com.choranet.gestiflotte.SocieteWindow, ref("societeService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    categorieWindow(com.choranet.gestiflotte.CategorieWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    typeFraisCirculationPeriodiqueWindow(com.choranet.gestiflotte.TypeFraisCirculationPeriodiqueWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    bonFraisCirculationWindow(com.choranet.gestiflotte.BonFraisCirculationWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    facturationWindow(com.choranet.gestiflotte.FacturationWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    paiementGroupWindow(com.choranet.gestiflotte.PaiementGroupWindow, ref("societeService"), ref("fraisCirculationPeriodiqueService"), ref("entretienPeriodiqueService"), ref("entretienService"), ref("bonFraisCirculationService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    tdbWindow(com.choranet.gestiflotte.TdbWindow, ref("vehiculeService"), ref("interventionService"), ref("bonFraisCirculationService"), ref("fraisCirculationPeriodiqueService"), ref("entretienService"), ref("entretienPeriodiqueService"),ref("reservationService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    paternCompteurWindow(com.choranet.gestiflotte.PaternCompteurWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    trajetWindow(com.choranet.gestiflotte.TrajetWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    tarificationWindow(com.choranet.gestiflotte.TarificationWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    regionWindow(com.choranet.gestiflotte.RegionWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    fournisseurWindow(com.choranet.gestiflotte.FournisseurWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
}
