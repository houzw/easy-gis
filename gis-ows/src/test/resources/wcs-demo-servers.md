

# WCS demo servers
https://demo.mapserver.org/cgi-bin/wcs?SERVICE=WCS&VERSION=1.0.0&REQUEST=GetCapabilities
https://elevation.nationalmap.gov/arcgis/services/3DEPElevation/ImageServer/WCSServer
http://ogcdev.bgs.ac.uk/MIWP-7bSample.html
http://ogcdev.bgs.ac.uk/ogcclient/WCS/GetCoverage_v2_0_1.html

https://www.sentinel-hub.com/develop/api/ogc/standard-parameters/wcs/


    http://gsky.nci.org.au/ows/dea?service=WCS
      &crs=EPSG:4326
      &format=NetCDF
      &request=GetCoverage
      &height=256
      &width=256
      &version=1.0.0
      &bbox=147,-37,148,-35
      &coverage=landsat8_nbart_16day
      &time=2013-04-20T00:00:00.000Z
      &Styles=tc.
     
    
    http://modwebsrv.modaps.eosdis.nasa.gov/wcs/5/MOD021KM/getCoverage?
     service=WCS
     &version=1.0.0
     &request=GetCoverage
     &coverage=EV_500_Aggr1km_RefSB:Day
     &bbox=-80,35,-75,40
     &time=2010-10-21T00:00:00
     &format=hdf4
     &response_crs=EPSG:4326
     &Band_500M=2/2/1
     &resx=0.01
     &resy=0.01

 https://ofmpub.epa.gov/rsig/rsigserver?
 SERVICE=wcs
 &VERSION=1.0.0
 &REQUEST=GetCoverage
 &COVERAGE=cmaq.amad.conus.metdot3d.uwind,cmaq.amad.conus.metdot3d.vwind
 &TIME=2005-08-28T20:00:00Z/2005-08-29T01:59:59Z
 &BBOX=-90,30,-88,32,1,1
 &FORMAT=netcdf-ioapi



















