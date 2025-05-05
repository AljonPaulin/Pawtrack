 package com.example.pawtrack

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.pawtrack.ui.theme.PawtrackTheme
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.DogViewModel
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Toast

 class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel : AuthViewModel by viewModels()
        val dogViewModel : DogViewModel by viewModels()

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS),
                111)
        }
        else
            receiveMsg()

        setContent {
            PawtrackTheme {
               PawtrackNavigation(
                   authViewModel = authViewModel, dogViewModel = dogViewModel){
                       number, message ->
                        sendSms(number, message)
               }
            }
        }
    }

     override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<out String>,
         grantResults: IntArray,
         deviceId: Int
     ) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
         if (requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
             receiveMsg()
         }
     }

     private fun receiveMsg() {
         val br = object: BroadcastReceiver(){
             override fun onReceive(context: Context?, intent: Intent?) {
                 for(sms in Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                     val sender = sms.originatingAddress
                     val message = sms.displayMessageBody

                     // Replace with your actual sender number
                     if (sender == "+639850758067" && message.contains("GLOC:")) {
                         //sendSms("+639850758067", "ako simcard")
                         Toast.makeText(context, "From $message", Toast.LENGTH_LONG).show()

                         // You can also log or process message here
                         Log.d("SmsReceiver", "Filtered SMS: $message")
                     }
                 }
             }
         }
         registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
     }

     private fun sendSms(phoneNumber: String, message: String) {
         val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
             val subscriptionId = SubscriptionManager.getDefaultSmsSubscriptionId()
             getSystemService(SmsManager::class.java).createForSubscriptionId(subscriptionId)
         } else {
             @Suppress("DEPRECATION")
             SmsManager.getDefault()
         }

         smsManager.sendTextMessage(phoneNumber, null, message, null, null)
     }
 }










