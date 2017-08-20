security {

    // see DefaultSecurityConfig.groovy for all settable/overridable properties

    active = true

    loginUserDomainClass = "com.choranet.gestiflotte.Utilisateur"
    authorityDomainClass = "com.choranet.gestiflotte.GroupeUtilisateur"
    requestMapClass = "com.choranet.gestiflotte.DroitUtilisateur"
        
    authenticationFailureUrl = '/login.zul?login_error=true'
    loginFormUrl = '/choraBarrage'
    cookieName = 'gestiflotte_remember_me'
    errorPage = '/zul/errors/denied.zul'
    defaultTargetUrl = '/zul/main.zul'
}
