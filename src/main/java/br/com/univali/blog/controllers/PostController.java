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

import br.com.univali.blog.converters.PostToPostForm;
import br.com.univali.blog.forms.PostForm;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.User;
import br.com.univali.blog.services.PostService;
import br.com.univali.blog.services.UserService;
import br.com.univali.blog.util.Pager;

@Controller
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private PostToPostForm postToPostForm;

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	public String redirToList() {
		return "redirect:/post/list";
	}

	@RequestMapping({"/post/list", "/post"})
	public String listPosts(@RequestParam(defaultValue = "0") int page, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userService.findByUsername(auth.getName());

		model.addAttribute("user", user);
		model.addAttribute("posts", postService.listAll());

		Page<Post> posts = postService.findAllByOrderByCreateDateDesc(page);
		Pager pager = new Pager(posts);

		model.addAttribute("pager", pager);

		return "/post/list";
	}

	@RequestMapping("/post/show/{id}")
	public String getPost(@PathVariable String id, Model model) {
		model.addAttribute("post", postService.getById(id));
		return "post/show";
	}

	@RequestMapping("post/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		Post post = postService.getById(id);
		PostForm postForm = postToPostForm.convert(post);

		model.addAttribute("postForm", postForm);
		return "post/postform";
	}

	@RequestMapping("/post/new")
	public String newPost(Model model) {
		model.addAttribute("postForm", new PostForm());

		return "post/postform";
	}

	@RequestMapping(value = "/post/new", method = RequestMethod.POST)
	public String saveOrUpdatePost(@Valid PostForm postForm, BindingResult bindingResult) {

		if (postForm.getTitle().isEmpty()) {
			bindingResult.rejectValue("title", "error.post", "Campo não pode ser vazio");
		}
		if (postForm.getBody().isEmpty()) {
			bindingResult.rejectValue("body", "error.post", "Campo não pode ser vazio");
		}

		if (!bindingResult.hasErrors()) {
			Post savedPost = postService.saveOrUpdatePostForm(postForm);
			return "redirect:/post/show/" + savedPost.getId();
		}

		return "post/postform";
	}

	@RequestMapping("/post/delete/{id}")
	public String delete(@PathVariable String id) {
		postService.delete(id);
		return "redirect:/post/list";
	}
}
