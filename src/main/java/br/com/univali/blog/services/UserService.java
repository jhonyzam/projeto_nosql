package br.com.univali.blog.services;

import br.com.univali.blog.models.User;

public interface UserService {
	public User findByUsername(String username);
	public void save(User user);
	public void deleteAll();
}
