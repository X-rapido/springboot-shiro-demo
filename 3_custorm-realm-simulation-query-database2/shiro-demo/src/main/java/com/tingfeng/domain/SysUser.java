package com.tingfeng.domain;

/**
 * 系统用户
 */
public class SysUser {

    private String username;
    private String password;
    private String authSalt;    // 盐值

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

    public String getAuthSalt() {
        return authSalt;
    }

    public void setAuthSalt(String authSalt) {
        this.authSalt = authSalt;
    }
}
