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


package fz.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import fz.cache.db.DataInfo;
import fz.cache.room.dao.DataInfoDao;


@Database(entities = {DataInfo.class}, version = 1)
public abstract class FineDatabase extends RoomDatabase {

    private static FineDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "basic-sample-db";

    public abstract DataInfoDao dataInfoDao();

    public static FineDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (FineDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    */
/**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     *//*

    private static FineDatabase buildDatabase(final Context appContext ) {
        return Room.databaseBuilder(appContext, FineDatabase.class, DATABASE_NAME).build();
    }

    */
/**
     * Check whether the database already exists and expose it via {@link #()}
     *//*

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
        }
    }



}
*/
