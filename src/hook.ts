import { useState, useEffect } from "react";
import BatteryInterface from "./interface";
import { BatteryStatus } from "./internal/types";

export default () => {
  const [batteryStatus, setBatteryStatus] = useState<BatteryStatus>({
    isCharging: false,
    chargePercent: 0,
  });

  useEffect(() => {
    return BatteryInterface.addEventListener(setBatteryStatus);
  }, []);

  return batteryStatus;
};
