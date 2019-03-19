package br.com.univali.blog.converters;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.univali.blog.commands.PostForm;
import br.com.univali.blog.models.Post;

@Component
public class PostFormToPost implements Converter<PostForm, Post> {

	@Override
	public Post convert(PostForm postForm) {
		Post post = new Post();
		if (postForm.getId() != null && !StringUtils.isEmpty(postForm.getId())) {
			post.setId(new ObjectId(postForm.getId()));
		}

		post.setTitle(postForm.getTitle());
		post.setBody(postForm.getBody());
		post.setCreateDate(new Date());
		
		return post;
	}
}
