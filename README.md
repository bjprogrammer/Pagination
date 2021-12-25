# Pagination with Dagger Hilt
Android app for demonstrating proficiency in Kotlin, RxJava(for searching) and jetpack components like Dagger Hilt and Pagination library

Architecture Used- MVVM (ViewModel, LiveData, RxJava, Glide, Dagger Hilt)

Features-<br/>
1.) Supports API 21 - 29 (portrait and landscape mode)<br/>
2.) Displays set of images in grid layout. 3 per row in portrait mode and 5 in landscape mode<br/>
3.) Search functionality based on movie name when user enters 3 or more characters<br/>
4.) Spannable text in filtered list highlighting searched in keyword in yellow colour<br /
5.) Smooth scrolling with minimum loading while scrolling infinitely with the help of pagination library

Known Issues-</br>
1.)Customizing the toolbar as per the redline. Android provides its own implementation for toolbar as per the material design guidelines. Customizing it completely is not recommended.
2.) Images provided should be svg(vector drawables) and not jpg
3.) Grid size in case of landscape mode is mentioned to be 7 in readme but actually used 5 in screenshots provided

Installation-
App can installed using release.apk file provided in project folder itself.<br/>
App can also be run by opening project in Android Studio 4.0.0 and running the app on emulator.
