package br.com.univali.blog.forms;

import java.util.Date;
import java.util.List;

import br.com.univali.blog.models.Secao;
import br.com.univali.blog.models.User;

public class PostForm {
	private String id;
	private String title;
	private String body;
	private Date createDate;
	private Date updateDate;
	private String blogKey;
	private User user;
	private String secao;
	private Boolean secaoActive;

	// Transferencia para entitidade
	private List<Secao> secoes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSecao() {
		return secao;
	}

	public void setSecao(String secao) {
		this.secao = secao;
	}

	public Boolean getSecaoActive() {
		return secaoActive;
	}

	public void setSecaoActive(Boolean secaoActive) {
		this.secaoActive = secaoActive;
	}

	public List<Secao> getSecoes() {
		return secoes;
	}

	public void setSecoes(List<Secao> secoes) {
		this.secoes = secoes;
	}
}