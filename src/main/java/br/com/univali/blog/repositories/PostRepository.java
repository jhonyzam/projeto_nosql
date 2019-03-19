package br.com.univali.blog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.User;

public interface PostRepository extends MongoRepository<Post, String> {

	Page<Post> findAllByOrderByCreateDateDesc(Pageable pageable);

	Page<Post> findByUserOrderByCreateDateDesc(User user, Pageable pageable);

}