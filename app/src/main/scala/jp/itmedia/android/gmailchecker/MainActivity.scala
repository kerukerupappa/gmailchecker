package jp.itmedia.android.gmailchecker

import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import android.webkit.{ WebChromeClient, WebViewClient, WebView}
import android.view.{View, KeyEvent}
import jp.itmedia.android.gmailchecker.alarm.AlarmUtil
import android.support.v4.widget.DrawerLayout
import android.widget.Button
import android.app.NotificationManager
import android.content.Context


/**
 * メインView
 */
class MainActivity extends ActionBarActivity with View.OnClickListener {

  private var webView: WebView = null
  private var mDrawer: DrawerLayout = null
  private val TOP_URL = "https://mail.google.com/mail/u/0/?hl=ja#inbox"

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    webView = findViewById(R.id.activity_main_webview).asInstanceOf[WebView]

    webView.setWebViewClient(new WebViewClient)
    webView.setWebChromeClient(new WebChromeClient)

    webView.getSettings.setJavaScriptEnabled(true)
    webView.loadUrl(TOP_URL)

    //new GmailCheck(this).execute()
    mDrawer = findViewById(R.id.activity_main_drawer_layout).asInstanceOf[DrawerLayout]

    val reload = findViewById(R.id.activity_main_drawer_button).asInstanceOf[Button]
    reload.setOnClickListener(this)

    // アラーム登録
    val alarmUtil = new AlarmUtil(this)
    alarmUtil.setAlarm

  }

  /**
   * 復帰イベント
   */
  override def onResume() {
    super.onResume()
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager];
    notificationManager.cancelAll();


  }

  /**
   * クリックイベント
   *
   * @param v
   */
  override def onClick(v: View) {
    v.getId match {
      case R.id.activity_main_drawer_button =>
        webView.reload()
        val alarmUtil = new AlarmUtil(this)
        alarmUtil.setAlarm
        mDrawer.closeDrawers

      case _ =>

    }
  }

  /**
   * キーダウンイベント
   * @param keyCode
   * @param event
   * @return
   */
  override def onKeyDown(keyCode: Int, event: KeyEvent): Boolean = {
    if (keyCode == KeyEvent.KEYCODE_BACK ) {
      //webView.goBack
      true
    }
    super.onKeyDown(keyCode, event);

  }
}

