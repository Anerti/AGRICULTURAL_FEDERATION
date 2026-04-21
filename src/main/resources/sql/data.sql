
INSERT INTO agricultural_federation_app.cooperative_federation (id) VALUES
    ('a1b2c3d4-0001-0001-0001-000000000001');

INSERT INTO agricultural_federation_app.cooperative (id, name, specialty, creation_date, location, coop_fed_id, status, federation_auth) VALUES
    ('b2c3d4e5-0001-0001-0001-000000000001', 'Coopérative du Vallée', 'CEREALS', '2015-03-15', 'Lyon', 'a1b2c3d4-0001-0001-0001-000000000001', 'active', TRUE),
    ('b2c3d4e5-0002-0002-0002-000000000002', 'Les Producteurs du Sud', 'VEGETABLES', '2018-07-22', 'Marseille', 'a1b2c3d4-0001-0001-0001-000000000001', 'active', TRUE),
    ('b2c3d4e5-0003-0003-0003-000000000003', 'Fruits et Traditions', 'FRUITS', '2020-01-10', 'Avignon', 'a1b2c3d4-0001-0001-0001-000000000001', 'active', FALSE),
    ('b2c3d4e5-0004-0004-0004-000000000004', 'Bio Équilibre', 'ORGANIC', '2019-09-05', 'Bordeaux', 'a1b2c3d4-0001-0001-0001-000000000001', 'active', TRUE);

INSERT INTO agricultural_federation_app.member (id, first_name, last_name, birth_date, gender, address, phone, profession, email, coop_id, status) VALUES
    ('c3d4e5f6-0001-0001-0001-000000000001', 'Jean', 'Dupont', '1975-04-12', 'M', '12 Rue de la Paix, Lyon', '0612345678', 'Agriculteur', 'jean.dupont@email.com', 'b2c3d4e5-0001-0001-0001-000000000001', 'active'),
    ('c3d4e5f6-0002-0002-0002-000000000002', 'Marie', 'Martin', '1982-08-23', 'F', '25 Avenue des Champs, Lyon', '0623456789', 'Productrice', 'marie.martin@email.com', 'b2c3d4e5-0001-0001-0001-000000000001', 'active'),
    ('c3d4e5f6-0003-0003-0003-000000000003', 'Pierre', 'Bernard', '1968-11-30', 'M', '8 Boulevard Saint-Michel, Marseille', '0634567890', 'Cultivateur', 'pierre.bernard@email.com', 'b2c3d4e5-0002-0002-0002-000000000002', 'active'),
    ('c3d4e5f6-0004-0004-0004-000000000004', 'Sophie', 'Lefebvre', '1990-02-14', 'F', '42 Rue du Port, Marseille', '0645678901', 'Maraîchère', 'sophie.lefebvre@email.com', 'b2c3d4e5-0002-0002-0002-000000000002', 'active'),
    ('c3d4e5f6-0005-0005-0005-000000000005', 'Lucas', 'Moreau', '1978-06-25', 'M', '15 Place du Temple, Avignon', '0656789012', 'Arboriculteur', 'lucas.moreau@email.com', 'b2c3d4e5-0003-0003-0003-000000000003', 'active'),
    ('c3d4e5f6-0006-0006-0006-000000000006', 'Emma', 'Petit', '1985-12-08', 'F', '33 Rue des Arènes, Avignon', '0667890123', 'Productrice bio', 'emma.petit@email.com', 'b2c3d4e5-0003-0003-0003-000000000003', 'inactive'),
    ('c3d4e5f6-0007-0007-0007-000000000007', 'Nicolas', 'Roux', '1992-03-19', 'M', '7 Avenue de la Gare, Bordeaux', '0678901234', 'Viticulteur', 'nicolas.roux@email.com', 'b2c3d4e5-0004-0004-0004-000000000004', 'active'),
    ('c3d4e5f6-0008-0008-0008-000000000008', 'Camille', 'Girard', '1988-09-27', 'F', '19 Rue du Commerce, Bordeaux', '0689012345', 'Agricultrice bio', 'camille.girard@email.com', 'b2c3d4e5-0004-0004-0004-000000000004', 'active');

INSERT INTO agricultural_federation_app.referrer (id, referrer_at, new_member, referrer_id) VALUES
    ('d4e5f6a7-0001-0001-0001-000000000001', '2015-04-01 10:00:00', 'c3d4e5f6-0002-0002-0002-000000000002', 'c3d4e5f6-0001-0001-0001-000000000001'),
    ('d4e5f6a7-0002-0002-0002-000000000002', '2018-08-15 14:30:00', 'c3d4e5f6-0003-0003-0003-000000000003', 'c3d4e5f6-0002-0002-0002-000000000002'),
    ('d4e5f6a7-0003-0003-0003-000000000003', '2018-08-20 09:15:00', 'c3d4e5f6-0004-0004-0004-000000000004', 'c3d4e5f6-0003-0003-0003-000000000003'),
    ('d4e5f6a7-0004-0004-0004-000000000004', '2019-06-10 11:00:00', 'c3d4e5f6-0005-0005-0005-000000000005', 'c3d4e5f6-0001-0001-0001-000000000001'),
    ('d4e5f6a7-0005-0005-0005-000000000005', '2019-06-15 14:30:00', 'c3d4e5f6-0006-0006-0006-000000000006', 'c3d4e5f6-0005-0005-0005-000000000005'),
    ('d4e5f6a7-0006-0006-0006-000000000006', '2020-01-05 10:30:00', 'c3d4e5f6-0007-0007-0007-000000000007', 'c3d4e5f6-0006-0006-0006-000000000006'),
    ('d4e5f6a7-0007-0007-0007-000000000007', '2020-02-20 16:00:00', 'c3d4e5f6-0008-0008-0008-000000000008', 'c3d4e5f6-0007-0007-0007-000000000007');
