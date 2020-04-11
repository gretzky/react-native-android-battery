import { NativeModules, NativeEventEmitter } from "react-native";
import Listener from "./internal/listener";
import {
  BatteryModule,
  BatteryStatus,
  BatteryChangeHandler,
  BatterySubscription,
} from "./internal/types";

const BatteryNativeModule: BatteryModule = NativeModules.RNBattery;

if (!BatteryNativeModule) {
  throw new Error(`@react-native-device-utils/battery: Native Module is null. Steps to fix:
• Run \`react-native-link @react-native-device-utils/battery\` in the project root
• Rebuild and run the app
• Manually link the library if necessary
• If you're getting this error while testing, you didn't read the README. #shame
If none of these fix the issue, open an issue on Github: https://github.com/gretzky/react-native-device-utils`);
}

let nativeEventEmitter: NativeEventEmitter | null = null;

const BatteryInterface = {
  ...BatteryNativeModule,
  get eventEmitter(): NativeEventEmitter | null {
    if (!nativeEventEmitter) {
      /// @ts-ignore
      nativeEventEmitter = new NativeEventEmitter(BatteryNativeModule);
    }

    return nativeEventEmitter;
  },

  addEventListener(listener: BatteryChangeHandler): BatterySubscription {
    Listener.add(listener);
    return (): void => {
      Listener.remove(listener);
    };
  },

  getCurrentState(): Promise<BatteryStatus> {
    return BatteryNativeModule.getCurrentState();
  },
};

export default BatteryInterface;
