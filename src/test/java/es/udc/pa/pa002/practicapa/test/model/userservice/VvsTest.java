package es.udc.pa.pa002.practicapa.test.model.userservice;

import static es.udc.pa.pa002.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa002.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizadaDao;
import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.categoria.CategoriaDao;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.evento.EventoDao;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa002.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import com.ibatis.common.jdbc.ScriptRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional

public class VvsTest {

	@Autowired
	private EventoDao eventoDao;
	
	@Autowired
	private CategoriaDao categoriaDao;
	
	@Autowired
	private UserProfileDao userProfileDao;
	
	@Autowired
	private ApuestaRealizadaDao apuestaRealizadaDao;
	

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Test
	public void saveEvento(){
    	Categoria categoria = new Categoria("Futbol");
    	getSession().save(categoria);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria);
    	eventoDao.save(evento);
    	assertNotNull(evento.getIdEvento());	
	}

	

	@Test
	public void updateEvento(){
    	Categoria categoria = new Categoria("Futbol");
    	getSession().save(categoria);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria);
    	getSession().save(evento);
    	evento.setNombre("nuevoNombre");
    	Evento saveEvento = evento;
    	eventoDao.save(evento);
    	assertEquals(evento,saveEvento);
	}

	
	@Test
	public void deleteEvento() throws InstanceNotFoundException{
    	Categoria categoria = new Categoria("Futbol");
    	getSession().save(categoria);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria);
    	getSession().save(evento);
    	eventoDao.remove(evento.getIdEvento());
    	
	}

	
	@Test(expected=InstanceNotFoundException.class)
	public void deleteEventoNotFound() throws InstanceNotFoundException{

    	eventoDao.remove(1L);
    	
	}
	
	@Test(expected=InstanceNotFoundException.class)
	public void findEventoByIdNotFound() throws InstanceNotFoundException{

		eventoDao.find(1L);
    	
	}
	@Test
	public void findEventoById() throws InstanceNotFoundException{
    	Categoria categoria = new Categoria("Futbol");
    	getSession().save(categoria);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria);
    	getSession().save(evento);
    	
    	Evento foundEvento = eventoDao.find(evento.getIdEvento());
    	
    	assertEquals(foundEvento,evento);
	}
	
	
    @Test
    public void testFindEventosByKeywordsAdmin(){
    	
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30); 
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("este es el nombre del evento", fecha, categoria1);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento3);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento3);
    	eventos.add(evento2);
    	List<Evento> foundEventos=eventoDao.findByParameters("eVeNT nomBRe", null, true, 0, 10);
    	    	
    	assertEquals(eventos, foundEventos);
    }
    
	
    @Test
    public void testFindEventosByKeywords(){
    	
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30); 
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("este es el nombre del evento", fecha, categoria1);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento3);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento2);
    	List<Evento> foundEventos=eventoDao.findByParameters("eVeNT nomBRe", null, false, 0, 10);
    	    	
    	assertEquals(eventos, foundEventos);
    }
    
    @Test
    public void testFindEventosByCategoryAdmin(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre", fecha2, categoria2);
    	getSession().save(evento3);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento3);
    	eventos.add(evento2);
    	List<Evento> foundEventos;
    	foundEventos = eventoDao.findByParameters(null, categoria2.getIdCategoria(),true, 0, 10);
    	assertEquals(eventos, foundEventos);
    }
    
    @Test
    public void testFindEventosByCategory(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre", fecha, categoria2);
    	getSession().save(evento3);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento2);
    	eventos.add(evento3);
    	List<Evento> foundEventos;
    	foundEventos = eventoDao.findByParameters(null, categoria2.getIdCategoria(),false, 0, 10);
    	assertEquals(eventos, foundEventos);
    }
	
    
    @Test
    public void testFindEventosByDateAdmin(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2015, Calendar.JUNE, 11, 10, 30);
    	Evento evento = new Evento("nombre1", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre2", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre3", fecha2, categoria2);
    	getSession().save(evento3);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento3);
    	eventos.add(evento);
    	eventos.add(evento2);
    	List<Evento>foundEventos;
    	foundEventos = eventoDao.findByParameters(null, null,true, 0, 10);
    	assertEquals(eventos, foundEventos);
    }
    
    @Test
    public void testFindEventosByDateUser(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2015, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre1", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre2", fecha2, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre3", fecha, categoria2);
    	getSession().save(evento3);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento);
    	eventos.add(evento3);
    	List<Evento> foundEventos;
    	foundEventos =eventoDao.findByParameters(null, null,false, 0, 10);
    	assertEquals(eventos, foundEventos);
    }
	
    @Test
    public void testFindEventosByKeywordsCategoryAdmin(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre del evento", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("evento", fecha2, categoria2);
    	getSession().save(evento3);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento2);
    	List<Evento> foundEventos;
    	foundEventos = eventoDao.findByParameters("eVENt NoMBRe", categoria2.getIdCategoria(),true, 0, 10);
    	assertEquals(eventos, foundEventos);
    }
    
    @Test
    public void testFindEventosByKeywordsCategoryUser(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("evento", fecha2, categoria2);
    	getSession().save(evento3);
    	Evento evento4 = new Evento("este es el nombre de un evento", fecha, categoria2);
    	getSession().save(evento4);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento4);
    	List<Evento> foundEventos;
    	foundEventos = eventoDao.findByParameters("eVENt NoMBRe", categoria2.getIdCategoria(),false, 0, 10);
    	assertEquals(eventos, foundEventos);
    }
    
    @Test
    public void testFindEventosByKeywordsCategoryUserPageStartIndex(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("evento", fecha2, categoria2);
    	getSession().save(evento3);
    	Evento evento4 = new Evento("este es el nombre de un evento", fecha, categoria2);
    	getSession().save(evento4);
    	Evento evento5 = new Evento("este es el nombre de un evento", fecha, categoria2);
    	getSession().save(evento5);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento5);
    	List<Evento> foundEventos;
    	foundEventos = eventoDao.findByParameters("eVENt NoMBRe", categoria2.getIdCategoria(),false, 1, 10);
    	assertEquals(eventos, foundEventos);
    }
    
    @Test
    public void testFindEventosByKeywordsCategoryUserPageCount(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("evento", fecha2, categoria2);
    	getSession().save(evento3);
    	Evento evento4 = new Evento("este es el nombre de un evento", fecha, categoria2);
    	getSession().save(evento4);
    	Evento evento5 = new Evento("este es el nombre de un evento", fecha, categoria2);
    	getSession().save(evento5);
    	List<Evento> eventos = new ArrayList<Evento>();
    	eventos.add(evento4);
    	List<Evento> foundEventos;
    	foundEventos = eventoDao.findByParameters("eVENt NoMBRe", categoria2.getIdCategoria(),false, 0, 1);
    	assertEquals(eventos, foundEventos);
    }
    
    
	@Test
	public void existsEvento(){
    	Categoria categoria = new Categoria("Futbol");
    	getSession().save(categoria);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria);
    	getSession().save(evento);
    	assertTrue(eventoDao.existsEvent("nombre", categoria.getIdCategoria(), fecha));
	}
	
	@Test
	public void NotExistsCategoryEvento(){
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Categoria categoria2 = new Categoria("Tenis");
		getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Evento evento = new Evento("nombre", fecha, categoria);
        getSession().save(evento);
    	assertFalse(eventoDao.existsEvent("nombre", categoria2.getIdCategoria(), fecha));
	}
		
	@Test
	public void NotExistsDateEvento(){
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria);
    	getSession().save(evento);
    	Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2017, Calendar.AUGUST, 12, 10, 30);
    	assertFalse(eventoDao.existsEvent("nombre", categoria.getIdCategoria(), fecha2));
	}
	
	@Test
	public void NotExistsNameEvento(){
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria);
    	getSession().save(evento);
    	assertFalse(eventoDao.existsEvent("OtroNombre", categoria.getIdCategoria(), fecha));
	}
	
	
    @Test
    public void testGetNumberOfEventosByKeywordsAdmin(){
    	
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30); 
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("este es el nombre del evento", fecha, categoria1);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento3);
    	int foundEventos=eventoDao.getNumberOfEventos("eVeNT nomBRe", null, true);
    	    	
    	assertEquals(2, foundEventos);
    }
    
	
    @Test
    public void testGetNumberOfEventosByKeywords(){
    	
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30); 
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("este es el nombre del evento", fecha, categoria1);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento3);
    	int foundEventos=eventoDao.getNumberOfEventos("eVeNT nomBRe", null, false);
    	    	
    	assertEquals(1, foundEventos);
    }
    
    @Test
    public void testGetNumberOfEventosByCategoryAdmin(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre", fecha2, categoria2);
    	getSession().save(evento3);
    	int foundEventos;
    	foundEventos = eventoDao.getNumberOfEventos(null, categoria2.getIdCategoria(),true);
    	assertEquals(2, foundEventos);
    }
    
    @Test
    public void testGetNumberOfEventosByCategory(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre", fecha, categoria2);
    	getSession().save(evento3);
    	int foundEventos;
    	foundEventos = eventoDao.getNumberOfEventos(null, categoria2.getIdCategoria(),false);
    	assertEquals(2, foundEventos);
    }
	
    
    @Test
    public void testGetNumberOfEventosByDateAdmin(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2015, Calendar.JUNE, 11, 10, 30);
    	Evento evento = new Evento("nombre1", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre2", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre3", fecha2, categoria2);
    	getSession().save(evento3);
    	int foundEventos;
    	foundEventos = eventoDao.getNumberOfEventos(null, null,true);
    	assertEquals(3, foundEventos);
    }
    
    @Test
    public void testGetNumberOfEventosByDateUser(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2015, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre1", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre2", fecha2, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("nombre3", fecha, categoria2);
    	getSession().save(evento3);
    	int foundEventos;
    	foundEventos =eventoDao.getNumberOfEventos(null, null,false);
    	assertEquals(2, foundEventos);
    }
	
    @Test
    public void testGetNumberOfEventosByKeywordsCategoryAdmin(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre del evento", fecha, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("evento", fecha2, categoria2);
    	getSession().save(evento3);
    	int foundEventos;
    	foundEventos = eventoDao.getNumberOfEventos("eVENt NoMBRe", categoria2.getIdCategoria(),true);
    	assertEquals(1, foundEventos);
    }
    
    @Test
    public void testGetNumberOfEventosByKeywordsCategoryUser(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Calendar fecha = Calendar.getInstance();
        fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
    	Evento evento = new Evento("nombre evento", fecha, categoria1);
    	getSession().save(evento);
    	Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
    	getSession().save(evento2);
    	Evento evento3 = new Evento("evento", fecha2, categoria2);
    	getSession().save(evento3);
    	Evento evento4 = new Evento("este es el nombre de un evento", fecha, categoria2);
    	getSession().save(evento4);
    	int foundEventos;
    	foundEventos = eventoDao.getNumberOfEventos("eVENt NoMBRe", categoria2.getIdCategoria(),false);
    	assertEquals(1, foundEventos);
    }
    
    @Test
    public void testFindAllCategories(){
    	Categoria categoria1 = new Categoria("Futbol");
    	getSession().save(categoria1);
    	Categoria categoria2 = new Categoria("Baloncesto");
    	getSession().save(categoria2);
    	Categoria categoria3 = new Categoria("Tenis");
    	getSession().save(categoria3);
    	
    	List<Categoria> categorias = new ArrayList<Categoria>();
    	categorias.add(categoria1);
    	categorias.add(categoria2);
    	categorias.add(categoria3);
    	
    	List<Categoria> foundCategorias = categoriaDao.findAll();
    	assertEquals(categorias, foundCategorias);
    }
	
    
	@Test(expected=InstanceNotFoundException.class)
	public void findUserProfileByLoginNotFound() throws InstanceNotFoundException{

		userProfileDao.findByLoginName("notExists");
    	
	}
	@Test
	public void findUserProfileByLogin() throws InstanceNotFoundException{
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);
		assertEquals(user,userProfileDao.findByLoginName("name"));
	}
	
	@Test
	public void findApuestasUsuario(){
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		Set<OpcionApuesta> opcionesApuesta=new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento,"pregunta",opcionesApuesta,false);
		tipoApuesta.setEvento(evento);
		getSession().save(tipoApuesta);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1",2,null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2",2,null);
		opcionApuesta2.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta2);
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);		
		ApuestaRealizada apuesta = new ApuestaRealizada(user,opcionApuesta,2,fecha);
		getSession().save(apuesta);	
		
		ApuestaRealizada apuesta2 = new ApuestaRealizada(user,opcionApuesta2,5,fecha);
		getSession().save(apuesta2);
		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta);
		apuestas.add(apuesta2);
		assertEquals(apuestaRealizadaDao.findByUser(user.getUserProfileId(), 0, 10),apuestas);

	}
	
	
	@Test
	public void findApuestasUsuarioPageStartIndex(){
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		Set<OpcionApuesta> opcionesApuesta=new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento,"pregunta",opcionesApuesta,false);
		tipoApuesta.setEvento(evento);
		getSession().save(tipoApuesta);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1",2,null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2",2,null);
		opcionApuesta2.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta2);
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);		
		ApuestaRealizada apuesta = new ApuestaRealizada(user,opcionApuesta,2,fecha);
		getSession().save(apuesta);	
		
		ApuestaRealizada apuesta2 = new ApuestaRealizada(user,opcionApuesta2,5,fecha);
		getSession().save(apuesta2);
		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta2);
		assertEquals(apuestaRealizadaDao.findByUser(user.getUserProfileId(), 1, 10),apuestas);

	}
	
	@Test
	public void findApuestasUsuarioPageCount(){
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		Set<OpcionApuesta> opcionesApuesta=new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento,"pregunta",opcionesApuesta,false);
		tipoApuesta.setEvento(evento);
		getSession().save(tipoApuesta);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1",2,null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2",2,null);
		opcionApuesta2.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta2);
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);		
		ApuestaRealizada apuesta = new ApuestaRealizada(user,opcionApuesta,2,fecha);
		getSession().save(apuesta);	
		
		ApuestaRealizada apuesta2 = new ApuestaRealizada(user,opcionApuesta2,5,fecha);
		getSession().save(apuesta2);
		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta);
		assertEquals(apuestaRealizadaDao.findByUser(user.getUserProfileId(), 0, 1),apuestas);

	}
	
	
	@Test
	public void findApuestasUsuarioNotFound(){

		assertTrue(apuestaRealizadaDao.findByUser(1L, 0, 1).isEmpty());

	}
	
	
	
}

