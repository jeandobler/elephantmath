package com.dynamic.dobler.elephantmath.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.dynamic.dobler.elephantmath.R;
import com.dynamic.dobler.elephantmath.activity.SplashActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RankingWidget extends AppWidgetProvider {

    private static DatabaseReference mFirebaseDatabaseReference;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Query query = mFirebaseDatabaseReference
                .child("ranking")
                .orderByChild("points")
                .limitToFirst(1);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_rankingt);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("points").getValue() != null) {
                    Log.e("Widget",dataSnapshot.child("points").getValue().toString() );

                    String points = dataSnapshot.child("points").getValue().toString();

                    views.setTextViewText(R.id.tv_widget, "Last Points");
                    views.setTextViewText(R.id.tv_widget2, points);

                }

                Intent intent = new Intent(context, SplashActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                views.setOnClickPendingIntent(R.id.view_widget, pendingIntent);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
