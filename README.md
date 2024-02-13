
# Trailerx App

Application of trailers for trending, premiere and popular movies. Create your own movie watchlist. Built with Multi-Module Clean Architecture, MVVM, Room Cache, Retrofit, Hilt, Material design and best development development, testing and deployment practices, such as; SOLID, Design Patterns, BDD and DevOps culture.

![1  login](https://github.com/ben331/Trailerx/assets/54720004/9f03bb0c-fd4d-453e-88fc-a16ada164e67)
![2  home](https://github.com/ben331/Trailerx/assets/54720004/cde313dc-bd2b-49ab-a346-baa7918d9f2b)
![3  movie](https://github.com/ben331/Trailerx/assets/54720004/8ac1781b-a918-4609-8059-64f304c54273)
![4  search](https://github.com/ben331/Trailerx/assets/54720004/0d1dc8cc-744e-46f7-b1e6-5a6b0582f97f)
![5  profile](https://github.com/ben331/Trailerx/assets/54720004/3693cba7-9441-4e22-a1bb-676ea1584648)

## Settings Development enviroment
1. Install lastest version of android studio
2. Be sure you are using the same version of java specified in project build.gradle files. (JAVA 17)

## Installation

1. Sync gradle
2. Download firebase credentials: [Guide](https://firebase.google.com/docs/android/setup?hl=es&authuser=0&_gl=1*ti29fm*_ga*MTI5MzgzMjkwNC4xNjk3MDQ4OTkw*_ga_CW55HF8NVT*MTcwMzY5NDM3Mi41NS4xLjE3MDM2OTQ3OTIuMzAuMC4w#add-config-file). Then, switch to project view in android studio and move the downloaded *google-services.json* file to *app/* directory.

3. Go to Facebook developer app > Settings > Basic and copy the app identifier and the secret key: [Guide](https://developers.facebook.com/docs/facebook-login/android/#manifest). Then create the file secrets.xml in app > src > main > res > values.

```xml
<string name="facebook_app_id">1234</string>
<string name="fb_login_protocol_scheme">fb1234</string>
<string name="facebook_client_token">*******</string>
```
*secrets.xml file is already ignored by git*

4. Create a new file secrets.xml in core > ui > src > main > res > values and create a key to encrypt user sessions:

```xml
...
<string name="tokens_secret_key">********</string>
```

5. Then, go to [TMDB account settings](https://www.themoviedb.org/settings/api) copy the *API Read Access Token* and add it to data > movies > src > main > res > values > secrets.xml file .
```xml
...
<string name="TMDB_api_token">*******</string>
```

6. Copy the account_id from [TMBD Api](https://developer.themoviedb.org/reference/account-details) and replace it in the Constants.kt file at core > common > main > java > tech.benhack.common > Constants:

```kotlin
  object Constants {
    ...
    const val TMDB_ACCOUNT= "*****"
  }
```

7. For deployment ask for release KeyStore and their credentials.

8. For the account delete functionality to work correctly, you need to publish the lambda function to path: firebase-functions/index.js in firebase functions. If you want to add a different firebase account you must delete the folder firebase-functions/functions and run the next commands in the directory firebase-functions:

```bash
  npm install -g firebase-tools
  firebase login
  firebase init
```
Then copy the firebase-functions/index.js file into firebase-functions/functions directory and there run:

```bash
  npm install
  firebase deploy
```
   
If you want to use the same firebase account, just run in firebase-functions/functions directory:

```bash
  npm install
  firebase deploy
```

9. Finally for AuthLogin be sure that both DebugKeyStore and ReleaseKeyStore SHA1 is registered in firebase settings, facebook platform developers app and apple.
    
## Authors

- [@Benjamin Silva](https://github.com/ben331)

