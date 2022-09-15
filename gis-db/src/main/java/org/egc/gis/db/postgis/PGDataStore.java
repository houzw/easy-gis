package org.egc.gis.db.postgis;

import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * GeoTools PostGIS data store
 * <pre>http://docs.geotools.org/stable/userguide/library/jdbc/postgis.html</pre>
 *
 * @author houzhiwei
 */
public class PGDataStore {
    private static final Logger log = LoggerFactory.getLogger(PGDataStore.class);
    private String host = "127.0.0.1";
    private int port = 5432;
    @NotBlank
    private String dbname;
    private String schema = "public";
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public DataStore getDataStore() {
        return dataStore;
    }

    private DataStore dataStore = null;

    public PGDataStore(String dbName, String username, String pw) {
        dbname = dbName;
        this.username = username;
        password = pw;
        connect();
    }

    public PGDataStore(String dbName, String username, String pw, String host, int port, String schema) {
        dbname = dbName;
        this.username = username;
        password = pw;
        if (StringUtils.isNotBlank(host)) {
            this.host = host;
        }
        if (StringUtils.isNotBlank(schema)) {
            this.schema = schema;
        }
        if (port > 0) {
            this.port = port;
        }
        connect();
    }

    private void connect() {
        Map<String, Object> params = new HashMap<>(8);
        params.put(JDBCDataStoreFactory.DBTYPE.key, "postgis");
        params.put(JDBCDataStoreFactory.HOST.key, host);
        params.put(JDBCDataStoreFactory.PORT.key, port);
        params.put(JDBCDataStoreFactory.SCHEMA.key, schema);
        params.put(JDBCDataStoreFactory.DATABASE.key, dbname);
        params.put(JDBCDataStoreFactory.USER.key, username);
        params.put(JDBCDataStoreFactory.PASSWD.key, password);
        params.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS.key, true);
        try {
            dataStore = DataStoreFinder.getDataStore(params);
            log.info("PostGIS database {} connected.", dbname);
        } catch (IOException e) {
            log.error("Failed to connect to PostGIS", e);
        }
    }

    public void dispose() {
        dataStore.dispose();
        dataStore = null;
        log.info("PostGIS database {} disposed.", dbname);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
