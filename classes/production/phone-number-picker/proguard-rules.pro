-keep public class com.yash.phonenumberpicker.PhoneNumberPicker {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/yash/phonenumberpicker/repack'
-flattenpackagehierarchy
-dontpreverify
