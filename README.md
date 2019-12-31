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