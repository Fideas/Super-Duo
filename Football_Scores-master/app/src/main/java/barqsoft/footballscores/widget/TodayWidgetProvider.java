package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Provider for a widget that shows today matches
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TodayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews remoteViews =
                    new RemoteViews(context.getPackageName(), R.layout.widget_today);

            //Set intent to launch main activity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            //Set intent for each list element
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

            //Set Empty View
            remoteViews.setEmptyView(R.id.widget_list, R.id.empty_view);

            //Set Remote Adapter
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, remoteViews);
            } else {
                setRemoteAdapterV11(context, remoteViews);
            }
            //update the widgets
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews remoteViews) {
        Intent serviceIntent = new Intent(context, TodayWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.widget_list, serviceIntent);
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews remoteViews) {
        Intent serviceIntent = new Intent(context, TodayWidgetService.class);
        remoteViews.setRemoteAdapter(0, R.id.widget_list, serviceIntent);
    }
}
