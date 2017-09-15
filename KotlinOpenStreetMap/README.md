# KotlinOpenStreetMap
This app is ran mostly by utilizing the library osmdroid (https://github.com/osmdroid/osmdroid).

When the app first starts it may ask for location and external storage writing permssions.
For the app to work offline external storage writing permissions must be enabled.

The map will first start in a, the city of Urbana, IL.
There is one preloaded small city, Castelldefels, that can be viewed without internet.
It will then try to find your location through GPS if location data is enabled.
When the user's location is found the first time it will move the map to the user's location and mark the user's location.
Using an emulator or 3rd party app you can mock your location.

You can drag and resize the map through swipe and pinch gestures.

Other features include a compass overlay and a Exit App button in the Toolbar.
