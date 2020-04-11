import { NativeEventSubscription } from "react-native";
import BatteryInterface from "../interface";
import { BatteryChangeHandler, BatteryStatus } from "./types";

const _subscriptions = new Set<BatteryChangeHandler>();
let _nativeEventSubscription: NativeEventSubscription | null = null;
let _latestState: BatteryStatus = null;

function _listenerHandler(state: BatteryStatus): void {
  _latestState = state;
  _subscriptions.forEach((handler): void => handler(state));
}

function add(
  handler: BatteryChangeHandler,
  latestOnListen: boolean = true
): void {
  _subscriptions.add(handler);

  if (latestOnListen) {
    if (_latestState) {
      handler(_latestState);
    } else {
      BatteryInterface.getCurrentState().then((state: BatteryStatus): void => {
        _latestState = state;
        handler(_latestState);
      });
    }
  }

  if (_subscriptions.size > 0 && !_nativeEventSubscription) {
    /// @ts-ignore
    _nativeEventSubscription = BatteryInterface.eventEmitter.addListener(
      "battery",
      _listenerHandler
    );
  }
}

function clear(): void {
  _subscriptions.clear();

  if (_nativeEventSubscription) {
    _nativeEventSubscription.remove();
    _nativeEventSubscription = null;
  }
}

function remove(handler: any): void {
  _subscriptions.delete(handler);

  if (_subscriptions.size === 0 && _nativeEventSubscription) {
    _nativeEventSubscription.remove();
    _nativeEventSubscription = null;
  }
}

export default {
  add,
  remove,
  clear,
};
