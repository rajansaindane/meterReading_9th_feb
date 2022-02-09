
package com.embeltech.meterreading.injection.module

import android.content.Context
import androidx.room.Room
import com.embeltech.meterreading.data.Config
import com.embeltech.meterreading.data.database.BeaconsDao
import com.embeltech.meterreading.data.database.MeterReadingDB
import com.embeltech.meterreading.data.database.MeterReadingDB.Companion.MIGRATION_1_2
import com.embeltech.meterreading.data.database.MeterReadingDB.Companion.MIGRATION_2_3
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DatabaseModule {

    companion object {
        private const val DATABASE = Config.DATABASE_NAME
    }

    @Provides
    @Named(DATABASE)
    fun provideDatabaseName(): String = Config.DATABASE_NAME

    @Provides
    @Singleton
    fun provideBIDb(context: Context, @Named(DATABASE) databaseName: String): MeterReadingDB {
        return Room.databaseBuilder(context, MeterReadingDB::class.java, databaseName)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideBIDao(db: MeterReadingDB): BeaconsDao = db.beaconInstallerDao()
}