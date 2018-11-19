# HttpsProject
## 第一步 Tomcat配置

直接去Tomcat的官网，查看响应的配置，我选择了HTTP1.0的TLS/SSL配置，根据官网文档对server.xml作相应的配置。

服务器环境使用Tomcat 9，放了一个json文件(tomcat.json)用来试验。

##  第二步 Android证书校验

```
port="8443" // HTTPS默认端口8443
type="RSA"  // 加密方式RSA
sslProtocol="TLS" // ssl协议版本 TLS
```

Android端的代码是根据Google Developer一步一步学习的，官方文档非常翔实，多看有益。

同时需要注意的是，如果是自签名的证书，需要自定义TrustManager，重写 checkServerTrusted方法校验证书是否和Android端预先存储的证书一致。



> tips
>
> 目前我看过的关于Android HTTPS知识讲解最棒的一篇博客
>
> https://www.cnblogs.com/alisecurity/p/5939336.html