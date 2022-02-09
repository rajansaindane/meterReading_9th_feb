package com.embeltech.meterreading.injection

import com.embeltech.meterreading.injection.annotations.PerActivity
import com.embeltech.meterreading.injection.module.ActivityModule
import com.embeltech.meterreading.ui.device.CreateDeviceActivity
import com.embeltech.meterreading.ui.home.HomeActivity
import com.embeltech.meterreading.ui.login.LoginActivity
import com.embeltech.meterreading.ui.scanbeacon.ScanBeaconActivity
import com.embeltech.meterreading.ui.main.MainActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [ActivityModule::class])

interface ActivityComponent {
    fun inject(scanBeaconActivity: ScanBeaconActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(createDeviceActivity: CreateDeviceActivity)
    fun inject(mainActivity: MainActivity)
}