/* $Id$ */
package com.muthuraj.chat.util;

import com.android.volley.VolleyError;

/**
 * Created by muthu-3955 on 27/11/16.
 */
public interface RequestProcessorListener {

    void onSuccess(String response);

    void onLoading();

    void onError(VolleyError error);

    abstract class SimpleRequestProcessor implements RequestProcessorListener{
        @Override
        public void onError(VolleyError error) {
            //some error occurred
        }

    }
}
