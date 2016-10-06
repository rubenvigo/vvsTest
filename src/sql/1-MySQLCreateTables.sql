-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

DROP TABLE ApuestaRealizada;
DROP TABLE UserProfile;
DROP TABLE OpcionApuesta;
DROP TABLE TipoApuesta;
DROP TABLE Evento;
DROP TABLE Categoria;

-- ------------------------------ UserProfile ----------------------------------

CREATE TABLE UserProfile (
    usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    enPassword VARCHAR(13) NOT NULL, 
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL, email VARCHAR(60) NOT NULL,
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
    CONSTRAINT LoginNameUniqueKey UNIQUE (loginName)) 
    ENGINE = InnoDB;

CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);

------------------------------------ CATEGORIA -----------------------------------

CREATE TABLE Categoria(idCategoria BIGINT NOT NULL AUTO_INCREMENT,
			nombre VARCHAR(40) COLLATE latin1_bin NOT NULL,
		CONSTRAINT Categoria_PK PRIMARY KEY (idCategoria))
		ENGINE = InnoDB;

------------------------------------ EVENTO --------------------------------------

CREATE TABLE Evento(idEvento BIGINT NOT NULL AUTO_INCREMENT,
			nombre VARCHAR(200) COLLATE latin1_bin NOT NULL,
			fecha TIMESTAMP DEFAULT 0 NOT NULL,
			idCategoria BIGINT NOT NULL,
		CONSTRAINT Evento_PK PRIMARY KEY (idEvento),
		CONSTRAINT CategoriaFK FOREIGN KEY(idCategoria)
        		REFERENCES Categoria(idCategoria) ON DELETE CASCADE)
		ENGINE = InnoDB;	

--------------------------------- TIPO APUESTA ------------------------------------

CREATE TABLE TipoApuesta(idTipoApuesta BIGINT NOT NULL AUTO_INCREMENT,
			pregunta VARCHAR(1024) COLLATE latin1_bin NOT NULL,
			idEvento BIGINT NOT NULL,
			multiplesGanadoras BOOLEAN NOT NULL,
		CONSTRAINT TipoApuesta_PK PRIMARY KEY(idTipoApuesta),
		CONSTRAINT EventoFK FOREIGN KEY(idEvento)
        		REFERENCES Evento(idEvento) ON DELETE CASCADE)
		ENGINE = InnoDB;

------------------------------- OPCION APUESTA ------------------------------------

CREATE TABLE OpcionApuesta(idOpcionApuesta BIGINT NOT NULL AUTO_INCREMENT,
			respuesta VARCHAR(1024) COLLATE latin1_bin NOT NULL,
			cuota FLOAT NOT NULL,
			estado BOOLEAN DEFAULT NULL,
			idTipoApuesta BIGINT NOT NULL,
		CONSTRAINT OpcionApuesta_PK PRIMARY KEY (idOpcionApuesta),
		CONSTRAINT TipoApuestaFK FOREIGN KEY(idTipoApuesta)
        		REFERENCES TipoApuesta(idTipoApuesta) ON DELETE CASCADE)
		ENGINE = InnoDB;

------------------------------- APUESTA REALIZADA --------------------------------

CREATE TABLE ApuestaRealizada(idApuestaRealizada BIGINT NOT NULL AUTO_INCREMENT,
			idUsuario BIGINT NOT NULL,
			idOpcionApuesta BIGINT NOT NULL,
			cantidadApostada FLOAT NOT NULL,
			fecha TIMESTAMP DEFAULT 0 NOT NULL,
		CONSTRAINT ApuestaRealizada_PK PRIMARY KEY (idApuestaRealizada),
		CONSTRAINT OpcionApuestaFK FOREIGN KEY(idOpcionApuesta)
        		REFERENCES OpcionApuesta(idOpcionApuesta) ON DELETE CASCADE,
		CONSTRAINT UserProfileFK FOREIGN KEY(idUsuario)
        		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
		ENGINE = InnoDB;

