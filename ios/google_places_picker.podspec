#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'google_places_picker'
  s.version          = '0.0.3'
  s.summary          = 'Flutter plugin for Google Places and Autocomplete'
  s.description      = <<-DESC
  Flutter plugin for Google Places Autocomplete
                       DESC
  s.homepage         = 'https://github.com/derTuca/flutter_google_places_picker'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Alexandru Tuca' => 'salexandru.tuca@outlook.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'GooglePlaces', '>= 7.0'
  s.static_framework = true
  s.ios.deployment_target = '14.0'
end

