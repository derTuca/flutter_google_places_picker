#import <Flutter/Flutter.h>
@import GooglePlacePicker;

@interface GooglePlacesPickerPlugin : NSObject<FlutterPlugin, GMSPlacePickerViewControllerDelegate, GMSAutocompleteViewControllerDelegate>
@end
