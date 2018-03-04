package com.azim.library;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Azim Ansari on 08/02/2018.
 * Main network manager
 */
public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager mInstance;
    private HashMap<String, NetworkTask> calls;

    private NetworkManager() {
        if (calls == null) {
            calls = new HashMap<>();
        }
    }

    public synchronized static NetworkManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    public void addToRequestQueue(NetworkRequest request, NetworkListener listener) {
        NetworkTask task = new NetworkTask(listener);
        calls.put(request.getApi(), task);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
    }

    /**
     * @param API API request to cancel
     */
    public void cancelRequest(String API) {
        if (calls == null || !calls.containsKey(API)) return;

        NetworkTask task = calls.get(API);
        if (task == null) {
            Log.e(TAG, "Invalid Request to cancel");
        } else {
            try {
                task.cancel(true);
            } catch (Exception e) {
                Log.e(TAG, "Task cannot be canceled");
                e.printStackTrace();
            }
        }
    }

    /**
     * Release task on finish
     *
     * @param API api for which the task should be removed
     */
    public void onTaskFinished(String API) {
        if (calls != null && calls.containsKey(API))
            calls.remove(API);
    }
}