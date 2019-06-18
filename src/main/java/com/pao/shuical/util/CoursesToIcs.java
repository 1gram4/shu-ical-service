package com.pao.shuical.util;

import com.pao.shuical.domain.Course;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.UidGenerator;
import org.thymeleaf.templateparser.text.TextParseException;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

public class CoursesToIcs {
    public static Calendar coursesToIcs(List<Course> courses){
        // 创建一个时区（TimeZone）
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
        VTimeZone tz = timezone.getVTimeZone();
        // 创建日历
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        try{
            for (int i=0;i<courses.size();++i){

            }
            DateTime startDate1 = new DateTime(format.parse(("2019/06/18 20:00")));
            DateTime endDate1 = new DateTime(format.parse(("2019/06/18 20:30")));
            // 创建事件
            String eventName = "test";
            VEvent event1 = new VEvent(startDate1, endDate1, eventName);
            //设置循环
            Recur recur = new Recur(Recur.WEEKLY, 4);
            recur.setInterval(1);
            RRule rule = new RRule(recur);
            event1.getProperties().add(new Location("上海大学")); //添加地点
            event1.getProperties().add(tz.getTimeZoneId());
            event1.getProperties().add(new Uid("1232131231231"));
            event1.getProperties().add(rule);
            calendar.getComponents().add(event1);
            // 验证
            calendar.validate();
            //写出
            FileOutputStream fout = new FileOutputStream("F:\\ics\\test.ics");
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
        return calendar;
    }
    public static int stringToInt(String s){
        if (s=="一")
            return 1;
        else if(s=="二")
            return 2;
        else if (s=="三")
            return 3;
        else if (s=="四")
            return 4;
        else if (s=="五")
            return 5;
        else
            return 0;
    }
}
