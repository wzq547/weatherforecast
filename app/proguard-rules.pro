# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\wzq547\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#// 排除okhttp
  -dontwarn com.squareup.**
  -dontwarn okio.**
#  -keep public class org.codehaus.* { *; }
#  -keep public class java.nio.* { *; }

#// 排除HeWeather
  -dontwarn interfaces.heweather.com.interfacesmodule.**
#  -keep class interfaces.heweather.com.interfacesmodule.** { *;}