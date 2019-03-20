package br.com.univali.blog.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.univali.blog.forms.BlogForm;
import br.com.univali.blog.models.Blog;

@Component
public class BlogToBlogForm implements Converter<Blog, BlogForm> {
	@Override
	public BlogForm convert(Blog blog) {
		BlogForm blogForm = new BlogForm();

		blogForm.setId(blog.getId().toHexString());
		blogForm.setKey(blog.getKey());
		blogForm.setTitle(blog.getTitle());
		blogForm.setCreateDate(blog.getCreateDate());

		return blogForm;
	}
}
