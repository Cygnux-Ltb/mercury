package io.mercury.transport.http.couch;

public class CouchBean {

	private String id;
	private String rev;
	private String value;

	public String getId() {
		return id;
	}

	public CouchBean setId(String id) {
		this.id = id;
		return this;
	}

	public String getRev() {
		return rev;
	}

	public CouchBean setRev(String rev) {
		this.rev = rev;
		return this;
	}

	public String getValue() {
		return value;
	}

	public CouchBean setValue(String value) {
		this.value = value;
		return this;
	}

}
