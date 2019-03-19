package br.com.univali.blog.services;

import java.util.List;

import br.com.univali.blog.commands.PostForm;
import br.com.univali.blog.models.Post;

public interface PostService {

	List<Post> listAll();

	Post getById(String id);

	Post saveOrUpdate(Post post);

	void delete(String id);

	Post saveOrUpdatePostForm(PostForm postForm);
}
