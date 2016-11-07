package com.eaglegenomics.simlims.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p/>
 * A system user.
 * <p/>
 *
 * @author Richard Holland
 * @since 0.0.1
 *
 * @author Rob Davey
 * @since 0.0.2
 */
@Entity
@Table(name = "`User`")
public class UserImpl implements User {
  protected static final Logger log = LoggerFactory.getLogger(UserImpl.class);

  private static final long serialVersionUID = 1L;

  /**
   * Use this ID to indicate that a user has not yet been saved, and therefore
   * does not yet have a unique ID.
   */
  public static final Long UNSAVED_ID = null;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long userId = UserImpl.UNSAVED_ID;
  private String fullName = "";
  private String loginName = "";
  private String email = "";
  private String password = "";
  private boolean internal = false;
  private boolean external = false;
  private boolean admin = false;
  private boolean active = true;
  @ManyToMany
  private Collection<Group> groups = new HashSet<Group>();
  private String[] roles = new String[0];

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public String getPassword() {
    return password;
  }

  public Collection<Group> getGroups() {
    return groups;
  }

  public String getLoginName() {
    return loginName;
  }

  public String[] getRoles() {
    return roles;
  }

  public Collection<GrantedAuthority> getRolesAsAuthorities() {
    List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
    for (String s : roles) {
      auths.add(new GrantedAuthorityImpl(s));
    }
    return auths;
  }

  public Collection<GrantedAuthority> getPermissionsAsAuthorities() {
    List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
    if (isAdmin()) {
      auths.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
    }

    if (isInternal()) {
      auths.add(new GrantedAuthorityImpl("ROLE_INTERNAL"));
    }

    if (isExternal()) {
      auths.add(new GrantedAuthorityImpl("ROLE_EXTERNAL"));
    }

    return auths;
  }

  public boolean isAdmin() {
    return admin;
  }

  public boolean isExternal() {
    return external;
  }

  public boolean isInternal() {
    return internal;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public void setExternal(boolean external) {
    this.external = external;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setPassword(String password) {
    //ALWAYS encrypt password
/*    if (password.startsWith("{SHA}")) {
      log.info("Password already encrypted");
      this.password = password;
    }
    else {
      log.info("Encrypting password");
      this.password = PasswordCodecService.getInstance().encrypt(password);
    }*/
    this.password = password;
  }

  public void setGroups(Collection<Group> groups) {
    this.groups = groups;
  }

  public void setInternal(boolean internal) {
    this.internal = internal;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  /**
   * Users are equated by login name.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if (!(obj instanceof User))
      return false;
    User them = (User) obj;
    return this.getLoginName().equals(them.getLoginName());
  }

  @Override
  public int hashCode() {
    return getLoginName().hashCode();
  }

  /**
   * Equivalent to getLoginName().
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getLoginName());
    sb.append(":").append(getFullName());
    sb.append(":").append(getEmail());
    sb.append(":").append(isActive());
    sb.append(":").append(isAdmin());
    sb.append(":").append(isInternal());
    sb.append(":").append(isExternal());
    return sb.toString();
  }

  public int compareTo(Object o) {
    return this.equals(o) ? 0 : 1;
  }
}
