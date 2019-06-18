package com.pao.shuical.domain;

import lombok.Data;
import org.jsoup.select.Elements;

@Data
public class Course {
    private String xh; //序号
    private String kch; //课程号
    private String kcm; //课程名
    private String jsh; //教师号
    private String jsm; //教师名
    private String xf; //学分
    private String sksj; //上课时间
    private String skdd; //上课地点
    private String xq;  //校区
    private String dysj; //答疑时间
    private String dydd; //答疑地点

    public Course(){};
    public Course(Elements course){
        this.xh = course.get(0).text();
        this.kch = course.get(1).text();
        this.kcm = course.get(2).text();
        this.jsh = course.get(3).text();
        this.jsm = course.get(4).text();
        this.xf = course.get(5).text();
        this.sksj = course.get(6).text();
        this.skdd = course.get(7).text();
        this.xq = course.get(8).text();
        this.dysj = course.get(9).text();
        this.dydd = course.get(10).text();
    }
}
