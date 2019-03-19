package br.com.univali.blog.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.univali.blog.commands.PostForm;
import br.com.univali.blog.converters.PostToPostForm;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.services.PostService;

@Controller
public class PostController {
	private PostService postService;

	private PostToPostForm postToPostForm;

	@Autowired
	public void setPostToPostForm(PostToPostForm postToPostForm) {
		this.postToPostForm = postToPostForm;
	}

	@Autowired
	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	@RequestMapping("/")
	public String redirToList() {
		return "redirect:/post/list";
	}

	@RequestMapping({ "/post/list", "/post" })
	public String listPosts(Model model) {
		model.addAttribute("posts", postService.listAll());
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

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String saveOrUpdatePost(@Valid PostForm postForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "post/postform";
		}

		Post savedPost = postService.saveOrUpdatePostForm(postForm);

		return "redirect:/post/show/" + savedPost.getId();
	}

	@RequestMapping("/post/delete/{id}")
	public String delete(@PathVariable String id) {
		postService.delete(id);
		return "redirect:/post/list";
	}
}
