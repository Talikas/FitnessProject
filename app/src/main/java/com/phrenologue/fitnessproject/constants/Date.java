package com.phrenologue.fitnessproject.constants;

public class Date {
    private int day;
    private int month;
    private int year;
    private boolean night;

    public Date(String now){
        setDay(now);
        setMonth(now);
        setYear(now);
        setNight(now);
    }

    public int getDay() {
        return day;
    }

    public void setDay(String now) {
        String day = now.substring(8,10);
        this.day = Integer.parseInt(day);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(String now) {
        String month = now.substring(4,7);
        switch (month) {
            case "Jan":
                this.month = 1;
                break;
            case "Feb":
                this.month = 2;
                break;
            case "Mar":
                this.month = 3;
                break;
            case "Apr":
                this.month = 4;
                break;
            case "May":
                this.month = 5;
                break;
            case "Jun":
                this.month = 6;
                break;
            case "Jul":
                this.month = 7;
                break;
            case "Aug":
                this.month = 8;
                break;
            case "Sep":
                this.month = 9;
                break;
            case "Oct":
                this.month = 10;
                break;
            case "Nov":
                this.month = 11;
                break;
            case "Dec":
                this.month = 12;
                break;
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(String now) {
        String year = now.substring(30);
        this.year = Integer.parseInt(year);
    }

    public boolean isNight() {
        return night;
    }

    public void setNight(String now) {
        String hour = now.substring(11,13);
        int h = Integer.parseInt(hour);
        if (6 > h | h > 18) {
            this.night = true;
        } else {
            this.night = false;
        }
    }
}
