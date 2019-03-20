package br.com.univali.blog.converters;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.univali.blog.forms.BlogForm;
import br.com.univali.blog.models.Blog;
import br.com.univali.blog.models.User;
import br.com.univali.blog.services.UserService;

@Component
public class BlogFormToBlog implements Converter<BlogForm, Blog> {

	@Autowired
	UserService userService;

	@Override
	public Blog convert(BlogForm blogForm) {
		Blog blog = new Blog();
		if (blogForm.getId() != null && !StringUtils.isEmpty(blogForm.getId())) {
			blog.setId(new ObjectId(blogForm.getId()));
			blog.setCreateDate(blogForm.getCreateDate());
		}else{
			blog.setCreateDate(new Date());
		}

		blog.setKey(blogForm.getKey().toLowerCase());
		blog.setTitle(blogForm.getTitle());
		

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());

		blog.setUser(user);

		return blog;
	}
}
