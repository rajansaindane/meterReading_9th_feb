
package com.embeltech.meterreading.injection.module

import com.embeltech.meterreading.data.repository.BIDataSource
import com.embeltech.meterreading.data.repository.local.BILocalDataSource
import com.embeltech.meterreading.data.repository.remote.BIRemoteDataSource
import com.embeltech.meterreading.injection.annotations.Local
import com.embeltech.meterreading.injection.annotations.Remote
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Local
    fun providesLocalDataSource(localDataSource: BILocalDataSource): BIDataSource = localDataSource

    @Provides
    @Singleton
    @Remote
    fun providesRemoteDataSource(remoteDataSource: BIRemoteDataSource): BIDataSource = remoteDataSource

}