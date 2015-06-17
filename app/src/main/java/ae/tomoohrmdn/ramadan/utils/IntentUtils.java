package ae.tomoohrmdn.ramadan.utils;

import java.io.File;
import java.util.List;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;


public class IntentUtils {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Static Fields
    // ===========================================================

    // ===========================================================
    // Static Initializers
    // ===========================================================

    // ===========================================================
    // Static Methods
    // ===========================================================

    /**
     * Checks whether there are applications installed which are able to handle the given
     * action/data.
     *
     * @param context
     *            the current context
     * @param action
     *            the action to check
     * @param uri
     *            that data URI to check (may be null)
     * @param mimeType
     *            the MIME type of the content (may be null)
     * @return true if there are apps which will respond to this action/data
     */
    public static boolean
    isIntentAvailable(Context context, String action, Uri uri, String mimeType) {
        final Intent intent = (uri != null) ? new Intent(action, uri) : new Intent(action);
        if (mimeType != null) {
            intent.setType(mimeType);
        }
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    public static boolean isAppInstalled(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * Checks whether there are applications installed which are able to handle the given
     * action/type.
     *
     * @param context
     *            the current context
     * @param action
     *            the action to check
     * @param mimeType
     *            the MIME type of the content (may be null)
     * @return true if there are apps which will respond to this action/type
     */
    public static boolean isIntentAvailable(Context context, String action, String mimeType) {
        final Intent intent = new Intent(action);
        if (mimeType != null) {
            intent.setType(mimeType);
        }
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * Checks whether there are applications installed which are able to handle the given intent.
     *
     * @param context
     *            the current context
     * @param intent
     *            the intent to check
     * @return true if there are apps which will respond to this intent
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * Starts an intent using {@link Context#startActivity(Intent)} and ignores
     * {@link ActivityNotFoundException} if thrown.
     *
     * @param context
     *            The Context used to start the activity.
     * @param intent
     *            The description of the activity to start.
     */
    public static void startActivityIgnoreException(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
        }
    }

    /**
     * Intent to install an application package (APK) file
     *
     * @param apkPath
     *            path to the APK file to install
     * @return
     */
    public static Intent newApkInstallIntent(String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * Intent to uninstall an application package.
     *
     * @param packageName
     *            The package name of the application to uninstall
     * @return
     */
    public static Intent newAppUninstallIntent(String packageName) {
        return new Intent(Intent.ACTION_DELETE,
                Uri.parse("package:" + packageName));
    }

    /**
     * Create an intent to send an email to a single recipient
     *
     * @param address
     *            The recipient address (or null if not specified)
     * @param subject
     *            The subject of the email (or null if not specified)
     * @param body
     *            The body of the email (or null if not specified)
     * @return the intent
     */
    public static Intent newEmailIntent(String address, String subject, String body) {
        return newEmailIntent(address, subject, body, null);
    }

    /**
     * Create an intent to send an email with an attachment to a single recipient
     *
     * @param address
     *            The recipient address (or null if not specified)
     * @param subject
     *            The subject of the email (or null if not specified)
     * @param body
     *            The body of the email (or null if not specified)
     * @param attachment
     *            The URI of a file to attach to the email. Note that the URI must point to a
     *            location the email application is allowed to read and has permissions to access.
     * @return the intent
     */
    public static Intent
    newEmailIntent(String address, String subject, String body, Uri attachment) {
        return newEmailIntent(address == null ? null : new String[] {
                address
        }, subject, body, attachment);
    }

    /**
     * Create an intent to send an email with an attachment
     *
     * @param addresses
     *            The recipients addresses (or null if not specified)
     * @param subject
     *            The subject of the email (or null if not specified)
     * @param body
     *            The body of the email (or null if not specified)
     * @param attachment
     *            The URI of a file to attach to the email. Note that the URI must point to a
     *            location the email application is allowed to read and has permissions to access.
     * @return the intent
     */
    public static Intent newEmailIntent(String[] addresses, String subject, String body,
                                        Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (addresses != null) intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if (body != null) intent.putExtra(Intent.EXTRA_TEXT, body);
        if (subject != null) intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (attachment != null) intent.putExtra(Intent.EXTRA_STREAM, attachment);
        intent.setType("message/rfc822");
        return intent;
    }

    /**
     * Intent that should allow opening a map showing the given address (if it exists)
     *
     * @param address
     *            The address to search
     * @param placeTitle
     *            The title to show on the marker
     * @return the intent
     */
    public static Intent newMapsIntent(String address, String placeTitle) {
        StringBuilder sb = new StringBuilder();
        sb.append("geo:0,0?q=");

        String addressEncoded = Uri.encode(address);
        sb.append(addressEncoded);

        // pass text for the info window
        String titleEncoded = Uri.encode("(" + placeTitle + ")");
        sb.append(titleEncoded);

        // set locale; probably not required for the maps app?
        sb.append("&hl=" + Locale.getDefault().getLanguage());

        return new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
    }

    /**
     * Opens the Maps application to the given location.
     *
     * @param latitude
     *            Latitude
     * @param longitude
     *            Longitude
     * @param zoomLevel
     *            A zoom level of 1 shows the whole Earth, centered at the given lat,lng. A zoom
     *            level of 2 shows a quarter of the Earth, and so on. The highest zoom level is 23.
     *            A larger zoom level will be clamped to 23.
     *
     */
    public static Intent newShowLocationIntent(float latitude, float longitude, Integer zoomLevel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String data = String.format("geo:%s,%s", latitude, longitude);
        if (zoomLevel != null) {
            data = String.format("%s?z=%s", data, zoomLevel);
        }
        intent.setData(Uri.parse(data));
        return intent;
    }

    /**
     * Opens the Street View application to the given location. The URI scheme is based on the
     * syntax used for Street View panorama information in Google Maps URLs.
     *
     * @param latitude
     *            Latitude
     * @param longitude
     *            Longitude
     * @param yaw
     *            Panorama center-of-view in degrees clockwise from North.
     *            <p/>
     *            Note: The two commas after the yaw parameter are required. They are present for
     *            backwards-compatibility reasons.
     * @param pitch
     *            Panorama center-of-view in degrees from -90 (look straight up) to 90 (look
     *            straight down.)
     * @param zoom
     *            Panorama zoom. 1.0 = normal zoom, 2.0 = zoomed in 2x, 3.0 = zoomed in 4x, and so
     *            on. A zoom of 1.0 is 90 degree horizontal FOV for a nominal landscape mode 4 x 3
     *            aspect ratio display Android phones in portrait mode will adjust the zoom so that
     *            the vertical FOV is approximately the same as the landscape vertical FOV. This
     *            means that the horizontal FOV of an Android phone in portrait mode is much
     *            narrower than in landscape mode. This is done to minimize the fisheye lens effect
     *            that would be present if a 90 degree horizontal FOV was used in portrait mode.
     * @param mapZoom
     *            The map zoom of the map location associated with this panorama. This value is
     *            passed on to the Maps activity when the Street View "Go to Maps" menu item is
     *            chosen. It corresponds to the zoomLevel parameter in
     *
     */
    public static Intent newStreetViewIntent(float latitude,
                                             float longitude,
                                             Float yaw,
                                             Integer pitch,
                                             Float zoom,
                                             Integer mapZoom) {
        StringBuilder builder = new StringBuilder("google.streetview:cbll=").append(latitude)
                .append(",").append(longitude);
        if (yaw != null || pitch != null || zoom != null) {
            String cbpParam = String.format("%s,,%s,%s", yaw == null ? "" : yaw, pitch == null ? ""
                    : pitch, zoom == null ? "" : zoom);
            builder.append("&cbp=1,").append(cbpParam);
        }
        if (mapZoom != null) {
            builder.append("&mz=").append(mapZoom);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(builder.toString()));
        return intent;
    }

    /**
     * Open the video player to play the given
     *
     * @param url
     *            The URL of the video to play.
     * @return the intent
     */
    public static Intent newPlayVideoIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/*");
        return intent;
    }

    /**
     * Creates an intent that will launch a browser (most probably as other apps may handle specific
     * URLs, e.g. YouTube) to view the provided URL.
     *
     * @param url
     *            the URL to open
     * @return the intent
     */
    public static Intent newOpenWebBrowserIntent(String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        return intent;
    }

    /**
     * Creates an intent that will launch the camera to take a picture that's saved to a temporary
     * file so you can use it directly without going through the gallery.
     *
     * @param tempFile
     *            the file that should be used to temporarily store the picture
     * @return the intent
     */
    public static Intent newTakePictureIntent(File tempFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        return intent;
    }

    /**
     * Creates an intent that will launch the camera to take a picture that's saved to a temporary
     * file so you can use it directly without going through the gallery.
     *
     * @param tempFile
     *            the file that should be used to temporarily store the picture
     * @return the intent
     */
    public static Intent newTakePictureIntent(String tempFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempFile)));
        return intent;
    }

    /**
     * Creates an intent that will launch the phone's picture gallery to select a picture from it.
     *
     * @return the intent
     */
    public static Intent newSelectPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * Creates an intent that will allow to send an SMS without specifying the phone number
     *
     * @param body
     *            The text to send
     * @return the intent
     */
    public static Intent newSmsIntent(String body) {
        return newSmsIntent(null, body);
    }

    /**
     * Creates an intent that will allow to send an SMS to a phone number
     *
     * @param phoneNumber
     *            The phone number to send the SMS to (or null if you don't want to specify it)
     * @param body
     *            The text to send
     * @return the intent
     */
    public static Intent newSmsIntent(String phoneNumber, String body) {
        final Intent intent;
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        }
        intent.putExtra("sms_body", body);
        return intent;
    }

