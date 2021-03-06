package com.earth.places.utilities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.view.View;

import com.earth.places.R;
import com.earth.places.fragment.FragmentDisasters;
import com.earth.places.fragment.FragmentGoodActs;
import com.earth.places.fragment.FragmentPlaces;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils extends Activity {


    /**
     *  Handles the different internet connection states and acts respectively with loading the content or showing a message.
     */
    public static void handleInternetConnection(Activity context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { // connected to wifi
                loadContent(context);
            }

            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) { // connected to the mobile provider's data plan
                Dialogs.showMobileData(context);
            }

        } else Dialogs.showNoInternet(context); // not connected to the internet


    }

    /**
     *  Load all info from the JSONs and display content
     */
    public static void loadContent(Activity context) {

        FragmentPlaces.loadPlacesList(context);
        FragmentDisasters.loadDisastersList(context);
        FragmentGoodActs.loadGoodActsList(context);
    }


    /**
     * Filter all enitities out of a string.
     * @param string
     * @return string (cleaned)
     */
    public static String cleanString(String string) {

        // TODO: Convert HTML entities and characters correctly. Haven't found a solution yet.

        string = string.replace("\\n ", "\n");
        string =  Html.fromHtml(string).toString();

        string = Normalizer.normalize(string, Normalizer.Form.NFD);

        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        string = pattern.matcher(string).replaceAll("");

        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return string;

    }

    /**
     * Compare two strings with each other.
     * @param string1
     * @param string2
     * @return boolean (true if equal)
     */
    public static boolean equalStrings(String string1, String string2) {

        return string1.toLowerCase().replace(" ", "").replace(",", "")
                .equals(string2.toLowerCase().replace(" ", "").replace(",", ""));
    }

    /**
     * Check if the first string contains the second string.
     * @param string1
     * @param string2
     * @return boolean (true if equal)
     */
    public static boolean stringIsContained(String string1, String string2) {

        return string1.toLowerCase().replace(" ", "").replace(",", "")
                .contains(string2.toLowerCase().replace(" ", "").replace(",", ""));
    }

    /**
     * Custom Typefaces
     * @param context
     * @param index
     * @return
     */
    public static Typeface customTypeface(Context context, int index) {

        Typeface typeface = null;

        if (index == 1)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BreeSerif-Regular.ttf");
        if (index == 2)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        if (index == 3)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

        return typeface;
    }


    public static void openChromeTab(Context context, String link, int color) {
        final CustomTabsClient[] mClient = new CustomTabsClient[1];
        final CustomTabsSession[] mCustomTabsSession = new CustomTabsSession[1];

        CustomTabsServiceConnection mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mClient[0] = customTabsClient;
                mClient[0].warmup(0L);
                mCustomTabsSession[0] = mClient[0].newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient[0] = null;
            }
        };

        if (color == 0) color = ContextCompat.getColor(context, R.color.cardBackground);

        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", mCustomTabsServiceConnection);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession[0])
                .setToolbarColor(color)
                .setShowTitle(true)
                .addDefaultShareMenuItem()
                .build();

        customTabsIntent.launchUrl((Activity) context, Uri.parse(link));

        context.unbindService(mCustomTabsServiceConnection); // important
    }

    /**
     * Depending on the given intensity, the given color will be darken or brighten.
     * @param color
     * @param intensity
     * @return colorVariant
     */
    public static int colorVariant(int color, float intensity) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= intensity;
        color = Color.HSVToColor(hsv);
        return color;
    }

    /**
     * Try to get the respective color out of the palette.
     * @param context
     * @param palette
     * @return
     */
    public static int colorFromPalette(Context context, Palette palette) {

        final int defaultColor = context.getResources().getColor(R.color.colorPrimary);
        int mutedLight = palette.getLightMutedColor(defaultColor);
        int vibrantLight = palette.getLightVibrantColor(mutedLight);
        int muted = palette.getMutedColor(vibrantLight);
        int mutedDark = palette.getDarkMutedColor(muted);
        return palette.getDarkVibrantColor(mutedDark);

    }

    /**
     * Show simple snack bar.
     * @param context
     * @param color
     * @param view
     * @param text
     * @param length
     */
    public static void showSnackBar(Activity context, int color, int view, int text, int length) {

        View layout = context.findViewById(view);
        Snackbar snackbar = Snackbar.make(layout, text, length)
                .setActionTextColor(context.getResources().getColor(R.color.white));

        View snackBarView = snackbar.getView();
        if (color == 0) color = ContextCompat.getColor(context, R.color.backgroundColor);
        snackBarView.setBackgroundColor(color);
        snackbar.show();
    }

}