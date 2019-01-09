package fz.cache.monitor;
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

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 项目名称: z
 * 类描述：
 * 创建时间:2019/1/8 16:45
 *
 * @author tanping
 */
public class MonitorBean implements MultiItemEntity {

    public static final int HEADER = 1;
    public static final int ITEM = 0;
    public static final int ITEM_VALUE = 2;
    public static final int ITEM_LIST = 3;
    public static final int ITEM_MAP = 4;



    public Object value;
    public int type ;
    public String key;


    public Object getItemValue() {
        return value;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
