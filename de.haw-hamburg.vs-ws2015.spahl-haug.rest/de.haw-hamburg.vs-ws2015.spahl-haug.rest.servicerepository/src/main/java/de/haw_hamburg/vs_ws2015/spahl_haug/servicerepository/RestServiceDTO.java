package de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository;

public class RestServiceDTO{
	String _uri;
	String description;
	String name;
	String service;
	String status;
	String uri;
	public String get_uri() {
		return _uri;
	}
	public void set_uri(final String _uri) {
		this._uri = _uri;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getService() {
		return service;
	}
	public void setService(final String service) {
		this.service = service;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(final String status) {
		this.status = status;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(final String uri) {
		this.uri = uri;
	}

}
