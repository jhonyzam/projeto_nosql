package br.com.univali.blog.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.univali.blog.models.Blog;

@Service
public class ValidateService {

	@Autowired
	BlogService blogService;

	public Boolean validateUserPermissionBlog(String blogKey) {
		if (blogKey == null)
			return false;

		Blog blog = blogService.getByKey(blogKey);

		if (blog == null)
			return false;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (blog.getUser().getUsername().equals(auth.getName()));
	}

	public Boolean validateStringNoEspecialCaracter(String s) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]+");
		return p.matcher(s).matches();
	}
}
