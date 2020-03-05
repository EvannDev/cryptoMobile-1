# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Entry points
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.LoginActivity

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

#-renamesourcefileattribute SourceFile
#-keep Name

# You can specify any path and filename.
# To output a full report of all the rules that R8 applies when building the project
-printconfiguration ~/tmp/full-r8-config.txt

