package es.udc.pa.pa002.practicapa.test.model.userservice;

import static es.udc.pa.pa002.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa002.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizadaDao;
import es.udc.pa.pa002.practicapa.model.categoria.CategoriaDao;
import es.udc.pa.pa002.practicapa.model.evento.EventoDao;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa002.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class vvs_PU_UserProfileDaoTest {

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

	@Test(expected = InstanceNotFoundException.class)
	public void findUserProfileByLoginNotFound()
			throws InstanceNotFoundException {

		userProfileDao.findByLoginName("notExists");

	}

	@Test
	public void findUserProfileByLogin() throws InstanceNotFoundException {
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);
		assertEquals(user, userProfileDao.findByLoginName("name"));
	}

}