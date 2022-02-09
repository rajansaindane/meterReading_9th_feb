package com.embeltech.meterreading.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.embeltech.meterreading.injection.ViewModelFactory
import com.embeltech.meterreading.ui.billing.BillingViewModel
import com.embeltech.meterreading.ui.device.CreateDeviceViewModel
import com.embeltech.meterreading.ui.device.DeviceDetailsViewModel
import com.embeltech.meterreading.ui.home.HomeScreenViewModel
import com.embeltech.meterreading.ui.login.LoginViewModel
import com.embeltech.meterreading.ui.report.ReportViewModel
import com.embeltech.meterreading.ui.scanbeacon.ScannedBeaconViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
open class ViewModelModule {

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(ScannedBeaconViewModel::class)
    fun provideScannedBeaconViewModel(viewModel: ScannedBeaconViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(HomeScreenViewModel::class)
    fun provideHomeScreenViewModel(viewModel: HomeScreenViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(LoginViewModel::class)
    fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(CreateDeviceViewModel::class)
    fun provideCreateDeviceViewModel(viewModel: CreateDeviceViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(DeviceDetailsViewModel::class)
    fun getDeviceDetailsViewModel(viewModel: DeviceDetailsViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(ReportViewModel::class)
    fun provideReportViewModel(viewModel: ReportViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(BillingViewModel::class)
    fun provideBillingViewModel(viewModel: BillingViewModel): ViewModel = viewModel
}