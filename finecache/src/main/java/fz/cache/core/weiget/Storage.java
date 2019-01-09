package fz.cache.core.weiget;

import java.util.List;

import fz.cache.db.DataInfo;

/**
 * 存储
 */
public interface Storage {

    boolean put(DataInfo dataInfo);

    DataInfo get(String key);

    DataInfo get(DataInfo dataInfo);


    DataInfo lpop(DataInfo dataInfo);

    DataInfo lrpop(DataInfo dataInfo);

    List<DataInfo> getAll(DataInfo dataInfo);


    int delete(DataInfo dataInfo);

    boolean deleteAll();

    int count(DataInfo dataInfo);

    boolean contains(String key);

    List<DataInfo>  groups();
}
