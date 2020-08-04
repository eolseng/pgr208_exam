> EXAM || PGR208 - Android Programming || ID 457 || Grade: A
# SailAway
![](app/src/main/res/drawable/place_placeholder_image.jpg "Photo by Christian Joudrey on Unsplash")

The solution is based on the material learned during the course **PGR208 - Android Programming (Android programmering)**.

The application pulls a list of places from [NoForeignLand.com](https://www.noforeignland.com/) with **Retrofit2** and caches it using a **Room** handled **SQLite**-database in case of internet outages.
Users are able to select a place from the **CardView & RecyclerView** menu, which can be filtered with **FTS4** (Full-Text-Search) backed **SearchView** to fetch more data about the specific place.
The user can also open up a **GoogleMaps** SupportMapFragment View to see the location on a map.

It is designed with **MVVM** (Model View ViewModel) architecture using **Kotlin Coroutines** and **LiveData**.

To use the app you must supply a valid GOOGLE_MAPS_API_KEY in your gradle.properties file.

*NB: Shortly after the exam NoForeignLand deprecated the API to fetch individual places. The application will still work and fetch the list of places, but will warn the user that it failed to fetch data.*
