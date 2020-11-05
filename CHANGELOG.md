## [2.1.0+3] - 2020-11-04

* Fixed build errors on iOS caused by the 4.0.0 release of the Places iOS SDK.
* Locked the Places iOS SDK to '~>4.0'.
* Updated Kotlin version for Android. 

## [2.1.0+2] - 2019-12-31

* Changed onActivityResult return value so other plugins work.

## [2.1.0+1] - 2019-12-31

* Removed memory leak introduced in latest update.

## [2.1.0] - 2019-12-31

* Updated plugin to use v2 Android embedding
* Lowered Intent request code so it fits in the lower 16bits
* Updated Android Google Places SDK

## [2.0.2+2] - 2019-09-16

* Fixed swapped north-east and south-west coordinated on `bias` and `restriction`.

## [2.0.2+1] - 2019-06-03

* Fixed Kotlin Smart Cast not working on certain setups.

## [2.0.2] - 2019-05-24

* Updated Android Gradle and Kotlin versions.

## [2.0.1] - 2019-04-20

* Added check for request code in `onActivityResult` so we don't swallow other plugins' callbacks.

## [2.0.0] - 2019-03-16

* Added option to filter options via type filter, restrict bounds, bias bounds and country.

## [1.0.0] - 2019-03-16

* Removed Place Picker from plugin as it is deprecated by Google

## [0.1.0] - 2019-01-19

* Fixed crashes on iOS devices when canceling the Place Picker or the Autocomplete

## [0.0.9] - 2018-11-03

* Fixed more Android build errors

## [0.0.8] - 2018-11-03

* Updated Kotlin version

## [0.0.7] - 2018-11-03

* Fixed compile error on Flutter 0.10.x

## [0.0.6] - 2018-09-21

* Fixed scenario where random point selected in the Place Picker would crash the app due to no address

## [0.0.5] - 2018-09-03

* Updated sdk dependency for flutter 0.6.0
