package br.com.univali.blog.models;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Post {
	@Id
	private ObjectId _id;

	private String title;

	private String body;

	private Date createDate;

	private Date updateDate;

	private String blogKey;

	private User user;

	private List<Secao> secoes;

	public ObjectId getId() {
		return _id;
	}

	public void setId(ObjectId _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getBlogKey() {
		return blogKey;
	}

	public void setBlogKey(String blogKey) {
		this.blogKey = blogKey;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Secao> getSecoes() {
		if (this.secoes != null)
			return this.secoes.stream().sorted(Comparator.comparing(Secao::getParagrafo)).collect(Collectors.toList());
		else
			return secoes;
	}

	public void setSecoes(List<Secao> secoes) {
		this.secoes = secoes;
	}
}