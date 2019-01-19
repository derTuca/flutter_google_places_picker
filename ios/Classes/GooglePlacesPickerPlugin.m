#import "GooglePlacesPickerPlugin.h"
@import GoogleMaps;
@import GooglePlaces;
@import GooglePlacePicker;



@implementation GooglePlacesPickerPlugin
FlutterResult _result;
UIViewController *vc;
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    vc = [UIApplication sharedApplication].delegate.window.rootViewController;
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"plugin_google_place_picker"
            binaryMessenger:[registrar messenger]];
  GooglePlacesPickerPlugin* instance = [[GooglePlacesPickerPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    _result = result;
  if ([@"showPlacePicker" isEqualToString:call.method]) {
      [self showPlacePicker];
  } else if ([@"showAutocomplete" isEqualToString:call.method]) {
      [self showAutocomplete];
  } else {
    result(FlutterMethodNotImplemented);
  }
}

-(void)showPlacePicker {
    
    GMSPlacePickerConfig *config = [[GMSPlacePickerConfig alloc] initWithViewport:nil];
    GMSPlacePickerViewController *placePicker = [[GMSPlacePickerViewController alloc] initWithConfig:config];
    placePicker.delegate = self;
    [vc presentViewController:placePicker animated:YES completion:nil];
}

-(void)showAutocomplete {
    GMSAutocompleteViewController *autocompleteController = [[GMSAutocompleteViewController alloc] init];
    autocompleteController.delegate = self;
    UIViewController *vc = [UIApplication sharedApplication].delegate.window.rootViewController;
    [vc presentViewController:autocompleteController animated:YES completion:nil];
    
}

- (void)placePicker:(nonnull GMSPlacePickerViewController *)viewController didPickPlace:(nonnull GMSPlace *)place {
    [vc dismissViewControllerAnimated:YES completion:nil];
    NSDictionary *placeMap = @{
                               @"name" : place.name,
                               @"latitude" : [NSString stringWithFormat:@"%.7f", place.coordinate.latitude],
                               @"longitude" : [NSString stringWithFormat:@"%.7f", place.coordinate.longitude],
                               @"id" : place.placeID,
                               };
    NSMutableDictionary *mutablePlaceMap = placeMap.mutableCopy;
    if (place.formattedAddress != nil) {
        mutablePlaceMap[@"address"] = place.formattedAddress;
    }
    _result(mutablePlaceMap);
    
}

- (void)viewController:(nonnull GMSAutocompleteViewController *)viewController didAutocompleteWithPlace:(nonnull GMSPlace *)place {
    [vc dismissViewControllerAnimated:YES completion:nil];
    NSDictionary *placeMap = @{
                               @"name" : place.name,
                               @"latitude" : [NSString stringWithFormat:@"%.7f", place.coordinate.latitude],
                               @"longitude" : [NSString stringWithFormat:@"%.7f", place.coordinate.longitude],
                               @"id" : place.placeID,
                               };
    NSMutableDictionary *mutablePlaceMap = placeMap.mutableCopy;
    if (place.formattedAddress != nil) {
        mutablePlaceMap[@"address"] = place.formattedAddress;
    }
    _result(mutablePlaceMap);
}

- (void)viewController:(nonnull GMSAutocompleteViewController *)viewController didFailAutocompleteWithError:(nonnull NSError *)error {
    [vc dismissViewControllerAnimated:YES completion:nil];
    FlutterError *fError = [FlutterError errorWithCode:@"PLACE_AUTOCOMPLETE_ERROR" message:error.localizedDescription details:nil];
    _result(fError);
}

- (void)placePicker:(GMSPlacePickerViewController *)viewController didFailWithError:(NSError *)error {
    [vc dismissViewControllerAnimated:YES completion:nil];
    FlutterError *fError = [FlutterError errorWithCode:@"PLACE_PICKER_ERROR" message:error.localizedDescription details:nil];
    _result(fError);
}

- (void)wasCancelled:(nonnull GMSAutocompleteViewController *)viewController {
    [vc dismissViewControllerAnimated:YES completion:nil];
    FlutterError *fError = [FlutterError errorWithCode:@"USER_CANCELED" message:@"User has canceled the operation." details:nil];
    _result(fError);
}

- (void)placePickerDidCancel:(GMSPlacePickerViewController *)viewController {
    [vc dismissViewControllerAnimated:YES completion:nil];
    FlutterError *fError = [FlutterError errorWithCode:@"USER_CANCELED" message:@"User has canceled the operation." details:nil];
    _result(fError);
}

@end
