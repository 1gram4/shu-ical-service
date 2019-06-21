package com.pao.shuical.util;

import com.pao.shuical.domain.Course;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class ShuLogin {
    public static Boolean login(OkHttpClient client,String userName, String passWord){
        boolean login = false;
        String relayState="";
        String samlRequest="";
        String samlResponce="";
        try {
            //发送GET请求，来获取SAML信息
            Request request1 = new Request.Builder()
                    .url("http://xk.autoisp.shu.edu.cn:8080")
                    .build();
            Response response1 = client.newCall(request1).execute();
            //这里重定向了一次，okhttp帮我们自动重定向了
            //使用Jsoup解析html拿到SAMLRequest和RelayState
            String samlReString = response1.body().string();
            Document doc = Jsoup.parse(samlReString);
            Elements samlReq=doc.select("input");
            for(Element e :samlReq)
            {
                if(e.attr("name").equals("SAMLRequest"))
                    samlRequest=e.val();
                else if(e.attr("name").equals("RelayState"))
                    relayState=e.val();
            }
            RequestBody samlReForm = new FormBody.Builder()
                    .add("SAMLRequest",samlRequest)
                    .add("RelayState",relayState)
                    .build();
            Request request2 = new Request.Builder()
                    .url("https://sso.shu.edu.cn/idp/profile/SAML2/POST/SSO")
                    .post(samlReForm)
                    .build();
            client.newCall(request2).execute();//这里面实际上重定向了两次。最后得到了登陆界面
            /*
             **post密码和账号
             */
            RequestBody loginBody = new FormBody.Builder()
                    .add("j_username",userName)
                    .add("j_password",passWord)
                    .build();
            Request request3 = new Request.Builder()
                    .url("https://sso.shu.edu.cn/idp/Authn/UserPassword")
                    .addHeader("Referer","https://sso.shu.edu.cn/idp/Authn/UserPassword")
                    .post(loginBody)
                    .build();
            Response response3 = client.newCall(request3).execute();
            //上面的代码得到一个html，里面有个浏览器自动提交的表单
            //所以我们需要将表单里的samlResponce提取出来，然后构建一个post请求提交
            String resp3 = response3.body().string();
            Document doc2 = Jsoup.parse(resp3);
            Elements samlResp=doc2.getElementsByTag("input");
            for(Element e :samlResp)
            {
                if(e.attr("name").equals("SAMLResponse"))
                    samlResponce=e.val();
            }
            RequestBody samlRespForm = new FormBody.Builder()
                    .add("SAMLResponse",samlResponce)
                    .add("RelayState",relayState)
                    .build();
            Request request4 = new Request.Builder()
                    .url("http://oauth.shu.edu.cn/oauth/Shibboleth.sso/SAML2/POST")
                    .post(samlRespForm)
                    .build();
            Response response4 = client.newCall(request4).execute();//这里实际上重定向了两次
            if(Cookie.parse(request4.url(),response4.header("Set-Cookie")).name().equals("ASP.NET_SessionId")){
                login = true;
                System.out.println("登陆成功");
            }
            else
                System.out.println("登陆失败");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return login;
    }
}
