package com.alibaba.middleware.race.mom.utils;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class StringUtils {

    public static boolean isBlank(String t){
        String s=t.replaceAll(" ","");
        System.out.println("s.equals(\"\") = " + s.equals(""));
        if(s==null || s.equals("") || s.equals(" "))
            return  true;
        return false;
    }

    public static boolean isNotBlank(String t){
        String s=t.replaceAll(" ","");
        System.out.println("s.equals(\"\") = " + s.equals(""));
        if(s==null || s.equals("") || s.equals(" "))
            return  false;
        return true;
    }
    public static void main(String[] args) {


        System.out.println(isBlank(""));
    }
}
