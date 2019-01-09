package fz.cache;
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

import android.content.Context;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;

import fz.cache.core.DefaultEncryption;
import fz.cache.core.FineFacadeImpl;
import fz.cache.core.GsonSerializer;
import fz.cache.core.DBStorage;
import fz.cache.core.weiget.Encryption;
import fz.cache.core.weiget.FacadeApi;
import fz.cache.core.weiget.Serializer;
import fz.cache.core.weiget.Storage;
import fz.cache.db.DataInfo;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableAll;
import io.reactivex.internal.operators.flowable.FlowableCache;

/**
 * 项目名称: z
 * 类描述：
 * 创建时间:2019/1/7 11:44
 *
 * @author tanping
 */
public class FineCache {

    Encryption encryption;
    Serializer serializer;
    Storage storage;
    FacadeApi apiImpl;

    private static FineCache fineCache;


    public static <T> T get(String key) {
        return get(DataInfo.DEFAULT_GROUP, key, null);
    }

    public static <T> T get(String key, T defaultValue) {
        return get(DataInfo.DEFAULT_GROUP, key, defaultValue);
    }

    public static <T> T get(String group, String key, T defaultValue) {
        return getInstance().apiImpl.get(group, key, defaultValue);
    }


    public static <T> void put(String key, T value) {
        put(DataInfo.DEFAULT_GROUP, key,0, value);
    }

    public static <T> void put(String key, T value,int expire) {
        put(DataInfo.DEFAULT_GROUP, key,0, value);
    }

    public static <T> void put(String group, String key,int expire, T value) {
        getInstance().apiImpl.put(group, key, expire, value);
    }



    /// list 或 set 数据格式相关 API

    public static <T> void lpush(String name, T value) {
        lpush(DataInfo.DEFAULT_GROUP, name, value);
    }

    public static <T> void lpush(String name, int expire,T value) {
        lpush(DataInfo.DEFAULT_GROUP, name, expire, value);
    }

    public static <T> void lpush(String group, String name, T value) {
        getInstance().apiImpl.lpush(group, name,0, value);
    }

    public static <T> void lpush(String group, String name,int expire, T value) {
        getInstance().apiImpl.lpush(group, name,expire, value);
    }

    public static <T> Flowable<Boolean> lAsyncPush(String name, int expire,T value) {
        return lAsyncPush(DataInfo.DEFAULT_GROUP, name, expire, value);
    }

