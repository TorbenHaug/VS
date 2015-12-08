package de.haw_hamburg.vs_ws2015.spahl_haug.frontend.common.windows;

public interface ILoginActions extends IWindowActions{
	public void onLogin(final String userName) throws RepositoryException;
}
