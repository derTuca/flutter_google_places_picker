package com.theantimony.googleplacespicker

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlacePicker
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar



class GooglePlacesPickerPlugin(): MethodCallHandler, PluginRegistry.ActivityResultListener {
  lateinit var mActivity: Activity
  var mResult: Result? = null

  companion object {
    val PLACE_PICKER_REQUEST_CODE = 131070
    val PLACE_AUTOCOMPLETE_REQUEST_CODE = 131071

    @JvmStatic
    fun registerWith(registrar: Registrar): Unit {
      val channel = MethodChannel(registrar.messenger(), "plugin_google_place_picker")
      val instance = GooglePlacesPickerPlugin().apply {
        mActivity = registrar.activity()
      }
      registrar.addActivityResultListener(instance)
      channel.setMethodCallHandler(instance)
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result): Unit {
    mResult = result
    if (call.method.equals("showPlacePicker")) {
      showPlacesPicker()
    } else if (call.method.equals("showAutocomplete")) {
      showAutocompletePicker(call.argument("mode"))
    } else {
      result.notImplemented()
    }
  }

  fun showPlacesPicker() {
    val builder = PlacePicker.IntentBuilder()
    try {
      mActivity.startActivityForResult(builder.build(mActivity), PLACE_PICKER_REQUEST_CODE)
    } catch (e: GooglePlayServicesRepairableException) {
      mResult?.error("GooglePlayServicesRepairableException", e.message, null)
    } catch (e: GooglePlayServicesNotAvailableException) {
      mResult?.error("GooglePlayServicesNotAvailableException", e.message, null)
    }


  }

  fun showAutocompletePicker(mode: Int?) {
    val modeToUse = mode ?: 71
    val intent = PlaceAutocomplete.IntentBuilder(if (modeToUse == 71) PlaceAutocomplete.MODE_OVERLAY else PlaceAutocomplete.MODE_FULLSCREEN).build(mActivity)
    try {
      mActivity.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
    } catch (e: GooglePlayServicesNotAvailableException) {
      mResult?.error("GooglePlayServicesNotAvailableException", e.message, null)
    } catch (e: GooglePlayServicesRepairableException) {
      mResult?.error("GooglePlayServicesRepairableException", e.message, null)
    }

  }

  override fun onActivityResult(p0: Int, p1: Int, p2: Intent?): Boolean {
    if (p1 == RESULT_OK) {
      when (p0) {
        PLACE_PICKER_REQUEST_CODE -> {
          val place = PlacePicker.getPlace(mActivity, p2)
          val placeMap = mutableMapOf<String, Any>()
          placeMap.put("latitude", place.latLng.latitude.toString() + "")
          placeMap.put("longitude", place.latLng.longitude.toString() + "")
          placeMap.put("id", place.id)
          placeMap.put("name", place.name.toString())
          placeMap.put("address", place.address.toString())
          mResult?.success(placeMap)
          return true

        }
        PLACE_AUTOCOMPLETE_REQUEST_CODE -> {
          val place = PlaceAutocomplete.getPlace(mActivity, p2)
          val placeMap = mutableMapOf<String, Any>()
          placeMap.put("latitude", place.latLng.latitude)
          placeMap.put("longitude", place.latLng.longitude)
          placeMap.put("id", place.id)
          placeMap.put("name", place.name.toString())
          placeMap.put("address", place.address.toString())
          mResult?.success(placeMap)
          return true
        }
      }
    } else if (p1 == PlaceAutocomplete.RESULT_ERROR) {
      val status = PlaceAutocomplete.getStatus(mActivity, p2)
      mResult?.error("PLACE_AUTOCOMPLETE_ERROR", status.statusMessage, null)
    } else if (p1 == PlacePicker.RESULT_ERROR) {
      val status = PlacePicker.getStatus(mActivity, p2)
      mResult?.error("PLACE_PICKER_ERROR", status.statusMessage, null)
    } else if (p1 == RESULT_CANCELED) {
      mResult?.error("USER_CANCELED", "User has canceled the operation.", null)
    } else {
      mResult?.error("UNKNOWN", "Unknown error.", null)
    }
    return false
  }
}
