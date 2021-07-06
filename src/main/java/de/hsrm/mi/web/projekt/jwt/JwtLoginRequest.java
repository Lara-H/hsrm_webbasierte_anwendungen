package de.hsrm.mi.web.projekt.jwt;

/**
 * JwtLoginRequest - einfache Java-Datenklasse, um vom
 * Vue-Frontend (doLogin()) gelieferte JSON-Struktur
 * { 'username': '...', 'password': '...' }
 * aufzunehmen.
 */
public class JwtLoginRequest {
    private String username;
    private String password;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "JwtLoginRequest [password=" + password + ", username=" + username + "]";
    }
}
