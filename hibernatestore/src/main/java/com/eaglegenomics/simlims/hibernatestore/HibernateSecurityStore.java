package com.eaglegenomics.simlims.hibernatestore;

import java.io.IOException;
import java.util.Collection;

import com.eaglegenomics.simlims.core.SecurityProfile;
import com.eaglegenomics.simlims.core.UserImpl;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.eaglegenomics.simlims.core.Group;
import com.eaglegenomics.simlims.core.User;
import com.eaglegenomics.simlims.core.store.SecurityStore;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p/>
 * Uses Hibernate table to store User and Group entities.
 * N.B. THIS SOLUTION USES PLAINTEXT PASSWORDS! CONSIDER SETTING UP
 * AN OpenLDAP SERVER TO MANAGE SHA CRYPT PASSWORDS AND AUTHORITIES
 * BY USING THE LDAPSECURITYSTORE CLASS
 *  
 * @author Richard Holland
 * @since 0.0.1
 *
 * @author Rob Davey
 * @since 0.0.2
 */
public class HibernateSecurityStore extends HibernateDaoSupport implements SecurityStore {

  @Transactional(readOnly = false)
  public long saveUser(User user) throws IOException {
    if (SecurityContextHolder.getContext().getAuthentication() != null &&
        SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

      //if we're sure this user is the same as the one that needs to be updated, we'll need to evict the current session user object
      getHibernateTemplate().evict(user);
    }

    getHibernateTemplate().saveOrUpdate(user);
    return -1;
  }

  @Transactional(readOnly = true)
  public User getUserById(Long userId) throws IOException {
    return (User) getHibernateTemplate().load(UserImpl.class, userId);
  }

  @Transactional(readOnly = true)
  public User getUserByLoginName(final String loginName) throws IOException {
    return (User) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session)
              throws HibernateException {
        Query query = session
                .createQuery("from User as user where user.loginName = :name");
        query.setParameter("name", loginName);
        return (User) query.uniqueResult();
      }
    });
  }

  @Transactional(readOnly = true)
  public User getUserByEmail(final String email) throws IOException {
    return (User) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session)
              throws HibernateException {
        Query query = session
                .createQuery("from User as user where user.email = :email");
        query.setParameter("email", email);
        return (User) query.uniqueResult();
      }
    });
  }

  @Transactional(readOnly = false)
  public long saveGroup(Group group) throws IOException {
    getHibernateTemplate().saveOrUpdate(group);
    return -1;
  }

  @Transactional(readOnly = true)
  public Group getGroupById(Long groupId) throws IOException {
    return (Group) getHibernateTemplate().load(Group.class, groupId);
  }

  @Transactional(readOnly = true)
  public Group getGroupByName(final String name) throws IOException {
    return (Group) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session)
              throws HibernateException {
        Query query = session
                .createQuery("from _Group as group where group.name = :name");
        query.setParameter("name", name);
        return (Group) query.uniqueResult();
      }
    });
  }

  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public Collection<Group> listAllGroups() throws IOException {
    return (Collection<Group>) getHibernateTemplate().execute(
            new HibernateCallback() {
              public Object doInHibernate(Session session)
                      throws HibernateException {
                return session.createQuery("from _Group").list();
              }
            });
  }

  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public Collection<Group> listGroupsByIds(final Collection<Long> groupIds) throws IOException {
    return (Collection<Group>) getHibernateTemplate().execute(
            new HibernateCallback() {
              public Object doInHibernate(Session session)
                      throws HibernateException {
                Query query = session.createQuery("from _Group as group where group.groupId in (:ids)");
                query.setParameterList("ids", groupIds);
                return query.list();
              }
            });
  }

  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public Collection<User> listAllUsers() throws IOException {
    return (Collection<User>) getHibernateTemplate().execute(
            new HibernateCallback() {
              public Object doInHibernate(Session session)
                      throws HibernateException {
                return session.createQuery("from User").list();
              }
            });
  }

  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public Collection<User> listUsersByIds(final Collection<Long> userIds) throws IOException {
    return (Collection<User>) getHibernateTemplate().execute(
            new HibernateCallback() {
              public Object doInHibernate(Session session)
                      throws HibernateException {
                Query query = session.createQuery("from User as user where user.userId in (:ids)");
                query.setParameterList("ids", userIds);
                return query.list();
              }
            });
  }

  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public Collection<User> listUsersByGroupName(final String name) throws IOException {
    return (Collection<User>) getHibernateTemplate().execute(
            new HibernateCallback() {
              public Object doInHibernate(Session session)
                      throws HibernateException {
                Query query = session.createQuery("from _Group as group where group.name = :name");
                query.setParameter("name", name);
                return query.list();
              }
            });
  }

  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public SecurityProfile getSecurityProfileById(Long profileId) throws IOException {
    return (SecurityProfile) getHibernateTemplate().load(SecurityProfile.class, profileId);
  }
}
