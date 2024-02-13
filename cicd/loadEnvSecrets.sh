#!/bin/sh

# Define the file paths
google_services_path="$PROJECT_LOCATION/app/google-services.json"
app_secrets_path="$PROJECT_LOCATION/app/src/main/res/values/secrets.xml"
ui_secrets_path="$PROJECT_LOCATION/core/ui/src/main/res/values/secrets.xml"
movies_secrets_path="$PROJECT_LOCATION/data/movies/src/main/res/values/secrets.xml"
# shellcheck disable=SC2034
common_constants_path="$PROJECT_LOCATION/core/common/src/main/java/tech/benhack/common/Constants.kt"


# Append environment variables to files
echo "$GOOGLE_SERVICES_FILE" >> "$google_services_path"

{
  echo "<string name=\"facebook_app_id\">$FACEBOOK_APP_ID</string>"
  echo "<string name=\"fb_login_protocol_scheme\">$FB_LOGIN_PROTOCOL_SCHEME</string>"
  echo "<string name=\"facebook_client_token\">$FACEBOOK_CLIENT_TOKEN</string>"
} >> "$app_secrets_path"

echo echo "<string name=\"tokens_secret_key\">$TOKENS_SECRET_KEY</string>" >> "$ui_secrets_path"
echo echo "<string name=\"TMDB_api_token\">$TMDB_API_TOKEN</string>" >> "$movies_secrets_path"
sed -i "s/REPLACE_WITH_TMDB_ACCOUNT/$TMDB_ACCOUNT/" common_constants_path