package fz.cache.monitor.group;
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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fz.cache.FineCache;
import fz.cache.core.GsonParser;
import fz.cache.core.weiget.Parser;
import fz.cache.db.DataInfo;
import fz.cache.db.StringUtil;
import fz.cache.monitor.MonitorAdapter;
import fz.cache.monitor.MonitorBean;
import fz.cache.monitor.R;

import static fz.cache.core.GsonSerializer.DELIMITER;
import static fz.cache.monitor.MonitorBean.HEADER;
import static fz.cache.monitor.MonitorBean.ITEM_LIST;
import static fz.cache.monitor.MonitorBean.ITEM_MAP;
import static fz.cache.monitor.MonitorBean.ITEM_VALUE;

/**
 * 项目名称: z
 * 类描述：
 * 创建时间:2019/1/8 16:22
 *
 * @author tanping
 */
public class MonitorActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    MonitorAdapter adapter;

    Parser parser = new GsonParser(new com.google.gson.Gson());
    String group;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = getIntent().getStringExtra("group");
        setTitle("组： " +group);


        setContentView(R.layout.fine_cache_activity_main_manitor);
        recyclerView = findViewById(R.id.recyclerview);


        List<MonitorBean> monitorBeans = new ArrayList<>();
        listAys(monitorBeans);
        mapAys(monitorBeans);

        objectAys(monitorBeans);

        adapter = new MonitorAdapter(new ArrayList<MonitorBean>(monitorBeans));
        //限制
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }


    public void objectAys(List monitorBeans){
        List<DataInfo> objs = FineCache.getAll(group,DataInfo.OBJ);

        MonitorBean bean = new MonitorBean();
        bean.type = HEADER ;
        bean.value = "所有的对象组("+objs.size()+")";
        monitorBeans.add(bean);
        for (DataInfo dataInfo :objs){
            bean = new MonitorBean();
            bean.type = ITEM_VALUE;
            bean.key = dataInfo.key ;
            try {
                bean.value =parser.toJson(FineCache.dataDecode(dataInfo));
                String[] infos = dataInfo.cls.split(DELIMITER);

                if (StringUtil.isNotEmpty(infos[1])){
                    if (infos[2].equals(DataInfo.LIST+"")){
                        bean.key = bean.key +" ; type: List" ;
                    }else if (infos[2].equals(DataInfo.HASH+"")){
                        bean.key = bean.key +" ; type: Map" ;
                    }else if (infos[2].equals(DataInfo.SET+"")){
                        bean.key = bean.key +" ; type: Set" ;
                    }
                }else {
                    bean.key = bean.key +" ; type:"+infos[0];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            monitorBeans.add(bean);
        }
    }


    public void listAys(List monitorBeans){
        List<DataInfo> objs = FineCache.getAll(group,DataInfo.LIST);

        Set<String> list = new HashSet<>();
        for (DataInfo dataInfo:objs){
            list.add(dataInfo.name);
        }


        MonitorBean bean = new MonitorBean();
        bean.type = HEADER ;
        bean.value = "所有的List组("+list.size()+")";
        monitorBeans.add(bean);


        for (String string : list){
            bean = new MonitorBean();
            bean.type = ITEM_LIST ;
            bean.value = string;
            monitorBeans.add(bean);
        }
    }

    public void mapAys(List monitorBeans){
        List<DataInfo> objs = FineCache.getAll(group,DataInfo.HASH);

        Set<String> list = new HashSet<>();
        for (DataInfo dataInfo:objs){
            list.add(dataInfo.name);
        }

        MonitorBean bean = new MonitorBean();
        bean.type = HEADER ;
        bean.value = "所有的MAP组("+list.size()+")";
        monitorBeans.add(bean);

        for (String string : list){
            bean = new MonitorBean();
            bean.type = ITEM_MAP ;
            bean.value = string;
            monitorBeans.add(bean);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MonitorBean monitorBean = (MonitorBean) adapter.getData().get(position);
        if (monitorBean.type == ITEM_LIST){
            Intent intent = new Intent(this,DetailActivity.class);
            intent.putExtra("list",monitorBean.value.toString());
            intent.putExtra("group",group);

            startActivity(intent);
        }else if (monitorBean.type == ITEM_MAP){
            Intent intent = new Intent(this,DetailActivity.class);
            intent.putExtra("map",monitorBean.value.toString());
            intent.putExtra("group",group);
            startActivity(intent);
        }

    }
}
