import 'package:flutter/material.dart';
import 'package:google_places_picker/google_places_picker.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _placeName = 'Unknown';

  @override
  initState() {
    super.initState();
    PluginGooglePlacePicker.initialize(
      androidApiKey: "YOUR_ANDROID_API_KEY",
      iosApiKey: "YOUR_IOS_API_KEY",
    );
  }

  _showAutocomplete() async {
    String placeName;
    var locationBias = LocationBias()
      ..northEastLat = 20.0
      ..northEastLng = 20.0
      ..southWestLat = 0.0
      ..southWestLng = 0.0;

    var locationRestriction = LocationRestriction()
      ..northEastLat = 20.0
      ..northEastLng = 20.0
      ..southWestLng = 0.0
      ..southWestLat = 0.0;

    var country = "US";

    // Platform messages may fail, so we use a try/catch PlatformException.
    var place = await PluginGooglePlacePicker.showAutocomplete(
        mode: PlaceAutocompleteMode.MODE_OVERLAY,
        countryCode: country,
        restriction: locationRestriction,
        typeFilter: TypeFilter.ESTABLISHMENT);
    placeName = place.name ?? "Null place name!";

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _placeName = placeName;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Place picker example app'),
        ),
        body: new Center(
          child: new Column(
            children: <Widget>[
              TextButton(
                  onPressed: _showAutocomplete,
                  child: new Text("Show autocomplete")),
              Row(
                children: <Widget>[
                  new Text("Place name: "),
                  new Text(_placeName)
                ],
              )
            ],
          ),
        ),
      ),
    );
  }
}
