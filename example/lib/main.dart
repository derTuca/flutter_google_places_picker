import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
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
    // Platform messages may fail, so we use a try/catch PlatformException.
    var place = await PluginGooglePlacePicker.showAutocomplete(PlaceAutocompleteMode.MODE_OVERLAY);
    placeName = place.name;


    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted)
      return;

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
              new FlatButton(onPressed: _showAutocomplete, child: new Text("Show autocomplete")),
              new Row(
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
