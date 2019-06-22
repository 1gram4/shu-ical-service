package com.pao.shuical.util;

import com.pao.shuical.domain.Course;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CoursesToIcs {
    //从配置文件读取配置
    private static String termStartDateString;
    private static String icsPath;
    @Value("${ics_path}")
    public void setIcsPath(String icsPath) {
        CoursesToIcs.icsPath = icsPath;
    }
    @Value("${term_start_date}")
    public void setTermStartDateString(String termStartDateString) {
        CoursesToIcs.termStartDateString = termStartDateString;
    }

    //读入course列表，生成ics文件
    public static Boolean coursesToIcs(String icsName,List<Course> courses){
        try{
            //创建学期开始日期
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            //每天的上课时刻表,每天8:00上课
            int course_minutes[] = {0, 55, 120, 175, 250, 295, 370, 425, 480, 535, 600, 655, 710};
            long termStartTime = format.parse(termStartDateString).getTime();
            // 创建一个时区（TimeZone）
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
            VTimeZone tz = timezone.getVTimeZone();
            // 创建日历
            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(CalScale.GREGORIAN);
            //处理课表中每一节课,将其转化为相关事件
            for (int i=0;i<courses.size();++i){
                Pattern pattern1 = Pattern.compile("(\\d)-(\\d?\\d)周"); //（1-5周）
                Pattern pattern2 = Pattern.compile("(\\d),(\\d?\\d)周"); //（1，5周）
                Pattern pattern3 = Pattern.compile("(一|二|三|四|五)(\\d?\\d)-(\\d?\\d)");// 五6-9
                Matcher m1 = pattern1.matcher(courses.get(i).getSksj());
                Matcher m2 = pattern2.matcher(courses.get(i).getSksj());
                Matcher m3 = pattern3.matcher(courses.get(i).getSksj());
                if(m1.find()){
                    //属于1-5周这样的情况
                    //不管属于哪种情况，每节课在某一周可能有多个连续上课阶段 如：一4-6 五6-9
                    // 所以会有多次匹配，每次匹配当一个单独事件来处理
                    while (m3.find()){
                        //起始时间
                        DateTime start = new DateTime(new java.util.Date(termStartTime +
                                (((Integer.parseInt(m1.group(1))-1)*7+dayToInt(m3.group(1))-1)*24*60+8*60+  
                                course_minutes[Integer.parseInt(m3.group(2))-1])*60*1000));
                        //结束时间
                        DateTime end = new DateTime(new java.util.Date(termStartTime +
                                (((Integer.parseInt(m1.group(1))-1)*7+dayToInt(m3.group(1))-1)*24*60+8*60+45+
                                        course_minutes[Integer.parseInt(m3.group(3))-1])*60*1000));
                        // 创建事件
                        String eventName = courses.get(i).getKcm(); //事件名为课程名
                        VEvent event = new VEvent(start, end, eventName);
                        //设置循环
                        Recur recur = new Recur(Recur.WEEKLY, 
                                Integer.parseInt(m1.group(2))-Integer.parseInt(m1.group(1))+1);
                        recur.setInterval(1); //设置循环间隔
                        RRule rule = new RRule(recur);
                        event.getProperties().add(new Location(courses.get(i).getSkdd())); //添加地点
                        event.getProperties().add(tz.getTimeZoneId());
                        //设置uid这里用kch+jsh
                        event.getProperties().add(new Uid(courses.get(i).getKch()+courses.get(i).getJsh())); 
                        event.getProperties().add(rule);
                        calendar.getComponents().add(event);
                    }
                }
                else if(m2.find()){
                    //属于1，5周这样的情况
                    while (m3.find()){
                        //起始时间
                        DateTime start = new DateTime(new java.util.Date(termStartTime +
                                (((Integer.parseInt(m2.group(1))-1)*7+dayToInt(m3.group(1))-1)*24*60+8*60+
                                        course_minutes[Integer.parseInt(m3.group(2))-1])*60*1000));
                        //结束时间
                        DateTime end = new DateTime(new java.util.Date(termStartTime +
                                (((Integer.parseInt(m2.group(1))-1)*7+dayToInt(m3.group(1))-1)*24*60+8*60+45+
                                        course_minutes[Integer.parseInt(m3.group(3))-1])*60*1000));
                        // 创建事件
                        String eventName = courses.get(i).getKcm(); //事件名为课程名
                        VEvent event = new VEvent(start, end, eventName);
                        //设置循环
                        Recur recur = new Recur(Recur.WEEKLY, 2);
                        recur.setInterval(Integer.parseInt(m2.group(2))-Integer.parseInt(m2.group(1))); //设置循环间隔
                        RRule rule = new RRule(recur);
                        event.getProperties().add(new Location(courses.get(i).getSkdd())); //添加地点
                        event.getProperties().add(tz.getTimeZoneId());
                        event.getProperties().add(new Uid(courses.get(i).getKch()+courses.get(i).getJsh()));
                        event.getProperties().add(rule);
                        calendar.getComponents().add(event);
                    }
                }
                else{
                    //十周全部都有
                    while (m3.find()){
                        //起始时间
                        DateTime start = new DateTime(new java.util.Date(termStartTime +
                                ((dayToInt(m3.group(1))-1)*24*60+8*60+
                                        course_minutes[Integer.parseInt(m3.group(2))-1])*60*1000));
                        //结束时间
                        DateTime end = new DateTime(new java.util.Date(termStartTime +
                                ((dayToInt(m3.group(1))-1)*24*60+8*60+45+
                                        course_minutes[Integer.parseInt(m3.group(3))-1])*60*1000));
                        // 创建事件
                        String eventName = courses.get(i).getKcm(); //事件名为课程名
                        VEvent event = new VEvent(start, end, eventName);
                        //设置循环
                        Recur recur = new Recur(Recur.WEEKLY, 10);
                        recur.setInterval(1); //设置循环间隔
                        RRule rule = new RRule(recur);
                        event.getProperties().add(new Location(courses.get(i).getSkdd())); //添加地点
                        event.getProperties().add(tz.getTimeZoneId());
                        event.getProperties().add(new Uid(courses.get(i).getKch()+courses.get(i).getJsh()));
                        event.getProperties().add(rule);
                        calendar.getComponents().add(event);
                    }
                }
            }
            // 验证
            calendar.validate();
            //写出
            FileOutputStream fout = new FileOutputStream(icsPath+icsName+".ics");
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ValidationException e){
            e.printStackTrace();
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return true;
    }
    public static int dayToInt(String s){
        if (s.equals("一"))
            return 1;
        else if(s.equals("二"))
            return 2;
        else if (s.equals("三"))
            return 3;
        else if (s.equals("四"))
            return 4;
        else if (s.equals("五"))
            return 5;
        else
            return 0;
    }
}
