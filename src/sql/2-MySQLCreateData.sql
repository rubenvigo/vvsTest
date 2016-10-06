-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pojo" database.
-------------------------------------------------------------------------------
INSERT INTO Categoria (nombre) VALUES ('Futbol');
INSERT INTO Categoria (nombre) VALUES ('Baloncesto');
INSERT INTO Categoria (nombre) VALUES ('Tenis');
INSERT INTO Categoria (nombre) VALUES ('Atletismo');
INSERT INTO Categoria (nombre) VALUES ('Motor');


INSERT INTO UserProfile (loginName, enPassword, firstName, lastName, email) 
	VALUES ('Admin', 'UI5MX4pMxDJP2', 'Admin', 'Admin', 'admin@udc.es');
INSERT INTO UserProfile (loginName, enPassword, firstName, lastName, email) 
	VALUES ('user1', 'UG98OQgD/cI.A', 'User', 'User', 'user1@udc.es');
INSERT INTO UserProfile (loginName, enPassword, firstName, lastName, email) 
	VALUES ('user2', 'UG98OQgD/cI.A', 'User', 'User', 'user2@udc.es');


INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Madrid-Barcelona', '2016-06-12 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Deportivo-Celta', '2016-02-20 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Sevilla-Valencia', '2017-06-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Malaga-Levante', '2017-05-20 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Rayo-Valencia', '2017-06-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Ath. Bilbao-Betis', '2017-06-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Villareal-Espanyol', '2017-06-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Eibar-Betis', '2017-06-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Sporting Gijón-Getafe', '2017-06-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Granada-Sevilla', '2017-06-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Villareal-Espanyol', '2017-11-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Eibar-Betis', '2017-11-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Sporting Gijón-Getafe', '2017-11-13 20:00:00', 1);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Granada-Sevilla', '2017-11-13 20:00:00', 1);


INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Madrid-Barcelona', '2016-02-13 20:00:00', 2);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Obradoiro-Unicaja', '2017-06-13 20:00:00', 2);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Estudiantes-Valencia', '2016-04-13 20:00:00', 2);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Zaragoza-Joventut', '2016-06-13 20:00:00', 2);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Obradoiro-Unicaja', '2016-06-13 20:00:00', 2);
INSERT INTO Evento (nombre, fecha, idCategoria) VALUES ('Manresa-Sevilla', '2016-06-13 20:00:00', 2);

INSERT INTO TipoApuesta (pregunta, idEvento, multiplesGanadoras) VALUES ('Goles marcados', 1, false);
INSERT INTO TipoApuesta (pregunta, idEvento, multiplesGanadoras) VALUES ('Puntos', 15, false);
INSERT INTO TipoApuesta (pregunta, idEvento, multiplesGanadoras) VALUES ('Ganador', 15, false);
INSERT INTO TipoApuesta (pregunta, idEvento, multiplesGanadoras) VALUES ('Goleadores', 2, true);
INSERT INTO TipoApuesta (pregunta, idEvento, multiplesGanadoras) VALUES ('Resultado', 2, false);
INSERT INTO TipoApuesta (pregunta, idEvento, multiplesGanadoras) VALUES ('1x2', 2, false);
INSERT INTO TipoApuesta (pregunta, idEvento, multiplesGanadoras) VALUES ('Primero en marcar', 2, false);

INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('0', 3.6, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('1', 2, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('2', 2.1, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('3', 5.2, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('4', 21.3, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('5', 60.2, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('6', 115.2, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('7', 231, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('8', 354, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('9', 835, 1);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('10', 1000, 1);

INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('0-20', 103.2, 2);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('20-40', 85, 2);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('40-60', 3.3, 2);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('60-80', 2.5, 2);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('80-100', 2.6, 2);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('100-120', 7.1, 2);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('120+', 60.2, 2);

INSERT INTO OpcionApuesta (respuesta, cuota, estado, idTipoApuesta) VALUES ('Madrid', 1.2, FALSE, 3);
INSERT INTO OpcionApuesta (respuesta, cuota, estado, idTipoApuesta) VALUES ('Barcelona', 1.5, TRUE, 3);
INSERT INTO OpcionApuesta (respuesta, cuota, estado, idTipoApuesta) VALUES ('Empate', 2.1, FALSE, 3);

INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Lucas Pérez', 1.2, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Oriol Riera', 1.5, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Borja Bastón', 3.1, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Diego Seoane', 5.3, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Lassad', 10.2, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Alex Bergantiños', 1.5, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Nolito', 1.5, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Guidetti', 1.5, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Iago Aspas', 2.1, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Orellana', 3.2, 4);
INSERT INTO OpcionApuesta (respuesta, cuota, idTipoApuesta) VALUES ('Beauvue', 3.5, 4);

INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 20, 10, '2016-01-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 20, 30, '2016-01-13 20:10:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 19, 20, '2016-01-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 19, 20, '2016-01-13 20:10:25');

INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');
INSERT INTO ApuestaRealizada (idUsuario, idOpcionApuesta, cantidadApostada, fecha) VALUES (2, 5, 10, '2016-05-13 20:13:25');


