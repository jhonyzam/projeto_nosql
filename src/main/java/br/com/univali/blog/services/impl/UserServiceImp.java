package br.com.univali.blog.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.univali.blog.models.User;
import br.com.univali.blog.repositories.UserRepository;
import br.com.univali.blog.services.UserService;

@Service
public class UserServiceImp implements UserService{
	
	@Autowired
	private UserRepository repository;

	public UserRepository getRepository() {
		return repository;
	}

	public void setRepository(UserRepository repository) {
		this.repository = repository;
	}
	
	public User findByUsername(String username){
		return repository.findByUsername(username);
	}

	@Override
	public void save(User user) {
		repository.save(user);
	}

	public void deleteAll() {
		repository.deleteAll();
	}
	
	
}
