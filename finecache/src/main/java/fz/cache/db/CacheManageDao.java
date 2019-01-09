package fz.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 生气了缓存存储 - 可序列化
 * 
 * @author tanping
 * 
 */
public class CacheManageDao implements Serializable {

	public static final String TAG = "CacheManageDao";
	
	private static CacheManageDao instance;
	private SQLiteDatabase db;

	private CacheManageDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
		System.out.println("db----->" + db);
	}

	public synchronized static CacheManageDao getInstance(Context context) {
		if (instance == null) {
			instance = new CacheManageDao(context);
		}
		return instance;
	}

	/**
	 * 插入或
	 * 
	 * @param dataInfo
	 * @return
	 */
	public synchronized long saveOrUpdate(DataInfo dataInfo) {

		if (dataInfo == null) {
			return 0;
		} else {
			 
			ContentValues cvs = new ContentValues();
			cvs.put(DataInfo.GROUP,dataInfo.group);
			cvs.put(DataInfo.NAME,dataInfo.name);
			cvs.put(DataInfo.KEY,dataInfo.key);
			cvs.put(DataInfo.VALUE,dataInfo.value);
			cvs.put(DataInfo.CLS,dataInfo.cls);
			cvs.put(DataInfo.TYPE,dataInfo.type);
			cvs.put(DataInfo.TIMESTAMP,dataInfo.timestamp);
			cvs.put(DataInfo.EXPIRE,dataInfo.expire);

			db.beginTransaction();
			DataInfo  exists =  queryOnlyOne(dataInfo);
			// 传送文件重复插入bug处理方法
			if (exists == null) {
				System.out.println("PushNoticeBean insert..." +exists);
				long backId = db.insert(DataInfo.TABLE_NAME, null, cvs);
				db.setTransactionSuccessful();
				db.endTransaction();
				return backId;
			} else {
				try {
					int count = db.update(DataInfo.TABLE_NAME, cvs, DataInfo.ID +"=?" , new String[] {
									exists.id+"" });
					if (count != 1){
						new IllegalArgumentException("值不唯一数据出错");
					}
					db.setTransactionSuccessful();
					db.endTransaction();
					return count;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return 0;
	}
	

	/**
	 * 是否存在
	 * 
	 * @return
	 */
	public List<DataInfo> query(DataInfo bean) {

		Cursor mCursor =db.query(DataInfo.TABLE_NAME,createdColum(),condtion(bean,false),condtionValue(bean),null,null,null);
		List<DataInfo> dataInfos = new ArrayList<>();

		while (mCursor.moveToNext()) {
			DataInfo info = getObject(mCursor);
			dataInfos.add(info);
		}
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}

		return dataInfos;
	}

	public List<DataInfo> queryInKey(DataInfo bean) {

		return  null;
	}
		/**
         *
         *
         * @return
         */
	public List<DataInfo> query(DataInfo bean,String orderBy,String limit,String groupBy) {

		Cursor mCursor =db.query(DataInfo.TABLE_NAME,createdColum(),condtion(bean,false),condtionValue(bean),groupBy,null,orderBy,limit);
		List<DataInfo> dataInfos = new ArrayList<>();

		while (mCursor.moveToNext()) {
			DataInfo info = getObject(mCursor);
			dataInfos.add(info);
		}
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}

		return dataInfos;
	}


	/**
	 * 查找一个
	 * @param bean
	 * @return
	 */
	public DataInfo queryOnlyOne(DataInfo bean){
		List<DataInfo> result = query(bean);

		if (StringUtil.isNotEmpty(result)){
			return result.get(0);
		}
		return  null;
	}


	/**
	 * 清理会话
	 * @return
	 */
	public int deleteAll(DataInfo bean) {
		Log.d("dele", "chat:del");
		String sql ="delete from " +DataInfo.TABLE_NAME ;

		if (bean!=null){
			sql += " where " + condtion(bean,false);
		}

		int count = db.delete(DataInfo.TABLE_NAME,condtion(bean,false),condtionValue(bean));
		return count;
	}


 
 
	/**
	 * 查询总条数
	 * 
	 * @return
	 */
	public int getCount(DataInfo bean) {
		String sql ="select count(*)from " +DataInfo.TABLE_NAME ;
		/*if (bean!=null){
			sql += " where " + condtion(bean);
		}*/
		if (bean.group == null){
			bean.group = DataInfo.DEFAULT_GROUP;
		}
		Cursor cursor = db.query(DataInfo.TABLE_NAME,new String[]{"count(*)"},condtion(bean,false),condtionValue(bean),null,null,null);

///		Cursor cursor = db.rawQuery(sql, condtionValue(bean));
		cursor.moveToFirst();
		int count = (int) cursor.getLong(0);
		cursor.close();
		Log.d(" pushBean count", count + "");
		return count;
	}


	/**
	 * 组装条件
	 * @param dataInfo
	 * @return
	 */
	private String condtion(DataInfo dataInfo,boolean keys){
		String sql = "";

		if (StringUtil.isNotEmpty(dataInfo.group)){
			sql += DataInfo.GROUP + "= ? " +" ";
		}

		if (dataInfo.id != 0){
			sql += " and " + DataInfo.ID + "= ? "+" ";
		}

		if (StringUtil.isNotEmpty(dataInfo.name)){
			sql += " and " + DataInfo.NAME + "= ? " +" ";
		}
		if (StringUtil.isNotEmpty(dataInfo.key)){
			  sql += " and " + DataInfo.KEY + "= ? " + " ";
		}
		if (dataInfo.type>=0) {
			sql += " and " + DataInfo.TYPE + " = ? " + " ";
		}

		sql = sql.trim();
		if(sql.startsWith("and")){
			sql =sql.substring(3,sql.length());
		}

		return sql;
	}

	private String[] condtionValue(DataInfo dataInfo){
		List<String> values = new ArrayList();
		if (StringUtil.isNotEmpty(dataInfo.group)){
			values.add(dataInfo.group);
		}

		if (dataInfo.id != 0){
			values.add(dataInfo.id+"");
		}

		if (StringUtil.isNotEmpty(dataInfo.name)){
			values.add(dataInfo.name);
		}
		if (StringUtil.isNotEmpty(dataInfo.key)){
			values.add(dataInfo.key);
		}

		if (dataInfo.type>=0) {
			values.add(dataInfo.type + "");
		}

		/*if (StringUtil.isNotEmpty(dataInfo.value)){
			sql += " and " + DataInfo.NAME + " like %"+dataInfo.value +"% ";
		}*/

		return  values.toArray(new String[values.size()]);
	}


	// 赋值
	public DataInfo getObject(Cursor c) {
		DataInfo bean = new DataInfo();
		bean.id = c.getLong(c.getColumnIndex(DataInfo.ID));
		bean.group = c.getString(c.getColumnIndex(DataInfo.GROUP));
		bean.name = c.getString(c.getColumnIndex(DataInfo.NAME));
		bean.key = c.getString(c.getColumnIndex(DataInfo.KEY));
		bean.value = c.getString(c.getColumnIndex(DataInfo.VALUE));
		bean.type = c.getInt(c.getColumnIndex(DataInfo.TYPE));
		bean.cls = c.getString(c.getColumnIndex(DataInfo.CLS));
		bean.expire = c.getInt(c.getColumnIndex(DataInfo.EXPIRE));
		bean.timestamp = c.getLong(c.getColumnIndex(DataInfo.TIMESTAMP));
		return bean;


	}


	/**
	 * 创建查询集合
	 * @return
	 */
	private String[] createdColum(){
		List<String> col = new ArrayList<String>();
		col.add(DataInfo.ID);
		col.add(DataInfo.GROUP);
		col.add(DataInfo.NAME);
		col.add(DataInfo.KEY);
		col.add(DataInfo.VALUE);
		col.add(DataInfo.TYPE);
		col.add(DataInfo.CLS);
		col.add(DataInfo.EXPIRE);
		col.add(DataInfo.TIMESTAMP);

		return col.toArray(new String[col.size()]);
	}
	 

}
