# Easy-GIS

依赖：https://github.com/lreis2415/egc-commons

```
<dependency>
    <groupId>org.egc</groupId>
    <artifactId>commons</artifactId>
    <version>2.1-SNAPSHOT</version>
</dependency>
```

- easy-to-use
- easy-to-learn


---

决定项目或模块是否 install 或 deploy 的配置
```xml
<properties>
   <maven.deploy.skip>true</maven.deploy.skip>
   <maven.install.skip>true</maven.install.skip>
</properties>
```

---

## 注意事项
### 部分子模块暂未提交到 github，使用时注意在根目录的 `pom.xml` 中排除
### gis-ows/gis-wps-client
1. geotools 版本
52n-wps 依赖的版本为 8.7，更新到最新版可能出现问题
2. xercesImpl
如果把 xercesImpl （2.7.1） 更新为 2.12.0，可能出现 `could not decode InputStream` （wps-client-lib） 
或 `Error occured while transfer` （52n-wps-client-lib）
```xml
 <dependency>
     <groupId>xerces</groupId>
     <artifactId>xercesImpl</artifactId>
     <version>2.12.0</version>
 </dependency>
```


## 参考项目

- gdal
- geotools
- proj4j
- jts
- geoserver-manager
