package com.fly.widget.pickerview;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 地址选择器
 *
 * @version 0.1 king 2015-10
 */
public class OptionsWindowHelper {

    private static ArrayList<LocationInfo> yearInfos = null;
    private static ArrayList<LocationInfo> monthInfos = null;
    private static ArrayList<LocationInfo> dayInfos = null;
    private static ArrayList<LocationInfo> timeFormateInfos = null;


    public static interface OnOptionsSelectListener {
        public void onOptionsSelect(LocationInfo province, LocationInfo city, LocationInfo area);
    }

    private OptionsWindowHelper() {
    }

    /**
     * 初始化
     * @param mContext
     * @param positionStatus 联动效果，year，month
     * @param from "time"-日期，"time_formate"-日期格式
     * @param listener
     * @return
     */
    public static CharacterPickerWindow builder(Context mContext, String positionStatus, String from, final OnOptionsSelectListener listener) {
        //选项选择器
        CharacterPickerWindow mOptions = new CharacterPickerWindow(mContext);
        switch (from) {
            case "time":
                //初始化选项数据
                setDateData(mContext, positionStatus, mOptions, listener);
                break;
            case "time_formate":
                //初始化选项数据
                setDateFormateData(mContext, mOptions, listener);
                break;
        }
        return mOptions;
    }

