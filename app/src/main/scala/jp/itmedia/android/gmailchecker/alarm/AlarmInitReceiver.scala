package jp.itmedia.android.gmailchecker.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * アップデートアラームの再設定を行う
 */
class AlarmInitReceiver extends BroadcastReceiver {

  override def onReceive(context: Context, intent: Intent) {
    // アラームの実行
    val alarmUtil = new AlarmUtil(context)
    alarmUtil.setAlarm

  }
}
