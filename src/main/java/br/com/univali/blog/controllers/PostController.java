package br.com.univali.blog.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.univali.blog.converters.PostToPostForm;
import br.com.univali.blog.forms.PostForm;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.Secao;
import br.com.univali.blog.services.PostService;
import br.com.univali.blog.services.ValidateService;

@Controller
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private PostToPostForm postToPostForm;

	@Autowired
	private ValidateService validateService;

	@RequestMapping("/blog/{blogKey}/post/{id}")
	public String getPost(@PathVariable String id, Model model) {
		Post post = postService.getById(id);

		model.addAttribute("blogKey", post.getBlogKey());
		model.addAttribute("post", post);

		return "post/show";
	}

	@RequestMapping("/blog/{blogKey}/post/edit/{id}")
	public String editPost(@PathVariable String id, @PathVariable String blogKey, Model model) {
		if (!validateService.validateUserPermissionBlog(blogKey)) {
			return "redirect:/blog/" + blogKey;
		}

		Post post = postService.getById(id);
		PostForm postForm = postToPostForm.convert(post);

		model.addAttribute("postForm", postForm);
		return "post/postform";
	}

	@RequestMapping("/blog/{blogKey}/post/new")
	public String newPost(@PathVariable("blogKey") String blogKey, Model model) {

		if (!validateService.validateUserPermissionBlog(blogKey)) {
			return "redirect:/blog/" + blogKey;
		}

		PostForm postForm = new PostForm();
		postForm.setBlogKey(blogKey);

		model.addAttribute("postForm", postForm);

		return "post/postform";
	}

	@RequestMapping(value = "/post/new", method = RequestMethod.POST)
	public String savePost(@Valid PostForm postForm, BindingResult bindingResult) throws JsonParseException, JsonMappingException, IOException {
		String blogKey = postForm.getBlogKey();

		if (postForm.getSecaoActive()) {
			// Seta branco
			postForm.setBody("");

			if (postForm.getSecao().isEmpty()) {
				bindingResult.rejectValue("secoes", "error.post", "Precisa inserir 1 ou mais seções");
			} else {
				ObjectMapper mapper = new ObjectMapper();
				String json = postForm.getSecao();
				List<Secao> secoes = mapper.readValue(json, new TypeReference<List<Secao>>() {
				});

				postForm.setSecoes(secoes);
			}

		} else {
			if (postForm.getBody().isEmpty()) {
				bindingResult.rejectValue("body", "error.post", "Campo não pode ser vazio");
			}
		}

		if (postForm.getTitle().isEmpty()) {
			bindingResult.rejectValue("title", "error.post", "Campo não pode ser vazio");
		}

		// Pegar data de criacao
		if (!postForm.getId().isEmpty()) {
			Post post = postService.getById(postForm.getId());
			postForm.setCreateDate(post.getCreateDate());
		}

		if (!bindingResult.hasErrors()) {
			Post savedPost = postService.savePost(postForm);
			return "redirect:/blog/" + blogKey + "/post/" + savedPost.getId();
		}

		return "post/postform";
	}

	// @RequestMapping("/blog/{blogKey}/post/delete/{id}")
	// public String deletePost(@PathVariable String id) {
	// postService.delete(id);
	// return "redirect:/post/list";
	// }
}
