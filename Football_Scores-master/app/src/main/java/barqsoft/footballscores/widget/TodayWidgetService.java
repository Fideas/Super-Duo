package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Nicolás Carrasco on 21/10/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TodayWidgetService extends RemoteViewsService {

    private static final String[] MATCH_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL
    };

    //indices must match the projection above
    private static final int INDEX_HOME_TEAM = 0;
    private static final int INDEX_AWAY_TEAM = 1;
    private static final int INDEX_HOME_SCORE = 2;
    private static final int INDEX_AWAY_SCORE = 3;
    private static final int INDEX_MATCH_TIME = 4;

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
            return data == null ? 0 : data.getCount();
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (data != null) {
                data.close();
            }
            final long identityToken = Binder.clearCallingIdentity();
            Date today = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


            data = getContentResolver().query(
                    DatabaseContract.scores_table.buildScoreWithDate(),
                    MATCH_COLUMNS,
                    null,
                    new String[]{dateFormat.format(today)},
                    null
            );
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
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

            //Get values from the cursor
            String homeTeamName = data.getString(INDEX_HOME_TEAM);
            String awayTeamName = data.getString(INDEX_AWAY_TEAM);
            String matchScore = Utilies.getScores(
                    data.getInt(INDEX_HOME_SCORE),
                    data.getInt(INDEX_AWAY_SCORE)
            );
            String matchTime = data.getString(INDEX_MATCH_TIME);

            int homeTeamCrest = Utilies.getTeamCrestByTeamName(homeTeamName);
            int awayTeamCrest = Utilies.getTeamCrestByTeamName(awayTeamName);

            //Set values to the layout
            remoteViews.setTextViewText(R.id.widget_home_name, homeTeamName);
            remoteViews.setTextViewText(R.id.widget_away_name, awayTeamName);
            remoteViews.setTextViewText(R.id.widget_score, matchScore);
            remoteViews.setTextViewText(R.id.widget_match_time, matchTime);
            remoteViews.setImageViewResource(R.id.widget_home_crest, homeTeamCrest);
            remoteViews.setImageViewResource(R.id.widget_away_crest, awayTeamCrest);

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
