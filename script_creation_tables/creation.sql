CREATE TABLE Box (
    id_box INT PRIMARY KEY AUTO_INCREMENT,
    type_box VARCHAR(50) NOT NULL,
    capacite_max INT
);

CREATE TABLE Famille (
    id_famille INT PRIMARY KEY AUTO_INCREMENT,
    type_famille VARCHAR(50),
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(255),
    contact VARCHAR(100)
);

CREATE TABLE Creneau (
    id_creneau INT PRIMARY KEY AUTO_INCREMENT,
    nb_benevole INT,
    heure_d TIME NOT NULL, 
    heure_f TIME NOT NULL  
);

CREATE TABLE Activite (
    id_activite INT PRIMARY KEY AUTO_INCREMENT,
    type_act VARCHAR(100) NOT NULL
);

CREATE TABLE Personnel (
    id_pers INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    type_pers VARCHAR(50),
    tel VARCHAR(20),
    user VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Incident (
    id_incident INT PRIMARY KEY AUTO_INCREMENT,
    type_incident VARCHAR(100),
    intitule VARCHAR(255) NOT NULL,
    commentaire TEXT,
    date_incident DATETIME NOT NULL
);











CREATE TABLE Animal (
    id_animal INT PRIMARY KEY AUTO_INCREMENT,
    puce VARCHAR(50) UNIQUE,
    espece VARCHAR(50) NOT NULL,
    nom VARCHAR(100),
    date_naissance DATE,
    statut VARCHAR(50),
    tests_humain BOOLEAN,
    tests_bebe BOOLEAN,
    tests_chien BOOLEAN,
    tests_chat BOOLEAN,
    date_arrivee DATE NOT NULL
);


CREATE TABLE Soin (
    id_soin INT PRIMARY KEY AUTO_INCREMENT,
    id_animal INT NOT NULL, 
    type_soin VARCHAR(100) NOT NULL,
    libelle VARCHAR(100),
    commentaire TEXT,
    date_soin DATETIME NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES Animal(id_animal)
);


CREATE TABLE Affectation_Creneau_Activite (
    id_creneau INT NOT NULL,
    id_personne INT NOT NULL,
    id_activite INT NOT NULL,
    PRIMARY KEY (id_creneau, id_personne),
    FOREIGN KEY (id_creneau) REFERENCES Creneau(id_creneau),
    FOREIGN KEY (id_personne) REFERENCES Personnel(id_pers),
    FOREIGN KEY (id_activite) REFERENCES Activite(id_activite)
);

CREATE TABLE Sejour_Famille (
    id_animal INT NOT NULL,
    id_famille INT NOT NULL,
    DATE_D DATE NOT NULL,
    DATE_F_FAMILLE DATE,
    PRIMARY KEY (id_animal, DATE_D), -- Clé composite pour identifier le séjour
    FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
    FOREIGN KEY (id_famille) REFERENCES Famille(id_famille)
);









CREATE TABLE Sejour_Box (
    id_animal INT NOT NULL,
    id_box INT NOT NULL,
    DATE_D DATE NOT NULL,
    DATE_F_BOX DATE,
    PRIMARY KEY (id_animal, DATE_D), -- Clé composite pour identifier le séjour
    FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
    FOREIGN KEY (id_box) REFERENCES Box(id_box)
);


CREATE TABLE Animal_Incident (
    id_animal INT NOT NULL,
    id_incident INT NOT NULL,
    PRIMARY KEY (id_animal, id_incident),
    FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
    FOREIGN KEY (id_incident) REFERENCES Incident(id_incident)
);


CREATE TABLE Veterinaire(
    id_personne INT NOT NULL,
    id_soin INT NOT NULL,
    PRIMARY KEY (id_personne, id_soin),
    FOREIGN KEY (id_personne) REFERENCES Personnel(id_pers),
    FOREIGN KEY (id_soin) REFERENCES Soin(id_soin)
);

CREATE TABLE Planning_Animal (
    id_animal INT NOT NULL,
    id_creneau INT NOT NULL,
    id_pers INT NOT NULL,
    DATE_D DATE NOT NULL,
    PRIMARY KEY (id_animal, id_creneau, id_pers, DATE_D),
    FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
    FOREIGN KEY (id_creneau) REFERENCES Creneau(id_creneau),
    FOREIGN KEY (id_pers) REFERENCES Personnel(id_pers)
);

