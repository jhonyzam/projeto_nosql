package br.com.univali.blog.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.univali.blog.models.Blog;

public interface BlogRepository extends MongoRepository<Blog, String> {

	Page<Blog> findAllByOrderByCreateDateDesc(Pageable pageable);

	Optional<Blog> findByKey(String key);

}