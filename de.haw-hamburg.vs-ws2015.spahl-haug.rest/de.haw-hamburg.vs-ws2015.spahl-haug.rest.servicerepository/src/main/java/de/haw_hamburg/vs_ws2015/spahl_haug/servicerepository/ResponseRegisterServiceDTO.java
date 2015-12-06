package de.haw_hamburg.vs_ws2015.spahl_haug.servicerepository;

public class ResponseRegisterServiceDTO extends RegisterServiceDTO{
	String _uri;
	String status;
	public String get_uri() {
		return _uri;
	}
	public void set_uri(final String _uri) {
		this._uri = _uri;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(final String status) {
		this.status = status;
	}


}
