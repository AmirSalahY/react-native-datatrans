import * as React from 'react';

import { StyleSheet, View, Text, Pressable } from 'react-native';
import { initDatatrans } from 'react-native-datatrans';

export default function App() {
  return (
    <View style={styles.container}>
      <Pressable
        onPress={() => {
          console.log('pressed');
          try {
            initDatatrans('46bf14359573b2b07ff6b998cfab28ca0333a1fa166bf225', {
              savedPaymentMethods: [
                // {
                //   alias: '7LHXscqwAAEAAAGI9oezceCm2487ADfV',
                //   paymentMethod: 'MASTER_CARD',
                //   expiryMonth: 0o6,
                //   expiryYear: 2025,
                // },
                // {
                //   alias: '7LHXscqwAAEAAAGI9yoGMFhnhCVDAL3l',
                //   paymentMethod: 'VISA',
                //   expiryMonth: 0o6,
                //   expiryYear: 2025,
                // },
              ],
              isTesting: true,
              isUseCertificatePinning: true,
              appCallbackScheme: 'com.datatransexample.dtpl',
            })
              .then((e) => console.log('-------------', e))
              .catch((err) =>
                console.error('----error from process catch----', err)
              );
          } catch (error) {
            console.error('----error from catch ----', error);
          }
        }}
      >
        <Text>Click me</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
