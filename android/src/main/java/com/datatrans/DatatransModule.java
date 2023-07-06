package com.datatrans;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.datatrans.payment.api.Transaction;
import ch.datatrans.payment.api.TransactionListener;
import ch.datatrans.payment.api.TransactionRegistry;
import ch.datatrans.payment.api.TransactionSuccess;
import ch.datatrans.payment.exception.TransactionException;
import ch.datatrans.payment.paymentmethods.CardExpiryDate;
import ch.datatrans.payment.paymentmethods.PaymentMethodType;
import ch.datatrans.payment.paymentmethods.SavedCard;
import ch.datatrans.payment.paymentmethods.SavedPaymentMethod;

public class DatatransModule extends ReactContextBaseJavaModule {

  public static final String NAME = "Datatrans";
  private static final String E_ACTIVITY_DOES_NOT_EXIST =
    "E_ACTIVITY_DOES_NOT_EXIST";
  private final String CALLBACK_TYPE_SUCCESS = "success";
  private final String CALLBACK_TYPE_ERROR = "error";
  private final String CALLBACK_TYPE_CANCEL = "cancel";
  private Context mActivityContext;
  private JSONArray jsonarray;
  private JSONObject jsonobject;
  private WritableMap mapObj;
  //private Callback mTokenCallback;
  private Promise mPromise;

  DatatransModule(ReactApplicationContext context) {
    super(context);
  }

  @NonNull
  @Override
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }

  @ReactMethod
  public void initDatatrans(
    String mobileToken,
    ReadableMap options,
    final Promise promise
  ) {
    mPromise = promise;
    Activity activity = getCurrentActivity();
    Transaction transaction;
    if (activity == null) {
      WritableMap map = Arguments.createMap();
      map.putString("message", "Activity doesn't exist");
      consumeCallback(E_ACTIVITY_DOES_NOT_EXIST, map);
      return;
    }

    try {
      ReadableArray savedPaymentMethods; //=new ArrayList<>();
      List<SavedPaymentMethod> paymentMethodTypes = new ArrayList<>();

      savedPaymentMethods = options.getArray("savedPaymentMethods");
      Log.d(
        "------ Payment Method: ",
        "------ Payment Method: " + options.getArray("savedPaymentMethods")
      );

      if (savedPaymentMethods != null) {
        for (int i = 0; i < savedPaymentMethods.size(); i++) {
          ReadableMap spm;
          spm = savedPaymentMethods.getMap(i);
          CardExpiryDate ced = new CardExpiryDate(06, 2025);
          SavedCard ct = new SavedCard(
            PaymentMethodType.valueOf(spm.getString("paymentMethod")),
            spm.getString("alias"),
            ced,
            spm.getString("ccNumber"),
            ""
          );
          Log.d(
            "------ Payment Method: ",
            "------ Payment Method: " + savedPaymentMethods.size()
          );
          paymentMethodTypes.add(ct);
        }
        transaction =
          new Transaction(
            mobileToken,
            (List<? extends SavedPaymentMethod>) paymentMethodTypes
          );
      } else {
        transaction = new Transaction(mobileToken);
      }

      TransactionListener transactionListener = new TransactionListener() {
        @Override
        public void onTransactionCancel() {
          WritableMap map = Arguments.createMap();
          WritableMap data = Arguments.createMap();

          map.putMap("data", data);
          map.putString("action", "Cancel");

          Toast
            .makeText(
              getReactApplicationContext(),
              "Cancelled",
              Toast.LENGTH_LONG
            )
            .show();
          mapObj = map;
          consumeCallback(CALLBACK_TYPE_CANCEL, map);
        }

        @Override
        public void onTransactionError(@NotNull TransactionException e) {
          for (int i = 0; i < e.getStackTrace().length; i++) {
            System.err.print(
              "---------error from onTransactionError: " + e.getStackTrace()[i]
            );
          }
          WritableMap map = Arguments.createMap();
          WritableMap data = Arguments.createMap();
          data.putString("transactionId", e.getTransactionId());
          data.putString("message", e.getMessage());
          data.putString(
            "paymentMethodType",
            e.getPaymentMethodType().getIdentifier()
          );
          map.putMap("data", data);
          map.putString("action", "Error");
          Toast
            .makeText(getReactApplicationContext(), "Error", Toast.LENGTH_LONG)
            .show();
          mapObj = map;
          consumeCallback(CALLBACK_TYPE_ERROR, map);
        }

        @Override
        public void onTransactionSuccess(
          @NotNull TransactionSuccess transactionSuccess
        ) {
          WritableMap map = Arguments.createMap();
          WritableMap data = Arguments.createMap();
          Log.d(
            "-----savedPaymentMethod",
            "-----savedPaymentMethod" +
              String.valueOf(transactionSuccess.getSavedPaymentMethod())
          );
          data.putString(
            "-----------savedPaymentMethod: ",
            String.valueOf(transactionSuccess.getSavedPaymentMethod())
          );
          Log.d(
            "----------transactionId",
            "----------transactionId: " + transactionSuccess.getTransactionId()
          );
          data.putString(
            "transactionId",
            transactionSuccess.getTransactionId()
          );
          data.putString("paymentMethodToken", ""); //transactionSuccess.getPaymentMethodToken().toString()
          Log.d(
            "-----paymentMethodType",
            "----------paymentMethodType: " +
              transactionSuccess.getPaymentMethodType().getIdentifier()
          );
          data.putString(
            "paymentMethodType",
            transactionSuccess.getPaymentMethodType().getIdentifier()
          );

          map.putMap("data", data);
          map.putString("action", "Finish");

          Toast
            .makeText(
              getReactApplicationContext(),
              "Success",
              Toast.LENGTH_LONG
            )
            .show();
          mapObj = map;
          consumeCallback(CALLBACK_TYPE_SUCCESS, map);
        }
      };
      transaction.setListener(transactionListener); // this refers to Android's Activity
      transaction.getOptions().setTesting(options.getBoolean("isTesting"));
      transaction
        .getOptions()
        .setAppCallbackScheme(options.getString("appCallbackScheme"));

      transaction
        .getOptions()
        .setUseCertificatePinning(
          options.getBoolean("isUseCertificatePinning")
        );
      TransactionRegistry.INSTANCE.startTransaction(activity, transaction);
    } catch (Exception e) {
      for (int i = 0; i < e.getStackTrace().length; i++) {
        System.err.print("---------error from catch: " + e.getStackTrace()[i]);
      }
      mPromise.reject(e);
    }
    //        return transaction;
  }

  private void consumeCallback(String type, WritableMap map) {
    if (mPromise != null) {
      map.putString("type", type);
      map.putString("provider", "Datatrans");
      Log.d(CALLBACK_TYPE_SUCCESS, "------------sending");
      mPromise.resolve(map);
      mPromise = null;
    }
  }
}
