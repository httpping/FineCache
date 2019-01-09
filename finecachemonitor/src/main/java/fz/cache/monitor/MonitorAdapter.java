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

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static fz.cache.monitor.MonitorBean.HEADER;
import static fz.cache.monitor.MonitorBean.ITEM;
import static fz.cache.monitor.MonitorBean.ITEM_LIST;
import static fz.cache.monitor.MonitorBean.ITEM_MAP;
import static fz.cache.monitor.MonitorBean.ITEM_VALUE;

/**
 * 项目名称: z
 * 类描述：
 * 创建时间:2019/1/8 16:45
 *
 * @author tanping
 */
public class MonitorAdapter extends BaseMultiItemQuickAdapter<MonitorBean,BaseViewHolder> {


    public MonitorAdapter(List<MonitorBean> data) {
        super(data);
        addItemType(ITEM,R.layout.fine_cache_item_group);
        addItemType(HEADER,R.layout.fine_cache_item_header);
        addItemType(ITEM_VALUE,R.layout.fine_cache_item_value);
        addItemType(ITEM_LIST,R.layout.fine_cache_item_group);
        addItemType(ITEM_MAP,R.layout.fine_cache_item_group);

    }

    @Override
    protected void convert(BaseViewHolder helper, MonitorBean item) {

        if (item.getItemType() == ITEM_VALUE){
            helper.setText(R.id.tv_content, "key : " + item.key);
            helper.setText(R.id.tv_value, "value : " + item.value);
        }
        else {
            helper.setText(R.id.tv_content, item.value.toString());
        }
    }


}
