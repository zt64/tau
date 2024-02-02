-keepattributes Signature,LineNumberTable

-keep,includedescriptorclasses,allowoptimization class dev.zt64.tau.** { public protected *; }
-keep class dev.zt64.tau.MainKt {
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

# https://github.com/ajalt/mordant/blob/master/mordant/src/jvmMain/resources/META-INF/proguard/mordant.pro
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,RuntimeVisibleTypeAnnotations,AnnotationDefault
-dontwarn org.graalvm.**
-dontwarn com.oracle.svm.core.annotate.Delete