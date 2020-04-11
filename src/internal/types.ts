export interface BatteryInterface {
  isCharging: boolean;
  chargePercent: number;
}

export interface BatteryComponentProps {
  state: BatteryInterface;
  color?: string;
}

export type BatteryStatus = BatteryInterface | null;
export type BatteryChangeHandler = (state: BatteryStatus) => void;
export type BatterySubscription = () => void;

export interface BatteryModule {
  getCurrentState: () => Promise<BatteryStatus>;
  addListener: (type: string, handler: Function) => void;
  removeListeners: (type: string, handler: Function) => void;
}
