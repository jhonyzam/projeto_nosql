package br.com.univali.blog.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.univali.blog.converters.PostFormToPost;
import br.com.univali.blog.forms.PostForm;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.User;
import br.com.univali.blog.repositories.PostRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostFormToPost postFormToPost;

	public List<Post> listAll() {
		List<Post> posts = new ArrayList<>();
		postRepository.findAll().forEach(posts::add); // fun with Java 8
		return posts;
	}

	public Post getById(String id) {
		Post post = postRepository.findById(id).orElse(null);
		
//		List<Secao> secoes = post.getSecoes().stream()
//				  .sorted(Comparator.comparing(Secao::getParagrafo))
//				  .collect(Collectors.toList());
//		
//		post.setSecoes(secoes);
		return post;
	}

	public Post save(Post post) {
		postRepository.save(post);
		return post;
	}

	public void delete(String id) {
		postRepository.deleteById(id);
	}

	public Post savePost(PostForm postForm) {
		Post savedPost = save(postFormToPost.convert(postForm));

		System.out.println("Saved Post Id: " + savedPost.getId());
		return savedPost;
	}

	@SuppressWarnings("deprecation")
	public Page<Post> findAllByOrderByCreateDateDesc(int page) {
		return postRepository.findAllByOrderByCreateDateDesc(new PageRequest(subtractPageByOne(page), 5));
	}

	private int subtractPageByOne(int page) {
		return (page < 1) ? 0 : page - 1;
	}

	@SuppressWarnings("deprecation")
	public Page<Post> findByUserOrderByCreateDateDesc(User user, int page) {
		return postRepository.findByUserOrderByCreateDateDesc(user, new PageRequest(subtractPageByOne(page), 5));
	}

	@SuppressWarnings("deprecation")
	public Page<Post> findByBlogKeyOrderByCreateDateDesc(String blogKey, int page) {
		return postRepository.findByBlogKeyOrderByCreateDateDesc(blogKey, new PageRequest(subtractPageByOne(page), 5));
	}

}
