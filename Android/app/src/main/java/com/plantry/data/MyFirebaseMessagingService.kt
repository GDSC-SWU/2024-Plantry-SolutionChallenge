package com.plantry.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.plantry.R
import com.plantry.presentation.auth.ui.SignInActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 토큰 값 따로 저장
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
    }


    // 메시지 수신
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: " + remoteMessage.from)

        if (remoteMessage.data.isNotEmpty()) {
            Log.i("바디", remoteMessage.data["body"].toString())
            Log.i("타이틀", remoteMessage.data["title"].toString())
            sendNotification(remoteMessage)
        } else {
            Log.i("수신에러 : ", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
            Log.i("data값 :", remoteMessage.data.toString())
        }
    }

    // 알림 생성 (아이콘, 알림 소리 등)
    private fun sendNotification(remoteMessage: RemoteMessage) {
        val uniId = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, SignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        for (key in remoteMessage.data.keys) {
            intent.putExtra(key, remoteMessage.data[key])
        }

        val pendingIntent = PendingIntent.getActivity(
            this, uniId, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        // 알림 채널 이름
        val channelId = getString(R.string.app_name)

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentTitle(remoteMessage.notification?.body.toString())
            .setContentText(remoteMessage.notification?.title.toString())
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Android SDK 26 이상에서는 notification을 만들 때 channel을 지정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "plantry",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

            // 알림 생성
            notificationManager.notify(uniId, notificationBuilder.build())
        }

    }
}