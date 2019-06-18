package com.pao.shuical.util;

import ch.qos.logback.core.net.server.Client;
import com.pao.shuical.domain.Course;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    public static List<Course> CrawlCourse(OkHttpClient client,String userName){
        List<Course> courses = new ArrayList<Course>();
        try{
            RequestBody stuForm = new FormBody.Builder()
                    .add("studentNo",userName)
                    .build();
            Request request = new Request.Builder()
                    .url("http://xk.autoisp.shu.edu.cn:8080/StudentQuery/CtrlViewQueryCourseTable")
                    .post(stuForm)
                    .build();
            Response response = client.newCall(request).execute();
            Document doc = Jsoup.parse(response.body().string());
            Elements elements = doc.getElementsByTag("table").first().getElementsByTag("tr");
            for(int i=3;i<elements.size()-1;i++){
                courses.add(new Course(elements.get(i).select("td")));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return courses;
    }

}
