package br.com.univali.blog.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.univali.blog.models.User;

public interface UserRepository extends MongoRepository<User, String>{
	public User findByUsernameAndPassword(String username,String password);
	public User findByUsername(String username);	
	public User findByEmail(String email);
}
