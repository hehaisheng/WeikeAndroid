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
        StringBuilder builder = new StringBuilder();

        TreeMap<String , Object> map = new TreeMap<>();

       if (j.getClass().getSuperclass() != null) {
           compassAllData(j.getClass().getSuperclass(),j , map);
       }

        compassAllData(j.getClass(),j,map);

        for (String key : map.keySet()) {
            builder.append(key + "=" + map.get(key));
        }
        builder.append(Config.APP_SECRET);

        String md5 = MD5Util.MD5(builder.toString());

        return md5;
    }


    private static Map<String, Object> compassAllData(Class clz,Object obj, TreeMap<String, Object> map) {
        // 得到类对象

        LogUtil.d("acthome","-->" +clz + "," +clz.getDeclaredFields().length);

        /* 得到类中的所有属性集合 */
        Field[] fs = clz.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            if (f.getName().equals("sign")||f.getName().contains("$")) continue;
            try {
                val = f.get(obj);
                // 得到此属性的值

                map.put(f.getName(), val);// 设置键值

                LogUtil.d("acthome","key:"+ f.getName() + "," +val);
            } catch (Exception e) {
                LogUtil.d("acthome",e.getMessage());
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
