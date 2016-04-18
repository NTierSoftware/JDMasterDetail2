package jd.slalom.jdmasterdetail1;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.*;
/* http://www.androidhive.info/2014/07/android-custom-listview-with-image-and-text-using-volley/ */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache{

public static int DefaultLruCacheSize = (int) (Runtime.getRuntime().maxMemory()  / (1024/8)) ;

public LruBitmapCache(){ this(DefaultLruCacheSize); }

public LruBitmapCache(int sizeInKiloBytes){ super(sizeInKiloBytes); }

@Override protected int sizeOf(String key, Bitmap value){
return value.getRowBytes() * value.getHeight() / 1024;
}

@Override public Bitmap getBitmap(String url){ return get(url); }

@Override public void putBitmap(String url, Bitmap bitmap){ put(url, bitmap); }

}//LruBitmapCache
