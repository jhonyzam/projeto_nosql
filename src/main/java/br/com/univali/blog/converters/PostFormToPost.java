package br.com.univali.blog.converters;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.univali.blog.forms.PostForm;
import br.com.univali.blog.models.Post;
import br.com.univali.blog.models.User;
import br.com.univali.blog.services.UserService;

@Component
public class PostFormToPost implements Converter<PostForm, Post> {

	@Autowired
	UserService userService;

	@Override
	public Post convert(PostForm postForm) {
		Post post = new Post();
		if (postForm.getId() != null && !StringUtils.isEmpty(postForm.getId())) {
			post.setId(new ObjectId(postForm.getId()));			
			post.setCreateDate(postForm.getCreateDate());
		} else {
			post.setCreateDate(new Date());
		}
		
		post.setUpdateDate(new Date());
		post.setTitle(postForm.getTitle());		
		post.setSecoes(postForm.getSecoes());		
		post.setBody(postForm.getBody());
		post.setBlogKey(postForm.getBlogKey());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());

		post.setUser(user);

		return post;
	}
}
