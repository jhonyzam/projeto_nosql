package br.com.univali.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.User;
import br.com.univali.blog.services.PostService;
import br.com.univali.blog.services.UserService;
import br.com.univali.blog.util.Pager;

@Controller
public class BlogController {

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String blogForUsername(@PathVariable String username, @RequestParam(defaultValue = "0") int page, Model model) {

		User user = userService.findByUsername(username);

		if (user != null) {
			Page<Post> posts = postService.findByUserOrderByCreateDateDesc(user, page);
			Pager pager = new Pager(posts);

			model.addAttribute("pager", pager);
			model.addAttribute("user", user);

			return "/post/list";

		} else {
			return "/error";
		}
	}
}
