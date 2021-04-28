package de.hsrm.mi.web.projekt.login;

/**
 * Benutzer des Systems
 */

public class User {
    private String username;
    private String passwort;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

}