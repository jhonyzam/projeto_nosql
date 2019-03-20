package br.com.univali.blog.util;

import org.springframework.data.domain.Page;

import br.com.univali.blog.models.Post;

/**
 * @author Dusan Raljic
 */
public class PagerPost {

    private final Page<Post> posts;

    public PagerPost(Page<Post> posts) {
        this.posts = posts;
    }

    public int getPageIndex() {
        return posts.getNumber() + 1;
    }

    public int getPageSize() {
        return posts.getSize();
    }

    public boolean hasNext() {
        return posts.hasNext();
    }

    public boolean hasPrevious() {
        return posts.hasPrevious();
    }

    public int getTotalPages() {
        return posts.getTotalPages();
    }

    public long getTotalElements() {
        return posts.getTotalElements();
    }

    public Page<Post> getPosts() {
        return posts;
    }

    public boolean indexOutOfBounds() {
        return getPageIndex() < 0 || getPageIndex() > getTotalElements();
    }

}
