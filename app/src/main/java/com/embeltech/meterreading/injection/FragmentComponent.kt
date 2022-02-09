
package com.embeltech.meterreading.injection

import com.embeltech.meterreading.injection.annotations.PerActivity
import com.embeltech.meterreading.injection.module.FragmentModule
import com.embeltech.meterreading.ui.adduser.AddUserFragment
import com.embeltech.meterreading.ui.billing.BillingFragment
import com.embeltech.meterreading.ui.device.DeviceDetailsFragment
import com.embeltech.meterreading.ui.statistics.GraphFragment
import com.embeltech.meterreading.ui.report.ReportFragment
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [(FragmentModule::class)])
interface FragmentComponent {
    fun inject(deviceDetailsFragment: DeviceDetailsFragment)
    fun inject(deviceDetailsFragment: ReportFragment)
    fun inject(billingFragment: BillingFragment)
    fun inject(graphFragment: GraphFragment)
    fun inject(addUserFragment: AddUserFragment)

}