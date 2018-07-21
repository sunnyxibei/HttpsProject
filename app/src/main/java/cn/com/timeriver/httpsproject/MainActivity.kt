package cn.com.timeriver.httpsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.ByteArrayOutputStream
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

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
        toast("Send HTTPS Request")
        val uri = "https://192.168.31.139:8443/tomcat.json"
        doAsync {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            // 这一步相当于浏览器（客户端）获取服务器加密证书的过程
            // 不过这里，因为是demo，直接把证书文件（服务器公钥）放在asset目录中
            val cf = CertificateFactory.getInstance("X.509")
            val certificate = cf.generateCertificate(assets.open("tomcat.cert"))

            // Create a KeyStore containing our trusted CAs
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            // Clear keystore
            keyStore.load(null)
            keyStore.setCertificateEntry("tomcat", certificate)

            // Create a TrustManager that trusts the CAs in our KeyStore
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            tmf.init(keyStore)

            // Create an SSLContext that uses our TrustManager
            // 浏览器实现时，接收到服务器公钥时，会生成一个随机密码串，但是这里我们直接传值为null？
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)

            // Tell the URLConnection to use a SocketFactory from our SSLContext
            val connection = URL(uri).openConnection() as HttpsURLConnection
            connection.sslSocketFactory = sslContext.socketFactory
            // Create an HostnameVerifier that hardwires the expected hostname.
            // Note that is different than the URL's hostname:
            // example.com versus example.org
            // Tell the URLConnection to use our HostnameVerifier
            connection.setHostnameVerifier { hostname, session ->
                TextUtils.equals("192.168.31.139", hostname)
            }

            val inputStream = connection.inputStream
            val buffer = ByteArray(1024 * 8)
            val baos = ByteArrayOutputStream()
            var length = inputStream.read(buffer)
            while (length != -1) {
                baos.write(buffer, 0, length)
                length = inputStream.read(buffer)
            }
            baos.flush()
            val result = baos.toString()
            warn { result }
            uiThread {
                toast("HTTPS Request Success")
                toast(result)
            }
        }
    }
}
