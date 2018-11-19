package cn.com.timeriver.httpsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.net.URL
import java.security.*
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * 手动配置HTTPS证书，发送HTTPS请求
 */
class MainActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        const val HOST_URI = "192.168.2.147"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_send_request.setOnClickListener {
            sendHttpsRequest()
        }
    }

    private fun sendHttpsRequest() {
        toast("Send HTTPS Request")
        val uri = "https://$HOST_URI:8443/tomcat.json"
        doAsync {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            // 这一步相当于浏览器（客户端）获取服务器加密证书的过程
            // 不过这里，因为是demo，直接把证书文件（服务器公钥）放在asset目录中
            val cf = CertificateFactory.getInstance("X.509")
            val certificate = cf.generateCertificate(assets.open("tomcat.crt"))

            // Create a KeyStore containing our trusted CAs
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            // Clear keystore
            keyStore.load(null)
            keyStore.setCertificateEntry("tomcat", certificate)

            // Create an SSLContext that uses our TrustManager
            val sslContext = SSLContext.getInstance("TLS")
            /******************* 方式一 使用默认的trustManager ********************/
            // Create a TrustManager that trusts the CAs in our KeyStore
            // val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            // tmf.init(keyStore)
            
            // 浏览器实现时，接收到服务器公钥时，会生成一个随机密码串
            // 使用SecureRandom生成随机数，用于后续对称加密的密钥
            // 这里的trustManagers使用的是默认的TrustManager
            // sslContext.init(null, tmf.trustManagers, SecureRandom())
            /*******************************************************************/

            /********** 方式二 使用自定义的trustManager对服务器的公钥进行校验 **********/
            // 为了保证安全，这里要对服务器的公钥做校验
            val tm: TrustManager = object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    // do nothing，接受任意客户端证书
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    //  do nothing，接受任意服务端证书
                    chain?.forEach {
                        // Make sure that it hasn't expired.
                        it.checkValidity()
                        // Verify the certificate's public key chain.
                        // 和App assets目录中预存的证书进行比对校验
                        try {
                            if (certificate is X509Certificate) {
                                it.verify(certificate.publicKey)
                            }
                        } catch (e: NoSuchAlgorithmException) {
                            e.printStackTrace()
                        } catch (e: InvalidKeyException) {
                            e.printStackTrace()
                        } catch (e: NoSuchProviderException) {
                            e.printStackTrace()
                        } catch (e: SignatureException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            sslContext.init(null, arrayOf(tm), SecureRandom())

            // Tell the URLConnection to use a SocketFactory from our SSLContext
            val connection = URL(uri).openConnection() as HttpsURLConnection
            connection.sslSocketFactory = sslContext.socketFactory
            // Create an HostnameVerifier that hardwires the expected hostname.
            // Note that is different than the URL's hostname:
            // example.com versus example.org
            // Tell the URLConnection to use our HostnameVerifier
            connection.setHostnameVerifier { hostname, session ->
                TextUtils.equals(HOST_URI, hostname)
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
