ALTER TABLE intervention ADD COLUMN pourcentage_consommation double precision;
ALTER TABLE intervention ALTER COLUMN pourcentage_consommation SET STORAGE PLAIN;


ALTER TABLE vehicule ADD COLUMN consommation_gasoil_avide_min double precision;
ALTER TABLE vehicule ALTER COLUMN consommation_gasoil_avide_min SET STORAGE PLAIN;
##ALTER TABLE vehicule ALTER COLUMN consommation_gasoil_avide_min SET NOT NULL;

ALTER TABLE vehicule ADD COLUMN consommation_gasoil_avide_max double precision;
ALTER TABLE vehicule ALTER COLUMN consommation_gasoil_avide_max SET STORAGE PLAIN;
##ALTER TABLE vehicule ALTER COLUMN consommation_gasoil_avide_max SET NOT NULL;


ALTER TABLE intervention ADD COLUMN "etat_chargement" character varying(255);
ALTER TABLE intervention ALTER COLUMN "etat_chargement" SET STORAGE EXTENDED;


ALTER TABLE intervention ADD COLUMN "num_bon_gasoil" character varying(255);
ALTER TABLE intervention ALTER COLUMN "num_bon_gasoil" SET STORAGE EXTENDED;


-- Column: vehicule_id

-- ALTER TABLE bon_frais_circulation DROP COLUMN vehicule_id;

ALTER TABLE bon_frais_circulation ADD COLUMN vehicule_id bigint;
ALTER TABLE bon_frais_circulation ALTER COLUMN vehicule_id SET STORAGE PLAIN;


-- Column: fournisseur_id

-- ALTER TABLE paiementGroup DROP COLUMN fournisseur_id;

ALTER TABLE paiement_group ADD COLUMN fournisseur_id bigint;
ALTER TABLE paiement_group ALTER COLUMN fournisseur_id SET STORAGE PLAIN;


ALTER TABLE intervention ADD COLUMN consommation_gasoil_theorique_max double precision;
ALTER TABLE intervention ALTER COLUMN consommation_gasoil_theorique_max SET STORAGE PLAIN;

ALTER TABLE intervention ADD COLUMN surplus_consommation_gasoil double precision;
ALTER TABLE intervention ALTER COLUMN surplus_consommation_gasoil SET STORAGE PLAIN;


ALTER TABLE bon_frais_circulation ADD COLUMN charge_theorique double precision;
ALTER TABLE bon_frais_circulation ALTER COLUMN charge_theorique SET STORAGE PLAIN;


-- Column: date_depart_annee

-- ALTER TABLE intervention DROP COLUMN date_depart_annee;

ALTER TABLE intervention ADD COLUMN date_depart_annee integer;
ALTER TABLE intervention ALTER COLUMN date_depart_annee SET STORAGE PLAIN;

-- Column: date_depart_mois

-- ALTER TABLE intervention DROP COLUMN date_depart_mois;

ALTER TABLE intervention ADD COLUMN date_depart_mois integer;
ALTER TABLE intervention ALTER COLUMN date_depart_mois SET STORAGE PLAIN;
