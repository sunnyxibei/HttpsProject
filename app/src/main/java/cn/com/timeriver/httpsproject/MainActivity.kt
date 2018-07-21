package cn.com.timeriver.httpsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.warn
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * 手动配置HTTPS证书，发送HTTPS请求
 */
class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_send_request.onClick {
            sendHttpsRequest()
        }
    }

    private fun sendHttpsRequest() {
        toast("发送HTTPS请求")
        val uri = "https://192.168.31.139:8443/alipay.json"
        doAsync {
            val openConnection = URL(uri).openConnection() as HttpsURLConnection
            val inputStream = openConnection.inputStream
            val buffer = ByteArray(1024 * 8)
            var length = 0
            val baos = ByteArrayOutputStream()
            while (length != -1) {
                length = inputStream.read(buffer)
                baos.write(buffer, 0, length)
            }
            baos.flush()
            warn { baos.toString() }
        }
    }
}
