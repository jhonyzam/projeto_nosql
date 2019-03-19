package br.com.univali.blog.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.univali.blog.commands.PostForm;
import br.com.univali.blog.converters.PostFormToPost;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.repositories.PostRepository;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	private PostFormToPost postFormToPost;

	@Autowired
	public PostServiceImpl(PostRepository postRepository, PostFormToPost postFormToPost) {
		this.postRepository = postRepository;
		this.postFormToPost = postFormToPost;
	}

	@Override
	public List<Post> listAll() {
		List<Post> posts = new ArrayList<>();
		postRepository.findAll().forEach(posts::add); // fun with Java 8
		return posts;
	}

	@Override
	public Post getById(String id) {
		return postRepository.findById(id).orElse(null);
	}

	@Override
	public Post saveOrUpdate(Post post) {
		postRepository.save(post);
		return post;
	}

	@Override
	public void delete(String id) {
		postRepository.deleteById(id);
	}

	@Override
	public Post saveOrUpdatePostForm(PostForm postForm) {
		Post savedPost = saveOrUpdate(postFormToPost.convert(postForm));

		System.out.println("Saved Post Id: " + savedPost.getId());
		return savedPost;
	}
}
