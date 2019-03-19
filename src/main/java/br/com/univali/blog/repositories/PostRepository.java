package br.com.univali.blog.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.univali.blog.models.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}