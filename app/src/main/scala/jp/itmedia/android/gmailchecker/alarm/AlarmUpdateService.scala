package jp.itmedia.android.gmailchecker.alarm

import android.app.IntentService
import android.content.Intent
import jp.itmedia.android.gmailchecker.GmailCheck

/**
 * 定時処理を行う
 */
object AlarmUpdateService {
  final val TAG  = "AlarmUpdateService"
}

class AlarmUpdateService extends IntentService(AlarmUpdateService.TAG) {

  protected def onHandleIntent(intent: Intent) {

    new GmailCheck(getApplicationContext).execute()

    val alarmUtil: AlarmUtil = new AlarmUtil(getApplicationContext)
    alarmUtil.setAlarm
  }
}
