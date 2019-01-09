package fz.cache.core;
/*

                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG

*/

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fz.cache.core.weiget.Parser;
import fz.cache.core.weiget.Serializer;
import fz.cache.db.DataInfo;

/**
 * 项目名称: z
 * 类描述： 序列化
 * 创建时间:2019/1/7 11:53
 *
 * @author tanping
 */
public class GsonSerializer implements Serializer {

    /**
     * 分隔符
     */
    public static final String DELIMITER = "@";
    private static final String TAG = "fine-cache";

    Parser parser = new GsonParser(new Gson());

    @Override
    public <T> DataInfo serialize(DataInfo dataInfo,T value) {
        if (value == null || dataInfo== null){
            return dataInfo;
        }

        //hash 中key的名称
        String keyClassName = "";
        //hash list 中 value的类型
        String valueClassName = "";
         int dataType ;
        if (List.class.isAssignableFrom(value.getClass())) {
            List<?> list = (List<?>) value;
            if (!list.isEmpty()) {
                keyClassName = list.get(0).getClass().getName();
            }
            dataType = DataInfo.LIST;
        } else if (Map.class.isAssignableFrom(value.getClass())) {
            dataType = DataInfo.HASH;
            Map<?, ?> map = (Map) value;
            if (!map.isEmpty()) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    keyClassName = entry.getKey().getClass().getName();
                    valueClassName = entry.getValue().getClass().getName();
                    break;
                }
            }
        } else if (Set.class.isAssignableFrom(value.getClass())) {
            Set<?> set = (Set<?>) value;
            if (!set.isEmpty()) {
                Iterator<?> iterator = set.iterator();
                if (iterator.hasNext()) {
                    keyClassName = iterator.next().getClass().getName();
                }
            }
            dataType = DataInfo.SET;
        } else {
            dataType = DataInfo.OBJ;
            keyClassName = value.getClass().getName();
        }


        String cls =  keyClassName + DELIMITER +
                valueClassName + DELIMITER +
                dataType ;


        dataInfo.cls = cls;
        dataInfo.value  =  parser.toJson(value);

        return dataInfo;
    }

    @Override
    public <T> T deserialize(DataInfo dataInfo) {
        try {
            return fromString(dataInfo.value,dataInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public <T> T fromString(String value, DataInfo info) throws Exception {
        if (value == null) {
            return null;
        }
        //0、key 类型  1、值类型 3、整体类型
        String[] infos = info.cls.split(DELIMITER);

        //整体类型
        int type = Integer.parseInt(infos[2]);


        Class<?> keyType = null;
        Class<?> valueType = null;

        String firstElement = infos[0];
        if (firstElement != null && firstElement.length() != 0) {
            try {
                keyType = Class.forName(firstElement);
            } catch (ClassNotFoundException e) {
                Log.d(TAG,"Serializer -> " + e.getMessage());
            }
        }

        String secondElement = infos[1];
        if (secondElement != null && secondElement.length() != 0) {
            try {
                valueType = Class.forName(secondElement);
            } catch (ClassNotFoundException e) {
                Log.d(TAG," Serializer -> " + e.getMessage());
            }
        }

        switch (type) {
            case DataInfo.OBJ:
                return toObject(value, keyType);
            case DataInfo.LIST:
                return toList(value, keyType);
            case DataInfo.HASH:
                return toMap(value, keyType, valueType);
            case DataInfo.SET:
                return toSet(value, keyType);
            default:
                return null;
        }
    }



    private <T> T toObject(String json, Class<?> type) throws Exception {
        return parser.fromJson(json, type);
    }

    @SuppressWarnings("unchecked")
    private <T> T toList(String json, Class<?> type) throws Exception {
        if (type == null) {
            return (T) new ArrayList<>();
        }
        List<T> list = parser.fromJson(
                json,
                new TypeToken<List<T>>() {
                }.getType()
        );

        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.set(i, (T) parser.fromJson(parser.toJson(list.get(i)), type));
        }
        return (T) list;
    }

    @SuppressWarnings("unchecked")
    private <T> T toSet(String json, Class<?> type) throws Exception {
        Set<T> resultSet = new HashSet<>();
        if (type == null) {
            return (T) resultSet;
        }
        Set<T> set = parser.fromJson(json, new TypeToken<Set<T>>() {
        }.getType());

        for (T t : set) {
            String valueJson = parser.toJson(t);
            T value = parser.fromJson(valueJson, type);
            resultSet.add(value);
        }
        return (T) resultSet;
    }

    @SuppressWarnings("unchecked")
    private <K, V, T> T toMap(String json, Class<?> keyType, Class<?> valueType) throws Exception {
        Map<K, V> resultMap = new HashMap<>();
        if (keyType == null || valueType == null) {
            return (T) resultMap;
        }
        Map<K, V> map = parser.fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());

        for (Map.Entry<K, V> entry : map.entrySet()) {
            String keyJson = parser.toJson(entry.getKey());
            K k = parser.fromJson(keyJson, keyType);

            String valueJson = parser.toJson(entry.getValue());
            V v = (V) parser.fromJson(valueJson, valueType);
            resultMap.put(k, v);
        }
        return (T) resultMap;
    }
}
