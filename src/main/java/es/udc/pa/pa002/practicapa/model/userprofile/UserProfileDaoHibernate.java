/*
 * 
 */
package es.udc.pa.pa002.practicapa.model.userprofile;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class UserProfileDaoHibernate.
 */
@Repository("userProfileDao")
public class UserProfileDaoHibernate extends
        GenericDaoHibernate<UserProfile, Long> implements UserProfileDao {

/**
 * Returns an UserProfile by login name (not user identifier).
 *
 * @param loginName
 *            the user identifier
 * @return the UserProfile
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public final UserProfile findByLoginName(final String loginName)
        throws InstanceNotFoundException {

    UserProfile userProfile = (UserProfile) getSession()
            .createQuery(
                    "SELECT u FROM UserProfile u WHERE u.loginName = :loginName")
            .setParameter("loginName", loginName).uniqueResult();
    if (userProfile == null) {
        throw new InstanceNotFoundException(loginName,
                UserProfile.class.getName());
    } else {
        return userProfile;
    }

}

}