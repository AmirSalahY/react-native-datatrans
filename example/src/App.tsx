import * as React from 'react';

import { StyleSheet, View, Text, Pressable } from 'react-native';
import { initDatatrans, multiply } from 'react-native-datatrans';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    multiply(3, 7).then((t) => {
      setResult(t);
      console.log('-----', t);
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Pressable
        onPress={() => {
          console.log('pressed');
          try {
            initDatatrans('d11d0aa39e2ef1f74d1023cb1b6018d60333593c3f2c4387', {
              savedPaymentMethods: [
                {
                  alias: '7LHXscqwAAEAAAGI9oezceCm2487ADfV',
                  paymentMethod: 'MASTER_CARD',
                  expiryMonth: 0o6,
                  expiryYear: 2025,
                },
                {
                  alias: '7LHXscqwAAEAAAGI9yoGMFhnhCVDAL3l',
                  paymentMethod: 'VISA',
                  expiryMonth: 0o6,
                  expiryYear: 2025,
                },
              ],
              isTesting: true,
              isUseCertificatePinning: true,
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
