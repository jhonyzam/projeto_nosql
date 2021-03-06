package br.com.univali.blog.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.univali.blog.forms.PostForm;
import br.com.univali.blog.models.Post;

@Component
public class PostToPostForm implements Converter<Post, PostForm> {
	@Override
	public PostForm convert(Post post) {
		PostForm postForm = new PostForm();

		postForm.setId(post.getId().toHexString());
		postForm.setTitle(post.getTitle());
		postForm.setBlogKey(post.getBlogKey());
		postForm.setCreateDate(post.getCreateDate());
		postForm.setUpdateDate(post.getUpdateDate());

		postForm.setBody(post.getBody());
		postForm.setSecoes(post.getSecoes());

		if (post.getSecoes() == null) {
			postForm.setSecaoActive(false);
		} else {
			postForm.setSecaoActive(true);
		}

		return postForm;
	}
}
