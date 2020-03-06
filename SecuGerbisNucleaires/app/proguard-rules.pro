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
-android
-dontpreverify
-allowaccessmodification
-optimizations !code/simplification/arithmetic

-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.LoginActivity{fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.AddPatientActivity{ fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.AddVisitActivity{fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.EditPersonalActivity{fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.PatientsInfoActivity{fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.PersonalInfoActivity{fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.SignUpActivity{fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.SpecificPatientActivity{fun onCreate();}
-keep class fr.isen.gerbisnucleaires.secugerbisnucleaires.SpecificVisitActivity{fun onCreate();}

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

