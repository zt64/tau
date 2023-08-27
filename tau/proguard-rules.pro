-keepattributes Signature,LineNumberTable

-keep,includedescriptorclasses,allowoptimization class zt.tau.** { public protected *; }
-keep class zt.tau.MainKt {
    public static void main(java.lang.String[]);
}
-keep class 'module-info'

# Kotlin
-dontwarn kotlinx.datetime.**

# Log4J
-dontwarn org.apache.logging.log4j.**
-dontwarn org.apache.commons.logging.**
-keep,includedescriptorclasses class org.apache.logging.log4j.** { *; }

# SLF4J
-dontwarn org.apache.logging.slf4j.**
-keep,includedescriptorclasses class org.apache.logging.slf4j.** { *; }

# XML
-dontwarn org.apache.batik.**
-dontwarn javax.xml.**
-dontwarn jdk.xml.**
-dontwarn org.w3c.dom.**
-dontwarn org.xml.**

# Other
-dontwarn org.apache.tika.**
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }