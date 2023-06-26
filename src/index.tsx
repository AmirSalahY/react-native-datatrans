import { NativeModules, Platform } from 'react-native';

type Options = {
  savedPaymentMethods?: {
    alias: string;
    paymentMethod: string;
    ccNumber?: string;
    expiryMonth?: number;
    expiryYear?: number;
    cardHolder?: string;
  }[];
  isTesting: boolean;
  isUseCertificatePinning: boolean;
  appCallbackScheme?: string;
};

const LINKING_ERROR =
  `The package 'react-native-datatrans' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Datatrans = NativeModules.Datatrans
  ? NativeModules.Datatrans
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: any, b: any): Promise<any> {
  return Datatrans.multiply(a, b);
}

export function initDatatrans(
  mobileToken: string,
  options?: Options
): Promise<any> {
  return Datatrans.initDatatrans(mobileToken, options);
}
