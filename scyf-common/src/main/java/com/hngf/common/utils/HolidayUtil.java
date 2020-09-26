package com.hngf.common.utils;


import java.io.BufferedReader;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

/**

 * 调用API接口判断日期是否是工作日 周末还是节假日

 *

 * @author i

 *

 */

public class HolidayUtil {

    /**

     * @param httpArg

     *            :参数

     * @return 返回结果

     */

    public static int request( String httpArg) {

       // String httpUrl="http://api.goseek.cn/Tools/holiday";
        String httpUrl="http://timor.tech/api/holiday/info/";

        BufferedReader reader = null;

        String result = null;

        StringBuffer sbf = new StringBuffer();

        httpUrl = httpUrl  + httpArg;

        int d=0;

        try {

            URL url = new URL(httpUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.connect();

            InputStream is = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String strRead = null;

            while ((strRead = reader.readLine()) != null) {

                sbf.append(strRead);

                sbf.append("\r\n");

            }

            reader.close();

            result = sbf.toString();

            JSONObject ob= JSONObject.fromObject(result);

            if(ob!=null){
                d =Integer.parseInt(JSONObject.fromObject(ob.getString("type")).getString("type"));  // "type": enum(0, 1, 2), // 节假日类型，分别表示 工作日、周末、节日。
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return d;

    }



    public static void main(String[] args) {

        //判断今天是否是工作日 周末 还是节假日
         Date date = new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr= f.format(date);
        String httpArg="2020-08-01";//f.format(new Date());

        System.out.println(httpArg);

        int n = request(httpArg);

        System.out.println(n);
    }

}

