// Based on the code from https://www.codenameone.com/blog/tip-intercept-incoming-sms-on-android.html
package org.hacksugar.smsmouse

import android.content.*
import android.os.Bundle
import android.telephony.*
import android.util.Log
import java.lang.Exception


abstract class SMSListener: BroadcastReceiver() {
    @Override
    fun onRecieve(cntxt: Context, intent: Intent) {
        // Code executed when an SMS is recieved
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECIEVED")) {
            val bundle: Bundle? = intent.extras
            // Array to hold SMS messages
            var messages: Array<SmsMessage>? = null
            // Make sure bundle is not empty when converting PDU to SMS message
            if (bundle != null) {
                // Try to get PDUs and get SMS message
                try {
                    val pdus: Array<Object> = bundle.get("pdus") as Array<Object>
                    messages = Array<SmsMessage>(pdus.size)
                    for (i in pdus.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        val msgBody = messages[i].messageBody
                        SMSCallback.SMSRecieved(msgBody)
                    }
                } catch (e: Exception) {
                    Log.e("SMSListener","Try/catch failed.",e)
                    SMSCallback.recieveError(e)
                }
            }
        }
    }
}