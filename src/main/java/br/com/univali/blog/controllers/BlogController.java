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

import br.com.univali.blog.converters.BlogToBlogForm;
import br.com.univali.blog.forms.BlogForm;
import br.com.univali.blog.models.Blog;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.User;
import br.com.univali.blog.services.BlogService;
import br.com.univali.blog.services.PostService;
import br.com.univali.blog.services.UserService;
import br.com.univali.blog.services.ValidateService;
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

	@Autowired
	private ValidateService validateService;

	@Autowired
	BlogToBlogForm blogToBlogForm;

	@RequestMapping({"/blog/list", "/blog"})
	public String listPosts(@RequestParam(defaultValue = "0") int page, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userService.findByUsername(auth.getName());

		model.addAttribute("tipo", "blog");
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

	@RequestMapping("/blog/{blogKey}/edit/{id}")
	public String editPost(@PathVariable String id, @PathVariable String blogKey, Model model) {
		if (!validateService.validateUserPermissionBlog(blogKey)) {
			return "redirect:/blog/" + blogKey;
		}

		Blog blog = blogService.getById(id);
		BlogForm blogForm = blogToBlogForm.convert(blog);

		model.addAttribute("edit", true);
		model.addAttribute("blogForm", blogForm);
		return "blog/blogform";
	}

	@RequestMapping(value = "/blog/new", method = RequestMethod.POST)
	public String saveBlog(@Valid BlogForm blogForm, BindingResult bindingResult, Model model) {

		if (blogForm.getId().isEmpty()) {
			if (blogForm.getKey().isEmpty()) {
				bindingResult.rejectValue("key", "error.blog", "Campo não pode ser vazio");
			}
			if (blogService.getByKey(blogForm.getKey().toLowerCase()) != null) {
				bindingResult.rejectValue("key", "error.blog", "Chave de acesso já cadastrada");
			}
			if (!validateService.validateStringNoEspecialCaracter(blogForm.getKey())) {
				bindingResult.rejectValue("key", "error.blog", "Chave de acesso não pode conter caracter especial");
			}
		} else {
			Blog blog = blogService.getById(blogForm.getId());
			blogForm.setKey(blog.getKey());
			blogForm.setCreateDate(blog.getCreateDate());
		}

		if (blogForm.getTitle().isEmpty()) {
			bindingResult.rejectValue("title", "error.blog", "Campo não pode ser vazio");
		}

		if (!bindingResult.hasErrors()) {
			Blog savedPost = blogService.saveBlog(blogForm);
			return "redirect:/blog/" + savedPost.getKey();
		}

		return "blog/blogform";
	}

	@RequestMapping(value = "/blog/{blogKey}", method = RequestMethod.GET)
	public String blogForUsername(@PathVariable String blogKey, @RequestParam(defaultValue = "0") int page, Model model) {
		if (blogService.getByKey(blogKey) == null) {
			model.addAttribute("errorMessage", "Blog " + blogKey + " não existe");
			return "/post/list";
		}

		Page<Post> posts = postService.findByBlogKeyOrderByCreateDateDesc(blogKey, page);

		PagerPost pager = new PagerPost(posts);

		model.addAttribute("blogKey", blogKey);
		model.addAttribute("pager", pager);

		return "/post/list";
	}
}