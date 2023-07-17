import * as React from 'react';

import { StyleSheet, View, Text, Pressable, TextInput } from 'react-native';
import { initDatatrans } from 'react-native-datatrans';

export default function App() {
  const [mobileToken, setMobileToken] = React.useState();
  const [aliasData, setAliasData] = React.useState('');
  return (
    <View style={styles.container}>
      <TextInput
        style={{ borderColor: '#999', borderWidth: 1 }}
        placeholder="please add mobile token"
        onChangeText={setMobileToken}
      />
      <Pressable
        // disabled={mobileToken === '' ? true : false}
        onPress={() => {
          console.log('pressed', mobileToken);
          try {
            initDatatrans(
              mobileToken ?? '3a6fc1c899409fd369c9d53f7bc942180333abfb05425b86',
              {
                savedPaymentMethods: [
                  // {
                  //   alias: '7LHXscqwAAEAAAGI9oezceCm2487ADfV',
                  //   paymentMethod: 'MASTER_CARD',
                  //   expiryMonth: 0o6,
                  //   expiryYear: 2025,
                  // },
                  {
                    alias: '7LHXscqwAAEAAAGJK1hrciBD8pkaAEh1',
                    paymentMethod: 'VISA',
                    expiryMonth: 0o6,
                    expiryYear: 2025,
                  },
                ],
                isTesting: true,
                isUseCertificatePinning: true,
                appCallbackScheme: 'com.datatransexample.dtpl',
              }
            )
              .then((e) => {
                console.log('-------------', e.data);
                setAliasData(JSON.stringify(e.data));
              })
              .catch((err) => {
                setAliasData('error : ' + err);

                console.error('----error from process catch----', err);
              });
          } catch (error) {
            setAliasData('error : ' + error);
            console.error('----error from catch ----', error);
          }
        }}
      >
        <Text>Click me</Text>
      </Pressable>
      <Text selectable={true} style={{ fontSize: 20, color: 'black' }}>
        {aliasData ?? 'Data will appear here'}
      </Text>
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
