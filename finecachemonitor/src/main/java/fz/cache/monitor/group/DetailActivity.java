package fz.cache.monitor.group;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fz.cache.FineCache;
import fz.cache.core.GsonParser;
import fz.cache.core.weiget.Parser;
import fz.cache.monitor.MonitorAdapter;
import fz.cache.monitor.MonitorBean;
import fz.cache.monitor.R;

import static fz.cache.monitor.MonitorBean.ITEM;
import static fz.cache.monitor.MonitorBean.ITEM_VALUE;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MonitorAdapter adapter;
    Parser parser = new GsonParser(new com.google.gson.Gson());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String list  = getIntent().getStringExtra("list");
        String map  = getIntent().getStringExtra("map");
        String group = getIntent().getStringExtra("group");

        if (list !=null){
            setTitle("List： " +list);
        }

        if (map !=null){
            setTitle("Map： " +map);
        }
        setContentView(R.layout.fine_cache_activity_main_manitor);

        List<MonitorBean> monitorBeans = new ArrayList<>();

        if (list !=null){

            List result = FineCache.lget(group,list);
            for (Object obj :result){
                MonitorBean bean = new MonitorBean();
                bean.type = ITEM ;
                bean.value = parser.toJson(obj);
                monitorBeans.add(bean);
            }

        }else {
            Map map1 =  FineCache.hget(group,map);
            Iterator it = map1.keySet().iterator();
            while (it.hasNext()){
                String key = it.next().toString();
                String value = parser.toJson(map1.get(key));
                MonitorBean bean = new MonitorBean();
                bean.type = ITEM_VALUE;
                bean.key = key;
                bean.value = value;
                monitorBeans.add(bean);

            }
        }

        recyclerView = findViewById(R.id.recyclerview);

        adapter = new MonitorAdapter(monitorBeans);
        //限制
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }
}
