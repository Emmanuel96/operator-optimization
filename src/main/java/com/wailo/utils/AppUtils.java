package com.wailo.utils;

public class AppUtils {

    private AppUtils(){}

    public static Float parseStringToFloat(String string){
        try {
            return Float.parseFloat(string);
        }catch (Exception e){
            return null;
        }
    }

    public static Double parseStringToDouble(String string){
        try {
            return Double.parseDouble(string);
        }catch (Exception e){
            return null;
        }
    }
}
