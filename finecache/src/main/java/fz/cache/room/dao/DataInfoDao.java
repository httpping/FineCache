/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*


package fz.cache.room.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import fz.cache.db.DataInfo;

@Dao
public interface DataInfoDao {

    @Query("SELECT * FROM fine_cache where `key` = :key")
    List<DataInfo> get(String key);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void put(DataInfo dataInfo);


    @Query("SELECT * FROM fine_cache where `group` = :group and  `name` = :name")
    List<DataInfo> lget(String group,String name);


}
*/
