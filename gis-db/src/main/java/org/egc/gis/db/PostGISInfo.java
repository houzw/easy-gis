package org.egc.gis.db;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * <pre>
 * postgis 数据库连接信息。
 * 若导入栅格数据，必须设置 rasterTable 值
 * 若导入shapefile数据，必须设置 shapeTable 值
 * schema默认为public
 * host默认localhost
 * binDirectory默认为环境变量PSQL值
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/26 18:03
 */
@Data
@Slf4j
public class PostGISInfo {

    public PostGISInfo() {
    }

    public PostGISInfo(String username, String database, String password) {
        this.username = username;
        this.database = database;
        this.password = password;
    }

    private String username;
    private String database;
    private String password;
    private Integer port = 5432;
    private String host = "localhost";
    private String schema = "public";
    private String rasterTable;
    private String shapeTable;
    /**
     * postgis executable files directory
     */
    private String binDirectory = System.getenv("PSQL");
}
