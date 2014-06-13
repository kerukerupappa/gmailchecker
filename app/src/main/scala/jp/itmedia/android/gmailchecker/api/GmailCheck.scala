package jp.itmedia.android.gmailchecker

import android.util.Log
import android.webkit.CookieManager
import java.net.URI
import java.util
import jp.itmedia.android.gmailchecker.utils.SimpleAsyncTask
import org.apache.http.client.methods.HttpGet
import org.apache.http.{HttpStatus, HttpResponse}
import org.apache.http.impl.client.{BasicResponseHandler, DefaultHttpClient}
import scala.util.matching.Regex
import android.content.{Context, Intent}
import android.app.{NotificationManager, Notification, PendingIntent}
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat

/**
 * 定数
 */
object GmailCheck {
  final val URL_GMAIL_ATOM = "https://mail.google.com/mail/feed/atom/"
  final val HEADER_COOKIE = "Cookie"

}

/**
 * メールチェック
 *
 * @param appContext
 */
class GmailCheck(appContext: Context) extends SimpleAsyncTask[Unit, Unit, String] {

  /**
   * バックグラウンド処理
   *
   * @param value
   * @return
   */
  override protected def doInBackground(value: Unit): String = {
    var feeds = "";

    try {
      val client = new DefaultHttpClient
      val cookies = CookieManager.getInstance.getCookie(GmailCheck.URL_GMAIL_ATOM)
      Log.e("cookie", cookies)

      val get = new HttpGet(new URI(GmailCheck.URL_GMAIL_ATOM))

      get.setHeader(GmailCheck.HEADER_COOKIE, cookies)
      val response: HttpResponse = client.execute(get)

      if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
        feeds = new BasicResponseHandler().handleResponse(response)

      }

      client.getConnectionManager.shutdown

    } catch {
      case e: Exception =>
        e printStackTrace()

    }
    return feeds

  }

  /**
   * 終了処理
   * @param feeds
   */
  override protected def onPostExecute(feeds: String) {
    Log.e("feeds", feeds)
    if (feeds.length > 0) {

      // 本文チェックメソッド
      def regex(regex: Regex, target: String, index: Int): String = {
        val list = new util.ArrayList[String]
        regex.findAllIn(feeds).matchData.foreach(
          m => list.add(m.group(1))
        )
        list.get(index)

      }

      val count = regex( """<fullcount>(.*)</fullcount>""".r, feeds, 0).toInt

      if (count > 0) {
        val title = regex( """<title>(.*)</title>""".r, feeds, 1)
        val subject = regex( """<summary>(.*)</summary>""".r, feeds, 0)

        sendNotification(count, title, subject)
      }

    } else {

      // ゼロ件の場合は通知を削除
      val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager];
      notificationManager.cancelAll();

    }

  }


  /**
   * notificationの表示
   */
  private def sendNotification(count: Int, title: String, subject: String) {

    val intent = new Intent(appContext, classOf[MainActivity])
    val contentIntent = PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    val largeIcon = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_launcher);

    val builder = new NotificationCompat.Builder(appContext)
      .setContentIntent(contentIntent)
      .setTicker(title)
      .setSmallIcon(R.drawable.ic_launcher)
      .setContentTitle(title)
      .setContentText(subject)
      .setLargeIcon(largeIcon)
      .setWhen(System.currentTimeMillis())
      .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
      .setAutoCancel(true);

    val bigTextStyle = new NotificationCompat.BigTextStyle(builder);
    bigTextStyle.bigText(subject);
    bigTextStyle.setBigContentTitle(title);
    bigTextStyle.setSummaryText("Gmailチェッカー for itmedia (%d)".format(count));

    val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager];
    manager.notify(0, bigTextStyle.build());

  }
}
