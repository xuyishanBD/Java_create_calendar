package app;

import bean.Constellation_JB;
import bean.DateJB;
import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.csvreader.CsvWriter;
import sun.nio.cs.ext.GBK;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class cal_shadow {
    public static void main(String[] args) throws IOException {
        ArrayList<DateJB> result_list = new ArrayList<>();
        ArrayList<Constellation_JB> conf_lists = CalUtil.readConf();
        String now_String;
        String month_String;
        String day_String;
        int now_year = 1901;
        int now_month = 1;
        int now_day = 1;
        while(now_year <= 2040){
            while(now_month <= 12){
                while(now_day <= CalUtil.getMonthLastDay(now_year,now_month)){
                    month_String = now_month+"";
                    day_String = now_day+"";
                    now_String = now_year
                            + "-"
                            + (month_String.length()==1?("0"+now_month):month_String)
                            + "-"
                            + (day_String.length()==1?("0"+now_day):day_String);
                    DateJB build = DateJB.builder().sunCal(now_String).build();
                    result_list.add(CalUtil.handleDate(build,conf_lists));
                    now_day++;
                }
                now_month++;
                now_day=1;
            }
            now_year++;
            now_month=1;
        }
        CalUtil.createResultCsv(result_list);
    }
}
class CalUtil{
    static DateJB handleDate(DateJB dateJB,ArrayList<Constellation_JB> conf_lists){
        DateTime dateTime = DateUtil.parseDate(dateJB.getSunCal());
        ChineseDate chineseDate = new ChineseDate(dateTime);
        String s = chineseDate.toString();
        dateJB.setShadowCal(s.substring(5));
        dateJB.setAnimalSign(s.substring(2,3));
        dateJB.setConstellation(CalUtil.makeResult(dateJB,conf_lists));
        return dateJB;
    }
    //
    public static void createResultCsv(ArrayList<DateJB> check_list) throws IOException {
        CsvWriter cw = new CsvWriter("C:\\Users\\User\\Desktop\\animalSign.csv", ',', new GBK());
        cw.writeRecord(DateJB.header, true);
        for (DateJB element : check_list) {
            String[] list = element.toStringList();
            cw.writeRecord(list);
        }
        cw.close();
    }
    //
    static int[] run_dayList = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static int[] dayList = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static boolean is_run(int year){
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }
    public static int getMonthLastDay(int year,int month){
        if (is_run(year))
            return run_dayList[month-1];
        else
            return dayList[month-1];
    }
    public static ArrayList<Constellation_JB> readConf(){
        ArrayList<Constellation_JB> conf_lists = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("conf/constellationConf.csv"), new GBK()))
        {
            //缓冲流单行
            String line;
            while ((line = br.readLine()) != null) {
                //切割
                String[] splits = line.split(",");
                //收纳进集合
                conf_lists.add(new Constellation_JB(splits[0],splits[1],splits[2]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return conf_lists;
    }
    public static String makeResult(DateJB bean, ArrayList<Constellation_JB> conf_lists){
        String month_day = bean.getMonth_day();
        String[] split = month_day.split("-");
        int compare_day = Integer.parseInt(split[0]+split[1]);
        for (Constellation_JB conf_bean : conf_lists) {
            if (compare_day >= Integer.parseInt(conf_bean.getStart())
                    && compare_day <= Integer.parseInt(conf_bean.getEnd())){
                return conf_bean.getName();
            }
        }
        return "摩羯座";
    }
}