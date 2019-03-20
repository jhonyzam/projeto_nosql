package br.com.univali.blog.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.univali.blog.forms.BlogForm;
import br.com.univali.blog.models.Blog;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.User;
import br.com.univali.blog.services.BlogService;
import br.com.univali.blog.services.PostService;
import br.com.univali.blog.services.UserService;
import br.com.univali.blog.util.PagerBlog;
import br.com.univali.blog.util.PagerPost;

@Controller
public class BlogController {

	@Autowired
	private UserService userService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private PostService postService;

	@RequestMapping({"/blog/list", "/blog"})
	public String listPosts(@RequestParam(defaultValue = "0") int page, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userService.findByUsername(auth.getName());

		model.addAttribute("user", user);
		model.addAttribute("blogs", blogService.listAll());

		Page<Blog> blogs = blogService.findAllByOrderByCreateDateDesc(page);
		PagerBlog pager = new PagerBlog(blogs);

		model.addAttribute("pager", pager);

		return "/blog/list";
	}

	@RequestMapping("/blog/new")
	public String newPost(Model model) {
		model.addAttribute("blogForm", new BlogForm());

		return "blog/blogform";
	}

	@RequestMapping(value = "/blog/new", method = RequestMethod.POST)
	public String saveBlog(@Valid BlogForm blogForm, BindingResult bindingResult, Model model) {

		if (blogForm.getKey().isEmpty()) {
			bindingResult.rejectValue("key", "error.blog", "Campo não pode ser vazio");
		}
		if (blogForm.getTitle().isEmpty()) {
			bindingResult.rejectValue("title", "error.blog", "Campo não pode ser vazio");
		}		
		if (blogService.getByKey(blogForm.getKey().toLowerCase()) != null  ) {
			bindingResult.rejectValue("key", "error.blog", "Chave de acesso já cadastrada");
		}

		if (!bindingResult.hasErrors()) {
			Blog savedPost = blogService.saveBlog(blogForm);
			return "redirect:/blog/" + savedPost.getKey();
		}

		return "blog/blogform";
	}

	@RequestMapping(value = "/blog/{blogKey}", method = RequestMethod.GET)
	public String blogForUsername(@PathVariable String blogKey, @RequestParam(defaultValue = "0") int page, Model model) {
		Page<Post> posts = postService.findByBlogKeyOrderByCreateDateDesc(blogKey, page);		
		
		PagerPost pager = new PagerPost(posts);

		model.addAttribute("blogKey", blogKey);
		model.addAttribute("pager", pager);

		return "/post/list";
	}

	// @RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	// public String blogForUsername(@PathVariable String username,
	// @RequestParam(defaultValue = "0") int page, Model model) {
	//
	// User user = userService.findByUsername(username);
	//
	// if (user != null) {
	// Page<Post> posts = postService.findByUserOrderByCreateDateDesc(user,
	// page);
	// Pager pager = new Pager(posts);
	//
	// model.addAttribute("pager", pager);
	// model.addAttribute("user", user);
	//
	// return "/post/list";
	//
	// } else {
	// return "/error";
	// }
	// }
}
