#!/bin/sh

# Define the file paths
app_secrets_path="$PROJECT_LOCATION/app/src/main/res/values/secrets.xml"
ui_secrets_path="$PROJECT_LOCATION/core/ui/src/main/res/values/secrets.xml"
movies_secrets_path="$PROJECT_LOCATION/data/movies/src/main/res/values/secrets.xml"

sed -i "s/REPLACE_WITH_TMDB_ACCOUNT/$TMDB_ACCOUNT/" "$PROJECT_LOCATION/core/common/src/main/java/tech/benhack/common/Constants.kt"

# Append environment variables to files

{
  echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
  echo "<resources>"
  echo "  <string name=\"facebook_app_id\">$FACEBOOK_APP_ID</string>"
  echo "  <string name=\"fb_login_protocol_scheme\">$FB_LOGIN_PROTOCOL_SCHEME</string>"
  echo "  <string name=\"facebook_client_token\">$FACEBOOK_CLIENT_TOKEN</string>"
  echo "</resources>"
} >> "$app_secrets_path"

{
    echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    echo "<resources>"
    echo "  <string name=\"tokens_secret_key\">$TOKENS_SECRET_KEY</string>"
    echo "</resources>"
} >> "$ui_secrets_path"

{
      echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
      echo "<resources>"
      echo "  <string name=\"TMDB_api_token\">$TMDB_API_TOKEN</string>"
      echo "</resources>"
} >> "$movies_secrets_path"