## 注意事项

GDAL 版本为 `2.1.0` （理论上应当是兼容新版本的），对应 `gdal-201-1800-x64-core.msi`

GDAL 安装文件及 JAR 文件下载自 http://www.gisinternals.com/archive.php 

需要使用与 dll 文件一致的，gisinternals 提供的gdal.jar文件。开发中使用nexus管理

`dll`文件通过配置系统环境变量解决：`C:\Program Files\GDAL`


---
reference:
- https://gdal.org/java/overview-summary.html
- https://trac.osgeo.org/gdal/browser/trunk/gdal/swig/java/apps/