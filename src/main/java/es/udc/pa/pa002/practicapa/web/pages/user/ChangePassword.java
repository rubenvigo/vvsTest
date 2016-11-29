package es.udc.pa.pa002.practicapa.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa002.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.pages.Index;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.CookiesManager;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class ChangePassword.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ChangePassword {

/** The old password. */
@Property
private String oldPassword;

/** The new password. */
@Property
private String newPassword;

/** The retype new password. */
@Property
private String retypeNewPassword;

/** The user session. */
@SessionState(create = false)
private UserSession userSession;

/** The change password form. */
@Component
private Form changePasswordForm;

/** The cookies. */
@Inject
private Cookies cookies;

/** The messages. */
@Inject
private Messages messages;

/** The user service. */
@Inject
private UserService userService;

/**
 * On validate from change password form.
 *
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
void onValidateFromChangePasswordForm() throws InstanceNotFoundException {

    if (!changePasswordForm.isValid()) {
        return;
    }

    if (!newPassword.equals(retypeNewPassword)) {
        changePasswordForm
                .recordError(messages.get("error-passwordsDontMatch"));
    } else {

        try {
            userService.changePassword(userSession.getUserProfileId(),
                    oldPassword, newPassword);
        } catch (IncorrectPasswordException e) {
            changePasswordForm.recordError(messages
                    .get("error-invalidPassword"));
        }

    }

}

/**
 * On success.
 *
 * @return the object
 */
Object onSuccess() {

    CookiesManager.removeCookies(cookies);
    return Index.class;

}

}
