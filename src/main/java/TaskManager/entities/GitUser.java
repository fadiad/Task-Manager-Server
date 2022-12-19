package TaskManager.entities;

import java.util.Objects;

public class GitUser {
    private String name;
    private String email;

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login;


    public GitUser(String name, String email, String accessToken, String login) {
        this.name = name;
        this.email = email;
        this.accessToken = accessToken;
        this.login = login;
    }

    public GitUser() {
    }

    @Override
    public String toString() {
        return "GitUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitUser gitUser = (GitUser) o;
        return Objects.equals(name, gitUser.name) && Objects.equals(email, gitUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

}
