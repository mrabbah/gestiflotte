import com.choranet.gestiflotte.*
class BootStrap {

    def authenticateService
    
    def entretienPeriodiqueService
    
    def fraisCirculationPeriodiqueService
    
    def entretienService
    
    def bonFraisCirculationService
    
    def interventionService
    
    def deplacementService
    
    def init = { servletContext ->
        environments {
            zzzz {
                def clientInfo = new ChoraClientInfo(nomsociete:'CHORA INFORMATIQUE',raisonSocial : 'CHORA INFORMATIQUE', telephone : '05 22 34 23 33', fax : '05 22 34 23 33', email:'contact@choranet.com',patente:null,rc:null,idF:null,cnss:null,site:'www.choranet.com',repertoirBackup:".")
                clientInfo.setTrans_logo(null)
                clientInfo.save()
                def roleRoot = new GroupeUtilisateur(authority:'ROLE_ROOT', description:'Super Utilisateur').save()
                def roleAdmin = new GroupeUtilisateur(authority:'ROLE_ADMIN', description:'Administrateur').save() 
                def roleGestion = new GroupeUtilisateur(authority:'ROLE_GESTION', description:'Gestionnaire du site').save() 
                
                def userRoot = new Utilisateur(username:'root',
                    userRealName:'root',
                    enabled: true,
                    emailShow: true,
                    email: 'admin@choranet.com',
                    passwd: authenticateService.encodePassword('root')).save()
                
                def userAdmin = new Utilisateur(username:'admin',
                    userRealName:'admin',
                    enabled: true,
                    emailShow: true,
                    email: 'admin@choranet.com',
                    passwd: authenticateService.encodePassword('admin')).save()
                
                def userGestion = new Utilisateur(username:'gestion',
                    userRealName:'gestion',
                    enabled: true,
                    emailShow: true,
                    email: 'gestion@choranet.com',
                    passwd: authenticateService.encodePassword('gestion')).save()
                
                def secureRoot = new DroitUtilisateur(url: '/zul/root/**',
                    configAttribute:'ROLE_ROOT').save()
                def secureAdmin = new DroitUtilisateur(url: '/zul/admin/**',
                    configAttribute:'ROLE_ADMIN').save()
                def secureGestion = new DroitUtilisateur(url: '/zul/gestion/**',
                    configAttribute:'ROLE_GESTION').save()
                
                userRoot.addToAuthorities(roleRoot) 
                userRoot.addToAuthorities(roleAdmin)
                userRoot.addToAuthorities(roleGestion)
                userAdmin.addToAuthorities(roleAdmin)  
                userGestion.addToAuthorities(roleGestion)  
                
                
                //Energie
                def ene = new Energie(libelle : 'Essence').save()
                def end = new Energie(libelle : 'Diesel').save()

                def marqueVolvo = new Marque(libelle : 'VOLVO').save()
                def volvoModel = new Model(libelle : '1020', marque : marqueVolvo).save()
                new Model(libelle : 'F10', marque : marqueVolvo).save()
                new Model(libelle : 'FH', marque : marqueVolvo).save()
                new Model(libelle : 'FL', marque : marqueVolvo).save()
                new Model(libelle : 'N10', marque : marqueVolvo).save()
                
                def marqueUsuzu = new Marque(libelle : 'ISUZU').save()
                new Model(libelle : 'D-MAX', marque : marqueUsuzu).save()
                new Model(libelle : 'LANDWIND', marque : marqueUsuzu).save()
                new Model(libelle : 'SUPER CARRY', marque : marqueUsuzu).save()
                new Model(libelle : 'TFR PICKUP', marque : marqueUsuzu).save()
                new Model(libelle : 'TROOPER', marque : marqueUsuzu).save()

                def marqueDaf = new Marque(libelle : 'DAF').save()
                new Model(libelle : 'BACHE', marque : marqueDaf).save()
                new Model(libelle : 'DAF X85', marque : marqueDaf).save()
                new Model(libelle : 'CF Series', marque : marqueDaf).save()
                new Model(libelle : 'LF Series', marque : marqueDaf).save()
                new Model(libelle : 'LF Hybrid', marque : marqueDaf).save()
                def DAFModel = new Model(libelle : 'XF105', marque : marqueDaf).save()
                
                def marqueMan = new Marque(libelle : 'MAN').save()
                new Model(libelle : 'PLATEAU', marque : marqueMan).save()
                new Model(libelle : 'TGX', marque : marqueMan).save()
                new Model(libelle : 'TGS', marque : marqueMan).save()
                new Model(libelle : 'TGL', marque : marqueMan).save()
                def model_Man_TGM = new Model(libelle : 'TGM', marque : marqueMan).save()

                def marqueBerliet = new Marque(libelle : 'BERLIET').save()
                new Model(libelle : '1/66 & sim', marque : marqueBerliet).save()
                new Model(libelle : '1/43 & sim', marque : marqueBerliet).save()
                new Model(libelle : '1/24 & sim', marque : marqueBerliet).save()
                new Model(libelle : 'Le Mans', marque : marqueBerliet).save()
                new Model(libelle : 'Le Mans', marque : marqueBerliet).save()
                new Model(libelle : 'Mille Miglia', marque : marqueBerliet).save()
                def model_Berliet_Mini = new Model(libelle : 'Mini', marque : marqueBerliet).save()
                
                def marquePinguely = new Marque(libelle : 'PINGUELY').save()
                new Model(libelle : 'TTR 220-20T', marque : marquePinguely).save()
                new Model(libelle : 'TT 10', marque : marquePinguely).save()
                new Model(libelle : 'INTEGRALE 18-TTR180', marque : marquePinguely).save()
                new Model(libelle : '5TTR230R', marque : marquePinguely).save()
                new Model(libelle : 'TTR290', marque : marquePinguely).save()
                new Model(libelle : 'TT286', marque : marquePinguely).save()
                new Model(libelle : 'GC20250', marque : marquePinguely).save()

                def marqueGrove = new Marque(libelle : 'GROVE').save()
                new Model(libelle : '33T', marque : marqueGrove).save()
                new Model(libelle : '45T', marque : marqueGrove).save()

                def marqueCorradini = new Marque(libelle : 'CORRADINI').save()
                def CORRADINI_12T = new Model(libelle : '12T', marque : marqueCorradini).save()
                new Model(libelle : 'LC30', marque : marqueCorradini).save()
                new Model(libelle : 'EU840TI', marque : marqueCorradini).save()
                new Model(libelle : '965TV', marque : marqueCorradini).save()
                new Model(libelle : '824 TI', marque : marqueCorradini).save()

                def marquePeugeot = new Marque(libelle : 'Peugeot').save()
                new Model(libelle : '206', marque : marquePeugeot).save()
                def modelPg207 = new Model(libelle : '207', marque : marquePeugeot).save()
                new Model(libelle : '306', marque : marquePeugeot).save()
                new Model(libelle : '307', marque : marquePeugeot).save()
                new Model(libelle : '308', marque : marquePeugeot).save()
                new Model(libelle : '406', marque : marquePeugeot).save()
                new Model(libelle : '407', marque : marquePeugeot).save()
                new Model(libelle : '607', marque : marquePeugeot).save()
                new Model(libelle : '807', marque : marquePeugeot).save()
                new Model(libelle : 'Patner', marque : marquePeugeot).save()
                    
                def marqueRenault = new Marque(libelle : 'RENAULT').save()
                new Model(libelle : 'BACHE', marque : marqueRenault).save()
                new Model(libelle : '340', marque : marqueRenault).save()
                new Model(libelle : '440', marque : marqueRenault).save()
                new Model(libelle : 'Clio', marque : marqueRenault).save()
                new Model(libelle : 'Espace', marque : marqueRenault).save()
                new Model(libelle : 'Fluence', marque : marqueRenault).save()
                def kongooModel = new Model(libelle : 'Kangoo', marque : marqueRenault).save()
                new Model(libelle : 'Koleos', marque : marqueRenault).save()
                new Model(libelle : 'Logan', marque : marqueRenault).save()
                def modelMegane = new Model(libelle : 'Megane', marque : marqueRenault).save()
                new Model(libelle : 'Sandero', marque : marqueRenault).save()
                new Model(libelle : 'Scenic', marque : marqueRenault).save()
                new Model(libelle : 'Symbol', marque : marqueRenault).save()

                def marqueScania = new Marque(libelle : 'SCANIA').save()
                new Model(libelle : '113', marque : marqueScania).save()
                
                def marqueNA = new Marque(libelle : 'N/A').save()
                new Model(libelle : 'PLATEAU', marque : marqueScania).save()
                
                def marqueSeat = new Marque(libelle : 'Seat').save()
                new Model(libelle : 'Cordoba', marque : marqueSeat).save()
                new Model(libelle : 'Ibiza', marque : marqueSeat).save()
                new Model(libelle : 'Leon', marque : marqueSeat).save()

                def marqueSuziki = new Marque(libelle : 'Suziki').save()
                new Model(libelle : 'Alto', marque : marqueSuziki).save()
                new Model(libelle : 'Baleno', marque : marqueSuziki).save()
                new Model(libelle : 'Carry', marque : marqueSuziki).save()
                def modelSwift = new Model(libelle : 'Swift', marque : marqueSuziki).save()

                def marqueToyota = new Marque(libelle : 'Toyota').save()
                new Model(libelle : 'Auris', marque : marqueToyota).save()
                new Model(libelle : 'Avensis', marque : marqueToyota).save()
                new Model(libelle : 'Corolla', marque : marqueToyota).save()
                new Model(libelle : 'Corolla verso', marque : marqueToyota).save()
                new Model(libelle : 'Prado', marque : marqueToyota).save()
                new Model(libelle : 'RAV4', marque : marqueToyota).save()
                new Model(libelle : 'Verso', marque : marqueToyota).save()
                new Model(libelle : 'Yaris', marque : marqueToyota).save()
                new Model(libelle : 'Yaris verso', marque : marqueToyota).save()

                def marqueVolkswagon = new Marque(libelle : 'Volkswagon').save()
                new Model(libelle : 'Bora', marque : marqueVolkswagon).save()
                new Model(libelle : 'Caddy', marque : marqueVolkswagon).save()
                new Model(libelle : 'Fox', marque : marqueVolkswagon).save()
                new Model(libelle : 'Gol', marque : marqueVolkswagon).save()
                new Model(libelle : 'Passat', marque : marqueVolkswagon).save()
                def modelPolo = new Model(libelle : 'Polo', marque : marqueVolkswagon).save()
                new Model(libelle : 'Tiguan', marque : marqueVolkswagon).save()
                def touaregModel = new Model(libelle : 'Touareg', marque : marqueVolkswagon).save()
                new Model(libelle : 'Touran', marque : marqueVolkswagon).save()
                
                //Marques et Models de voiture
//                def marqueAlfaromeo = new Marque(libelle : 'Alfa romeo').save()
//                new Model(libelle : '145', marque : marqueAlfaromeo).save()
//                new Model(libelle : '146', marque : marqueAlfaromeo).save()
//                new Model(libelle : '147', marque : marqueAlfaromeo).save()
//                new Model(libelle : '156', marque : marqueAlfaromeo).save()
//                new Model(libelle : '159', marque : marqueAlfaromeo).save()
//                new Model(libelle : '164', marque : marqueAlfaromeo).save()
//                new Model(libelle : '166', marque : marqueAlfaromeo).save()

                def marqueAudi = new Marque(libelle : 'Audi').save()
                new Model(libelle : 'A1', marque : marqueAudi).save()
                new Model(libelle : 'A2', marque : marqueAudi).save()
                new Model(libelle : 'A3', marque : marqueAudi).save()
                new Model(libelle : 'A4', marque : marqueAudi).save()
                new Model(libelle : 'A5', marque : marqueAudi).save()
                new Model(libelle : 'A6', marque : marqueAudi).save()
                new Model(libelle : 'A8', marque : marqueAudi).save()
                new Model(libelle : 'Q5', marque : marqueAudi).save()
                new Model(libelle : 'Q7', marque : marqueAudi).save()
                new Model(libelle : '164', marque : marqueAudi).save()

                def marqueBmw = new Marque(libelle : 'BMW').save()
                new Model(libelle : 'M', marque : marqueBmw).save()
                new Model(libelle : 'Serie 1', marque : marqueBmw).save()
                new Model(libelle : 'Serie 3', marque : marqueBmw).save()
                new Model(libelle : 'Serie 5', marque : marqueBmw).save()
                new Model(libelle : 'Serie 6', marque : marqueBmw).save()
                new Model(libelle : 'Serie 7', marque : marqueBmw).save()
                new Model(libelle : 'X3', marque : marqueBmw).save()
                new Model(libelle : 'X5', marque : marqueBmw).save()
                new Model(libelle : 'X6', marque : marqueBmw).save()

//                def marqueChoverolet = new Marque(libelle : 'Choverolet').save()
//                new Model(libelle : 'Optra', marque : marqueChoverolet).save()
//                new Model(libelle : 'Spark', marque : marqueChoverolet).save()
//                    
//                def marqueChery = new Marque(libelle : 'Chery').save()
//                new Model(libelle : 'A113', marque : marqueChery).save()
//                new Model(libelle : 'A516', marque : marqueChery).save()
//                new Model(libelle : 'Eastar', marque : marqueChery).save()
//                new Model(libelle : 'QQ', marque : marqueChery).save()
//                new Model(libelle : 'Tiggo', marque : marqueChery).save()

                def marqueCitroen = new Marque(libelle : 'Citroen').save()
                new Model(libelle : 'Berlingo', marque : marqueCitroen).save()
                new Model(libelle : 'C1', marque : marqueCitroen).save()
                new Model(libelle : 'C2', marque : marqueCitroen).save()
                new Model(libelle : 'C3', marque : marqueCitroen).save()
                new Model(libelle : 'C4', marque : marqueCitroen).save()
                new Model(libelle : 'C4 Picasso', marque : marqueCitroen).save()
                new Model(libelle : 'C5', marque : marqueCitroen).save()
                new Model(libelle : 'C6', marque : marqueCitroen).save()
                new Model(libelle : 'Jumper', marque : marqueCitroen).save()
                new Model(libelle : 'Jumpy', marque : marqueCitroen).save()
                new Model(libelle : 'Saxo', marque : marqueCitroen).save()
                new Model(libelle : 'Xsara', marque : marqueCitroen).save()
                new Model(libelle : 'Xsara Picasso', marque : marqueCitroen).save()

                def marqueDacia = new Marque(libelle : 'Dacia').save()
                new Model(libelle : 'Duster', marque : marqueDacia).save()
                def modelLogan = new Model(libelle : 'Logan', marque : marqueDacia).save()
                new Model(libelle : 'Sandero', marque : marqueDacia).save()
                new Model(libelle : 'Solenzo', marque : marqueDacia).save()

                def marqueDaihatsu = new Marque(libelle : 'Daihatsu').save()
                new Model(libelle : 'Hijet', marque : marqueDaihatsu).save()
                new Model(libelle : 'Rocky', marque : marqueDaihatsu).save()
                new Model(libelle : 'Terios', marque : marqueDaihatsu).save()
                new Model(libelle : 'Sirion', marque : marqueDaihatsu).save()

                def marqueFiat = new Marque(libelle : 'Fiat').save()
                new Model(libelle : 'Brava', marque : marqueFiat).save()
                new Model(libelle : 'Bravo', marque : marqueFiat).save()
                new Model(libelle : 'Linea', marque : marqueFiat).save()
                new Model(libelle : 'Palio', marque : marqueFiat).save()
                new Model(libelle : 'Panda', marque : marqueFiat).save()
                def modelPunto = new Model(libelle : 'Punto', marque : marqueFiat).save()
                new Model(libelle : 'Siena', marque : marqueFiat).save()
                new Model(libelle : 'Stilo', marque : marqueFiat).save()
                new Model(libelle : 'Uno', marque : marqueFiat).save()

                def marqueFord = new Marque(libelle : 'Ford').save()
                new Model(libelle : 'Fiesta', marque : marqueFord).save()
                new Model(libelle : 'Focus', marque : marqueFord).save()
                new Model(libelle : 'Fusion', marque : marqueFord).save()
                new Model(libelle : 'Tourneo', marque : marqueFord).save()

                def marqueHonda = new Marque(libelle : 'Honda').save()
                new Model(libelle : 'Accord', marque : marqueHonda).save()
                new Model(libelle : 'City', marque : marqueHonda).save()
                new Model(libelle : 'Civic', marque : marqueHonda).save()
                new Model(libelle : 'Jazz', marque : marqueHonda).save()
                new Model(libelle : 'Legend', marque : marqueHonda).save()
                new Model(libelle : 'Odyssey', marque : marqueHonda).save()
                new Model(libelle : 'Stream', marque : marqueHonda).save()
                new Model(libelle : 'Vigor', marque : marqueHonda).save()

                def marqueHyundai = new Marque(libelle : 'Hyundai').save()
                new Model(libelle : 'Accent', marque : marqueHyundai).save()
                def modelAtos = new Model(libelle : 'Atos', marque : marqueHyundai).save()
                new Model(libelle : 'Atos Prime', marque : marqueHyundai).save()
                new Model(libelle : 'Azera', marque : marqueHyundai).save()
                new Model(libelle : 'Elantra', marque : marqueHyundai).save()
                new Model(libelle : 'Galloper', marque : marqueHyundai).save()
                new Model(libelle : 'i10', marque : marqueHyundai).save()
                new Model(libelle : 'i30', marque : marqueHyundai).save()
                new Model(libelle : 'Tucson', marque : marqueHyundai).save()

                def marqueKia = new Marque(libelle : 'Kia').save()
                new Model(libelle : 'Carens', marque : marqueKia).save()
                new Model(libelle : "Cee'd", marque : marqueKia).save()
                new Model(libelle : 'Cerato', marque : marqueKia).save()
                def modelPicanto = new Model(libelle : 'Picanto', marque : marqueKia).save()
                new Model(libelle : 'Rio', marque : marqueKia).save()
                new Model(libelle : 'Sorento', marque : marqueKia).save()
                new Model(libelle : 'Soul', marque : marqueKia).save()
                new Model(libelle : 'Sportage', marque : marqueKia).save()

                def marqueMazda = new Marque(libelle : 'Mazda').save()
                new Model(libelle : '2', marque : marqueMazda).save()
                new Model(libelle : '3', marque : marqueMazda).save()
                new Model(libelle : '323', marque : marqueMazda).save()
                new Model(libelle : '5', marque : marqueMazda).save()
                new Model(libelle : '6', marque : marqueMazda).save()
                new Model(libelle : '626', marque : marqueMazda).save()

                def marqueMercedesBenz = new Marque(libelle : 'Mercedes-Benz').save()
                new Model(libelle : '190', marque : marqueMercedesBenz).save()
                new Model(libelle : '200', marque : marqueMercedesBenz).save()
                new Model(libelle : '207', marque : marqueMercedesBenz).save()
                new Model(libelle : '250', marque : marqueMercedesBenz).save()
                new Model(libelle : '260', marque : marqueMercedesBenz).save()
                new Model(libelle : '270', marque : marqueMercedesBenz).save()
                new Model(libelle : '300', marque : marqueMercedesBenz).save()
                new Model(libelle : '310', marque : marqueMercedesBenz).save()
                new Model(libelle : '350', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe A', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe B', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe C', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe CL', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe CLK', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe CLS', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe D', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe E', marque : marqueMercedesBenz).save()

//                def marqueMini = new Marque(libelle : 'Mini').save()
//                new Model(libelle : 'Cabrio', marque : marqueMini).save()
//                new Model(libelle : 'Cooper', marque : marqueMini).save()

                def marqueMitsubishi = new Marque(libelle : 'Mini').save()
                new Model(libelle : 'L200', marque : marqueMitsubishi).save()
                new Model(libelle : 'L300', marque : marqueMitsubishi).save()
                new Model(libelle : 'Lancer', marque : marqueMitsubishi).save()

//                def marqueNissan = new Marque(libelle : 'Nissan').save()
//                new Model(libelle : 'Cedric', marque : marqueNissan).save()
//                new Model(libelle : 'Micra', marque : marqueNissan).save()
//                new Model(libelle : 'Prado', marque : marqueNissan).save()
//                new Model(libelle : 'Premira', marque : marqueNissan).save()
//                new Model(libelle : 'Qashqai', marque : marqueNissan).save()
//                new Model(libelle : 'Tirrano', marque : marqueNissan).save()
//                new Model(libelle : 'X-Trail', marque : marqueNissan).save()

                def marqueOpel = new Marque(libelle : 'Opel').save()
                new Model(libelle : 'Astra', marque : marqueOpel).save()
                new Model(libelle : 'Combo', marque : marqueOpel).save()
                new Model(libelle : 'Prado', marque : marqueOpel).save()
                new Model(libelle : 'Corsa', marque : marqueOpel).save()
                new Model(libelle : 'Insignia', marque : marqueOpel).save()
                new Model(libelle : 'Tigra', marque : marqueOpel).save()
                  
                //societes
                def obati = new Societe(code : 'obati', raisonSociale : 'OBATI', societeDuGroup : true, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                def LT_LOGISTICS = new Societe(code : 'lt', raisonSociale : 'LT LOGISTICS', societeDuGroup : true, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'locagrue', raisonSociale : 'LOCAGRUE', societeDuGroup : true, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'ventec', raisonSociale : 'Ventec Groupe', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                def yapi = new Societe(code : 'yapi', raisonSociale : 'Yapi Merkezi', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                def afriquia_gaz = new Societe(code : 'afriquia_gaz', raisonSociale : 'Afriquia Gaz', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                def Vitogaz = new Societe(code : 'vitogaz', raisonSociale : 'Vitogaz', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                def SBI_Schitter = new Societe(code : 'sbi', raisonSociale : 'SBI Schitter', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                def beromar = new Societe(code : 'beromar', raisonSociale : 'Beromar', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'prefa', raisonSociale : 'Prefa Maroc', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'lafarge', raisonSociale : 'Lafarge', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'spaib', raisonSociale : 'Spaib Elonk', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'sipti', raisonSociale : 'Sipti cobomi', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'navatis', raisonSociale : 'Navatis', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'flm', raisonSociale : 'FLM', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Societe(code : 'ecowell', raisonSociale : 'Ecowell', societeDuGroup : false, idFiscal : '', patente : '', rc : '', cnss : '', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()

                //Fournisseurs
                new Fournisseur(code : 'f1', raisonSociale : 'daba', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                new Fournisseur(code : 'f2', raisonSociale : 'zid', adresse : '', ville : '', tel : '', fax : '', email : '', siteWeb : '').save()
                
                 // nationalite
                def marocain = new Nationalite(libelle : 'Maroc').save()
                new Nationalite(libelle : 'France').save()
                new Nationalite(libelle : 'Angletaire').save()
                new Nationalite(libelle : 'Italie').save()
                
                // Trajets
                def trajet1 = new Trajet(code : 'T1', source : 'Casa', destination : 'Rabat', kilometrage : 90).save()
                def trajet2 = new Trajet(code : 'T2', source : 'Casa', destination : 'Eljadida', kilometrage : 90).save()
                def trajet3 = new Trajet(code : 'T3', source : 'Casa', destination : 'Fes', kilometrage : 400).save()
                def trajet4 = new Trajet(code : 'T4', source : 'Rabat', destination : 'settat', kilometrage : 130).save()
                
                // Regions
                def region1 = new Region(code : 'R1', intitule : 'Grande casablanca', kilometrageMin : 25, kilometrageMax : 35).save()
                def region2 = new Region(code : 'R2', intitule : 'Settat', kilometrageMin : 15, kilometrageMax : 25).save()
                
                
                // Tarification
                def tarif1 = new Tarification(modeTarif : 'Journalier', annee : 2012, debutPeriode : 1, finPeriode : 25, quantite : 1, unite : 'Jour', prix : 250d, clients : [Vitogaz]).save()
                def tarif2 = new Tarification(modeTarif : 'Forfitaire/Trajet', annee : 2012, debutPeriode : 1, finPeriode : 25, quantite : 1, unite : 'Trajet', prix : 2500d, trajet : trajet1, clients : [afriquia_gaz]).save()
                def tarif3 = new Tarification(modeTarif : 'Forfitaire/Region', annee : 2012, debutPeriode : 1, finPeriode : 25, quantite : 1, unite : 'Region', prix : 2000d, region : region1, clients : [yapi]).save()
                def tarif4 = new Tarification(modeTarif : 'Tonnage/Trajet', annee : 2012, debutPeriode : 1, finPeriode : 25, quantite : 100, unite : 'Tonne', prix : 3000d, trajet : trajet2, clients : [afriquia_gaz]).save()
                def tarif5 = new Tarification(modeTarif : 'Kilometrage', annee : 2012, debutPeriode : 1, finPeriode : 25, quantite : 100, unite : 'Km', prix : 200d, clients : [beromar]).save()
                def tarif6 = new Tarification(modeTarif : 'Forfitaire/Trajet', annee : 2012, debutPeriode : 1, finPeriode : 25, quantite : 1, unite : 'Trajet', prix : 2200d, trajet : trajet3, clients : [afriquia_gaz]).save()
                def tarif7 = new Tarification(modeTarif : 'Tonnage/Trajet', annee : 2012, debutPeriode : 1, finPeriode : 25, quantite : 50, unite : 'Tonne', prix : 3050d, trajet : trajet4, clients : [afriquia_gaz]).save()
                
                // Intervenants
                def abdelkarim = new Intervenant(code : 'abd', cin : 'BB1664', nom : 'ABDELKARIM', prenom : 'ABDELKARIM', typePermis : 'B',
                    numeroPermis : 'ZAFF123f', sousTraite : false, lieuNaissance : 'casa', dateNaissance : new Date()-10950, adresse : 'bd SDFS 234 ZZ',
                    numeroCNSS : '3245325', gsm : '056055040', email : 'abdelkarim@choranet.com', commission : 5d, nationalite : marocain, employer : obati).save()

                def azouz = new Intervenant(code : 'az', cin : 'BH1664', nom : 'azouz', prenom : 'azouz', typePermis : 'C',
                    numeroPermis : 'ZATR123f', sousTraite : false, lieuNaissance : 'casa', dateNaissance : new Date()-10585, adresse : 'bd gh 234 ZZ',
                    numeroCNSS : '3245325', gsm : '0660550404', email : 'azouz@choranet.com',commission : 6d, nationalite : marocain, employer : LT_LOGISTICS).save()

                def khalil = new Intervenant(code : 'kh', cin : 'BK1664', nom : 'khalil', prenom : 'ezzahir', typePermis : 'A',
                    numeroPermis : 'ZSDR123f', sousTraite : false, lieuNaissance : 'casa', dateNaissance : new Date()-10220, adresse : 'bd gh 234 ZZ',
                    numeroCNSS : '3245325', gsm : '0660550404', email : 'aezzahir@choranet.com', commission : 8d, nationalite : marocain, employer : LT_LOGISTICS).save()
				
				

                //Categories des vehicules
                def camion = new Categorie(libelle : 'CAMION', prixJournalier : 500d, categoryParente : null).save()
                def citerneAbutane = new Categorie(libelle : 'CITERNE A BUTANE', prixJournalier : 0d, categoryParente : camion).save()
                def citerneAcide = new Categorie(libelle : 'CITERNE ACIDE', prixJournalier : 0d, categoryParente : camion).save()
                def bache = new Categorie(libelle : 'BACHE', prixJournalier : 0d, categoryParente : camion).save()
                def solo = new Categorie(libelle : 'SOLO', prixJournalier : 700d, categoryParente : camion).save()
                def semiremorque = new Categorie(libelle : 'SEMI-REMORQUE', prixJournalier : 3000d, categoryParente : camion).save()
                def voiture = new Categorie(libelle : 'VOITURE', prixJournalier : 0d, categoryParente : null).save()
                def grues = new Categorie(libelle : 'GRUE', prixJournalier : 600d, categoryParente : null).save()
                
                // Vehicules
                def kongoo = new Vehicule(code :'kongoo', immatriculation : '53326-A-7', numeroRemorque : 'aa', dateMiseEnCirculation : new Date()-365,
                    vehiculeDeService : true, kilometrage : 30500, consommationGasoilMin : 5, consommationGasoilMax : 10, consommationHuileMin : 2, 
                    consommationHuileMax : 4, proprietaireParticulier : null, model : kongooModel, prorprietaireSociete : LT_LOGISTICS, categorie : voiture, energie : end).save()
                
                def touareg = new Vehicule(code :'touareg', immatriculation : '54835-A-7', numeroRemorque : 'ab', dateMiseEnCirculation : new Date()-465,
                    vehiculeDeService : true, kilometrage : 10500, consommationGasoilMin : 5, consommationGasoilMax : 10, consommationHuileMin : 2, 
                    consommationHuileMax : 4, proprietaireParticulier : null, model : touaregModel, prorprietaireSociete : LT_LOGISTICS, 
                    categorie : voiture, energie : end).save()
                
                def volvo1020 = new Vehicule(code :'volvo1020', immatriculation : '16862-A-59', numeroRemorque : 'ac', dateMiseEnCirculation : new Date()-1095,
                    vehiculeDeService : true, kilometrage : 60000, consommationGasoilMin : 5, consommationGasoilMax : 10, consommationHuileMin : 2, 
                    consommationHuileMax : 4, proprietaireParticulier : null, model : volvoModel,
                    prorprietaireSociete : obati, categorie : solo, energie : end).save()
                
                def volvoF88 = new Vehicule(code :'volvoF88', immatriculation : '83563-A-8', numeroRemorque : 'ad', dateMiseEnCirculation : new Date()-1460,
                    vehiculeDeService : true, kilometrage : 87100, consommationGasoilMin : 5, consommationGasoilMax : 10, consommationHuileMin : 2, 
                    consommationHuileMax : 4, proprietaireParticulier : null, model : volvoModel,
                    prorprietaireSociete : obati, categorie : semiremorque, energie : end).save()
                
                def DAF = new Vehicule(code :'daf', immatriculation : '29206-A-7', numeroRemorque : 'ae', dateMiseEnCirculation : new Date()-1460,
                    vehiculeDeService : true, kilometrage : 89100, consommationGasoilMin : 5, consommationGasoilMax : 10, consommationHuileMin : 2, 
                    consommationHuileMax : 4, proprietaireParticulier : null, model : DAFModel,
                    prorprietaireSociete : obati, categorie : semiremorque, energie : end).save()
                
                def vehicule_grues = new Vehicule(code :'grue', immatriculation : '1783-111-2', numeroRemorque : 'af', dateMiseEnCirculation : new Date()-2190,
                    vehiculeDeService : false, kilometrage : 7000, consommationGasoilMin : 5, consommationGasoilMax : 10, consommationHuileMin : 2, 
                    consommationHuileMax : 4, proprietaireParticulier : null, model : model_Berliet_Mini,
                    prorprietaireSociete : SBI_Schitter, categorie : grues, energie : end).save()
                
                // Unités de mesure
                def Litre = new UniteDeMesure(idUnite : 'Litre', valeur : 7.30).save()
                def LitreHuile = new UniteDeMesure(idUnite : 'LitreHuile', valeur : 35.0).save()
                def Repas = new UniteDeMesure(idUnite : 'Repas', valeur : null).save()
                def Heure_gardinage = new UniteDeMesure(idUnite : 'Heure', valeur : 5d).save()
                def Nuit = new UniteDeMesure(idUnite : 'Nuit', valeur : null).save()
                def Dirham = new UniteDeMesure(idUnite : 'Dirham', valeur : 1d).save()
                
                //Categorie frais circulation
                def bonGasoilCtgr = new CategorieFraisCirculation(libelle : 'Bon gasoil', uniteMesure : Litre).save()
                def bonHuileCtgr = new CategorieFraisCirculation(libelle : 'Bon huile', uniteMesure : LitreHuile).save()
                def restauration  = new CategorieFraisCirculation(libelle : 'Restauration', uniteMesure : Repas).save()
                def gardinage     = new CategorieFraisCirculation(libelle : 'Gardinage', uniteMesure : Heure_gardinage).save()
                def hotel         = new CategorieFraisCirculation(libelle : 'Hotel', uniteMesure : Nuit).save()
                def ProcesCirculation = new CategorieFraisCirculation(libelle : 'Procès', uniteMesure : Dirham).save()
                
                    
                // Reservations
                def reservation1 = new Reservation(numReservation : 'manu07032012001', numBC : 'BC0001', mission : 'intervention Ventec', prixParJour : 5000d,
                                    dateDepart : new Date()-30, dateRestitution : new Date()-28, nombreDeJours : 2, lieuLivraison : 'casa', lieuReprise : 'casa', observations : 'urgent',
                                    conducteur : khalil, vehicule : volvo1020, responsableReservations : userAdmin, client : Vitogaz).save()
                                
                def reservation2 = new Reservation(numReservation : 'manu07032012002', numBC : 'BC0002', mission : 'intervention Snep', prixParJour : 6000d,
                                    dateDepart : new Date()-27, dateRestitution : new Date()-25, nombreDeJours : 3, lieuLivraison : 'casa', lieuReprise : 'casa', 
                                    observations : 'sous la décision de DG', conducteur : azouz, vehicule : volvoF88, responsableReservations : userAdmin, client : Vitogaz).save()
                                
                
                // compteurs paterns
                new PaternCompteur(libelle : 'Pattern intervention', prefixe : '{type}{jour}{mois}{annee}', suffixe : '', pas : 1, remplissage : 3, numeroSuivant : 1, type : 'INTERVENTION').save();
                
                // Interventions
//                def intervention1 = new Intervention(numIntervention : '001', type : 'MANUTENTION_LEVAGE', numBC : 'BC0001', mission : 'intervention Ventec', prixParJour : 5000d,
//                                    dateDepart : new Date()-30, dateRestitution : new Date()-28, nombreDeJours : 2, kilometrageDepart : 60100, kilometrageRetour : 60800,
//                                    kilometrageDisque : 7000, lieuLivraison : 'casa', lieuReprise : 'casa', observations : 'dans les normes', contratNumeriser : null,
//                                    numFacture : 'FA12122011', montantFacture : 10000, conducteur : khalil, vehicule : volvo1020, agentLoueurResponsable : userAdmin, client : Vitogaz).save()
//                    interventionService.updateVehiculeKilometrage(intervention1)                                 
//                     
//                    // Frais de circulation d'une intervetion
//                    def gasoil_inter1 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b0001', date : new Date()-30, details : 'bon gasoil intervention 1', montant : 1200d, chargeEnUM : 160,
//                                categorieFraisCirculation : bonGasoilCtgr, deplacement : null, intervention : intervention1))
//                    def restaurant_inter11 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b00011', date : new Date()-30, details : 'restauration', montant : 100d, chargeEnUM : 2,
//                                categorieFraisCirculation : restauration, deplacement : null, intervention : intervention1))
//                    def restaurant_inter12 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b00012', date : new Date()-29, details : 'restauration', montant : 100d, chargeEnUM : 2,
//                                categorieFraisCirculation : restauration, deplacement : null, intervention : intervention1))        
//                    def restaurant_inter13 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b00013', date : new Date()-28, details : 'restauration', montant : 100d, chargeEnUM : 2,
//                                categorieFraisCirculation : restauration, deplacement : null, intervention : intervention1))                
//                            
//                                
//                def intervention2 = new Intervention(numIntervention : '002', type : 'MANUTENTION_LEVAGE', numBC : 'BC0013', mission : 'intervention SNEP', prixParJour : 6000d,
//                                    dateDepart : new Date()-27, dateRestitution : new Date()-25, nombreDeJours : 3, kilometrageDepart : 87100, kilometrageRetour : 97200,
//                                    kilometrageDisque : 800, lieuLivraison : 'ain sebaa', lieuReprise : 'casa', observations : 'non conforme!!!', contratNumeriser : null,
//                                    numFacture : 'FA14122011', montantFacture : 18000, conducteur : azouz, vehicule : volvoF88, agentLoueurResponsable : userAdmin, client : Vitogaz).save()
//                    interventionService.updateVehiculeKilometrage(intervention2)                                 
//                    
//                    // Frais de circulation d'une intervetion
//                    def gasoil_inter2 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b0002', date : new Date()-27, details : 'bon gasoil intervention 2', montant : 1000d, chargeEnUM : 134,
//                                categorieFraisCirculation : bonGasoilCtgr, deplacement : null, intervention : intervention2))
//                    def restaurant_inter21 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b00021', date : new Date()-27, details : 'restauration', montant : 100d, chargeEnUM : 2,
//                                categorieFraisCirculation : restauration, deplacement : null, intervention : intervention2))
//                    def restaurant_inter22 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b00022', date : new Date()-26, details : 'restauration', montant : 100d, chargeEnUM : 2,
//                                categorieFraisCirculation : restauration, deplacement : null, intervention : intervention2))        
//                    def restaurant_inter23 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'b00023', date : new Date()-25, details : 'restauration', montant : 100d, chargeEnUM : 2,
//                                categorieFraisCirculation : restauration, deplacement : null, intervention : intervention2))                
//                    
                                
                // Types des entretiens Normaux non périodiques
                def maintenance = new TypeEntretien(libelle : 'Maintenance provisoire').save()
                def reparation = new TypeEntretien(libelle : 'Réparation').save()
                def lavage = new TypeEntretien(libelle : 'Lavage provisoire').save()
                def pneumatique = new TypeEntretien(libelle : 'Pneumatique').save()
                def batterie = new TypeEntretien(libelle : 'Changement de batterie').save()
                def huile = new TypeEntretien(libelle : 'Changement de huiles').save()
                def peinture = new TypeEntretien(libelle : 'Peinture').save()
                def graissage = new TypeEntretien(libelle : 'Graissage').save()

                // Entretiens
                entretienService.save(new Entretien(numBon : 'be0001', dateEntretien : new Date(), details : 'maintenance', montant : 100.0,
                    typeEntretien : maintenance, vehicule : volvo1020))
                entretienService.save(new Entretien(numBon : 'be0002',dateEntretien : new Date(), details : 'reparation', montant : 200.0,
                    typeEntretien : reparation, vehicule : volvoF88))
                entretienService.save(new Entretien(numBon : 'be0003', dateEntretien : new Date(), details : 'Lavage', montant : 130.0,
                    typeEntretien : lavage, vehicule : vehicule_grues))

                // Types des Entretiens Périodiques
                def vidange = new TypeEntretienPeriodique(libelle:'Vidange camion', periodicite:10000, chargeDeBase:300d, categorieVehicules : camion).save()
                def filtration = new TypeEntretienPeriodique(libelle:'Filtration camion', periodicite:20000, chargeDeBase:200d, categorieVehicules : camion).save()
                def tachygraphe = new TypeEntretienPeriodique(libelle:'Changement de tachygraphe', periodicite:20000, chargeDeBase:200d, categorieVehicules : camion).save()
                
                //def climatisation = new TypeEntretienPeriodique(libelle : 'Climatisation pour voitures', periodicite : 20000, chargeDeBase:350d, categorieVehicules : voiture).save()
                //def vidangeCamion = new TypeEntretienPeriodique(libelle:'Vidange pour camions', periodicite:15000, chargeDeBase:400d, categorieVehicules : camion).save()

                // Entretiens Périodiques
                entretienPeriodiqueService.genererEntretienPeriodique(kongoo)
                entretienPeriodiqueService.genererEntretienPeriodique(touareg)
                entretienPeriodiqueService.genererEntretienPeriodique(volvo1020)
                entretienPeriodiqueService.genererEntretienPeriodique(volvoF88)
                entretienPeriodiqueService.genererEntretienPeriodique(DAF)
                entretienPeriodiqueService.genererEntretienPeriodique(vehicule_grues)
                
                // Type de Frais de circulation périodique
                //def assurance = new TypeFraisCirculationPeriodique(libelle:'Assurance pour voitures', periodicite:365, chargeDeBase:4000, categorieVehicules : voiture).save()
                //def vignette = new TypeFraisCirculationPeriodique(libelle : 'Vignette pour voitures', periodicite:365, chargeDeBase:1500, categorieVehicules : voiture).save()
                //def visite_technique = new TypeFraisCirculationPeriodique(libelle : 'Visite technique pour voitures', periodicite:182, chargeDeBase:400, categorieVehicules : voiture).save()

                def assuranceCamion = new TypeFraisCirculationPeriodique(libelle:'Assurance camion', periodicite:365, chargeDeBase:4000, categorieVehicules : camion).save()
                def vignette = new TypeFraisCirculationPeriodique(libelle : 'Vignette camion', periodicite:365, chargeDeBase:1500, categorieVehicules : camion).save()
                def visite_technique = new TypeFraisCirculationPeriodique(libelle : 'Visite technique camion', periodicite:365, chargeDeBase:400, categorieVehicules : camion).save()
                def taxeAEssieux = new TypeFraisCirculationPeriodique(libelle : "Taxe à l'essieux", periodicite:365, chargeDeBase:400, categorieVehicules : camion).save()
                
                // Frais de circulation périodique
                fraisCirculationPeriodiqueService.genererFraisCirculationPeriodique(kongoo)
                fraisCirculationPeriodiqueService.genererFraisCirculationPeriodique(touareg)
                fraisCirculationPeriodiqueService.genererFraisCirculationPeriodique(volvo1020)
                fraisCirculationPeriodiqueService.genererFraisCirculationPeriodique(volvoF88)
                fraisCirculationPeriodiqueService.genererFraisCirculationPeriodique(DAF)
                fraisCirculationPeriodiqueService.genererFraisCirculationPeriodique(vehicule_grues)

                // Deplacements pour une journé
                 def deplacementCasashors = new Deplacement(numeroDeplacement : 'dp00002', mission : 'Casashors immobilisation',
                                    dateDepart : new Date()-30, dateRetour : new Date()-29, nombreDeJours : 1, kilometrageDepart : 10600, kilometrageRetour : 11100,
                                    distanceParcourue : 500, observations : 'observat', personnel : khalil, voitureService : touareg).save()
                     deplacementService.updateVehiculeKilometrage(deplacementCasashors)           
                    
                // Frais de circulation
                    def gasoil1 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'bg001', date : new Date()-31, details : 'bon gasoil pour déplacement 1', montant : 200d, chargeEnUM : 27,
                                categorieFraisCirculation : bonGasoilCtgr, deplacement : deplacementCasashors, intervention : null))
                    def restaurant1 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'br001', date : new Date()-30, details : 'restauration pour déplacement 1', montant : 150d, chargeEnUM : 2,
                                categorieFraisCirculation : restauration, deplacement : deplacementCasashors, intervention : null))
                        
                // Deplacements pour 2 journés
                def deplacementVentec = new Deplacement(numeroDeplacement : 'dp00001', mission : 'deplacement Tantan',
                                    dateDepart : new Date()-15, dateRetour : new Date()-13, nombreDeJours : 5, kilometrageDepart : 30600, kilometrageRetour : 39000,
                                    distanceParcourue : 300, observations : 'observat', personnel : abdelkarim, voitureService : kongoo).save()
                    deplacementService.updateVehiculeKilometrage(deplacementVentec)           
                    
                    // Frais de circulation
                    def gasoil2 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'bg002',date : new Date()-16, details : 'bon gasoil pour déplacement 2', montant : 300d, chargeEnUM : 40,
                                categorieFraisCirculation : bonGasoilCtgr, deplacement : deplacementVentec, intervention : null))
                    def restaurant21 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'br0021',date : new Date()-15, details : 'restauration pour déplacement 2', montant : 150d, chargeEnUM : 2,
                                categorieFraisCirculation : restauration, deplacement : deplacementVentec, intervention : null))            
                    def restaurant22 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'br0022',date : new Date()-14, details : 'restauration pour déplacement 2', montant : 150d, chargeEnUM : 2,
                                categorieFraisCirculation : restauration, deplacement : deplacementVentec, intervention : null))                    
                    def hotel2 = bonFraisCirculationService.save(new BonFraisCirculation(numBon : 'bh001',date : new Date()-15, details : 'hotel pour déplacement 2', montant : 250d, chargeEnUM : 1,
                                categorieFraisCirculation : restauration, deplacement : deplacementVentec, intervention : null))                                        
            }
            kkkk {
        
                //def clientInfo = new ChoraClientInfo(nomsociete:'CHORA INFORMATIQUE',raisonSocial : 'CHORA INFORMATIQUE', telephone : '05 22 34 23 33', fax : '05 22 34 23 33', email:'contact@choranet.com',patente:null,rc:null,idF:null,cnss:null,site:'www.choranet.com',repertoirBackup:".")
                def clientInfo = new ChoraClientInfo(nomsociete:'Ouarid Transport',raisonSocial : 'Ouarid Transport SARL', telephone : '05 22 40 68 50', fax : '', email:'contact@ouaridtransport.com',patente:'30701738',rc:'51133',idF:'02520020',cnss:'2046060',site:'www.ouaridtransport.com',repertoirBackup:".")
                clientInfo.setTrans_logo(null)
               // println "clientInfo.nomsociete :" + clientInfo.nomsociete
                clientInfo.save()
                def roleRoot = new GroupeUtilisateur(authority:'ROLE_ROOT', description:'Super Utilisateur').save()
                def roleAdmin = new GroupeUtilisateur(authority:'ROLE_ADMIN', description:'Administrateur').save() 
                def roleGestion = new GroupeUtilisateur(authority:'ROLE_GESTION', description:'Gestionnaire du site').save() 
                
                def userRoot = new Utilisateur(username:'root',
                    userRealName:'root',
                    enabled: true,
                    emailShow: true,
                    email: 'admin@choranet.com',
                    passwd: authenticateService.encodePassword('root')).save()                                
                
                def secureRoot = new DroitUtilisateur(url: '/zul/root/**',
                    configAttribute:'ROLE_ROOT').save()
                def secureAdmin = new DroitUtilisateur(url: '/zul/admin/**',
                    configAttribute:'ROLE_ADMIN').save()
                def secureGestion = new DroitUtilisateur(url: '/zul/gestion/**',
                    configAttribute:'ROLE_GESTION').save()
                
                userRoot.addToAuthorities(roleRoot) 
                userRoot.addToAuthorities(roleAdmin)
                userRoot.addToAuthorities(roleGestion)                
                
                //Energie
                def ene = new Energie(libelle : 'Essence').save()
                def end = new Energie(libelle : 'Diesel').save()

                def marqueVolvo = new Marque(libelle : 'VOLVO').save()
                def volvoModel = new Model(libelle : 'V1020', marque : marqueVolvo).save()
                new Model(libelle : 'v', marque : marqueVolvo).save()
                new Model(libelle : 'F10', marque : marqueVolvo).save()
                new Model(libelle : 'FH', marque : marqueVolvo).save()
                new Model(libelle : 'FL', marque : marqueVolvo).save()
                new Model(libelle : 'N10', marque : marqueVolvo).save()
                
                def marqueUsuzu = new Marque(libelle : 'ISUZU').save()
                new Model(libelle : 'D-MAX', marque : marqueUsuzu).save()
                new Model(libelle : 'LANDWIND', marque : marqueUsuzu).save()
                new Model(libelle : 'SUPER CARRY', marque : marqueUsuzu).save()
                new Model(libelle : 'TFR PICKUP', marque : marqueUsuzu).save()
                new Model(libelle : 'TROOPER', marque : marqueUsuzu).save()

                def marqueDaf = new Marque(libelle : 'DAF').save()
                new Model(libelle : 'd', marque : marqueDaf).save()
                new Model(libelle : 'X85', marque : marqueDaf).save()
                new Model(libelle : 'CF Series', marque : marqueDaf).save()
                new Model(libelle : 'LF Series', marque : marqueDaf).save()
                new Model(libelle : 'LF Hybrid', marque : marqueDaf).save()
                def DAFModel = new Model(libelle : 'XF105', marque : marqueDaf).save()
                
                def marqueMan = new Marque(libelle : 'MAN').save()
                new Model(libelle : 'm', marque : marqueMan).save()
                new Model(libelle : 'TGX', marque : marqueMan).save()
                new Model(libelle : 'TGS', marque : marqueMan).save()
                new Model(libelle : 'TGL', marque : marqueMan).save()
                def model_Man_TGM = new Model(libelle : 'TGM', marque : marqueMan).save()

                def marqueBerliet = new Marque(libelle : 'BERLIET').save()
                new Model(libelle : '1/66 & sim', marque : marqueBerliet).save()
                new Model(libelle : '1/43 & sim', marque : marqueBerliet).save()
                new Model(libelle : '1/24 & sim', marque : marqueBerliet).save()
                new Model(libelle : 'Le Mans', marque : marqueBerliet).save()
                new Model(libelle : 'Le Mans', marque : marqueBerliet).save()
                new Model(libelle : 'Mille Miglia', marque : marqueBerliet).save()
                def model_Berliet_Mini = new Model(libelle : 'Mini', marque : marqueBerliet).save()
                
                def marquePinguely = new Marque(libelle : 'PINGUELY').save()
                new Model(libelle : 'TTR 220-20T', marque : marquePinguely).save()
                new Model(libelle : 'TT 10', marque : marquePinguely).save()
                new Model(libelle : 'INTEGRALE 18-TTR180', marque : marquePinguely).save()
                new Model(libelle : '5TTR230R', marque : marquePinguely).save()
                new Model(libelle : 'TTR290', marque : marquePinguely).save()
                new Model(libelle : 'TT286', marque : marquePinguely).save()
                new Model(libelle : 'GC20250', marque : marquePinguely).save()

                def marqueGrove = new Marque(libelle : 'GROVE').save()
                new Model(libelle : '33T', marque : marqueGrove).save()
                new Model(libelle : '45T', marque : marqueGrove).save()

                def marqueCorradini = new Marque(libelle : 'CORRADINI').save()
                def CORRADINI_12T = new Model(libelle : '12T', marque : marqueCorradini).save()
                new Model(libelle : 'LC30', marque : marqueCorradini).save()
                new Model(libelle : 'EU840TI', marque : marqueCorradini).save()
                new Model(libelle : '965TV', marque : marqueCorradini).save()
                new Model(libelle : '824 TI', marque : marqueCorradini).save()

                def marquePeugeot = new Marque(libelle : 'Peugeot').save()
                new Model(libelle : '206', marque : marquePeugeot).save()
                def modelPg207 = new Model(libelle : '207', marque : marquePeugeot).save()
                new Model(libelle : '306', marque : marquePeugeot).save()
                new Model(libelle : '307', marque : marquePeugeot).save()
                new Model(libelle : '308', marque : marquePeugeot).save()
                new Model(libelle : '406', marque : marquePeugeot).save()
                new Model(libelle : '407', marque : marquePeugeot).save()
                new Model(libelle : '607', marque : marquePeugeot).save()
                new Model(libelle : '807', marque : marquePeugeot).save()
                new Model(libelle : 'Patner', marque : marquePeugeot).save()
                    
                def marqueRenault = new Marque(libelle : 'RENAULT').save()
                new Model(libelle : 'r', marque : marqueRenault).save()
                new Model(libelle : 'R340', marque : marqueRenault).save()
                new Model(libelle : 'R440', marque : marqueRenault).save()
                new Model(libelle : 'Clio', marque : marqueRenault).save()
                new Model(libelle : 'Espace', marque : marqueRenault).save()
                new Model(libelle : 'Fluence', marque : marqueRenault).save()
                def kongooModel = new Model(libelle : 'Kangoo', marque : marqueRenault).save()
                new Model(libelle : 'Koleos', marque : marqueRenault).save()
                new Model(libelle : 'Logan', marque : marqueRenault).save()
                def modelMegane = new Model(libelle : 'Megane', marque : marqueRenault).save()
                new Model(libelle : 'Sandero', marque : marqueRenault).save()
                new Model(libelle : 'Scenic', marque : marqueRenault).save()
                new Model(libelle : 'Symbol', marque : marqueRenault).save()

                def marqueScania = new Marque(libelle : 'SCANIA').save()
                new Model(libelle : 'S113', marque : marqueScania).save()
                
                def marqueNA = new Marque(libelle : 'N/A').save()
                new Model(libelle : 'na', marque : marqueScania).save()
                
                def marqueSeat = new Marque(libelle : 'Seat').save()
                new Model(libelle : 'Cordoba', marque : marqueSeat).save()
                new Model(libelle : 'Ibiza', marque : marqueSeat).save()
                new Model(libelle : 'Leon', marque : marqueSeat).save()

                def marqueSuziki = new Marque(libelle : 'Suziki').save()
                new Model(libelle : 'Alto', marque : marqueSuziki).save()
                new Model(libelle : 'Baleno', marque : marqueSuziki).save()
                new Model(libelle : 'Carry', marque : marqueSuziki).save()
                def modelSwift = new Model(libelle : 'Swift', marque : marqueSuziki).save()

                def marqueToyota = new Marque(libelle : 'Toyota').save()
                new Model(libelle : 'Auris', marque : marqueToyota).save()
                new Model(libelle : 'Avensis', marque : marqueToyota).save()
                new Model(libelle : 'Corolla', marque : marqueToyota).save()
                new Model(libelle : 'Corolla verso', marque : marqueToyota).save()
                new Model(libelle : 'Prado', marque : marqueToyota).save()
                new Model(libelle : 'RAV4', marque : marqueToyota).save()
                new Model(libelle : 'Verso', marque : marqueToyota).save()
                new Model(libelle : 'Yaris', marque : marqueToyota).save()
                new Model(libelle : 'Yaris verso', marque : marqueToyota).save()

                def marqueVolkswagon = new Marque(libelle : 'Volkswagon').save()
                new Model(libelle : 'Bora', marque : marqueVolkswagon).save()
                new Model(libelle : 'Caddy', marque : marqueVolkswagon).save()
                new Model(libelle : 'Fox', marque : marqueVolkswagon).save()
                new Model(libelle : 'Gol', marque : marqueVolkswagon).save()
                new Model(libelle : 'Passat', marque : marqueVolkswagon).save()
                def modelPolo = new Model(libelle : 'Polo', marque : marqueVolkswagon).save()
                new Model(libelle : 'Tiguan', marque : marqueVolkswagon).save()
                def touaregModel = new Model(libelle : 'Touareg', marque : marqueVolkswagon).save()
                new Model(libelle : 'Touran', marque : marqueVolkswagon).save()
                
                //Marques et Models de voiture
//                def marqueAlfaromeo = new Marque(libelle : 'Alfa romeo').save()
//                new Model(libelle : '145', marque : marqueAlfaromeo).save()
//                new Model(libelle : '146', marque : marqueAlfaromeo).save()
//                new Model(libelle : '147', marque : marqueAlfaromeo).save()
//                new Model(libelle : '156', marque : marqueAlfaromeo).save()
//                new Model(libelle : '159', marque : marqueAlfaromeo).save()
//                new Model(libelle : '164', marque : marqueAlfaromeo).save()
//                new Model(libelle : '166', marque : marqueAlfaromeo).save()

                def marqueAudi = new Marque(libelle : 'Audi').save()
                new Model(libelle : 'A1', marque : marqueAudi).save()
                new Model(libelle : 'A2', marque : marqueAudi).save()
                new Model(libelle : 'A3', marque : marqueAudi).save()
                new Model(libelle : 'A4', marque : marqueAudi).save()
                new Model(libelle : 'A5', marque : marqueAudi).save()
                new Model(libelle : 'A6', marque : marqueAudi).save()
                new Model(libelle : 'A8', marque : marqueAudi).save()
                new Model(libelle : 'Q5', marque : marqueAudi).save()
                new Model(libelle : 'Q7', marque : marqueAudi).save()
                new Model(libelle : '164', marque : marqueAudi).save()

                def marqueBmw = new Marque(libelle : 'BMW').save()
                new Model(libelle : 'M', marque : marqueBmw).save()
                new Model(libelle : 'Serie 1', marque : marqueBmw).save()
                new Model(libelle : 'Serie 3', marque : marqueBmw).save()
                new Model(libelle : 'Serie 5', marque : marqueBmw).save()
                new Model(libelle : 'Serie 6', marque : marqueBmw).save()
                new Model(libelle : 'Serie 7', marque : marqueBmw).save()
                new Model(libelle : 'X3', marque : marqueBmw).save()
                new Model(libelle : 'X5', marque : marqueBmw).save()
                new Model(libelle : 'X6', marque : marqueBmw).save()

//                def marqueChoverolet = new Marque(libelle : 'Choverolet').save()
//                new Model(libelle : 'Optra', marque : marqueChoverolet).save()
//                new Model(libelle : 'Spark', marque : marqueChoverolet).save()
//                    
//                def marqueChery = new Marque(libelle : 'Chery').save()
//                new Model(libelle : 'A113', marque : marqueChery).save()
//                new Model(libelle : 'A516', marque : marqueChery).save()
//                new Model(libelle : 'Eastar', marque : marqueChery).save()
//                new Model(libelle : 'QQ', marque : marqueChery).save()
//                new Model(libelle : 'Tiggo', marque : marqueChery).save()

                def marqueCitroen = new Marque(libelle : 'Citroen').save()
                new Model(libelle : 'Berlingo', marque : marqueCitroen).save()
                new Model(libelle : 'C1', marque : marqueCitroen).save()
                new Model(libelle : 'C2', marque : marqueCitroen).save()
                new Model(libelle : 'C3', marque : marqueCitroen).save()
                new Model(libelle : 'C4', marque : marqueCitroen).save()
                new Model(libelle : 'C4 Picasso', marque : marqueCitroen).save()
                new Model(libelle : 'C5', marque : marqueCitroen).save()
                new Model(libelle : 'C6', marque : marqueCitroen).save()
                new Model(libelle : 'Jumper', marque : marqueCitroen).save()
                new Model(libelle : 'Jumpy', marque : marqueCitroen).save()
                new Model(libelle : 'Saxo', marque : marqueCitroen).save()
                new Model(libelle : 'Xsara', marque : marqueCitroen).save()
                new Model(libelle : 'Xsara Picasso', marque : marqueCitroen).save()

                def marqueDacia = new Marque(libelle : 'Dacia').save()
                new Model(libelle : 'Duster', marque : marqueDacia).save()
                def modelLogan = new Model(libelle : 'Logan', marque : marqueDacia).save()
                new Model(libelle : 'Sandero', marque : marqueDacia).save()
                new Model(libelle : 'Solenzo', marque : marqueDacia).save()

                def marqueDaihatsu = new Marque(libelle : 'Daihatsu').save()
                new Model(libelle : 'Hijet', marque : marqueDaihatsu).save()
                new Model(libelle : 'Rocky', marque : marqueDaihatsu).save()
                new Model(libelle : 'Terios', marque : marqueDaihatsu).save()
                new Model(libelle : 'Sirion', marque : marqueDaihatsu).save()

                def marqueFiat = new Marque(libelle : 'Fiat').save()
                new Model(libelle : 'Brava', marque : marqueFiat).save()
                new Model(libelle : 'Bravo', marque : marqueFiat).save()
                new Model(libelle : 'Linea', marque : marqueFiat).save()
                new Model(libelle : 'Palio', marque : marqueFiat).save()
                new Model(libelle : 'Panda', marque : marqueFiat).save()
                def modelPunto = new Model(libelle : 'Punto', marque : marqueFiat).save()
                new Model(libelle : 'Siena', marque : marqueFiat).save()
                new Model(libelle : 'Stilo', marque : marqueFiat).save()
                new Model(libelle : 'Uno', marque : marqueFiat).save()

                def marqueFord = new Marque(libelle : 'Ford').save()
                new Model(libelle : 'Fiesta', marque : marqueFord).save()
                new Model(libelle : 'Focus', marque : marqueFord).save()
                new Model(libelle : 'Fusion', marque : marqueFord).save()
                new Model(libelle : 'Tourneo', marque : marqueFord).save()

                def marqueHonda = new Marque(libelle : 'Honda').save()
                new Model(libelle : 'Accord', marque : marqueHonda).save()
                new Model(libelle : 'City', marque : marqueHonda).save()
                new Model(libelle : 'Civic', marque : marqueHonda).save()
                new Model(libelle : 'Jazz', marque : marqueHonda).save()
                new Model(libelle : 'Legend', marque : marqueHonda).save()
                new Model(libelle : 'Odyssey', marque : marqueHonda).save()
                new Model(libelle : 'Stream', marque : marqueHonda).save()
                new Model(libelle : 'Vigor', marque : marqueHonda).save()

                def marqueHyundai = new Marque(libelle : 'Hyundai').save()
                new Model(libelle : 'Accent', marque : marqueHyundai).save()
                def modelAtos = new Model(libelle : 'Atos', marque : marqueHyundai).save()
                new Model(libelle : 'Atos Prime', marque : marqueHyundai).save()
                new Model(libelle : 'Azera', marque : marqueHyundai).save()
                new Model(libelle : 'Elantra', marque : marqueHyundai).save()
                new Model(libelle : 'Galloper', marque : marqueHyundai).save()
                new Model(libelle : 'i10', marque : marqueHyundai).save()
                new Model(libelle : 'i30', marque : marqueHyundai).save()
                new Model(libelle : 'Tucson', marque : marqueHyundai).save()

                def marqueKia = new Marque(libelle : 'Kia').save()
                new Model(libelle : 'Carens', marque : marqueKia).save()
                new Model(libelle : "Cee'd", marque : marqueKia).save()
                new Model(libelle : 'Cerato', marque : marqueKia).save()
                def modelPicanto = new Model(libelle : 'Picanto', marque : marqueKia).save()
                new Model(libelle : 'Rio', marque : marqueKia).save()
                new Model(libelle : 'Sorento', marque : marqueKia).save()
                new Model(libelle : 'Soul', marque : marqueKia).save()
                new Model(libelle : 'Sportage', marque : marqueKia).save()

                def marqueMazda = new Marque(libelle : 'Mazda').save()
                new Model(libelle : '2', marque : marqueMazda).save()
                new Model(libelle : '3', marque : marqueMazda).save()
                new Model(libelle : '323', marque : marqueMazda).save()
                new Model(libelle : '5', marque : marqueMazda).save()
                new Model(libelle : '6', marque : marqueMazda).save()
                new Model(libelle : '626', marque : marqueMazda).save()

                def marqueMercedesBenz = new Marque(libelle : 'Mercedes-Benz').save()
                new Model(libelle : '190', marque : marqueMercedesBenz).save()
                new Model(libelle : '200', marque : marqueMercedesBenz).save()
                new Model(libelle : '207', marque : marqueMercedesBenz).save()
                new Model(libelle : '250', marque : marqueMercedesBenz).save()
                new Model(libelle : '260', marque : marqueMercedesBenz).save()
                new Model(libelle : '270', marque : marqueMercedesBenz).save()
                new Model(libelle : '300', marque : marqueMercedesBenz).save()
                new Model(libelle : '310', marque : marqueMercedesBenz).save()
                new Model(libelle : '350', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe A', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe B', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe C', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe CL', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe CLK', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe CLS', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe D', marque : marqueMercedesBenz).save()
                new Model(libelle : 'Classe E', marque : marqueMercedesBenz).save()

//                def marqueMini = new Marque(libelle : 'Mini').save()
//                new Model(libelle : 'Cabrio', marque : marqueMini).save()
//                new Model(libelle : 'Cooper', marque : marqueMini).save()

                def marqueMitsubishi = new Marque(libelle : 'Mini').save()
                new Model(libelle : 'L200', marque : marqueMitsubishi).save()
                new Model(libelle : 'L300', marque : marqueMitsubishi).save()
                new Model(libelle : 'Lancer', marque : marqueMitsubishi).save()

//                def marqueNissan = new Marque(libelle : 'Nissan').save()
//                new Model(libelle : 'Cedric', marque : marqueNissan).save()
//                new Model(libelle : 'Micra', marque : marqueNissan).save()
//                new Model(libelle : 'Prado', marque : marqueNissan).save()
//                new Model(libelle : 'Premira', marque : marqueNissan).save()
//                new Model(libelle : 'Qashqai', marque : marqueNissan).save()
//                new Model(libelle : 'Tirrano', marque : marqueNissan).save()
//                new Model(libelle : 'X-Trail', marque : marqueNissan).save()

                def marqueOpel = new Marque(libelle : 'Opel').save()
                new Model(libelle : 'Astra', marque : marqueOpel).save()
                new Model(libelle : 'Combo', marque : marqueOpel).save()
                new Model(libelle : 'Prado', marque : marqueOpel).save()
                new Model(libelle : 'Corsa', marque : marqueOpel).save()
                new Model(libelle : 'Insignia', marque : marqueOpel).save()
                new Model(libelle : 'Tigra', marque : marqueOpel).save()
                  
                //societes
                //def ouaridTransport = new Societe(code : 'C0', raisonSociale : 'Ouarid Transport', societeDuGroup : true, idFiscal : '', patente : '', rc : '', cnss : '', adresse : 'adresse ouarid', ville : 'Casablanca', tel : '0522 ...', fax : '0522 ...', email : 'contact@ouaridtransport.ma', siteWeb : 'www.ouaridtransport.com').save()
                
                 // nationalite
                def marocain = new Nationalite(libelle : 'Maroc').save()
//                new Nationalite(libelle : 'France').save()
//                new Nationalite(libelle : 'Angletaire').save()
//                new Nationalite(libelle : 'Italie').save()
               
                //Categories des vehicules
                def camion = new Categorie(libelle : 'CAMION', prixJournalier : 500d, categoryParente : null).save()
                def citerneAbutane = new Categorie(libelle : 'CITERNE A BUTANE', prixJournalier : 0d, categoryParente : camion).save()
                def citerneAcide = new Categorie(libelle : 'CITERNE ACIDE', prixJournalier : 0d, categoryParente : camion).save()
                def bache = new Categorie(libelle : 'BACHE', prixJournalier : 0d, categoryParente : camion).save()
                def plateau = new Categorie(libelle : 'PLATEAU', prixJournalier : 0d, categoryParente : camion).save()
                def solo = new Categorie(libelle : 'SOLO', prixJournalier : 700d, categoryParente : camion).save()
                def semiremorque = new Categorie(libelle : 'SEMI-REMORQUE', prixJournalier : 3000d, categoryParente : camion).save()
                def voiture = new Categorie(libelle : 'VOITURE', prixJournalier : 0d, categoryParente : null).save()
                def grues = new Categorie(libelle : 'GRUE', prixJournalier : 600d, categoryParente : null).save()
                
                // Unités de mesure
                def Litre = new UniteDeMesure(idUnite : 'Litre', valeur : 7.30).save()
                def LitreHuile = new UniteDeMesure(idUnite : 'LitreHuile', valeur : 35.0).save()
                def Repas = new UniteDeMesure(idUnite : 'Repas', valeur : null).save()
                def Heure_gardinage = new UniteDeMesure(idUnite : 'Heure', valeur : 5d).save()
                def Nuit = new UniteDeMesure(idUnite : 'Nuit', valeur : null).save()
                def Dirham = new UniteDeMesure(idUnite : 'Dirham', valeur : 1d).save()
                
                // compteurs paterns
                new PaternCompteur(libelle : 'Pattern intervention', prefixe : '{type}{jour}{mois}{annee}', suffixe : '', pas : 1, remplissage : 3, numeroSuivant : 1, type : 'INTERVENTION').save();
                
                //Categorie frais circulation
                def bonGasoilCtgr = new CategorieFraisCirculation(libelle : 'Bon gasoil', uniteMesure : Litre).save()
                def bonHuileCtgr = new CategorieFraisCirculation(libelle : 'Bon huile', uniteMesure : LitreHuile).save()
                def restauration  = new CategorieFraisCirculation(libelle : 'Restauration', uniteMesure : Repas).save()
                def gardinage     = new CategorieFraisCirculation(libelle : 'Gardinage', uniteMesure : Heure_gardinage).save()
                def hotel         = new CategorieFraisCirculation(libelle : 'Hotel', uniteMesure : Nuit).save()
                def ProcesCirculation = new CategorieFraisCirculation(libelle : 'Procès', uniteMesure : Dirham).save()
                         
                // Types des entretiens Normaux non périodiques
                def maintenance = new TypeEntretien(libelle : 'Maintenance provisoire').save()
                def reparation = new TypeEntretien(libelle : 'Réparation').save()
                def lavage = new TypeEntretien(libelle : 'Lavage provisoire').save()
                def pneumatique = new TypeEntretien(libelle : 'Pneumatique').save()
                def batterie = new TypeEntretien(libelle : 'Changement de batterie').save()
                def huile = new TypeEntretien(libelle : 'Changement de huiles').save()
                def peinture = new TypeEntretien(libelle : 'Peinture').save()
                def graissage = new TypeEntretien(libelle : 'Graissage').save()

                // Types des Entretiens Périodiques
                def vidange = new TypeEntretienPeriodique(libelle:'Vidange camion', periodicite:10000, chargeDeBase:300d, categorieVehicules : camion).save()
                def filtration = new TypeEntretienPeriodique(libelle:'Filtration camion', periodicite:20000, chargeDeBase:200d, categorieVehicules : camion).save()
                def tachygraphe = new TypeEntretienPeriodique(libelle:'Changement de tachygraphe', periodicite:20000, chargeDeBase:200d, categorieVehicules : camion).save()
               
                // Type de Frais de circulation périodique
                def assuranceCamion = new TypeFraisCirculationPeriodique(libelle:'Assurance camion', periodicite:365, chargeDeBase:4000, categorieVehicules : camion).save()
                def vignette = new TypeFraisCirculationPeriodique(libelle : 'Vignette camion', periodicite:365, chargeDeBase:1500, categorieVehicules : camion).save()
                def visite_technique = new TypeFraisCirculationPeriodique(libelle : 'Visite technique camion', periodicite:365, chargeDeBase:400, categorieVehicules : camion).save()
                def taxeAEssieux = new TypeFraisCirculationPeriodique(libelle : "Taxe à l'essieux", periodicite:365, chargeDeBase:400, categorieVehicules : camion).save()
                
//                */     
            }
            production {
        
            }
        }
                
    }
    def destroy = {
    }
}
