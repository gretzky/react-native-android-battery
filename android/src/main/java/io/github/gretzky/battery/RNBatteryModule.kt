package io.github.gretzky.battery

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

import io.github.gretzky.battery.RNBatteryBroadcastReceiver
import io.github.gretzky.battery.RNBatteryReceiver

class RNBatteryModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val mBatteryReceiver: RNBatteryReceiver

    override fun getName(): String {
        return "RNBattery"
    }

    init {
        mBatteryReceiver = RNBatteryBroadcastReceiver(reactContext)
    }

    override fun initialize() {
        mBatteryReceiver.register()
    }

    override fun onCatalystInstanceDestroy() {
        mBatteryReceiver.unregister()
    }

    @ReactMethod
    fun getCurrentState(promise: Promise) {
        promise.resolve(mBatteryReceiver.createEventMap())
    }
}
