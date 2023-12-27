
# IMDb App

Aplicación móvil de listas de reproducción. Construida con Arquitectura Limpia, MVVM, Room, Retrofit, y las mejores prácticas de desarrollo.


## Installation

1. Sync gradle
2. Download firebase credentials: [Guide](https://firebase.google.com/docs/android/setup?hl=es&authuser=0&_gl=1*ti29fm*_ga*MTI5MzgzMjkwNC4xNjk3MDQ4OTkw*_ga_CW55HF8NVT*MTcwMzY5NDM3Mi41NS4xLjE3MDM2OTQ3OTIuMzAuMC4w#add-config-file). Then, switch to project view in android studio and move the downloaded *google-services.json* file to *app/* directory.

3. Go to Facebook developer app > Settings > Basic and copy the app identifier and the secret key: [Guide](https://developers.facebook.com/docs/facebook-login/android/#manifest). Then create the file secrets.xml in app > main > res > values.

```xml
<string name="facebook_app_id">1234</string>
<string name="fb_login_protocol_scheme">fb1234</string>
<string name="facebook_client_token">*******</string>
```
*secrets.xml file is already ignored by git*

4. In the same file create a key to encrypt user sessions:

```xml
...
<string name="tokens_secret_key">*******</string>
```

5. Then, go to [TMDB account settings](https://www.themoviedb.org/settings/api) copy the *API Read Access Token* and add it to the same file too.
```xml
...
<string name="TMDB_api_token">*******</string>
```

6. Finally, Copy the account_id from [TMBD Api](https://developer.themoviedb.org/reference/account-details) and replace it in the Constants.kt file at app/main/java/com.globant.imdb/core directory:

```kotlin
  object Constants {
    ...
    const val TMDB_ACCOUNT= "*****"
  }
```
    
## Authors

- [@Benjamin Silva](https://github.com/benja331)

