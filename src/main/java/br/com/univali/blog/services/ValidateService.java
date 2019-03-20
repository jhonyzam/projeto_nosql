package br.com.univali.blog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.univali.blog.models.Blog;

@Service
public class ValidateService {

	@Autowired
	BlogService blogService;

	public Boolean validateCreatePost(String blogKey) {
		if(blogKey == null)
			return false;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Blog blog = blogService.getByKey(blogKey);

		return (blog.getUser().getUsername().equals(auth.getName()));
	}

}
