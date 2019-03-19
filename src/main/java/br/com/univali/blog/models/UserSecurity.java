package br.com.univali.blog.models;

import org.springframework.security.core.authority.AuthorityUtils;

public class UserSecurity extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 5864366698587329644L;
	private User user;

	public UserSecurity(User user) {
		super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getId() {
		return user.getId();
	}

	public String getRole() {
		return user.getRole();
	}
}
