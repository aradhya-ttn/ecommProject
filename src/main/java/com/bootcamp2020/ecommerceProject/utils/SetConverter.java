package com.bootcamp2020.ecommerceProject.utils;


import java.util.HashSet;
import java.util.Set;

public class SetConverter {
    private final static  String delimeter=",";

    public static String convertToString(Set<String> stringSet) {
        String stringValue="";
        if (stringSet.isEmpty())
            return stringValue;
        else
        {
            stringValue=String.join(delimeter,stringSet);
            return stringValue;
        }
    }

    public static Set<String> convertToSet(String stringValue) {
        Set<String> stringSet=new HashSet<>();
        String [] values=stringValue.split(delimeter);
        for (String value:values) {
            stringSet.add(value);
        }
        return stringSet;
    }
}
