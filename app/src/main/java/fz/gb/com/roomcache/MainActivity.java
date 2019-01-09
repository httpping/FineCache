package fz.gb.com.roomcache;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fz.cache.FineCache;
import fz.cache.monitor.MainManitorActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        FineCache.hput("aa","ss");
//        FineCache.lput("aa","ss");

        testObject();
        testList();;
        testAsyncList();
        testMap();

        Intent intent = new Intent(this,MainManitorActivity.class);
        startActivity(intent);
    }


    public void testMap(){

        String name = "map";
        String group= "test-map";

        FineCache.hput(name,"abc","123");
        FineCache.hput(name,"str","tp");

        FineCache.hput(group,name,"test",0,"hello world");
        FineCache.hput(group,name,"name",0,"tp");

        FineCache.hput(name,"goods",new GoodsBean("gooosss"));

        Map map = FineCache.hget(group,name);
        map = FineCache.hget(group,name,"test","name");

        Log.d("ss",map+"");
    }


    public void testObject(){
        FineCache.put("abc","hello world");

        HashMap<String,Object> map = new HashMap<>();
        map.put("123",1);
        map.put("f",2.3);
        map.put("str","str");
        //map

        FineCache.put("map",map);

        FineCache.put("object-group","nihao",0,map);

        FineCache.put("test-object",new GoodsBean());

        String world =  FineCache.get("abc");
        HashMap map1 =  FineCache.get("map");
        Toast.makeText(this, "" +world, Toast.LENGTH_SHORT).show();

    }



    @SuppressLint("CheckResult")
    public void testAsyncList(){

        final String group = "async-group";
        final String name = "list-xx";

        FineCache.lAsyncPush(group,name,0,"12").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        List list = FineCache.lget(group, name);
                        Log.d("xx", list.size() + ""+aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                } );

        List list = FineCache.lget(group, name);
        Log.d("xx", list + "");
    }

    /**
     * 测试ListAPI
     */
    public void testList(){

        String name = "test-list";
        String group = "abc";
        List list = new ArrayList();

        list.add(1);
        list.add(2);

        FineCache.lremove(name);

        FineCache.lpush(name,1);
        FineCache.lpush(name,2);
        FineCache.lpush(name,3);

        //5s debug  卡5s 会自动消失
        FineCache.lpush(name,5,50);
        FineCache.lpush(name,100,55);

        FineCache.lpushUniqe(name,4);
        FineCache.lpushUniqe(name,4);

        FineCache.lpush(name,new GoodsBean());

        GoodsBean gooos = new GoodsBean();
        gooos.name = "taobao";

        FineCache.lpush(name,gooos);


        list = FineCache.lget(name);

        Object obj = FineCache.lpop(name);
        obj = FineCache.lpop(name);
        obj = FineCache.lrpop(name);


        int len = FineCache.llen(name);
        Toast.makeText(this,""+list.size(),Toast.LENGTH_LONG).show();
        list = FineCache.lget(name);

        FineCache.lpush(group,name,1);
        FineCache.lpush(group,name,"group"+group);

        list = FineCache.lget(group,name);
        len = FineCache.llen(group,name);

        Toast.makeText(this,""+list.size(),Toast.LENGTH_LONG).show();



        name ="old-goods";
        FineCache.lremove(group,name);
        FineCache.lpush(group,name,new GoodsBean("衣服"));
        FineCache.lpush(group,name,new GoodsBean("裤子"));
        FineCache.lpush(group,name,new GoodsBean("内衣"));
        FineCache.lpush(group,name,new GoodsBean("帽子"));
        FineCache.lpush(group,name,new GoodsBean("测试等"));
        FineCache.lrpop(group,name);
        FineCache.lpop(group,name);

        name ="压测";
        FineCache.lremove(group,name);
//        testLpush(group,name);
//        testLpush(group,name);
//        testLpush(group,name);
//        testLpush(group,name);
//        testLpush(group,name);

        for (int a= 0; a< 1;a++) {
//            FineCache.lremove(group,name);
            testLpush(group,name);
            testLPop(group,name);
            testLGet(group,name);
            int leng = FineCache.llen(group,name);
            Log.d("fine-cache", "长度：" + (leng));
        }

    }


    public void testLpush(String group,String name){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            FineCache.lpush(group, name, new GoodsBean("帽子"));
        }
        long end = System.currentTimeMillis();
        Log.d("fine-cache", "push花费：" + (end - start));

    }

    public void testLPop(String group,String name){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            FineCache.lpop(group, name);
        }
        long end = System.currentTimeMillis();
        Log.d("fine-cache", "pop花费：" + (end - start));

    }

    public void testLGet(String group,String name){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            FineCache.lget(group, name);
        }
        long end = System.currentTimeMillis();
        Log.d("fine-cache", "lget花费：" + (end - start));

    }

}


