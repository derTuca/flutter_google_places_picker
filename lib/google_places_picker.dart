import 'dart:async';

import 'package:flutter/services.dart';

class Place {
  double latitude;
  double longitude;
  String id;
  String name;
  String address;
}

enum PlaceAutocompleteMode {
  MODE_OVERLAY,
  MODE_FULLSCREEN

}
class PluginGooglePlacePicker {

  static const MethodChannel _channel =
      const MethodChannel('plugin_google_place_picker');

  static Future<Place> showPlacePicker() async {
    final Map placeMap = await _channel.invokeMethod('showPlacePicker');
    return _initPlaceFromMap(placeMap);
  }

  static Future<Place> showAutocomplete(PlaceAutocompleteMode mode) async {
    var argMap = new Map();
//    Random values
    argMap["mode"] = mode == PlaceAutocompleteMode.MODE_OVERLAY ? 71 : 72;
    final Map placeMap = await _channel.invokeMethod('showAutocomplete', argMap);
    return _initPlaceFromMap(placeMap);
  }

  static Place _initPlaceFromMap(Map placeMap) {
    if (placeMap["latitude"] is double) {
      return new Place()
        ..name = placeMap["name"]
        ..id = placeMap["id"]
        ..address = placeMap["address"]
        ..latitude = placeMap["latitude"]
        ..longitude = placeMap["longitude"];
    }
    else {
      return new Place()
        ..name = placeMap["name"]
        ..id = placeMap["id"]
        ..address = placeMap["address"]
        ..latitude = double.parse(placeMap["latitude"])
        ..longitude = double.parse(placeMap["longitude"]);
    }

  }

}
