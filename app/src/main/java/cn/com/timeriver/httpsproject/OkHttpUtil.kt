package cn.com.timeriver.httpsproject

import android.content.Context
import android.text.TextUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom
import java.security.SignatureException
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object OkHttpUtil : AnkoLogger {

  fun sendHttpsRequest(context: Context) {
    val uri = "https://${AppConfig.HOST_URI}:8443/tomcat.json"

    val cf = CertificateFactory.getInstance("X.509")
    val certificate = cf.generateCertificate(context.assets.open("tomcat.crt"))

    val client = OkHttpClient.Builder()
        .sslSocketFactory(getSLSocketFactory(certificate), getTrustManager(certificate))
        .hostnameVerifier { hostname, session ->
          if (TextUtils.equals(AppConfig.HOST_URI, hostname)) {
            true
          } else {
            // warning，这里，可以提前使用HttpsURLConnection.setDefaultHostnameVerifier做集中统一处理
            val hv = HttpsURLConnection.getDefaultHostnameVerifier()
            hv.verify(hostname, session)
          }
        }
        .build()
    val request = Request.Builder()
        .url(uri)
        .get()
        .build()
    client.newCall(request)
        .enqueue(object : Callback {
          override fun onFailure(
            call: Call,
            e: IOException
          ) {
            warn { e }
          }

          override fun onResponse(
            call: Call,
            response: Response
          ) {
            warn { "HTTPS via OKHTTP success" }
            warn {
              response.body()
                  .toString()
            }
          }

        })
  }

  private fun getSLSocketFactory(certificate: Certificate): SSLSocketFactory {
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, arrayOf(getTrustManager(certificate)), SecureRandom())
    return sslContext.socketFactory
  }

  private fun getTrustManager(certificate: Certificate) = object : X509TrustManager {

    override fun getAcceptedIssuers(): Array<X509Certificate> {
      return arrayOf()
    }

    override fun checkClientTrusted(
      chain: Array<out X509Certificate>?,
      authType: String?
    ) {
      // do nothing，接受任意客户端证书
    }

    override fun checkServerTrusted(
      chain: Array<out X509Certificate>?,
      authType: String?
    ) {
      // 校验服务端证书
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

}