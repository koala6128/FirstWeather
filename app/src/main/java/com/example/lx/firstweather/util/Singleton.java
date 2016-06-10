package com.example.lx.firstweather.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by koala on 2016/6/9.
 */
public class Singleton {
    private static Singleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;

    public Singleton(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static synchronized Singleton getInstance(Context context){
        if (mInstance == null){
            mInstance = new Singleton(context);
        }
        return mInstance;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
