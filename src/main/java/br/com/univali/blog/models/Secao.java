package br.com.univali.blog.models;

public class Secao {

	private String id;
	private Integer seq;
	private Integer parent;
	private String title;
	private String body;
	private String paragrafo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
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

	public String getParagrafo() {
		return paragrafo;
	}

	public void setParagrafo(String paragrafo) {
		this.paragrafo = paragrafo;
	}
}