package com.example.chinesenowwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.util.Log
import android.widget.RemoteViews

class ChineseNow : AppWidgetProvider() {

    companion object {
        private const val TAG = "ChineseNow"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d(TAG, "onUpdate called with ${appWidgetIds.size} widgets")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "Widget enabled")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "Widget disabled")
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        Log.d(TAG, "Updating widget $appWidgetId")
        
        try {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // Obtener datos en chino
            val chineseData = ChineseConverter.getCurrentChineseData()
            Log.d(TAG, "Chinese data: ${chineseData.time}")

            // Actualizar los TextViews con los datos
            views.setTextViewText(R.id.time_text, chineseData.time)
            views.setTextViewText(R.id.time_pinyin, chineseData.timePinyin)
            
            views.setTextViewText(R.id.day_text, chineseData.day)
            views.setTextViewText(R.id.day_pinyin, chineseData.dayPinyin)
            
            views.setTextViewText(R.id.month_text, chineseData.month)
            views.setTextViewText(R.id.month_pinyin, chineseData.monthPinyin)
            
            views.setTextViewText(R.id.year_text, chineseData.year)
            views.setTextViewText(R.id.year_pinyin, chineseData.yearPinyin)
            
            views.setTextViewText(R.id.season_text, chineseData.season)
            views.setTextViewText(R.id.season_pinyin, chineseData.seasonPinyin)

            // Intent para actualizar manualmente (botón refresh)
            val refreshIntent = Intent(context, ChineseNow::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
            }
            val refreshPendingIntent = PendingIntent.getBroadcast(
                context, 
                appWidgetId, 
                refreshIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.refresh_button, refreshPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
            Log.d(TAG, "Widget $appWidgetId updated successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating widget", e)
        }
    }
}
