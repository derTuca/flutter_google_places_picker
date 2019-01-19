# google_places_picker

Google Place Picker and Autocomplete for Flutter

## Getting Started

### Setting up
1. Go to your `AndroidManifest.xml` at `android/app/src/main` and add the following in between the `application` opening and closing tag, replacing `YOUR_API_KEY` with your api key, which you can get from the Google Developer Console:
```
<meta-data
      android:name="com.google.android.geo.API_KEY"
      android:value="YOUR_API_KEY"/>
```
2. Go to `AppDelegate.m/.swift` in `ios/Runner`, and in the `didFinishLaunchingWithOptions` method add the following lines:

- Swift
```$xslt

GMSPlacesClient.provideAPIKey("YOUR_API_KEY")
GMSSServices.provideAPIKey("YOUR_API_KEY")
```
- Objective-C
```$xslt
[GMSPlacesClient provideAPIKey:@"YOUR_API_KEY"];
[GMSServices provideAPIKey:@"YOUR_API_KEY"];
```

### Usage

You can use the plugin via the `showPlacePicker` and `showAutocomplete` methods. The `showAutocomplete` method takes a PlaceAutocompleteMode paramater to know whether to display the fullscreen or the overlay control on Android (it has no effect on iOS). Both methods return a `Place` object with the following properties:

- name
- id
- address
- latitude
- longitude