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

import android.content.Context;

import java.util.List;

import fz.cache.core.weiget.Storage;
import fz.cache.db.CacheManageDao;
import fz.cache.db.DataInfo;
import fz.cache.db.StringUtil;

/**
 * 项目名称: z
 * 类描述：
 * 创建时间:2019/1/7 11:52
 *
 * @author tanping
 */
public class DBStorage implements Storage {


    Context context;

    public DBStorage(Context context) {
        CacheManageDao.getInstance(context);
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean put(DataInfo dataInfo) {
        CacheManageDao.getInstance(context).saveOrUpdate(dataInfo);
        return true;
    }

    @Override
    public DataInfo get(String key) {
        DataInfo info = new DataInfo();
        info.key = key;
        return get(info);
    }

    @Override
    public DataInfo get(DataInfo dataInfo) {
        return CacheManageDao.getInstance(context).queryOnlyOne(dataInfo);
    }

    @Override
    public DataInfo lpop(DataInfo dataInfo) {
        String orderBy = DataInfo.TIMESTAMP +" ASC";
        String limit = "1";
        List<DataInfo> dataInfos = CacheManageDao.getInstance(context).query(dataInfo,orderBy,limit,null);
        if (StringUtil.isNotEmpty(dataInfos)){
            CacheManageDao.getInstance(context).deleteAll(dataInfos.get(0));
            return dataInfos.get(0);
        }
        return null;
    }

    @Override
    public DataInfo lrpop(DataInfo dataInfo) {
        String orderBy = DataInfo.TIMESTAMP +" DESC";
        String limit = "1";
        List<DataInfo> dataInfos = CacheManageDao.getInstance(context).query(dataInfo,orderBy,limit,null);
        if (StringUtil.isNotEmpty(dataInfos)){
            CacheManageDao.getInstance(context).deleteAll(dataInfos.get(0));
            return dataInfos.get(0);
        }
        return null;
    }

    @Override
    public List<DataInfo> getAll(DataInfo dataInfo) {
        return CacheManageDao.getInstance(context).query(dataInfo);
    }



    @Override
    public int delete(DataInfo dataInfo) {
        return CacheManageDao.getInstance(context).deleteAll(dataInfo);
    }



    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public int count(DataInfo dataInfo) {
        return CacheManageDao.getInstance(context).getCount(dataInfo);
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public List<DataInfo> groups() {
        DataInfo dataInfo = new DataInfo();
        dataInfo.group = null;
        dataInfo.type = -1;
        List<DataInfo> dataInfos = CacheManageDao.getInstance(context).query(dataInfo,null,null,DataInfo.GROUP);

        return dataInfos;
    }
}
