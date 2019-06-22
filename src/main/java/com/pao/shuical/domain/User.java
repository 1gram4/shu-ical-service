package com.pao.shuical.domain;

import com.pao.shuical.util.LocalCookieJar;
import lombok.Data;
import okhttp3.OkHttpClient;


/*思考了很久决定这么设计这个用户类，因为一
个client不允许同时登录两个用户
*/
@Data
public class User {
    private String userName;
    private String passWord;
    private LocalCookieJar localCookieJar;
    private OkHttpClient client;
    public User(){};
    public User(String userName,String passWord){
        this.userName = userName;
        this.passWord = passWord;
        this.localCookieJar = new LocalCookieJar();
        this.client = new OkHttpClient().newBuilder()
                // .followRedirects(false)    //禁止自动重定向！！！
                .cookieJar(localCookieJar)   //为OkHttp设置自动携带Cookie的功能
                .build();
    }
}
