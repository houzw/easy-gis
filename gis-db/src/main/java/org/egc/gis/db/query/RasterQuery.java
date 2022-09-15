package org.egc.gis.db.query;

import lombok.extern.slf4j.Slf4j;

/**
 * @author houzhiwei
 * @date 2020/6/11 15:12
 */
@Slf4j
public class RasterQuery {


    public void queryEnvelope(String tbl, double minX, double minY, double maxX, double maxY, int epsg) {
        StringBuilder sql = new StringBuilder();
        //geoTIFF
        sql.append("SELECT ST_AsTIFF(rast, 'LZW') ");
        sql.append("FROM( ");
        //ST_Union函数用于聚合选择出来的数据为一个整体；
        //ST_Clip 将选择出来的Tiles进行裁剪，得到geom范围的数据
        sql.append("SELECT ST_Union(ST_Clip(rast, geom)) AS rast ");
        sql.append("FROM ");
        sql.append(tbl);
        sql.append(" CROSS JOIN ");
        //构造一个矩形范围，其参数分别是最小Ｘ值，最小Ｙ值，最大Ｘ值，最大Ｙ值和坐标系代码
        sql.append("ST_MakeEnvelope(")
                .append(minX).append(",")
                .append(minY).append(",")
                .append(maxX).append(",")
                .append(maxY).append(",")
                .append(epsg).append(") As geom ");
        //选择出与geom矩形相交的栅格Tiles
        sql.append("WHERE ST_Intersects(rast, geom) ");
        sql.append(") AS rasttiff;");

    }
}
