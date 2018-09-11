package com.weike.data.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LogSortManager {



    public int beanCount=0;
    public String returnString="";

    public int toChangeCount=0;
    public String newValue="";
    Map<String, Object> valueMap = new HashMap<String, Object>();



    public static  LogSortManager newInstance(){
        return new LogSortManager();
    }

    public String  toSort(String jsonString){
        String loString=toMake(jsonString);
        List<Map.Entry<String,Object>> list = new ArrayList<Map.Entry<String,Object>>(valueMap.entrySet());

        Collections.sort(list,new Comparator<Map.Entry<String,Object>>() {
            //升序排序
            public int compare(Map.Entry<String, Object> o1,
                               Map.Entry<String,Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }

        });


        newValue="";
        String newReturnString="";
        for(int i=0;i<list.size();i++){
            String key=list.get(i).getKey();
            Object value=list.get(i).getValue();

            newValue=newValue+key+":"+value;
            if(newValue.length()>=50){
                newReturnString=newReturnString+"    "+key+":"+value+"\n";
                newValue="";
            }else{

                newReturnString=newReturnString+"    "+key+":"+value;
            }
        }


        valueMap=new HashMap<String, Object>();
        beanCount=0;
        returnString="";
        toChangeCount=0;
        newValue="";
        return newReturnString;
    }
    private String toMake(String jsonString){



        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonString);
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;

            while (keyIter.hasNext())
            {
                key =  keyIter.next();
                value = jsonObject.get(key);
                String valueString=key+"@#@"+value;
                valueMap.put(key,value);
                if(valueString.contains(":{")&&beanCount==0){
                    returnString=key+":"+"\n";
                    beanCount=beanCount+1;
                    returnString=toMake(valueString.split("@#@")[1]);


                }else{
                    toChangeCount=toChangeCount+1;

                    newValue=newValue+key+":"+value;
                    if(newValue.length()>=50){
                        returnString=returnString+"    "+key+":"+value+"\n";
                        newValue="";
                    }else{

                        returnString=returnString+"    "+key+":"+value;
                    }
//                    if(toChangeCount%2==0){
//                        returnString=returnString+"    "+key+":"+value+"\n";
//                    }else{
//                        returnString=returnString+"    "+key+":"+value;
//                    }

                }
            }
            return  returnString;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return returnString;


    }

}
