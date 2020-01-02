package com.theantimony.googleplacespicker

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.lang.Exception


class GooglePlacesPickerPlugin() : FlutterPlugin, MethodCallHandler, PluginRegistry.ActivityResultListener, ActivityAware {
    var mActivity: Activity? = null
    var mChannel: MethodChannel? = null
    var mBinding: ActivityPluginBinding? = null

    private var mResult: Result? = null
    private val mFilterTypes = mapOf(
            Pair("address", TypeFilter.ADDRESS),
            Pair("cities", TypeFilter.CITIES),
            Pair("establishment", TypeFilter.ESTABLISHMENT),
            Pair("geocode", TypeFilter.GEOCODE),
            Pair("regions", TypeFilter.REGIONS)
    )

    companion object {
        const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 57864

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val instance = GooglePlacesPickerPlugin().apply {
                mActivity = registrar.activity()
            }
            registrar.addActivityResultListener(instance)
            instance.onAttachedToEngine(registrar.messenger())
        }
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onAttachedToEngine(binding.binaryMessenger)
    }

    private fun onAttachedToEngine(messenger: BinaryMessenger) {
        mChannel = MethodChannel(messenger, "plugin_google_place_picker")
        mChannel?.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        mResult = result
        if (call.method.equals("showAutocomplete")) {
            showAutocompletePicker(
                    call.argument("mode"),
                    call.argument("bias"),
                    call.argument("restriction"),
                    call.argument("type"),
                    call.argument("country")
            )
        } else if (call.method.equals("initialize")) {
            initialize(call.argument("androidApiKey"))
        } else {
            result.notImplemented()
        }
    }

    fun initialize(apiKey: String?) {
        if (apiKey.isNullOrEmpty()) {
            mResult?.error("API_KEY_ERROR", "Invalid Android API Key", null)
            return
        }
        try {
            if (!Places.isInitialized()) {
                mActivity?.let {
                    Places.initialize(it.applicationContext, apiKey)
                }
            }
            mResult?.success(null)
        } catch (e: Exception) {
            mResult?.error("API_KEY_ERROR", e.localizedMessage, null)
        }
    }

    private fun showAutocompletePicker(
            mode: Int?,
            bias: HashMap<String, Double>?,
            restriction: HashMap<String, Double>?,
            type: String?,
            country: String?
    ) {
        val modeToUse = mode ?: 71
        val fields = listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.NAME,
                Place.Field.LAT_LNG
        )
        var intentBuilder = Autocomplete.IntentBuilder(if (modeToUse == 71) AutocompleteActivityMode.OVERLAY else AutocompleteActivityMode.FULLSCREEN, fields)

        bias?.let {
            val locationBias = RectangularBounds.newInstance(
                    LatLng(it["southWestLat"] ?: 0.0, it["southWestLng"] ?: 0.0),
                    LatLng(it["northEastLat"] ?: 0.0, it["northEastLng"] ?: 0.0)
            )
            intentBuilder = intentBuilder.setLocationBias(locationBias)
        }

        restriction?.let {
            val locationRestriction = RectangularBounds.newInstance(
                    LatLng(it["southWestLat"] ?: 0.0, it["southWestLng"] ?: 0.0),
                    LatLng(it["northEastLat"] ?: 0.0, it["northEastLng"] ?: 0.0)
            )
            intentBuilder = intentBuilder.setLocationRestriction(locationRestriction)
        }

        type?.let {
            intentBuilder = intentBuilder.setTypeFilter(mFilterTypes[it])
        }

        country?.let {
            intentBuilder = intentBuilder.setCountry(it)
        }

        mActivity?.let {
            val intent = intentBuilder.build(it)

            try {
                it.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
            } catch (e: GooglePlayServicesNotAvailableException) {
                mResult?.error("GooglePlayServicesNotAvailableException", e.message, null)
            } catch (e: GooglePlayServicesRepairableException) {
                mResult?.error("GooglePlayServicesRepairableException", e.message, null)
            }
        }



    }

    override fun onActivityResult(p0: Int, p1: Int, p2: Intent?): Boolean {
        if (p0 != PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            return false
        }
        if (p1 == RESULT_OK && p2 != null) {
            val place = Autocomplete.getPlaceFromIntent(p2)
            val placeMap = mutableMapOf<String, Any>()
            placeMap.put("latitude", place.latLng?.latitude ?: 0.0)
            placeMap.put("longitude", place.latLng?.longitude ?: 0.0)
            placeMap.put("id", place.id ?: "")
            placeMap.put("name", place.name ?: "")
            placeMap.put("address", place.address ?: "")
            mResult?.success(placeMap)
        } else if (p1 == AutocompleteActivity.RESULT_ERROR && p2 != null) {
            val status = Autocomplete.getStatusFromIntent(p2)
            mResult?.error("PLACE_AUTOCOMPLETE_ERROR", status.statusMessage, null)
        } else if (p1 == RESULT_CANCELED) {
            mResult?.error("USER_CANCELED", "User has canceled the operation.", null)
        } else {
            mResult?.error("UNKNOWN", "Unknown error.", null)
        }
        return true
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mActivity = null
        mChannel?.setMethodCallHandler(null)
        mBinding?.removeActivityResultListener(this)
        mChannel = null
        mBinding = null
    }

    override fun onDetachedFromActivity() {
        mActivity = null
        mBinding?.removeActivityResultListener(this)
        mBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        mActivity = binding.activity
        mBinding = binding
        binding.addActivityResultListener(this)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        mActivity = binding.activity
        mBinding = binding
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        mActivity = null
        mBinding?.removeActivityResultListener(this)
        mBinding = null
    }
}
