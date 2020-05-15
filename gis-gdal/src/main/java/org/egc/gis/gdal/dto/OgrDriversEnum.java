package org.egc.gis.gdal.dto;

import lombok.Getter;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/12 19:27
 */
@Getter
public enum OgrDriversEnum {
    DB2ODBC("DB2ODBC", "raster, vector", "read, write and update", "IBM DB2 Spatial Database",""),
    PCIDSK("PCIDSK", "raster, vector", "read, write and update, supporting virtual IO", "PCIDSK Database File","pix"),
    PDF("PDF", "raster, vector", "w+", "Geospatial PDF","pdf"),
    GPKG("GPKG", "raster, vector", "read, write and update, supporting virtual IO and subdatasets", "GeoPackage","gpkg"),
    HTTP("HTTP", "raster, vector", "read-only", "HTTP Fetching Wrapper",""),
    ESRI_Shapefile("ESRI Shapefile", "vector", "read, write and update, supporting virtual IO", "ESRI Shapefile",
                   "shp"),
    MapInfo_File("MapInfo File", "vector", "read, write and update, supporting virtual IO", "MapInfo File", ""),
    UK_NTF("UK .NTF", "vector", "read-only", "UK .NTF", ""),
    OGR_SDTS("OGR_SDTS", "vector", "read-only", "SDTS", ""),
    S57("S57", "vector", "read, write and update, supporting virtual IO", "IHO S-57 (ENC)", ""),
    DGN("DGN", "vector", "read, write and update", "Microstation DGN", ""),
    OGR_VRT("OGR_VRT", "vector", "read-only and virtual IO", "VRT,Virtual Datasource", "vrt"),
    REC("REC", "vector", "read-only", "EPIInfo .REC", ""),
    Memory("Memory", "vector", "read, write and update", "Memory", ""),
    BNA("BNA", "vector", "read, write and update, supporting virtual IO", "Atlas BNA", ""),
    CSV("CSV", "vector", "read, write and update, supporting virtual IO", "Comma Separated Value", "csv"),
    GML("GML", "vector", "read, write and update, supporting virtual IO", "Geography Markup Language(GML)", "gml"),
    GPX("GPX", "vector", "read, write and update, supporting virtual IO", "GPX", ""),
    KML("KML", "vector", "read, write and update, supporting virtual IO", "Keyhole Markup Language (KML)", "kml"),
    GeoJSON("GeoJSON", "vector", "read, write and update, supporting virtual IO", "GeoJSON", "json"),
    OGR_GMT("OGR_GMT", "vector", "read, write and update", "GMT ASCII Vectors", "gmt"),
    SQLite("SQLite", "vector", "read, write and update, supporting virtual IO", "SQLite / Spatialite", ""),
    ODBC("ODBC", "vector", "read, write and update", "ODBC", ""),
    WAsP("WAsP", "vector", "read, write and update, supporting virtual IO", "WAsP .map format", ""),
    PGeo("PGeo", "vector", "read-only", "ESRI Personal GeoDatabase", ""),
    MSSQLSpatial("MSSQLSpatial", "vector", "read, write and update", "Microsoft SQL Server Spatial Database", ""),
    OpenFileGDB("OpenFileGDB", "vector", "read-only and virtual IO", "ESRI FileGDB", ""),
    XPlane("XPlane", "vector", "read-only and virtual IO", "X-Plane/Flightgear aeronautical data", ""),
    DXF("DXF", "vector", "read, write and update, supporting virtual IO", "AutoCAD DXF", "dxf"),
    Geoconcept("Geoconcept", "vector", "read, write and update", "Geoconcept", ""),
    GeoRSS("GeoRSS", "vector", "read, write and update, supporting virtual IO", "GeoRSS", ""),
    GPSTrackMaker("GPSTrackMaker", "vector", "read, write and update, supporting virtual IO", "GPSTrackMaker", ""),
    VFK("VFK", "vector", "read-only", "Czech Cadastral Exchange Data Format", ""),
    PGDUMP("PGDUMP", "vector", "w+v", "PostgreSQL SQL dump", ""),
    OSM("OSM", "vector", "read-only and virtual IO", "OpenStreetMap XML and PBF", ""),
    GPSBabel("GPSBabel", "vector", "read, write and update", "GPSBabel", ""),
    SUA("SUA", "vector", "read-only and virtual IO", "Tim Newport-Peace's Special Use Airspace Format", ""),
    OpenAir("OpenAir", "vector", "read-only and virtual IO", "OpenAir", ""),
    OGR_PDS("OGR_PDS", "vector", "read-only and virtual IO", "Planetary Data Systems TABLE", ""),
    WFS("WFS", "vector", "read-only and virtual IO", "OGC WFS (Web Feature Service)", ""),
    HTF("HTF", "vector", "read-only and virtual IO", "Hydrographic Transfer Vector", ""),
    AeronavFAA("AeronavFAA", "vector", "read-only and virtual IO", "Aeronav FAA", ""),
    Geomedia("Geomedia", "vector", "read-only", "Geomedia .mdb", ""),
    EDIGEO("EDIGEO", "vector", "read-only and virtual IO", "French EDIGEO exchange format", ""),
    GFT("GFT", "vector", "read, write and update", "Google Fusion Tables", ""),
    GME("GME", "vector", "read, write and update", "Google Maps Engine", ""),
    SVG("SVG", "vector", "read-only and virtual IO", "Scalable Vector Graphics", ""),
    CouchDB("CouchDB", "vector", "read, write and update", "CouchDB / GeoCouch", ""),
    Cloudant("Cloudant", "vector", "read, write and update", "Cloudant / CouchDB", ""),
    Idrisi("Idrisi", "vector", "read-only and virtual IO", "Idrisi Vector", "vct"),
    ARCGEN("ARCGEN", "vector", "read-only and virtual IO", "Arc/Info Generate", ""),
    SEGUKOOA("SEGUKOOA", "vector", "read-only and virtual IO", "SEG-P1 / UKOOA P1/90", ""),
    SEGY("SEGY", "vector", "read-only and virtual IO", "SEG-Y", ""),
    XLS("XLS", "vector", "read-only", "MS Excel format", ""),
    ODS("ODS", "vector", "read, write and update, supporting virtual IO",
        "Open Document/ LibreOffice / OpenOffice Spreadsheet", ""),
    XLSX("XLSX", "vector", "read, write and update, supporting virtual IO", "MS Office Open XML spreadsheet", ""),
    ElasticSearch("ElasticSearch", "vector", "w+", "Elastic Search", ""),
    Walk("Walk", "vector", "read-only", "Walk", ""),
    CartoDB("CartoDB", "vector", "read, write and update", "CartoDB", ""),
    SXF("SXF", "vector", "read-only", "Storage and eXchange Format", ""),
    Selafin("Selafin", "vector", "read, write and update, supporting virtual IO", "Selafin", ""),
    JML("JML", "vector", "read, write and update, supporting virtual IO", "OpenJUMP JML", ""),
    PLSCENES("PLSCENES", "raster, vector", "read-only", "Planet Labs Scenes API", ""),
    CSW("CSW", "vector", "read-only", "OGC CSW (Catalog Service for the Web)", "xml"),
    TIGER("TIGER", "vector", "read, write and update, supporting virtual IO", "U.S. Census TIGER/Line", ""),
    AVCBin("AVCBin", "vector", "read-only", "Arc/Info Binary Coverage", ""),
    AVCE00("AVCE00", "vector", "read-only", "Arc/Info E00 (ASCII) Coverage", "e00");

    private String name;
    private String type;
    private String readOrWrite;
    private String description;
    private String extension;

    OgrDriversEnum(String name, String readOrWrite, String type, String description, String extension) {
        this.name = name;
        this.type = type;
        this.readOrWrite = readOrWrite;
        this.description = description;
        this.extension = extension;
    }
}
