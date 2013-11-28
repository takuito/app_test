// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package android.support.v4.view;

import android.animation.ValueAnimator;

class ViewCompatHC
{

    ViewCompatHC()
    {
    }

    static long getFrameTime()
    {
        return ValueAnimator.getFrameDelay();
    }
}
