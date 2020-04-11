# react-native-android-battery

Native Module for getting the battery status on Android devices.

## Installation

**Note:** This package requires React Native >=0.60.

Install with npm/yarn.

```bash
npm install react-native-android-battery --save
// or
yarn add react-native-android-battery
```

Since this package requires RN 0.60 or higher, this package will be linked automatically. If you run into an issue, you can try `react-native link react-native-android-battery`.

## Usage

This module exposes 2 possible methods for getting the battery status: an object interface and a react hook.

```js
import React from "react";
import {
  BatteryInterface,
  useBatteryStatus,
} from "react-native-android-battery";

// the interface methods for event listening and getting the current battery state
BatteryInterface.addEventListener(); // add your listener logic
BatteryInterface.getCurrentState(); // returns an object containing the battery charge percent and whether or not the battery is charging

// you can also use the hook, which already has the event listener hooked up
const Component = () => {
  const { batteryStatus } = useBatteryStatus();

  useEffect(() => {
    console.log(batteryStatus.isCharging);
    console.log(batteryStatus.chargePercent);
  }, [batteryStatus]);

  return <View />;
};
```

## API

The interface has 2 methods:

- **`addEventListener(listener): void`** - event listener for capturing battery status changes
- **`getCurrentState(): object`** - gets current battery status (object containing `isCharging` and `chargePercent`)

The hook sets up the event listener on component mount, and returns the battery status object.
