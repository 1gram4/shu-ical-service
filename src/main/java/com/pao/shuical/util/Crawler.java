package com.pao.shuical.util;

import ch.qos.logback.core.net.server.Client;
import com.pao.shuical.domain.Course;
import com.pao.shuical.domain.User;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Crawler {
    private static String termPort;
    @Value("${term_port}")
    public static void setTermPort(String termPort) {
        Crawler.termPort = termPort;
    }
    public static List<Course> CrawlCourse(User user){
        List<Course> courses = new ArrayList<Course>();
        try{
            RequestBody stuForm = new FormBody.Builder()
                    .add("studentNo",user.getUserName())
                    .build();
            Request request = new Request.Builder()
                    .url("http://xk.autoisp.shu.edu.cn:"+"8080"+"/StudentQuery/CtrlViewQueryCourseTable")
                    .post(stuForm)
                    .build();
            Response response = user.getClient().newCall(request).execute();
            Document doc = Jsoup.parse(response.body().string());
            response.close();
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
