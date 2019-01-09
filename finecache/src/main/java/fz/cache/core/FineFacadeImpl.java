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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fz.cache.core.weiget.Encryption;
import fz.cache.core.weiget.FacadeApi;
import fz.cache.core.weiget.Serializer;
import fz.cache.core.weiget.Storage;
import fz.cache.db.DataInfo;
import fz.cache.db.StringUtil;

import static fz.cache.db.DataInfo.HASH;
import static fz.cache.db.DataInfo.LIST;
import static fz.cache.db.DataInfo.OBJ;

/**
 * 项目名称: z
 * 类描述： fine cache api 实现
 * 创建时间:2019/1/7 13:36
 *
 * @author pzj
 */
public class FineFacadeImpl implements FacadeApi {

    Encryption encryption;
    Serializer serializer;
    Storage storage;

    public FineFacadeImpl(Storage storage, Serializer serializer, Encryption encryption){
        this.storage = storage;
        this.serializer =serializer;
        this.encryption = encryption;
    }

    @Override
    public <T> void put(String group, String key,int expire ,T value) {
        DataInfo dataInfo = createDataInfo(group,null,key,OBJ,expire,value);

        storage.put(dataInfo);
    }

    @Override
    public <T> T get(String group, String key, T defaultValue) {

        DataInfo dataInfo = createDataInfo(group,null,key,OBJ,0,null);

        dataInfo =  storage.get(dataInfo);
        T t = dataDecode(dataInfo);
        if (t == null){
            return defaultValue;
        }
        return t;
    }

    @Override
    public  List getAll(String group,@DataInfo.Type int type) {
        DataInfo dataInfo = createDataInfo(group,null,null,type,0,null);
        List<DataInfo> dataInfos =  storage.getAll(dataInfo);
       /* List result = new ArrayList();
        for (DataInfo bean : dataInfos){
            result.add(dataDecode(bean));
        }*/
        return dataInfos;
    }

    @Override
    public boolean isExist(String group, String key) {
        DataInfo dataInfo = createDataInfo(group,null,key,OBJ,0,null);
        dataInfo =  storage.get(dataInfo);
        return dataDecode(dataInfo)!=null;
    }


    @Override
    public int remove(String group, String key) {
        DataInfo dataInfo = new DataInfo();
        dataInfo.group =group;
        dataInfo.key = key;
        return storage.delete(dataInfo);
    }


    @Override
    public <T> void hput(String group, String name, String key, int expire,T value) {
        DataInfo dataInfo = createDataInfo(group,name,key,HASH,expire,value);
        storage.put(dataInfo);
    }

    @Override
    public Map<String, Object> hget(String group, String name, String... keys) {
        DataInfo dataInfo = createDataInfo(group,name,null,HASH,0,null);
        Map map = new HashMap();
        List<DataInfo> result = null;

        if (keys != null && keys.length>0) {
            result = new ArrayList<>();
            for (String key : keys) {
                dataInfo.key = key;
                DataInfo bean = storage.get(dataInfo);
                if (bean !=null) {
                    result.add(bean);
                }
            }
        }else{
            result = storage.getAll(dataInfo);
        }

        for (DataInfo bean : result) {
            map.put(bean.key, dataDecode(bean));
        }

        return map;
    }

    @Override
    public boolean isHexist(String group, String name, String key) {
        return false;
    }

    @Override
    public int hremove(String group, String name, String key) {
        DataInfo dataInfo = createDataInfo(group,name,key,HASH,0,null);

        int count = storage.delete(dataInfo);

        return count;
    }


    @Override
    public int hlen(String group, String name) {
        DataInfo dataInfo = createDataInfo(group,name,null,HASH,0,null);
        return storage.count(dataInfo);
    }


    private <T> DataInfo createDataInfo(String group,String name,String key,int type,int expire,T value){
        DataInfo dataInfo = new DataInfo();
        dataInfo.group =group;
        dataInfo.name = name;
        dataInfo.key = key;
        dataInfo.type = type;
        dataInfo.expire = expire;
        if (value!=null) {
            dataEncode(dataInfo, value);
        }

        return dataInfo;
    }


//// ======================== list api ======================


    @Override
    public <T> void lpush(String group, String name,int expire, T value) {
        DataInfo dataInfo = createDataInfo(group,name, UUID.randomUUID().toString(),LIST,expire,value);
        storage.put(dataInfo);
    }



    @Override
    public <T> void lpushUniqe(String group, String name, int expire,T value) {
        DataInfo dataInfo = createDataInfo(group,name, null,LIST,expire,value);
        //用 value md5值作为唯一标识，防止数据重复
        dataInfo.key = MD5Util.MD516(dataInfo.value);
        storage.put(dataInfo);
    }


    @Override
    public List lget(String group, String name) {
        DataInfo dataInfo = createDataInfo(group,name, null,LIST,0,null);

        List<DataInfo> dataInfos =  storage.getAll(dataInfo);
        List result = new ArrayList();
        for (DataInfo bean : dataInfos){
           Object obj = dataDecode(bean);
           if (obj!=null) {
               result.add(obj);
           }
        }

       return result;
    }



    @Override
    public <T> T lpop(String group, String name) {
        DataInfo dataInfo = createDataInfo(group,name, null,LIST,0,null);

        dataInfo = storage.lpop(dataInfo);
        return dataDecode(dataInfo);
    }


    @Override
    public <T> T lrpop(String group, String name) {
        DataInfo dataInfo = createDataInfo(group,name, null,LIST,0,null);

        dataInfo = storage.lrpop(dataInfo);
        return dataDecode(dataInfo);
    }


    @Override
    public boolean isLexist(String group, String name) {
        DataInfo dataInfo = createDataInfo(group,name, null,LIST,0,null);

        return storage.get(dataInfo) != null;
    }


    @Override
    public int lremove(String group, String name) {
        DataInfo dataInfo = createDataInfo(group,name, null,LIST,0,null);
        return storage.delete(dataInfo);
    }

    @Override
    public int lremoveAll() {
        DataInfo dataInfo = createDataInfo(null,null, null,LIST,0,null);
        return storage.delete(dataInfo);
    }


    @Override
    public int llen(String group, String name) {
        DataInfo dataInfo = createDataInfo(group,name, null,LIST,0,null);
        return storage.count(dataInfo) ;
    }

    @Override
    public List<DataInfo> groups() {
        return storage.groups();
    }


    /**
     * data 数据转化 包含序列化和加解密处理
     * @param dataInfo
     * @param value
     * @param <T>
     * @return
     */
    private <T> DataInfo dataEncode(DataInfo dataInfo,T value){
        //序列化
        serializer.serialize(dataInfo,value);
        try {
            //加密
            dataInfo.value = encryption.encrypt(dataInfo.value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataInfo;
    }

    /**
     * data 数据转换 包含序列化和加解密处理
     * @param dataInfo
     * @param <T>
     * @return
     */
    @Override
    public <T> T dataDecode(DataInfo dataInfo){

        try {
            long start = System.currentTimeMillis();
            //过期
            if (dataInfo.expire != 0 && (start-dataInfo.timestamp)/1000>dataInfo.expire){
                storage.delete(dataInfo);
                return  null;
            }

            dataInfo.value = encryption.decrypt(dataInfo.value);
            T t = serializer.deserialize(dataInfo);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                storage.delete(dataInfo);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
        return  null;
    }


}
