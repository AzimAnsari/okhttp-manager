package com.azim.library;

/**
 * Created by Azim Ansari on 08/02/2018.
 * Network Listener to be implemented by the user
 */
public interface NetworkListener {
    /**
     * Called when there is successful(200) response returned from the server.
     *
     * @param API      Requested API.
     * @param response Response string from the server.
     */
    void onSuccess(String API, String response);

    /**
     * Called when there is an error returned from the server.
     *
     * @param API      Requested API.
     * @param errorMsg Error message from the server.
     */
    void onError(String API, String errorMsg);
}