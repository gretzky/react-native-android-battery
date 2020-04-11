package io.github.gretzky.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import com.facebook.react.bridge.ReactApplicationContext

class BroadcastReceiver(reactContext: ReactApplicationContext) : RNBatteryReceiver(reactContext) {

    private val mBatteryBroadcastReceiver : BatteryBroadcastReceiver

    init {
        mBatteryBroadcastReceiver = BatteryBroadcastReceiver()
    }

    override fun register() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        mReactContext.registerReceiver(mBatteryBroadcastReceiver, filter)
        mBatteryBroadcastReceiver.isRegistered = true
        updateAndSendBattery()
    }

    override fun unregister() {
        if (mBatteryBroadcastReceiver.isRegistered) {
            mReactContext.unregisterReceiver(mBatteryBroadcastReceiver)
            mBatteryBroadcastReceiver.isRegistered = false
        }
    }

    private fun updateAndSendBattery() {
        updateBattery(batteryCharging, batteryLevel)
    }

    private inner class BatteryBroadcastReceiver : BroadcastReceiver() {

        var isRegistered = false

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.getAction()
            if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                updateAndSendBattery()
            }
        }
    }
}
