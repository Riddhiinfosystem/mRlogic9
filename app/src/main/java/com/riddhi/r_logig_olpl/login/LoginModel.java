
package com.riddhi.r_logig_olpl.login;

import java.io.Serializable;

public class LoginModel  implements Serializable {
    private String baseurl;
    private String userName;
    private String password;
    private String ccode;
    private boolean isCheck;

    public LoginModel() {
    }

    public LoginModel(String baseurl, String userName, String password, String ccode, boolean isCheck) {
        this.baseurl = baseurl;
        this.userName = userName;
        this.password = password;
        this.ccode = ccode;
        this.isCheck = isCheck;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "baseurl='" + baseurl + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", ccode='" + ccode + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
