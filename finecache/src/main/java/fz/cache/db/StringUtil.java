package fz.cache.db;

import java.util.List;

/**
 * Created by tanping on 2018/3/19.
 */

public class StringUtil {

    public  static boolean  isGif(String url){
        if (url == null) {
            return false;
        }

        int index = url.toLowerCase().indexOf(".gif");
        return index > 0;

    }

    public static boolean isEmpty(String str){
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str){
        return str != null && !str.trim().equals("") && !str.trim().equals("null");
    }

    public static boolean isEmpty(List list){
        return list == null || list.size() == 0;
    }
    public static boolean isNotEmpty(List list){
        return list != null && list.size() != 0;
    }


    /**
     * create sql tuper 结构
     * @param keys
     * @return
     */
    public static String arrayTOIn(String[] keys){
        String result = "";
        for (int i = 0 ;i<keys.length ;i++){
            result += keys[i];

            if (i+1 < keys.length){
                result+=",";
            }
        }

        result = "('"+result+"')";

        return result;
    }
}
