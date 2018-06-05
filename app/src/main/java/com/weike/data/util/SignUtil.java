package com.weike.data.util;

import com.weike.data.config.Config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by LeoLu on 2018/5/21.
 */

public class SignUtil {


    public static String signData(Object j) {

        TreeMap<String , Object> map = new TreeMap<>();
        compassAllData(j,map);


        return "";
    }


    public static Map<String, Object> compassAllData(Object clz , TreeMap<String, Object> map) {
        // 得到类对象

        /* 得到类中的所有属性集合 */
        Field[] fs = clz.getClass().getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(clz);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
                LogUtil.d("acthome",f.getName() + "," + val);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            /*
             * String type = f.getType().toString();//得到此属性的类型 if
             * (type.endsWith("String")) {
             * System.out.println(f.getType()+"\t是String"); f.set(obj,"12") ;
             * //给属性设值 }else if(type.endsWith("int") ||
             * type.endsWith("Integer")){
             * System.out.println(f.getType()+"\t是int"); f.set(obj,12) ; //给属性设值
             * }else{ System.out.println(f.getType()+"\t"); }
             */

        }



        return map;
    }

}
