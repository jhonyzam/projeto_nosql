package br.com.univali.blog.services;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Service;

import br.com.univali.blog.converters.BlogFormToBlog;
import br.com.univali.blog.forms.BlogForm;
import br.com.univali.blog.models.Blog;
import br.com.univali.blog.repositories.BlogRepository;

@Service
public class BlogService {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private BlogFormToBlog blogFormToBlog;

	public List<Blog> listAll() {
		List<Blog> blogs = new ArrayList<>();
		blogRepository.findAll().forEach(blogs::add); // fun with Java 8
		return blogs;
	}

	public Blog getById(String id) {
		return blogRepository.findById(id).orElse(null);
	}

	public Blog getByKey(String key) {
		return blogRepository.findByKey(key).orElse(null);
	}

	private Blog save(Blog blog) {
		blogRepository.save(blog);
		return blog;
	}

	public void delete(String id) {
		blogRepository.deleteById(id);
	}

	public Blog saveBlog(BlogForm blogForm) {
		Blog savedBlog = save(blogFormToBlog.convert(blogForm));

		System.out.println("Save blog Id: " + savedBlog.getId());
		return savedBlog;
	}

	@SuppressWarnings("deprecation")
	public Page<Blog> findAllByOrderByCreateDateDesc(int page) {
		return blogRepository.findAllByOrderByCreateDateDesc(new PageRequest(subtractPageByOne(page), 5));
	}

	@SuppressWarnings("deprecation")
	public Page<Blog> findAllByOrderByCreateDateDesc2(int page) {

		LookupOperation lookupOperation = LookupOperation.newLookup().from("post").localField("key").foreignField("blogKey").as("post");

		AggregationOperation group = Aggregation.group("_id", "key", "title", "createDate", "user").max("$post.createDate").as("dateLastPost");

		Aggregation aggregation = Aggregation.newAggregation(lookupOperation, group, sort(Sort.Direction.DESC, "dateLastPost"));

		List<Blog> blogs = mongoTemplate.aggregate(aggregation, "blog", Blog.class).getMappedResults();
				
		PageRequest pageRequest = new PageRequest(subtractPageByOne(page), 5);
		int start = (int) pageRequest.getOffset();
		int end = (start + pageRequest.getPageSize()) > blogs.size() ? blogs.size() : (start + pageRequest.getPageSize());        
		
		Page<Blog> pages = new PageImpl<Blog>(blogs.subList(start, end), pageRequest, blogs.size()); 
		
		return pages;
	}

	private int subtractPageByOne(int page) {
		return (page < 1) ? 0 : page - 1;
	}

	public String getTitleBlog(String blogKey) {
		Blog blog = blogRepository.findByKey(blogKey).orElse(null);
		return (blog != null) ? blog.getTitle() : "Lista de blogs";
	}

	public String getUrlBlog(String blogKey) {
		Blog blog = blogRepository.findByKey(blogKey).orElse(null);
		return (blog != null) ? "/blog/" + blog.getKey() : "/blog/list";
	}

	public String getIdBlog(String blogKey) {
		Blog blog = blogRepository.findByKey(blogKey).orElse(null);
		return (blog != null) ? "/blog/" + blog.getKey() + "/edit/" + blog.getId() : "/blog/list";
	}

}