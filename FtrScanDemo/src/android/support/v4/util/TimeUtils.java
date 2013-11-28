// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package android.support.v4.util;

import java.io.PrintWriter;

public class TimeUtils
{

    public TimeUtils()
    {
    }

    private static int accumField(int i, int j, boolean flag, int k)
    {
        if(i > 99 || flag && k >= 3)
            return j + 3;
        if(i > 9 || flag && k >= 2)
            return j + 2;
        if(flag || i > 0)
            return j + 1;
        else
            return 0;
    }

    public static void formatDuration(long l, long l1, PrintWriter printwriter)
    {
        if(l == 0L)
        {
            printwriter.print("--");
            return;
        } else
        {
            formatDuration(l - l1, printwriter, 0);
            return;
        }
    }

    public static void formatDuration(long l, PrintWriter printwriter)
    {
        formatDuration(l, printwriter, 0);
    }

    public static void formatDuration(long l, PrintWriter printwriter, int i)
    {
        synchronized(sFormatSync)
        {
            int j = formatDurationLocked(l, i);
            printwriter.print(new String(sFormatStr, 0, j));
        }
        return;
        exception;
        obj;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public static void formatDuration(long l, StringBuilder stringbuilder)
    {
        synchronized(sFormatSync)
        {
            int i = formatDurationLocked(l, 0);
            stringbuilder.append(sFormatStr, 0, i);
        }
        return;
        exception;
        obj;
        JVM INSTR monitorexit ;
        throw exception;
    }

    private static int formatDurationLocked(long l, int i)
    {
        if(sFormatStr.length < i)
            sFormatStr = new char[i];
        char ac[] = sFormatStr;
        if(l == 0L)
        {
            for(int k5 = i - 1; k5 < 0;)
                ac[0] = ' ';

            ac[0] = '0';
            return 1;
        }
        char c;
        int j;
        int k;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        if(l > 0L)
        {
            c = '+';
        } else
        {
            c = '-';
            l = -l;
        }
        j = (int)(l % 1000L);
        k = (int)Math.floor(l / 1000L);
        i1 = k;
        j1 = 0;
        if(i1 > 0x15180)
        {
            j1 = k / 0x15180;
            k -= 0x15180 * j1;
        }
        k1 = k;
        l1 = 0;
        if(k1 > 3600)
        {
            l1 = k / 3600;
            k -= l1 * 3600;
        }
        i2 = k;
        j2 = 0;
        if(i2 > 60)
        {
            j2 = k / 60;
            k -= j2 * 60;
        }
        k2 = 0;
        if(i != 0)
        {
            int j4 = accumField(j1, 1, false, 0);
            boolean flag4;
            int k4;
            boolean flag5;
            int l4;
            boolean flag6;
            int i5;
            byte byte4;
            int j5;
            if(j4 > 0)
                flag4 = true;
            else
                flag4 = false;
            k4 = j4 + accumField(l1, 1, flag4, 2);
            if(k4 > 0)
                flag5 = true;
            else
                flag5 = false;
            l4 = k4 + accumField(j2, 1, flag5, 2);
            if(l4 > 0)
                flag6 = true;
            else
                flag6 = false;
            i5 = l4 + accumField(k, 1, flag6, 2);
            if(i5 > 0)
                byte4 = 3;
            else
                byte4 = 0;
            for(j5 = i5 + (1 + accumField(j, 2, true, byte4)); j5 < i; j5++)
            {
                ac[k2] = ' ';
                k2++;
            }

        }
        ac[k2] = c;
        int l2 = k2 + 1;
        boolean flag;
        int i3;
        boolean flag1;
        byte byte0;
        int j3;
        boolean flag2;
        byte byte1;
        int k3;
        boolean flag3;
        byte byte2;
        int l3;
        byte byte3;
        int i4;
        if(i != 0)
            flag = true;
        else
            flag = false;
        i3 = printField(ac, j1, 'd', l2, false, 0);
        if(i3 != l2)
            flag1 = true;
        else
            flag1 = false;
        if(flag)
            byte0 = 2;
        else
            byte0 = 0;
        j3 = printField(ac, l1, 'h', i3, flag1, byte0);
        if(j3 != l2)
            flag2 = true;
        else
            flag2 = false;
        if(flag)
            byte1 = 2;
        else
            byte1 = 0;
        k3 = printField(ac, j2, 'm', j3, flag2, byte1);
        if(k3 != l2)
            flag3 = true;
        else
            flag3 = false;
        if(flag)
            byte2 = 2;
        else
            byte2 = 0;
        l3 = printField(ac, k, 's', k3, flag3, byte2);
        if(flag && l3 != l2)
            byte3 = 3;
        else
            byte3 = 0;
        i4 = printField(ac, j, 'm', l3, true, byte3);
        ac[i4] = 's';
        return i4 + 1;
    }

    private static int printField(char ac[], int i, char c, int j, boolean flag, int k)
    {
        if(flag || i > 0)
        {
            int l = j;
            if(flag && k >= 3 || i > 99)
            {
                int k1 = i / 100;
                ac[j] = (char)(k1 + 48);
                j++;
                i -= k1 * 100;
            }
            if(flag && k >= 2 || i > 9 || l != j)
            {
                int i1 = i / 10;
                ac[j] = (char)(i1 + 48);
                j++;
                i -= i1 * 10;
            }
            ac[j] = (char)(i + 48);
            int j1 = j + 1;
            ac[j1] = c;
            j = j1 + 1;
        }
        return j;
    }

    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 0x15180;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char sFormatStr[] = new char[24];
    private static final Object sFormatSync = new Object();

}
