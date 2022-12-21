package TaskManager.controller;

public class TokenResponse {
    private String access_token;
    private String token_type;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }
}