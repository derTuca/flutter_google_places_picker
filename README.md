# google_places_picker

Google Places Autocomplete for Flutter

## Getting Started

### Setting up
1. Run the `initialize` method in your `main.dart`'s `initState` (or anywhere it would only be called once) with your API keys as arguments:
```dart
import 'package:google_places_picker/google_places_picker.dart';

PluginGooglePlacePicker.initialize(
      androidApiKey: "YOUR_ANDROID_API_KEY",
      iosApiKey: "YOUR_IOS_API_KEY",
);
```

### Usage

You can use the plugin via the `showAutocomplete` methods, which takes a PlaceAutocompleteMode paramater to know whether to display the fullscreen or the overlay control on Android (it has no effect on iOS). It returns a `Place` object with the following properties:

- name
- id
- address
- latitude
- longitude

## Place Picker deprecation

As of 2019-01-27, the Place Picker has been deprecated by Google. As such, this plugin has removed that functionality. If you want to keep using it until 2019-07-29, when it will be completely disabled, switch to the `legacy` branch:
```yaml
google_places_picker:
    git:
      url: https://github.com/derTuca/flutter-contacts-plugin.git
      ref: legacy
```

The initialization steps for the old version of the plugin are as follows:
1. Go to your `AndroidManifest.xml` at `android/app/src/main` and add the following in between the `application` opening and closing tag, replacing `YOUR_API_KEY` with your api key, which you can get from the Google Developer Console:
```
<meta-data
      android:name="com.google.android.geo.API_KEY"
      android:value="YOUR_API_KEY"/>
```
2. Go to `AppDelegate.m/.swift` in `ios/Runner`, and in the `didFinishLaunchingWithOptions` method add the following lines:

- Swift
```
import GooglePlaces
import GoogleMaps

GMSPlacesClient.provideAPIKey("YOUR_API_KEY")
GMSServices.provideAPIKey("YOUR_API_KEY")
```

- Objective-C
```objectivec
@import GooglePlaces;
@import GoogleMaps;

[GMSPlacesClient provideAPIKey:@"YOUR_API_KEY"];
[GMSServices provideAPIKey:@"YOUR_API_KEY"];
```