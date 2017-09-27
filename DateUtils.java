package cc.leshenghuo.lecollect.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sagee on 2017/9/12.
 * 时间工具
 */

public class DateUtils {
    /**
     * @return 上一个月的第一天
     */
    public static String getMonthFirstDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //获取一个月前的日期第一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return formatter.format(calendar.getTime());
    }

    /**
     * @return 上一个月的最后一天
     */
    public static String getMonthLastDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //获取一个月前的日期第一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatter.format(calendar.getTime());
    }

    /**
     * @return 本月第一天
     */
    public static String getMonthDay(){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateFormater.format(cal.getTime());
    }

    /**
     * @return 昨天
     */
    public static String getYesterday(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date yesterdayDate = new Date(System.currentTimeMillis() - 86400000);
        return formatter.format(yesterdayDate);
    }

    /**
     * @return 今天
     */
    public static String getToday(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date yesterdayDate = new Date(System.currentTimeMillis());
        return formatter.format(yesterdayDate);
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"06-14 07:50"）
     *
     * @param time
     * @return
     */
    public static String timeFormat(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }
}
