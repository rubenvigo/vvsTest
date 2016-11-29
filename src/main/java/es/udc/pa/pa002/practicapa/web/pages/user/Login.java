package es.udc.pa.pa002.practicapa.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa002.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.pages.Index;
import es.udc.pa.pa002.practicapa.web.pages.useroperations.OpcionApuestaDetails;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.CookiesManager;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class Login.
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

/** The login name. */
@Property
private String loginName;

/** The password. */
@Property
private String password;

/** The remember my password. */
@Property
private boolean rememberMyPassword;

/** The user session. */
@SessionState(create = false)
private UserSession userSession;

/** The cookies. */
@Inject
private Cookies cookies;

/** The login form. */
@Component
private Form loginForm;

/** The messages. */
@Inject
private Messages messages;

/** The user service. */
@Inject
private UserService userService;

/** The user profile. */
private UserProfile userProfile = null;

/** The id opcion apuesta. */
@Persist
private Long idOpcionApuesta;

/** The opcion apuesta details. */
@InjectPage
private OpcionApuestaDetails opcionApuestaDetails;

/**
 * Sets the id opcion apuesta.
 *
 * @param idOpcionApuesta
 *            the new id opcion apuesta
 */
public void setIdOpcionApuesta(Long idOpcionApuesta) {
    this.idOpcionApuesta = idOpcionApuesta;
}

/**
 * Gets the id opcion apuesta.
 *
 * @return the id opcion apuesta
 */
public Long getIdOpcionApuesta() {
    return this.idOpcionApuesta;
}

/**
 * On validate from login form.
 */
void onValidateFromLoginForm() {

    if (!loginForm.isValid()) {
        return;
    }

    try {
        userProfile = userService.login(loginName, password, false);
    } catch (InstanceNotFoundException e) {
        loginForm.recordError(messages.get("error-authenticationFailed"));
    } catch (IncorrectPasswordException e) {
        loginForm.recordError(messages.get("error-authenticationFailed"));
    }

}

/**
 * On success.
 *
 * @return the object
 */
Object onSuccess() {

    userSession = new UserSession();
    userSession.setUserProfileId(userProfile.getUserProfileId());
    userSession.setFirstName(userProfile.getFirstName());
    userSession.setAdmin(loginName.equals("Admin"));

    if (rememberMyPassword) {
        CookiesManager.leaveCookies(cookies, loginName,
                userProfile.getEncryptedPassword());
    }
    if (idOpcionApuesta != null) {
        opcionApuestaDetails.setIdOpcionApuesta(idOpcionApuesta);
        idOpcionApuesta = null;
        return opcionApuestaDetails;
    }
    return Index.class;

}

}
