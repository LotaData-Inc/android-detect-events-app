
<p  align="center"> 
 <a href="https://lotadata.com/">
  <img src="https://lotadata.com/src/images/LD_blue_red.png" width="400" height="100">
 </a>
</p>

# GeoEvents
Sample app that demonstrates how integrate our SDK to detect events like fence transition (ENTER, EXIT and DWELL) and user's activity (ON FOOT, RUNNING, STILL, TILTING and DRIVING)

<p align="center">
<img src="https://i.ibb.co/7gnwfYX/geoevents.png" width="300" height="500">
</p>

# Events detected

## User actvity
Our SDK uses [Google Awareness API](https://developers.google.com/awareness) to detect user activity.

<p align="center">
<img src="https://i.ibb.co/DVNvNpT/activity-recog.jpg" width="400" height="250">
</p>


## Fence transition
Our SDK **DOESN'T use** [Google Geogence API](https://developers.google.com/location-context/geofencing) to detect fence transition.

What are the advantages?
 - With our SDK there isn't a limit of 100 geofences per app like Google Geogence API;
 - You can create polygonal or circular fences, while Google Geogence API only supports circular fences.

<p align="center">
<img src="https://i.ibb.co/nCg1GPK/fence-transition.gif" width="500" height="300">
</p>


# Documentation
Access our full documentation [here](https://docs.lotadata.com).
