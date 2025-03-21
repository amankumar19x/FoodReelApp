# Food Reels Android App

## 📌 Overview
Food Reels Android App is a video-based food discovery application that autoplay food reels using **ExoPlayer** and helps users locate nearby restaurants within a **10km radius**. The app integrates **Google Maps**, **Geocoder**, and **Overpass API** to fetch real-time restaurant locations, providing an interactive user experience.

## 🎯 Features
- **Autoplay Food Reels** - Uses ExoPlayer to autoplay videos in a RecyclerView.
- **Bottom Navigation View** - Enables seamless navigation between different app sections.
- **Nearby Food Vendor Locator** - Fetches restaurants within a 10km radius using Geocoder and Overpass API.
- **Google Maps Integration** - Displays nearby restaurants for easy navigation.
- **Bookmark Feature** - Allows users to save their favorite food reels and view them later.
- **Persistent Storage** - Bookmarks are saved using SharedPreferences.

## 🛠️ Tech Stack
- **Language:** Java
- **Libraries Used:**
  - ExoPlayer (for autoplaying videos)
  - RecyclerView (for displaying reels)
  - Volley (for API requests)
  - Overpass API (for fetching restaurant data)
  - Geocoder (for location services)
  - Google Maps SDK (for restaurant navigation)
  - SharedPreferences (for saving bookmarks)

## 🚀 Implementation Steps

### 1️⃣ Setting Up the Project
- Create a new Android project in **Android Studio**.
- Add dependencies for **ExoPlayer**, **Volley**, **Google Maps SDK**, and other required libraries in `build.gradle`.

### 2️⃣ Implementing ExoPlayer for Food Reels
- Use `RecyclerView` to display a scrollable list of food reels.
- Integrate **ExoPlayer** to autoplay videos.
- Optimize video playback by pausing when the user scrolls away.

### 3️⃣ Setting Up Bottom Navigation View
- Implement **BottomNavigationView** to switch between:
  - **Home (Food Reels)**
  - **Nearby Food Vendors**
  - **Bookmarks**
- Handle UI updates dynamically based on selected navigation items.

### 4️⃣ Fetching Nearby Restaurants
- Request **Location Permissions** to access user location.
- Use **Geocoder** to obtain latitude and longitude.
- Fetch nearby restaurants via **Overpass API**.
- Display restaurant data in a structured format.

### 5️⃣ Integrating Google Maps
- Embed **Google Maps** to show nearby restaurants.
- Mark locations with **Map Markers**.
- Allow users to navigate through restaurants interactively.

### 6️⃣ Implementing the Bookmark Feature
- Add a **Bookmark Icon** in each food reel item.
- When clicked, **save the reel** in **SharedPreferences**.
- Create a **Bookmark Fragment** to display saved reels.
- Load and display bookmarked reels when the Bookmark Fragment is opened.

### 7️⃣ Testing & Deployment
- Test the app on different devices to ensure **smooth video playback and accurate location results**.
- Optimize performance by handling **video lifecycle events** properly.
- Generate a **signed APK** and prepare the app for **Google Play Store deployment**.

## 📌 Screenshots
🚧 *Coming Soon...*

## 🔮 Future Improvements
- Implement **user authentication** for personalized recommendations.
- Add **filters for restaurant types and user preferences**.
- Improve **UI/UX with animations and transitions**.
- Migrate bookmarks from **SharedPreferences** to **Room Database**.



