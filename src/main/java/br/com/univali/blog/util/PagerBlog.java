package br.com.univali.blog.util;

import org.springframework.data.domain.Page;

import br.com.univali.blog.models.Blog;

/**
 * @author Dusan Raljic
 */
public class PagerBlog {

	private final Page<Blog> blogs;

	public PagerBlog(Page<Blog> blogs) {
		this.blogs = blogs	;
	}

	public int getPageIndex() {
		return blogs.getNumber() + 1;
	}

	public int getPageSize() {
		return blogs.getSize();
	}

	public boolean hasNext() {
		return blogs.hasNext();
	}

	public boolean hasPrevious() {
		return blogs.hasPrevious();
	}

	public int getTotalPages() {
		return blogs.getTotalPages();
	}

	public long getTotalElements() {
		return blogs.getTotalElements();
	}

	public Page<Blog> getBlogs() {
		return blogs;
	}

	public boolean indexOutOfBounds() {
		return getPageIndex() < 0 || getPageIndex() > getTotalElements();
	}

}
