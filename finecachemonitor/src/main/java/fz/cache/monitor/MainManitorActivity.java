package fz.cache.monitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import fz.cache.FineCache;
import fz.cache.db.DataInfo;
import fz.cache.monitor.group.MonitorActivity;

public class MainManitorActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    MonitorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fine_cache_activity_main_manitor);
        recyclerView = findViewById(R.id.recyclerview);

        setTitle("监控台");

        List<DataInfo> dataInfos =  FineCache.groups();

        List<MonitorBean> monitorBeans = new ArrayList<>();
        MonitorBean bean = new MonitorBean();

        bean.type = 1 ;
        bean.value = "所有的组("+dataInfos.size()+")";
        monitorBeans.add(bean);
        for (DataInfo dataInfo :dataInfos){
            bean = new MonitorBean();
            bean.type = 0;
            bean.value = dataInfo.group;
            monitorBeans.add(bean);
        }

        adapter = new MonitorAdapter(new ArrayList<MonitorBean>(monitorBeans));
        //限制
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MonitorBean monitorBean = (MonitorBean) adapter.getData().get(position);
        if (monitorBean.type == 0){
            Intent intent = new Intent(this,MonitorActivity.class);
            intent.putExtra("group",monitorBean.value.toString());
            startActivity(intent);
        }
    }
}
