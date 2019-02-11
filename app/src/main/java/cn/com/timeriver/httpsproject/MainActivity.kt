package cn.com.timeriver.httpsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.bt_send_request
import kotlinx.android.synthetic.main.activity_main.bt_send_request_via_https
import org.jetbrains.anko.AnkoLogger

/**
 * 手动配置HTTPS证书，发送HTTPS请求
 */
class MainActivity : AppCompatActivity(), AnkoLogger {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    bt_send_request.setOnClickListener {
      HttpUtil.sendHttpsRequest(this)
    }
    bt_send_request_via_https.setOnClickListener {
      OkHttpUtil.sendHttpsRequest(this)
    }
  }
}
