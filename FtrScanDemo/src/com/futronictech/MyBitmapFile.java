// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) ansi 

package com.futronictech;


public class MyBitmapFile
{
    public class BITMAPFILEHEADER
    {

        public int sizeof()
        {
            return 14;
        }

        public byte[] toBytes()
        {
            byte abyte0[] = new byte[14];
            System.arraycopy(convet2bytes.short2bytes(bfType), 0, abyte0, 0, 2);
            System.arraycopy(convet2bytes.int2bytes(bfSize), 0, abyte0, 2, 4);
            System.arraycopy(convet2bytes.short2bytes(bfReserved1), 0, abyte0, 6, 2);
            System.arraycopy(convet2bytes.short2bytes(bfReserved2), 0, abyte0, 8, 2);
            System.arraycopy(convet2bytes.int2bytes(bfOffBits), 0, abyte0, 10, 4);
            return abyte0;
        }

        public int bfOffBits;
        public short bfReserved1;
        public short bfReserved2;
        public int bfSize;
        public short bfType;
        final MyBitmapFile this$0;

        public BITMAPFILEHEADER()
        {
            this$0 = MyBitmapFile.this;
            super();
            bfType = 19778;
            bfReserved2 = 0;
            bfReserved1 = 0;
        }
    }

    public class BITMAPINFO
    {

        public int sizeof()
        {
            return 1024 + bmiHeader.biSize;
        }

        public RGBQUAD bmiColors;
        public BITMAPINFOHEADER bmiHeader;
        final MyBitmapFile this$0;

        public BITMAPINFO()
        {
            this$0 = MyBitmapFile.this;
            super();
            bmiHeader = new BITMAPINFOHEADER();
            bmiColors = new RGBQUAD();
        }
    }

    public class BITMAPINFOHEADER
    {

        public byte[] toBytes()
        {
            byte abyte0[] = new byte[40];
            System.arraycopy(convet2bytes.int2bytes(biSize), 0, abyte0, 0, 4);
            System.arraycopy(convet2bytes.int2bytes(biWidth), 0, abyte0, 4, 4);
            System.arraycopy(convet2bytes.int2bytes(biHeight), 0, abyte0, 8, 4);
            System.arraycopy(convet2bytes.short2bytes(biPlanes), 0, abyte0, 12, 2);
            System.arraycopy(convet2bytes.short2bytes(biBitCount), 0, abyte0, 14, 2);
            System.arraycopy(convet2bytes.int2bytes(biCompression), 0, abyte0, 16, 4);
            System.arraycopy(convet2bytes.int2bytes(biSizeImage), 0, abyte0, 20, 4);
            System.arraycopy(convet2bytes.int2bytes(biXPelsPerMeter), 0, abyte0, 24, 4);
            System.arraycopy(convet2bytes.int2bytes(biYPelsPerMeter), 0, abyte0, 28, 4);
            System.arraycopy(convet2bytes.int2bytes(biClrUsed), 0, abyte0, 32, 4);
            System.arraycopy(convet2bytes.int2bytes(biClrImportant), 0, abyte0, 36, 4);
            return abyte0;
        }

        public short biBitCount;
        public int biClrImportant;
        public int biClrUsed;
        public int biCompression;
        public int biHeight;
        public short biPlanes;
        public int biSize;
        public int biSizeImage;
        public int biWidth;
        public int biXPelsPerMeter;
        public int biYPelsPerMeter;
        final MyBitmapFile this$0;

        public BITMAPINFOHEADER()
        {
            this$0 = MyBitmapFile.this;
            super();
            biPlanes = 1;
            biBitCount = 8;
            biCompression = 0;
            biSizeImage = 0;
            biClrImportant = 0;
            biClrUsed = 0;
            biXPelsPerMeter = 19686;
            biYPelsPerMeter = 19686;
            biSize = 40;
        }
    }

    public class RGBQUAD
    {

        public byte[] GetGRBTableByteData()
        {
            byte abyte0[] = new byte[1024];
            int i = 0;
            int j = 0;
            do
            {
                if(j >= 256)
                    return abyte0;
                abyte0[i] = (byte)j;
                abyte0[i + 1] = (byte)j;
                abyte0[i + 2] = (byte)j;
                abyte0[i + 3] = (byte)j;
                i += 4;
                j++;
            } while(true);
        }

        public byte rgbBlue;
        public byte rgbGreen;
        public byte rgbRed;
        public byte rgbReserved;
        final MyBitmapFile this$0;

        public RGBQUAD()
        {
            this$0 = MyBitmapFile.this;
            super();
            rgbReserved = 0;
        }
    }

    public static class convet2bytes
    {

        public static byte[] int2bytes(int i)
        {
            byte abyte0[] = new byte[4];
            abyte0[0] = (byte)(i & 0xff);
            abyte0[1] = (byte)((0xff00 & i) >> 8);
            abyte0[2] = (byte)((0xff0000 & i) >> 16);
            abyte0[3] = (byte)((0xff000000 & i) >> 24);
            return abyte0;
        }

        public static byte[] short2bytes(short word0)
        {
            byte abyte0[] = new byte[2];
            abyte0[0] = (byte)(word0 & 0xff);
            abyte0[1] = (byte)((0xff00 & word0) >> 8);
            return abyte0;
        }

        public convet2bytes()
        {
        }
    }


    public MyBitmapFile()
    {
        m_fileHeaderBitmap = new BITMAPFILEHEADER();
        m_infoBitmap = new BITMAPINFO();
    }

    public MyBitmapFile(int i, int j, byte abyte0[])
    {
        m_fileHeaderBitmap = new BITMAPFILEHEADER();
        m_infoBitmap = new BITMAPINFO();
        int k = m_fileHeaderBitmap.sizeof() + m_infoBitmap.sizeof() + i * j;
        m_fileHeaderBitmap.bfSize = k;
        m_fileHeaderBitmap.bfOffBits = m_fileHeaderBitmap.sizeof() + m_infoBitmap.sizeof();
        m_infoBitmap.bmiHeader.biWidth = i;
        m_infoBitmap.bmiHeader.biHeight = j;
        m_BmpData = new byte[k];
        byte abyte1[] = m_fileHeaderBitmap.toBytes();
        System.arraycopy(abyte1, 0, m_BmpData, 0, abyte1.length);
        int l = abyte1.length;
        byte abyte2[] = m_infoBitmap.bmiHeader.toBytes();
        System.arraycopy(abyte2, 0, m_BmpData, l, abyte2.length);
        int i1 = l + abyte2.length;
        byte abyte3[] = m_infoBitmap.bmiColors.GetGRBTableByteData();
        System.arraycopy(abyte3, 0, m_BmpData, i1, abyte3.length);
        int j1 = i1 + abyte3.length;
        byte abyte4[] = new byte[i * j];
        int k1 = 0;
        int l1 = 0;
        do
        {
            if(l1 >= j)
            {
                System.arraycopy(abyte4, 0, m_BmpData, j1, i * j);
                byte[] _tmp = (byte[])null;
                byte[] _tmp1 = (byte[])null;
                return;
            }
            System.arraycopy(abyte0, i * (-1 + (j - l1)), abyte4, k1, i);
            k1 += i;
            l1++;
        } while(true);
    }

    public byte[] toBytes()
    {
        return m_BmpData;
    }

    private byte m_BmpData[];
    private BITMAPFILEHEADER m_fileHeaderBitmap;
    private BITMAPINFO m_infoBitmap;
}
