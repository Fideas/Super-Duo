package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/**
 * Created by Nicolás Carrasco on 21/10/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TodayWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }
}
