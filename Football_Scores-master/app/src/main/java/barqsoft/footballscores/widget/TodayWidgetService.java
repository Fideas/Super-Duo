package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.R;

/**
 * Created by Nicolás Carrasco on 21/10/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TodayWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new TodayWidgetRemoteViewsFactory();
    }

    public class TodayWidgetRemoteViewsFactory implements RemoteViewsFactory {

        Cursor data = null;

        public TodayWidgetRemoteViewsFactory() {
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (i == AdapterView.INVALID_POSITION ||
                    data == null ||
                    !data.moveToPosition(i)) {
                return null;
            }
            RemoteViews remoteViews =
                    new RemoteViews(getPackageName(), R.layout.widget_today_list_item);





            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
