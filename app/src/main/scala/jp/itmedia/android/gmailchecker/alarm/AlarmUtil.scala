package jp.itmedia.android.gmailchecker.alarm

import android.app.{AlarmManager, PendingIntent}
import android.content.{Context, Intent}
import android.util.Log
import java.util.Calendar

class AlarmUtil(val appContext: Context) {

  /**
   * アラームを設定
   */
  def setAlarm {

    val intent = new Intent(appContext, classOf[AlarmUpdateService])

    val pendingIntent = PendingIntent.getService(appContext, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager]

    //val currentTime = System.currentTimeMillis
    val cal = Calendar.getInstance()
    //cal.setTimeInMillis(currentTime)
    cal.add(Calendar.MINUTE, 30)
    cal.set(Calendar.SECOND, 0)

    Log.e("time", "%04d%02d%02d%02d%02d%02d".format(
      cal.get(Calendar.YEAR),
      (cal.get(Calendar.MONTH) + 1),
      cal.get(Calendar.DAY_OF_MONTH),
      cal.get(Calendar.HOUR_OF_DAY),
      cal.get(Calendar.MINUTE),
      cal.get(Calendar.SECOND)))

    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent)
  }
}
