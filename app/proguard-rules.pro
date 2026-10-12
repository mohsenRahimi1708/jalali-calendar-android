# Keep rules for kotlinx.serialization (generated serializers)
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class com.example.jalicalendar.** {
    kotlinx.serialization.KSerializer serializer(...);
}
# AppAuth
-keep class net.openid.appauth.** { *; }
# Retrofit / OkHttp
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keepattributes Signature, Exceptions
