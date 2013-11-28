// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package android.support.v4.view;

import android.content.Context;
import android.content.res.*;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.TextView;

// Referenced classes of package android.support.v4.view:
//            ViewPager, PagerAdapter, PagerTitleStripIcs

public class PagerTitleStrip extends ViewGroup
    implements ViewPager.Decor
{
    private class PageListener extends DataSetObserver
        implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener
    {

        public void onAdapterChanged(PagerAdapter pageradapter, PagerAdapter pageradapter1)
        {
            updateAdapter(pageradapter, pageradapter1);
        }

        public void onChanged()
        {
            updateText(mPager.getCurrentItem(), mPager.getAdapter());
        }

        public void onPageScrollStateChanged(int i)
        {
            mScrollState = i;
        }

        public void onPageScrolled(int i, float f, int j)
        {
            if(f > 0.5F)
                i++;
            updateTextPositions(i, f, false);
        }

        public void onPageSelected(int i)
        {
            if(mScrollState == 0)
                updateText(mPager.getCurrentItem(), mPager.getAdapter());
        }

        private int mScrollState;
        final PagerTitleStrip this$0;

        private PageListener()
        {
            this$0 = PagerTitleStrip.this;
            super();
        }

    }

    static interface PagerTitleStripImpl
    {

        public abstract void setSingleLineAllCaps(TextView textview);
    }

    static class PagerTitleStripImplBase
        implements PagerTitleStripImpl
    {

        public void setSingleLineAllCaps(TextView textview)
        {
            textview.setSingleLine();
        }

        PagerTitleStripImplBase()
        {
        }
    }

    static class PagerTitleStripImplIcs
        implements PagerTitleStripImpl
    {

        public void setSingleLineAllCaps(TextView textview)
        {
            PagerTitleStripIcs.setSingleLineAllCaps(textview);
        }

        PagerTitleStripImplIcs()
        {
        }
    }


    public PagerTitleStrip(Context context)
    {
        this(context, null);
    }

    public PagerTitleStrip(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mLastKnownCurrentPage = -1;
        mLastKnownPositionOffset = -1F;
        mPageListener = new PageListener();
        TextView textview = new TextView(context);
        mPrevText = textview;
        addView(textview);
        TextView textview1 = new TextView(context);
        mCurrText = textview1;
        addView(textview1);
        TextView textview2 = new TextView(context);
        mNextText = textview2;
        addView(textview2);
        TypedArray typedarray = context.obtainStyledAttributes(attributeset, ATTRS);
        int i = typedarray.getResourceId(0, 0);
        if(i != 0)
        {
            mPrevText.setTextAppearance(context, i);
            mCurrText.setTextAppearance(context, i);
            mNextText.setTextAppearance(context, i);
        }
        int j = typedarray.getDimensionPixelSize(1, 0);
        if(j != 0)
            setTextSize(0, j);
        if(typedarray.hasValue(2))
        {
            int k = typedarray.getColor(2, 0);
            mPrevText.setTextColor(k);
            mCurrText.setTextColor(k);
            mNextText.setTextColor(k);
        }
        mGravity = typedarray.getInteger(3, 80);
        typedarray.recycle();
        mTextColor = mCurrText.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(0.6F);
        mPrevText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        mCurrText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        mNextText.setEllipsize(android.text.TextUtils.TruncateAt.END);
        boolean flag = false;
        if(i != 0)
        {
            TypedArray typedarray1 = context.obtainStyledAttributes(i, TEXT_ATTRS);
            flag = typedarray1.getBoolean(0, false);
            typedarray1.recycle();
        }
        if(flag)
        {
            setSingleLineAllCaps(mPrevText);
            setSingleLineAllCaps(mCurrText);
            setSingleLineAllCaps(mNextText);
        } else
        {
            mPrevText.setSingleLine();
            mCurrText.setSingleLine();
            mNextText.setSingleLine();
        }
        mScaledTextSpacing = (int)(16F * context.getResources().getDisplayMetrics().density);
    }

    private static void setSingleLineAllCaps(TextView textview)
    {
        IMPL.setSingleLineAllCaps(textview);
    }

    int getMinHeight()
    {
        Drawable drawable = getBackground();
        int i = 0;
        if(drawable != null)
            i = drawable.getIntrinsicHeight();
        return i;
    }

    public int getTextSpacing()
    {
        return mScaledTextSpacing;
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        android.view.ViewParent viewparent = getParent();
        if(!(viewparent instanceof ViewPager))
        {
            throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
        } else
        {
            ViewPager viewpager = (ViewPager)viewparent;
            PagerAdapter pageradapter = viewpager.getAdapter();
            viewpager.setInternalPageChangeListener(mPageListener);
            viewpager.setOnAdapterChangeListener(mPageListener);
            mPager = viewpager;
            updateAdapter(null, pageradapter);
            return;
        }
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(mPager != null)
        {
            updateAdapter(mPager.getAdapter(), null);
            mPager.setInternalPageChangeListener(null);
            mPager.setOnAdapterChangeListener(null);
            mPager = null;
        }
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l)
    {
        if(mPager != null)
        {
            int i1 = mLastKnownPositionOffset != 0.0F;
            float f = 0.0F;
            if(i1 >= 0)
                f = mLastKnownPositionOffset;
            updateTextPositions(mPager.getCurrentItem(), f, true);
        }
    }

    protected void onMeasure(int i, int j)
    {
        int k = android.view.View.MeasureSpec.getMode(i);
        int l = android.view.View.MeasureSpec.getMode(j);
        int i1 = android.view.View.MeasureSpec.getSize(i);
        int j1 = android.view.View.MeasureSpec.getSize(j);
        if(k != 0x40000000)
            throw new IllegalStateException("Must measure with an exact width");
        int k1 = getMinHeight();
        int l1 = getPaddingTop() + getPaddingBottom();
        int i2 = j1 - l1;
        int j2 = android.view.View.MeasureSpec.makeMeasureSpec((int)(0.8F * (float)i1), 0x80000000);
        int k2 = android.view.View.MeasureSpec.makeMeasureSpec(i2, 0x80000000);
        mPrevText.measure(j2, k2);
        mCurrText.measure(j2, k2);
        mNextText.measure(j2, k2);
        if(l == 0x40000000)
        {
            setMeasuredDimension(i1, j1);
            return;
        } else
        {
            setMeasuredDimension(i1, Math.max(k1, l1 + mCurrText.getMeasuredHeight()));
            return;
        }
    }

    public void requestLayout()
    {
        if(!mUpdatingText)
            super.requestLayout();
    }

    public void setGravity(int i)
    {
        mGravity = i;
        requestLayout();
    }

    public void setNonPrimaryAlpha(float f)
    {
        mNonPrimaryAlpha = 0xff & (int)(255F * f);
        int i = mNonPrimaryAlpha << 24 | 0xffffff & mTextColor;
        mPrevText.setTextColor(i);
        mNextText.setTextColor(i);
    }

    public void setTextColor(int i)
    {
        mTextColor = i;
        mCurrText.setTextColor(i);
        int j = mNonPrimaryAlpha << 24 | 0xffffff & mTextColor;
        mPrevText.setTextColor(j);
        mNextText.setTextColor(j);
    }

    public void setTextSize(int i, float f)
    {
        mPrevText.setTextSize(i, f);
        mCurrText.setTextSize(i, f);
        mNextText.setTextSize(i, f);
    }

    public void setTextSpacing(int i)
    {
        mScaledTextSpacing = i;
        requestLayout();
    }

    void updateAdapter(PagerAdapter pageradapter, PagerAdapter pageradapter1)
    {
        if(pageradapter != null)
            pageradapter.unregisterDataSetObserver(mPageListener);
        if(pageradapter1 != null)
            pageradapter1.registerDataSetObserver(mPageListener);
        if(mPager != null)
        {
            mLastKnownCurrentPage = -1;
            mLastKnownPositionOffset = -1F;
            updateText(mPager.getCurrentItem(), pageradapter1);
            requestLayout();
        }
    }

    void updateText(int i, PagerAdapter pageradapter)
    {
        int j;
        CharSequence charsequence;
        TextView textview;
        CharSequence charsequence1;
        int k;
        CharSequence charsequence2;
        int l;
        int i1;
        int j1;
        int k1;
        if(pageradapter != null)
            j = pageradapter.getCount();
        else
            j = 0;
        mUpdatingText = true;
        charsequence = null;
        if(i >= 1)
        {
            charsequence = null;
            if(pageradapter != null)
                charsequence = pageradapter.getPageTitle(i - 1);
        }
        mPrevText.setText(charsequence);
        textview = mCurrText;
        if(pageradapter != null && i < j)
            charsequence1 = pageradapter.getPageTitle(i);
        else
            charsequence1 = null;
        textview.setText(charsequence1);
        k = i + 1;
        charsequence2 = null;
        if(k < j)
        {
            charsequence2 = null;
            if(pageradapter != null)
                charsequence2 = pageradapter.getPageTitle(i + 1);
        }
        mNextText.setText(charsequence2);
        l = getWidth() - getPaddingLeft() - getPaddingRight();
        i1 = getHeight() - getPaddingTop() - getPaddingBottom();
        j1 = android.view.View.MeasureSpec.makeMeasureSpec((int)(0.8F * (float)l), 0x80000000);
        k1 = android.view.View.MeasureSpec.makeMeasureSpec(i1, 0x80000000);
        mPrevText.measure(j1, k1);
        mCurrText.measure(j1, k1);
        mNextText.measure(j1, k1);
        mLastKnownCurrentPage = i;
        if(!mUpdatingPositions)
            updateTextPositions(i, mLastKnownPositionOffset, false);
        mUpdatingText = false;
    }

    void updateTextPositions(int i, float f, boolean flag)
    {
        int k1;
        int j2;
        int k2;
        int i5;
        int j5;
        int k5;
        int k6;
        int i7;
        int j7;
        int k7;
        int j;
        int k;
        int l;
        int i1;
        int j1;
        int l1;
        int i2;
        int l2;
        int i3;
        int j3;
        float f1;
        int k3;
        int l3;
        int i4;
        int j4;
        int k4;
        int l4;
        int l5;
        int i6;
        int j6;
        TextView textview;
        int l7;
        int i8;
        TextView textview1;
        int j8;
        int k8;
        int l8;
        TextView textview2;
        int i9;
        int j9;
        if(i != mLastKnownCurrentPage)
            updateText(i, mPager.getAdapter());
        else
        if(!flag && f == mLastKnownPositionOffset)
            return;
        mUpdatingPositions = true;
        j = mPrevText.getMeasuredWidth();
        k = mCurrText.getMeasuredWidth();
        l = mNextText.getMeasuredWidth();
        i1 = k / 2;
        j1 = getWidth();
        k1 = getHeight();
        l1 = getPaddingLeft();
        i2 = getPaddingRight();
        j2 = getPaddingTop();
        k2 = getPaddingBottom();
        l2 = l1 + i1;
        i3 = i2 + i1;
        j3 = j1 - l2 - i3;
        f1 = f + 0.5F;
        if(f1 > 1.0F)
            f1--;
        k3 = j1 - i3 - (int)(f1 * (float)j3) - k / 2;
        l3 = k3 + k;
        i4 = mPrevText.getBaseline();
        j4 = mCurrText.getBaseline();
        k4 = mNextText.getBaseline();
        l4 = Math.max(Math.max(i4, j4), k4);
        i5 = l4 - i4;
        j5 = l4 - j4;
        k5 = l4 - k4;
        l5 = i5 + mPrevText.getMeasuredHeight();
        i6 = j5 + mCurrText.getMeasuredHeight();
        j6 = k5 + mNextText.getMeasuredHeight();
        k6 = Math.max(Math.max(l5, i6), j6);
        0x70 & mGravity;
        JVM INSTR lookupswitch 2: default 312
    //                   16: 502
    //                   80: 541;
           goto _L1 _L2 _L3
_L1:
        i7 = j2 + i5;
        j7 = j2 + j5;
        k7 = j2 + k5;
_L5:
        textview = mCurrText;
        l7 = j7 + mCurrText.getMeasuredHeight();
        textview.layout(k3, j7, l3, l7);
        i8 = Math.min(l1, k3 - mScaledTextSpacing - j);
        textview1 = mPrevText;
        j8 = i8 + j;
        k8 = i7 + mPrevText.getMeasuredHeight();
        textview1.layout(i8, i7, j8, k8);
        l8 = Math.max(j1 - i2 - l, l3 + mScaledTextSpacing);
        textview2 = mNextText;
        i9 = l8 + l;
        j9 = k7 + mNextText.getMeasuredHeight();
        textview2.layout(l8, k7, i9, j9);
        mLastKnownPositionOffset = f;
        mUpdatingPositions = false;
        return;
_L2:
        int k9 = (k1 - j2 - k2 - k6) / 2;
        i7 = k9 + i5;
        j7 = k9 + j5;
        k7 = k9 + k5;
        continue; /* Loop/switch isn't completed */
_L3:
        int l6 = k1 - k2 - k6;
        i7 = l6 + i5;
        j7 = l6 + j5;
        k7 = l6 + k5;
        if(true) goto _L5; else goto _L4
_L4:
    }

    private static final int ATTRS[] = {
        0x1010034, 0x1010095, 0x1010098, 0x10100af
    };
    private static final PagerTitleStripImpl IMPL;
    private static final float SIDE_ALPHA = 0.6F;
    private static final String TAG = "PagerTitleStrip";
    private static final int TEXT_ATTRS[] = {
        0x101038c
    };
    private static final int TEXT_SPACING = 16;
    TextView mCurrText;
    private int mGravity;
    private int mLastKnownCurrentPage;
    private float mLastKnownPositionOffset;
    TextView mNextText;
    private int mNonPrimaryAlpha;
    private final PageListener mPageListener;
    ViewPager mPager;
    TextView mPrevText;
    private int mScaledTextSpacing;
    int mTextColor;
    private boolean mUpdatingPositions;
    private boolean mUpdatingText;

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 14)
            IMPL = new PagerTitleStripImplIcs();
        else
            IMPL = new PagerTitleStripImplBase();
    }
}
