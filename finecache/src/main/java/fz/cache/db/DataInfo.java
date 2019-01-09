package fz.cache.db;
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

import android.support.annotation.IntDef;

/**
 * 项目名称: z
 * 类描述：
 * 创建时间:2019/1/5 16:58
 *
 * @author tanping
 */

public class DataInfo {

    /**
     * 默认组
     */
    public static final String DEFAULT_GROUP = "def";

    public static final String TABLE_NAME = "fine_cache";
    public static final String ID = "id";
    public static final String KEY = "_key";
    public static final String VALUE = "_value";
    public static final String CLS = "_cls";
    public static final String TYPE = "_type";
    public static final String NAME = "_name";
    public static final String GROUP = "_group";
    public static final String TIMESTAMP = "_timestamp";
    public static final String EXPIRE = "_expire";


    public static final int OBJ = 0;
    public static final int LIST = 1;
    public static final int HASH = 2;
    public static final int SET = 3;

    @IntDef({OBJ,LIST,HASH})
    public @interface Type{}


    public long id;

    /**
     * 数据key
     */
    public String key ;

    /**
     * 数据值
     */
    public String value;

    /**
     * 数据类型  class
     */
    public String cls;

    /**
     * list map object
     */
    public @Type int type ;


    /**
     * list map 使用
     */
    public String name;


    /**
     * 组，例如 分用户，分临时和永久
     */
    public String group = DEFAULT_GROUP;

    /**
     * 更新时间
     */
    public long timestamp = System.currentTimeMillis();

    /**
     * 过期秒数
     */
    public int expire;



    public boolean keys;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
