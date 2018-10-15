package coolgroup.com.aline.model;

import android.content.Context;
import android.util.Log;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows the app to make voice and video calls over Internet Protocol, to an instance of
 * a User Class. Thi User needs some unique identifier (currently a phone number), and then this
 * class manages the call with them; in doing so it also fulfils the role of an iVOIPNotifier.
 */
public class SinchCommunicator implements iVOIPCommunicator, iVOIPNotifier {

    private SinchClient mSinchClient;
    private Call currentCall = null;

    // TODO: fill out these strings in a meaningful way
    private String mApplicationKey = "3a862a0e-a7f7-40d6-bb1a-0ed817743686";
    private String mApplicationSecret = "PLPQh5VGBUG4EFfpgdFMSA==";
    private String mEnvironmentHost = "sandbox.sinch.com";

    // List of Observers to notify
    private ArrayList<iVOIPListener> listeners = new ArrayList<>();

    public void addListener(iVOIPListener listener){
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(iVOIPListener listener) {
        if (listeners.contains(listener)){
            listeners.remove(listener);
        }
    }

    public SinchCommunicator(Context context, User currentUser){

        mSinchClient = Sinch.getSinchClientBuilder()
                .context(context)
                .userId(currentUser.getuID())
                .applicationKey(mApplicationKey)
                .applicationSecret(mApplicationSecret)
                .environmentHost(mEnvironmentHost)
                .build();

        mSinchClient.setSupportCalling(true);
        mSinchClient.startListeningOnActiveConnection();

        mSinchClient.start();

        mSinchClient.getCallClient().addCallClientListener(new CallClientListener() {
            @Override
            public void onIncomingCall(CallClient callClient, Call incomingCall) {
                currentCall = incomingCall;
                currentCall.answer();

                currentCall.addCallListener( new ObserverCallListener() );
            }
        });
    }

    @Override
    public void startCall(User user) {
        Log.d("SinchComm", "User id: " + user.getuID());
        hangUpCall();

        currentCall = mSinchClient.getCallClient().callUser(user.getuID());

        currentCall.addCallListener( new ObserverCallListener() );
    }

    @Override
    public void hangUpCall(){
        if (currentCall != null) {
            currentCall.hangup();
            currentCall = null;
        }
    }

    private class ObserverCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            for(iVOIPListener l : listeners) {
                l.onCallEnded();
            }
            currentCall = null;
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            for(iVOIPListener l : listeners) {
                l.onCallEstablished();
            }
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            //call is ringing
            for(iVOIPListener l : listeners) {
                l.onCallRinging();
            }
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }
}
