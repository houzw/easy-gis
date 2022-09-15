MAP
    NAME WCS_server
    STATUS ON
    IMAGETYPE "png24"
    SIZE ${mapObj.sizeX!400}} ${mapObj.sizeY!300}}
    SYMBOLSET "../etc/symbols.txt"
    # minx miny maxx maxy
    EXTENT -180 -90 180 90 # World
    UNITS ${mapObj.units!'DD'}}
    SHAPEPATH ${mapObj.shapepath}
    IMAGECOLOR 255 255 255
    FONTSET "../etc/fonts.txt"

    OUTPUTFORMAT
        NAME "png"
        MIMETYPE "image/png"
        DRIVER "AGG/PNG"
        EXTENSION "png"
        IMAGEMODE RGB
        TRANSPARENT FALSE
    END # OUTPUTFORMAT

    OUTPUTFORMAT
        NAME GEOTIFF_16
        DRIVER "GDAL/GTiff"
        MIMETYPE "image/tiff"
        IMAGEMODE FLOAT32
        EXTENSION "tif"
        FORMATOPTION "FILENAME=result.tif"
    END # OUTPUTFORMAT

    OUTPUTFORMAT
        NAME AAIGRID
        DRIVER "GDAL/AAIGRID"
        MIMETYPE "image/x-aaigrid"
        IMAGEMODE INT16
        EXTENSION "grd"
        FORMATOPTION "FILENAME=result.grd"
    END # OUTPUTFORMAT

    WEB
      IMAGEPATH "/ms4w/tmp/ms_tmp/"
      IMAGEURL "/ms_tmp/"
      METADATA
        "ows_srs" "EPSG:4326"

        "ows_allowed_ip_list" "127.0.0.1"

        "wcs_label"           "WCS Server" ### required
        "wcs_description"     "EasyGC web service"
        "wcs_onlineresource"  ${mapConfig.onlineresource} ### recommended
        "wcs_fees"            "none"
        "wcs_accessconstraints"    "none"
        "wcs_keywordlist"          "wcs,soil"
        "wcs_metadatalink_type"    "TC211"
        "wcs_metadatalink_format"  "text/plain"
        "wcs_city"                 "Beijing"
        "wcs_stateorprovince"      "Beijing"
        "wcs_postcode"             "100101"
        "wcs_country"              "China"
        "wcs_service_onlineresource"   ${mapConfig.onlineresource}
        "wcs_enable_request"           "*"

        # EPSG codes that the data can be served in
        "wms_srs"	"EPSG:4326 EPSG:3857"
        "wms_onlineresource"	${mapConfig.onlineresource}
        "wms_enable_request"	"*"
        "wms_title"	"WMS Server"
      END # WEB METADATA
    END # WEB

<#--
    # the real projection that the data is
    PROJECTION
      "init=epsg:3857"
    END
    -->
     # set defaults for GetLegendGraphic requests
     LEGEND
      LABEL
       TYPE BITMAP
       SIZE MEDIUM
       COLOR 0 0 0
      END
     END # LEGEND

    LAYER
      NAME ${layerObj.name}
      METADATA
        "wcs_label"           "Elevation/Bathymetry"  ### required
        "wcs_rangeset_name"   "Range 1"  ### required to support DescribeCoverage request
        "wcs_rangeset_label"  "My Label" ### required to support DescribeCoverage request
        "wms_enable_request"  "*"
      END # LAYER METADATA
      TYPE RASTER ### required
      STATUS ON
      # allow data access via query and WCS
      DUMP TRUE
      DATA ${layerObj.data}
      # the real projection that the data is
      PROJECTION
        "init=epsg:4326"
      END
    END # LAYER
  END # Map File