    public static <T> Flowable<Boolean> lAsyncPush(final String group,final String name,final int expire,final T value) {

       return Flowable.just(group).map(new Function<String, Boolean>() {
            @Override
            public Boolean apply(String s) throws Exception {
                try {
                    getInstance().apiImpl.lpush(group, name,expire, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });

//        getInstance().apiImpl.lpush(group, name,expire, value);
    }

    /**
     * value 唯一 相当于 set， 用lpush 和 lpushUniqe 存同样的数据会重复。
     *
     * @param name
     * @param value
     * @param <T>
     */
    public static <T> void lpushUniqe(String name, T value) {
        lpushUniqe(DataInfo.DEFAULT_GROUP, name, 0,value);
    }
    public static <T> void lpushUniqe(String name,int expire, T value) {
        lpushUniqe(DataInfo.DEFAULT_GROUP, name, expire,value);
    }

    public static <T> void lpushUniqe(String group, String name,int expire, T value) {
        getInstance().apiImpl.lpushUniqe(group, name,expire, value);
    }

    public static List lget(String name) {
        return lget(DataInfo.DEFAULT_GROUP, name);
    }

    public static List lget(String group, String name) {
        return getInstance().apiImpl.lget(group, name);
    }

    public static Flowable<List> lAsyncGet( final String name){
        return lAsyncGet(DataInfo.DEFAULT_GROUP,name);
    }
    public static Flowable<List> lAsyncGet(final String group, final String name) {
        return Flowable.just(group).map(new Function<String, List>() {

            @Override
            public List apply(String s) throws Exception {
                try {
                    return getInstance().apiImpl.lget(group, name);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });

    }

    public static <T> T lpop(String name) {
        return lpop(DataInfo.DEFAULT_GROUP, name);
    }

    public static <T> T lpop(String group, String name) {
        return getInstance().apiImpl.lpop(group, name);
    }

    public static <T> T lrpop(String name) {
        return lrpop(DataInfo.DEFAULT_GROUP, name);
    }

    public static <T> T lrpop(String group, String name) {
        return getInstance().apiImpl.lrpop(group, name);
    }

    public static boolean isLexist(String name) {
        return isLexist(DataInfo.DEFAULT_GROUP, name);
    }

    public static boolean isLexist(String group, String name) {
        return getInstance().apiImpl.isLexist(group, name);
    }

    public static int lremove(String name) {
        return lremove(DataInfo.DEFAULT_GROUP, name);
    }

    public static int lremove(String group, String name) {
        return getInstance().apiImpl.lremove(group, name);
    }

    public static int lremoveAll() {
        return getInstance().apiImpl.lremoveAll();
    }

    public static int llen(String name) {
        return llen(DataInfo.DEFAULT_GROUP, name);
    }

    public static int llen(String group, String name) {
        return getInstance().apiImpl.llen(group, name);
    }

    // map 相关API

    public static <T> void hput(String name, String key, T value) {
        hput(DataInfo.DEFAULT_GROUP, name, key, 0,value);
    }
    public static <T> void hput(String name, String key,int expire, T value) {
        hput(DataInfo.DEFAULT_GROUP, name, key,expire, value);
    }

    public static <T> void hput(String group, String name, String key,int expire, T value) {
        getInstance().apiImpl.hput(group, name, key,expire,value);
    }

    public static Map<String, ?> hget(String name) {
        return hget(DataInfo.DEFAULT_GROUP, name);
    }

    public static Map<String, ?> hget(String group, String name) {
        return  getInstance().apiImpl.hget(group, name);
    }

    public static Map<String, ?> hget(String group, String name, String... key) {
        return getInstance().apiImpl.hget(group, name, key);
    }

    public static boolean isHexist(String name) {
        return isHexist(DataInfo.DEFAULT_GROUP, name);
    }

    public static boolean isHexist(String group, String name) {
        return isHexist(DataInfo.DEFAULT_GROUP, name, null);
    }

    public static boolean isHexist(String group, String name, String key) {
        return getInstance().apiImpl.isHexist(group, name, key);
    }

    public static int hremove(String name) {
        return hremove(DataInfo.DEFAULT_GROUP, name);
    }

    public static int hremove(String group, String name) {
        return hremove(DataInfo.DEFAULT_GROUP, name, null);
    }

    public static int hremove(String group, String name, String key) {
        return getInstance().apiImpl.hremove(group, name, key);
    }

    public static int hlen(String name) {
        return hlen(DataInfo.DEFAULT_GROUP, name);
    }

    public static int hlen(String group, String name) {
        return getInstance().apiImpl.hlen(group, name);
    }


    @Deprecated
    public static List<DataInfo> groups(){
        return getInstance().apiImpl.groups();
    }

    /**
     * 监控使用
     * @param group
     * @return
     */
    @Deprecated
    public static List getAll(String group,int type) {
        return getInstance().apiImpl.getAll(group,type);
    }


    @Deprecated
    public static  <T> T dataDecode(DataInfo dataInfo){
        return getInstance().apiImpl.dataDecode(dataInfo);
    }


    private static FineCache getInstance() {
        return fineCache;
    }


    /**
     * build FineCache
     */
    public static class Builder {

        Context context;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            fineCache = new FineCache();
        }

        /**
         * 创建builder
         *
         * @param context
         * @return
         */
        public synchronized static Builder created(Context context) {
            Builder builder = new Builder(context);
            return builder;
        }

        public Builder encryption(Encryption encryption) {
            fineCache.encryption = encryption;
            return this;
        }

        public Builder storage(Storage storage) {
            fineCache.storage = storage;
            return this;
        }

        public Builder serializer(Serializer serializer) {
            fineCache.serializer = serializer;
            return this;
        }

        public Builder facadeImpl(FacadeApi api) {
            fineCache.apiImpl = api;
            return this;
        }


        public FineCache build() {

            if (fineCache.serializer == null) {
                fineCache.serializer = new GsonSerializer();
            }
            if (fineCache.storage == null) {
                fineCache.storage = new DBStorage(context);
            }
            if (fineCache.encryption == null) {
                fineCache.encryption = new DefaultEncryption();
            }

            if (fineCache.apiImpl == null) {
                fineCache.apiImpl = new FineFacadeImpl(fineCache.storage, fineCache.serializer, fineCache.encryption);
            }

            return fineCache;
        }


    }


}
