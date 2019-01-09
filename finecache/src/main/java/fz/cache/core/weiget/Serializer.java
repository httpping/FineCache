package fz.cache.core.weiget;

import fz.cache.db.DataInfo;

public interface Serializer {

  /**
   *  序列化
   *
   * @return serialized string
   */
  <T> DataInfo serialize(DataInfo dataInfo,T value);

  /**
   * 反序列化
   *
   * @return original object
   */
   <T> T deserialize(DataInfo dataInfo);


}