    /**
     * <pre>
     *   设置日期数据
     * </pre>
     */
    public static void setDateData(Context mContext, String positionStatus, CharacterPickerWindow mOptions, final OnOptionsSelectListener listener) {

        yearInfos = new ArrayList<LocationInfo>();
        monthInfos = new ArrayList<LocationInfo>();
        dayInfos = new ArrayList<LocationInfo>();
        yearInfos = getYear();
        monthInfos = getMonth(startInt[0]);
        dayInfos = getDay(startInt[0], startInt[1]);

        switch (positionStatus) {//三级联动效果
            case "year":
                mOptions.getPickerView().setPicker(yearInfos, null, null, mContext);
                break;
            case "month":
                mOptions.getPickerView().setPicker(yearInfos, monthInfos, null, mContext);
                break;
            default:
                mOptions.getPickerView().setPicker(yearInfos, monthInfos, dayInfos, mContext);
                break;
        }


        //设置默认选中的三级项目
        mOptions.setSelectOptions(endInt[0]-startInt[0], endInt[1]-startInt[1], endInt[2]-startInt[2]);
        //监听确定选择按钮
        mOptions.setOnoptionsSelectListener(new CharacterPickerWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (listener != null) {
                    LocationInfo year = yearInfos.get(options1);
                    LocationInfo month, day;

                    int yearInt = Integer.valueOf(year.getLocationCode());
                    //防止 滑动过快导致崩溃
                    if (option2 < getMonth(yearInt).size())
                        month = getMonth(yearInt).get(option2);
                    else
                        month = getMonth(yearInt).get(0);
                    int monthInt = Integer.valueOf(month.getLocationCode());
                    if (options3 < getDay(yearInt, monthInt).size())
                        day = getDay(yearInt, monthInt).get(options3);
                    else
                        day = getDay(yearInt, monthInt).get(0);

                    listener.onOptionsSelect(year, month, day);


                }
            }
        });

    }

    /**
     * <pre>
     *   设置日期格式
     * </pre>
     */
    public static void setDateFormateData(Context mContext, CharacterPickerWindow mOptions, final OnOptionsSelectListener listener) {

        timeFormateInfos = new ArrayList<LocationInfo>();
        timeFormateInfos = getTimeFormate();

        //三级联动效果
        mOptions.getPickerView().setPicker(timeFormateInfos, null, null, mContext);
        //设置默认选中的三级项目
        mOptions.setSelectOptions(0, 0, 0);
        //监听确定选择按钮
        mOptions.setOnoptionsSelectListener(new CharacterPickerWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (listener != null) {
                    LocationInfo timeFormate = timeFormateInfos.get(options1);
                    listener.onOptionsSelect(timeFormate, null, null);
                }
            }
        });

    }

    /**
     * 获取年份
     *
     * @return
     */
    private static ArrayList<LocationInfo> getTimeFormate() {

        ArrayList<LocationInfo> list = new ArrayList<>();
        LocationInfo dayInfo = new LocationInfo();
        dayInfo.setLocationName("日");
        dayInfo.setLocationCode("day");
        LocationInfo monthInfo = new LocationInfo();
        monthInfo.setLocationName("月");
        monthInfo.setLocationCode("month");
        LocationInfo yearInfo = new LocationInfo();
        yearInfo.setLocationName("年");
        yearInfo.setLocationCode("year");

        list.add(dayInfo);
        list.add(monthInfo);
        list.add(yearInfo);

        return list;
    }

    /**
     * 获取年份
     *
     * @return
     */
    private static ArrayList<LocationInfo> getYear() {

        ArrayList<LocationInfo> list = new ArrayList<>();
        for (int i = startInt[0]; i <= endInt[0]; i++) {
            LocationInfo info = new LocationInfo();
            info.setLocationName(i + "年");
            info.setLocationCode(i + "");
            list.add(info);
        }
        return list;
    }

    public static int[] startInt;
    private static int[] endInt;
    private static String endTime;
    private static String startTime;

    /**
     * 设置起始日期 如果time=""为今天
     */
    public static void setStartTime(String time) {
        if (StringUtils.isEmpty(time)) {
            startTime = "20100101";
        } else {
            startTime = time;
        }
        startInt = new int[6];
        try {
            startInt[0] = Integer.valueOf(startTime.substring(0, 4));
            startInt[1] = Integer.valueOf(startTime.substring(4, 6));
            startInt[2] = Integer.valueOf(startTime.substring(6, 8));
        } catch (Exception e) {
            startInt[0] = 2010;
            startInt[1] = 1;
            startInt[2] = 1;
        }

    }

    /**
     * 设置截止日期 如果time=""为今天
     */
    public static void setEndTime(String time) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        if (StringUtils.isEmpty(time)) {
            endTime = df.format(date);
        } else {
            endTime = time;
        }

        endInt = new int[6];
        try {
            endInt[0] = Integer.valueOf(endTime.substring(0, 4));
            endInt[1] = Integer.valueOf(endTime.substring(4, 6));
            endInt[2] = Integer.valueOf(endTime.substring(6, 8));
        } catch (Exception e) {
            endInt[0] = Integer.valueOf(df.format(date).substring(0, 4));
            endInt[1] = Integer.valueOf(df.format(date).substring(4, 6));
            endInt[2] = Integer.valueOf(df.format(date).substring(6, 8));
        }

    }

    /**
     * 获取月份
     *
     * @return
     */
    public static ArrayList<LocationInfo> getMonth(int year) {

        ArrayList<LocationInfo> list = new ArrayList<>();
        if (startInt[0] == endInt[0]) {//起始跟结束相等
            list = setMonthInfos(startInt[1], endInt[1], list);
        } else if (year == startInt[0]) {//起始年
            list = setMonthInfos(startInt[1], 12, list);
        } else if (year == endInt[0]) {//结束年
            list = setMonthInfos(1, endInt[1], list);
        } else {//默认
            list = setMonthInfos(1, 12, list);
        }

        return list;
    }

    /**
     * 设置月份 范围
     *
     * @param start
     * @param end
     * @param list
     * @return
     */
    private static ArrayList<LocationInfo> setMonthInfos(int start, int end, ArrayList<LocationInfo> list) {
        for (int i = start; i <= end; i++) {
            LocationInfo info = new LocationInfo();
            info.setLocationName(i + "月");
            if (i < 10) {
                info.setLocationCode("0" + i);
            } else {
                info.setLocationCode(i + "");
            }
            list.add(info);
        }
        return list;
    }

    /**
     * 获取天
     *
     * @return
     */
    public static ArrayList<LocationInfo> getDay(int year, int month) {

        ArrayList<LocationInfo> list = new ArrayList<>();
        int start, end;
        if (startInt[0] == endInt[0] && startInt[1] == endInt[1]) {//起始跟结束相等
            list = setDayInfos(startInt[2], endInt[2], list);
        } else if (year == startInt[0] && month == startInt[1]) {//起始年
            start = startInt[2];
            list = setDayInfos(start, getDayCount(year, month), list);
        } else if (year == endInt[0] && month == endInt[1]) {//结束年
            end = endInt[2];
            list = setDayInfos(1, end, list);
        } else {//默认
            list = setDayInfos(1, getDayCount(year, month), list);
        }
        return list;
    }

    /**
     * 设置天 范围
     *
     * @param start
     * @param end
     * @param list
     * @return
     */
    private static ArrayList<LocationInfo> setDayInfos(int start, int end, ArrayList<LocationInfo> list) {

        for (int i = start; i <= end; i++) {
            LocationInfo info = new LocationInfo();
            info.setLocationName(i + "日");
            if (i < 10) {
                info.setLocationCode("0" + i);
            } else {
                info.setLocationCode(i + "");
            }
            list.add(info);
        }
        return list;
    }

    /**
     * 根据year、month获取日期
     *
     * @param year
     * @param month
     * @return
     */
    private static int getDayCount(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }
}
