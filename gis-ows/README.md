# 基于 Java 的 GIS WebService
- OGC OWS (客户端) 操作
- 基于 SoapUI 的 SOAP 服务操作 （WSDL）
- Restful 操作
- HTTP 请求基于 Httpclient （或 okHttp）

---
OGC 服务请求一般有两种方式
1. Key-value pairs:
    如 `http://server.domain/wps?service=WPS&request=GetCapabilities&version=1.0.0`. 
    此时使用 HTTP GET 请求方式
2. XML payload:
    如
    ```xml
   <?xml version="1.0" encoding="UTF-8"?>
    <wps:GetCapabilities language="cz" service="WPS" xmlns:ows="http://www.opengis.net/ows/1.1" xmlns:wps="http://www.opengis.net/wps/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsGetCapabilities_request.xsd">
      <wps:AcceptVersions>
        <ows:Version>1.0.0</ows:Version>
      </wps:AcceptVersions>
    </wps:GetCapabilities>
    ```
   此时使用 HTTP POST 请求方式发送 XML 数据
   
---
suis4j 中的请求的格式为 xml， 不是 kvp。过程是：用户输入参数信息，转换为相应的Java对象，如`DescribeCoverage`，
然后再使用 JAXBContext 的 marshal 转换为 xml，最后最为请求的 request body 发送, 获得数据之后再转换为java 对象，如 `CoverageDescription`

服务的请求可参考使用 https://github.com/ZihengSun/suis4j 

或者使用 https://github.com/houzw/egc-suis4j 
（相同的内容，只是整理了一下项目结构和 maven 引用）