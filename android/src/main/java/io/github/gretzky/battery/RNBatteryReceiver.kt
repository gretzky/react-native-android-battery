package io.github.gretzky.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.modules.core.DeviceEventManagerModule

abstract class RNBatteryReceiver(val reactContext: ReactApplicationContext) {

    val mReactContext: ReactApplicationContext

    private var mBatteryCharging: Boolean = false
    private var mBatteryLevel: Int = 0

    val batteryCharging: Boolean
        get() {
            val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            // this registerReceiver fn registers null, which is ensures that the ACTION_BATTERY_CHANGE
            // intent doesn't fire off whenever android feels like it (which is the initial/intended behavior)
            // but because we're also registering this battery change in the main broadcast receiver, it's overridden
            // TODO: rework this to prevent battery intent from firing every few seconds
            val batteryStatus = reactContext.getApplicationContext().registerReceiver(null, filter)
            val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            return status == BatteryManager.BATTERY_STATUS_CHARGING
        }

    val batteryLevel: Int
        get() {
            val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryIntent = reactContext.getApplicationContext().registerReceiver(null, filter)
            val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            var batteryLevel = level / scale.toDouble() // get the closest possible value of the actual battery level
            batteryLevel *= 100 // percentitize!
            return Math.rint(batteryLevel).toInt() // get the nearest int
        }

    init {
        mReactContext = reactContext as ReactApplicationContext

        mBatteryCharging = batteryCharging
        mBatteryLevel = batteryLevel
    }

    internal abstract fun register()

    internal abstract fun unregister()

    fun sendChangeEvent() {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("battery", createEventMap())
    }

    fun updateBattery(isCharging: Boolean, level: Int) {
        val chargeStatusChanged = isCharging != mBatteryCharging
        val chargePercentChanged = level != mBatteryLevel

        if (chargeStatusChanged || chargePercentChanged) {
            mBatteryCharging = isCharging
            mBatteryLevel = level
            sendChangeEvent()
        }
    }

    fun createEventMap(): WritableMap {
        var event: WritableMap = WritableNativeMap()

        event.putInt("chargePercent", mBatteryLevel)
        event.putBoolean("isCharging", mBatteryCharging)

        return event
    }
}
