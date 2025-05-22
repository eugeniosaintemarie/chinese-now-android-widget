package com.example.widgetapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri // Added for Uri.parse()
import android.util.Log
import android.widget.RemoteViews
// No direct WebView import needed here as we can't directly manipulate it
// import android.webkit.WebView // Not useful for RemoteViews manipulation

const val ACTION_REFRESH_WIDGET = "com.example.widgetapp.ACTION_REFRESH_WIDGET"
const val TARGET_URL = "https://eugeniosaintemarie.github.io/chinese-now/"

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            Log.d("NewAppWidget", "onUpdate called for widget ID: $appWidgetId. Loading URL.")
            // Initial load of the URL
            loadUrlInWebView(context, appWidgetManager, appWidgetId, TARGET_URL)
            // Also, ensure the refresh button is set up correctly during onUpdate
            setupRefreshButton(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent) // Important to call super first
        Log.d("NewAppWidget", "onReceive received action: ${intent.action}")
        if (intent.action == ACTION_REFRESH_WIDGET) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            Log.d("NewAppWidget", "Refresh action received for widget ID: $appWidgetId")

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                loadUrlInWebView(context, appWidgetManager, appWidgetId, TARGET_URL)
            } else {
                // If no specific widget ID, could refresh all, or log error
                Log.e("NewAppWidget", "Refresh action received with invalid widget ID.")
                // Optionally, refresh all widgets of this provider
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val thisAppWidget = ComponentName(context.packageName, javaClass.name)
                val allAppWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
                for (id in allAppWidgetIds) {
                     loadUrlInWebView(context, appWidgetManager, id, TARGET_URL)
                }
            }
        }
    }

    override fun onEnabled(context: Context) {
        Log.d("NewAppWidget", "Widget enabled. Initial URL load will occur via onUpdate.")
    }

    override fun onDisabled(context: Context) {
        Log.d("NewAppWidget", "Widget disabled")
    }
}

// Helper function to set up the refresh button
internal fun setupRefreshButton(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
){
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    val refreshIntent = Intent(context, NewAppWidget::class.java).apply {
        action = ACTION_REFRESH_WIDGET
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId, // requestCode needs to be unique per widget instance for the intent
        refreshIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.widget_refresh_button, pendingIntent)
    appWidgetManager.updateAppWidget(appWidgetId, views) // Apply this part of the view update
    Log.d("NewAppWidget", "Refresh button PendingIntent set up for widget ID: $appWidgetId")
}


// Helper function to attempt loading URL.
// Note: Direct WebView manipulation (like loadUrl or JS enabling) via RemoteViews is limited.
internal fun loadUrlInWebView(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    url: String
) {
    Log.d("NewAppWidget", "Attempting to load URL: $url in WebView for widget ID: $appWidgetId")
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)

    // The core challenge: RemoteViews does not have a direct 'loadUrl' method for a WebView.
    // The core challenge: RemoteViews does not have a direct 'loadUrl' method for a WebView.
    // The 'setUri' method is typically for ImageViews or AdapterViews.
    // The subtask suggests trying `views.setUri(R.id.widget_webview, "loadUrl", Uri.parse(url))`
    // This is an unconventional use of setUri, where "loadUrl" is the method name.
    // Its effectiveness is uncertain and depends on specific Android internal implementations for WebView in RemoteViews.
    try {
        views.setUri(R.id.widget_webview, "loadUrl", Uri.parse(url))
        Log.d("NewAppWidget", "Called views.setUri(R.id.widget_webview, \"loadUrl\", Uri.parse(url))")
    } catch (e: Exception) {
        Log.e("NewAppWidget", "Error calling views.setUri for WebView: ${e.message}")
        // Fallback or error handling if needed
    }
    
    // Enabling JavaScript:
    // It's not possible to enable JavaScript directly on a WebView managed by RemoteViews
    // using a separate WebView instance as shown in the prompt, e.g.:
    // val tempWebView = WebView(context)
    // tempWebView.settings.javaScriptEnabled = true
    // This temporary instance is not the one displayed in the widget.
    // JavaScript must be enabled on the actual WebView instance used by the layout,
    // which is not directly accessible here. If the target URL requires JavaScript,
    // it may not render correctly. This is a known limitation of WebViews in AppWidgets.
    Log.w("NewAppWidget", "JavaScript cannot be programmatically enabled on WebView via RemoteViews from AppWidgetProvider.")

    // Ensure the refresh button is also configured when this method is called,
    // This would need to be configured in the WebView's settings when it's inflated,
    // which is not controllable from AppWidgetProvider directly without a RemoteViewsService.

    // Ensure the refresh button is also configured when this method is called,
    // as loadUrlInWebView might be called from onUpdate where the whole view is constructed.
    val refreshIntent = Intent(context, NewAppWidget::class.java).apply {
        action = ACTION_REFRESH_WIDGET
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId, // requestCode ensuring uniqueness
        refreshIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.widget_refresh_button, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    Log.d("NewAppWidget", "appWidgetManager.updateAppWidget called for widget ID: $appWidgetId after trying to load URL.")
}
