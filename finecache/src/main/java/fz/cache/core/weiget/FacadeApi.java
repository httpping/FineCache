package fz.cache.core.weiget;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fz.cache.db.DataInfo;

/**
 * 项目名称: z
 * 类描述：  Facade API 定义
 * 创建时间:2019/1/7 13:35
 *
 * @author tp
 */
public interface FacadeApi {

    /**
     * put object
     * @param group
     * @param key
     * @param expire
     * @param value
     * @param <T>
     */
    <T> void put(String group, String key,int expire, T value);

    <T> T get(String group, String key, T defaultValue);

    /**
     * 获取所有对象，损耗性能，监控时候才调用
     * @param group 组
     * @param type  类型
     * @return list
     */
    public  List getAll(String group,@DataInfo.Type int type) ;

    boolean isExist(String group, String key);

    int remove(String group, String key);


    <T> void hput(String group, String name, String key,int expire, T value);

    Map<String, Object> hget(String group, String name, String... key);

    boolean isHexist(String group, String name, String key);

    int hremove(String group, String name, String key);

    int hlen(String group, String name);

    //list set used

    <T> void lpush(String group, String name,int expire,T value);

    <T> void lpushUniqe(String group, String name,int expire, T value);

    List lget(String group, String name);

    <T> T lpop(String group, String name);

    <T> T lrpop(String group, String name);

    boolean isLexist(String group, String name);

    int lremove(String group, String name);

    int lremoveAll();

    /**
     * 过期的数据不排除，只做数据统计
     * @param group
     * @param name
     * @return
     */
    int llen(String group, String name);

     <T> T dataDecode(DataInfo dataInfo);

    /**
     * 用户监控
     */
    List<DataInfo> groups();
}
