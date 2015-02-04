# RainInterpolator
Finds daily rainfall for a watershed (any polygon basically) based on data available from several rain stations around the polygon. IDW is the interpolation method. It allows for changing IDW power so, thiessen method can pe approximated by using a high IDW power (power=50).
There is a howto on getting data and a ~10min video that shows how to use it.
Video: http://youtu.be/usw44r0_V80
See WIKI: https://github.com/skp703/RainInterpolator/wiki
Howto:
Guide to gather input to use Rain Interpolator

1)	Organize rain data in a CSV file.
For this example to compute IDW rain for Arlington County, VA. I got the data from rain gauges around the county from the National Climatic Data Center (NCDC) at http://www.ncdc.noaa.gov/. If you do not know where to start NCDC is a good place for USA at least. Rain data has to be arranged in following format StationName, Date (yyyymmdd), preci. First row with headers is also expected.
Example:
STATION_NAME,DATE,PRCP(10th of mm)
ALEXANDRIA 1.8 S VA US,20080906,1191
ALEXANDRIA 1.8 S VA US,20080910,10
ALEXANDRIA 1.8 S VA US,20080926,61
….

2)	Get the shapefile with all station locations.
NCDC gave me lat-lng in decimal Degree for stations (Tip: filter for stations and corresponding lat-lng using excel pivot table). Armed with lat-lng for the stations, it was quick task to convert lat-lng to point type shapefiles in ArcCatalog/ArcMap (See http://resources.arcgis.com/en/help/main/10.1/index.html#//00s50000001z000000 if you need help ).
a)	Remember to assign coordinate system to the layer, if data is in decimal degree it is probably in GCS_WGS_1984.
b)	“Station names” should match the “Station names” used in the rain data csv file.

3)	Get boundary polygon for the area of interest.
a.	Make sure that projection units are not in decimal degree for this polygon, if they are, re-project the shape file in some coordinate system that has units in meter or feet or any other distance measure.
i.	Why? Spatial Reference of this polygon is used as base for calculation, if the spatial reference is in degree the distance calculation, done simply as distance between two points (sqrt((x1-x2)^2+(y1-y2)^2)), is not correct. Maybe in a future version I will try to get rid of this requirement. For time being use Project tool in ArcGIS http://resources.arcgis.com/en/help/main/10.1/index.html#//00170000007m000000