    /**
     * Creates an intent that will open the phone app and enter the given number. Unlike
     * {@link #newCallNumberIntent(String)}, this does not actually dispatch the call, so it gives
     * the user a chance to review and edit the number.
     *
     * @param phoneNumber
     *            the number to dial
     * @return the intent
     */
    public static Intent newDialNumberIntent(String phoneNumber) {
        final Intent intent;
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
        } else {
            intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + phoneNumber.replace(" ", "")));
        }
        return intent;
    }

    /**
     * Creates an intent that will immediately dispatch a call to the given number. NOTE that unlike
     * {@link #newDialNumberIntent(String)}, this intent requires the
     * {@link android.Manifest.permission#CALL_PHONE} permission to be set.
     *
     * @param phoneNumber
     *            the number to call
     * @return the intent
     */
    public static Intent newCallNumberIntent(String phoneNumber) {
        final Intent intent;
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"));
        } else {
            intent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:" + phoneNumber.replace(" ", "")));
        }
        return intent;
    }

    /**
     * Creates a chooser to share some data.
     *
     * @param subject
     *            The subject to share (might be discarded, for instance if the user picks an SMS
     *            app)
     * @param message
     *            The message to share
     * @param chooserDialogTitle
     *            The title for the chooser dialog
     * @return the intent
     */
    public static Intent newShareTextIntent(String subject, String message,
                                            String chooserDialogTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.setType("text/*");
        return Intent.createChooser(shareIntent, chooserDialogTitle);
    }

    /**
     * Pick file from sdcard with file manager. Chosen file can be obtained from Intent in
     * onActivityResult. See code below for example:
     *
     * <pre>
     * <code>
     * {@literal @Override}
     * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     *     Uri file = data.getData();
     * }
     * </code>
     * </pre>
     */
    public static Intent newPickFileIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        return intent;
    }

    /**
     * Intent that should open the app store of the device on the current application page
     *
     * @param context
     *            The context associated to the application
     * @return the intent
     */
    public static Intent newMarketForAppIntent(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        return newMarketForAppIntent(context, packageName);
    }

    /**
     * Intent that should open the app store of the device on the given application
     *
     * @param context
     *            The context associated to the application
     * @param packageName
     *            The package name of the application to find on the market
     * @return the intent or null if no market is available for the intent
     */
    public static Intent newMarketForAppIntent(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
                + packageName));

        if (!isIntentAvailable(context, intent)) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p="
                    + packageName));
        }

        if (!isIntentAvailable(context, intent)) {
            intent = null;
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        return intent;
    }

    /**
     * Intent that should open either the Google Play app or if not available, the web browser on
     * the Google Play website
     *
     * @param context
     *            The context associated to the application
     * @param packageName
     *            The package name of the application to find on the market
     * @return the intent for native application or an intent to redirect to the browser if google
     *         play is not installed
     */
    public static Intent newGooglePlayIntent(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
                + packageName));

        if (!isIntentAvailable(context, intent)) {
            intent = newOpenWebBrowserIntent("https://play.google.com/store/apps/details?id="
                    + packageName);
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        return intent;
    }

    /**
     * Intent that should open either the Amazon store app or if not available, the web browser on
     * the Amazon website
     *
     * @param context
     *            The context associated to the application
     * @param packageName
     *            The package name of the application to find on the market
     * @return the intent for native application or an intent to redirect to the browser if google
     *         play is not installed
     */
    public static Intent newAmazonStoreIntent(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p="
                + packageName));

        if (!isIntentAvailable(context, intent)) {
            intent = newOpenWebBrowserIntent("http://www.amazon.com/gp/mas/dl/android?p="
                    + packageName);
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        return intent;
    }

    /**
     * Intent that shows settings to manage installed applications.
     *
     * @param context
     *            The context associated to the application
     * @return
     */
    public static Intent newManageApplicationsIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);

        if (!isIntentAvailable(context, intent)) {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName("com.android.settings",
                    "com.android.settings.ManageApplications"));
        }

        return intent;
    }

    /**
     * Intent that shows settings to manage installed applications/services.
     *
     * @param context
     *            The context associated to the application
     * @return
     */
    public static Intent newRunningServicesIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.RunningServices"));

        if (!isIntentAvailable(context, intent)) {
            return newManageApplicationsIntent(context);
        }

        return intent;
    }

    /**
     * <p>
     * Intent to show an applications details page in (Settings) com.android.settings
     * </p>
     *
     * @param context
     *            The context associated to the application
     * @param packageName
     *            The package name of the application
     * @return the intent to open the application info screen.
     */
    public static Intent newAppDetailsIntent(Context context, String packageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(
                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + packageName));
            return intent;
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");
            intent.putExtra("pkg", packageName);
            return intent;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.android.settings",
                "com.android.settings.InstalledAppDetails");
        intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        return intent;
    }

    /**
     * Intent to open a YouTube Video
     *
     * @param pm
     *            The {@link PackageManager}.
     * @param url
     *            The URL or YouTube video ID.
     * @return The intent to open the YouTube app or Web Browser to play the video
     */
    public static Intent newYouTubeIntent(PackageManager pm, String url) {
        Intent intent;
        if (url.length() == 11) {
            // youtube video id
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + url));
        } else {
            // url to video
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        try {
            if (pm.getPackageInfo("com.google.android.youtube", 0) != null) {
                intent.setPackage("com.google.android.youtube");
            }
        } catch (NameNotFoundException e) {
        }
        return intent;
    }

    /**
     * Intent to open the official Instagram app to the user's profile. If the Instagram app is not
     * installed then the Web Browser will be used.
     *
     * </br></br>Example usage:</br>
     * <code>newInstagramProfileIntent(context.getPackageManager(), "http://instagram.com/jaredrummler");</code>
     *
     * @param pm
     *            The {@link PackageManager}.
     * @param url
     *            The URL to the user's Instagram profile.
     * @return The intent to open the Instagram app to the user's profile.
     */
    public static Intent newInstagramProfileIntent(PackageManager pm, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                String username = url.substring(url.lastIndexOf("/") + 1);
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (NameNotFoundException e) {
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * Intent to open the official Google+ app to the user's profile. If the Google+ app is not
     * installed then the Web Browser will be used.
     *
     * </br></br>Example usage:</br>
     * <code>newGooglePlusIntent(context.getPackageManager(), "https://plus.google.com/+JaredRummler");</code>
     *
     * @param pm
     *            The {@link PackageManager}.
     * @param url
     *            The URL to the user's Google+ profile.
     * @return The intent to open the Google+ app to the user's profile.
     */
    public static Intent newGooglePlusIntent(PackageManager pm, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            if (pm.getPackageInfo("com.google.android.apps.plus", 0) != null) {
                intent.setPackage("com.google.android.apps.plus");
            }
        } catch (NameNotFoundException e) {
        }
        return intent;
    }

    /**
     * Intent to open the official Facebook app. If the Facebook app is not installed then the
     * default web browser will be used.
     *
     * </br></br>Example usage:</br>
     * <code>newFacebookIntent(context.getPackageManager(), "https://www.facebook.com/JRummyApps");</code>
     *
     * @param pm
     *            Instance of the {@link PackageManager}.
     * @param url
     *            The full URL to the Facebook page or profile.
     * @return An intent that will open the Facebook page/profile.
     */
    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri;
        try {
            pm.getPackageInfo("com.facebook.katana", 0);
            // http://stackoverflow.com/a/24547437/1048340
            uri = Uri.parse("fb://facewebmodal/f?href=" + url);
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Initializers
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    private IntentUtils() {

    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes and/or Interfaces
    // ===========================================================

}