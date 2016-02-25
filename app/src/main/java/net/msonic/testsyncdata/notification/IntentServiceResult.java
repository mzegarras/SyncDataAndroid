package net.msonic.testsyncdata.notification;

/**
 * Created by User01 on 24/02/2016.
 */
public class IntentServiceResult {

    int mResult;
    String mResultValue;

    IntentServiceResult(int resultCode, String resultValue) {
        mResult = resultCode;
        mResultValue = resultValue;
    }

    public int getResult() {
        return mResult;
    }

    public String getResultValue() {
        return mResultValue;
    }

}
