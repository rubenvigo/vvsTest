package es.udc.pa.pa002.practicapa.web.pages.user;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa002.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.pages.Index;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class UpdateProfile.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UpdateProfile {

/** The first name. */
@Property
private String firstName;

/** The last name. */
@Property
private String lastName;

/** The email. */
@Property
private String email;

/** The user session. */
@SessionState(create = false)
private UserSession userSession;

/** The user service. */
@Inject
private UserService userService;

/**
 * On prepare for render.
 *
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
final void onPrepareForRender() throws InstanceNotFoundException {

    UserProfile userProfile;

    userProfile = userService.findUserProfile(userSession.getUserProfileId());
    firstName = userProfile.getFirstName();
    lastName = userProfile.getLastName();
    email = userProfile.getEmail();

}

/**
 * On success.
 *
 * @return the object
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
final Object onSuccess() throws InstanceNotFoundException {

    userService.updateUserProfileDetails(userSession.getUserProfileId(),
            new UserProfileDetails(firstName, lastName, email));
    userSession.setFirstName(firstName);
    return Index.class;

}

}