package com.example.a18433.jwcmmvtc.entity;

import java.util.ArrayList;
import java.util.List;

public class kebiao {
    //学年
    private String Time, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMonday() {
        return Monday;
    }

    public void setMonday(String monday) {
        Monday = monday;
    }

    public String getTuesday() {
        return Tuesday;
    }

    public void setTuesday(String tuesday) {
        Tuesday = tuesday;
    }

    public String getWednesday() {
        return Wednesday;
    }

    public void setWednesday(String wednesday) {
        Wednesday = wednesday;
    }

    public String getThursday() {
        return Thursday;
    }

    public void setThursday(String thursday) {
        Thursday = thursday;
    }

    public String getFriday() {
        return Friday;
    }

    public void setFriday(String friday) {
        Friday = friday;
    }

    public String getSaturday() {
        return Saturday;
    }

    public void setSaturday(String saturday) {
        Saturday = saturday;
    }

    public String getSunday() {
        return Sunday;
    }

    public void setSunday(String sunday) {
        Sunday = sunday;
    }

    public static List<List<String>> getkebiao(List<kebiao> kebiaos) {
        ArrayList<List<String>> base = new ArrayList<>();
        for (int i = 0; i < kebiaos.size(); i++) {
            List<String> arrayList = new ArrayList<>();
            arrayList.add(kebiaos.get(i).getTime());
            arrayList.add(kebiaos.get(i).getMonday());
            arrayList.add(kebiaos.get(i).getThursday());
            arrayList.add(kebiaos.get(i).getWednesday());
            arrayList.add(kebiaos.get(i).getThursday());
            arrayList.add(kebiaos.get(i).getFriday());
            arrayList.add(kebiaos.get(i).getSaturday());
            arrayList.add(kebiaos.get(i).getSunday());
            base.add(arrayList);
        }

        return base;
    }
}
