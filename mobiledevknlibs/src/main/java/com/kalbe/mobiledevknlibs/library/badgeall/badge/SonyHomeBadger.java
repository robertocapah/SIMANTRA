package com.kalbe.mobiledevknlibs.library.badgeall.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.kalbe.mobiledevknlibs.library.badgeall.viewbadger.BadgerShortCut;
import com.kalbe.mobiledevknlibs.library.badgeall.viewbadger.ShortcutBadgeException;

import java.util.Arrays;
import java.util.List;


/**

 */
public class SonyHomeBadger implements BadgerShortCut {

    private static final String INTENT_ACTION = "com.sonyericsson.home.action.UPDATE_BADGE";
    private static final String INTENT_EXTRA_PACKAGE_NAME = "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME";
    private static final String INTENT_EXTRA_MESSAGE = "com.sonyericsson.home.intent.extra.badge.MESSAGE";
    private static final String INTENT_EXTRA_SHOW_MESSAGE = "com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException {
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, componentName.getPackageName());
        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
        intent.putExtra(INTENT_EXTRA_MESSAGE, String.valueOf(badgeCount));
        intent.putExtra(INTENT_EXTRA_SHOW_MESSAGE, badgeCount > 0);
        context.sendBroadcast(intent);
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList("com.sonyericsson.home");
    }
}
