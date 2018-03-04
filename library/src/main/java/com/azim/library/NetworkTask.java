package com.azim.library;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Azim Ansari on 08/02/2018.
 * Asynchronous network task.
 * Note : Must be instantiated by {@link NetworkManager}
 */
class NetworkTask extends AsyncTask<NetworkRequest, Void, String> {

    private static final String TAG = NetworkTask.class.getSimpleName();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    private boolean isError = true;
    private NetworkRequest networkRequest;
    private NetworkListener mListener;

    NetworkTask(NetworkListener listener) {
        mListener = listener;
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Override
    protected String doInBackground(NetworkRequest... params) {

        try {
            if (isCancelled()) {
                isError = true;
                return "Task canceled";
            }
            networkRequest = params[0];

            if (networkRequest.getMethod() == null) {
                throw new IllegalStateException("Invalid Request Method.");
            } else if (TextUtils.isEmpty(networkRequest.getUrl())) {
                throw new IllegalStateException("Invalid server url.");
            }

            Log.i(TAG, "==========================================================================================");
            Log.i(TAG, networkRequest.getMethod() + " : " + networkRequest.getUrl() + networkRequest.getCompleteApi());

            if (networkRequest.getMethod() == RequestMethod.METHOD_GET) {
                Request request = new Request.Builder()
                        .url(networkRequest.getUrl() + networkRequest.getCompleteApi())
                        .build();
                if (isCancelled()) {
                    isError = true;
                    Log.d(TAG, "Request : " + networkRequest.getApi() + " canceled.");
                }

                Log.i(TAG, "==========================================================================================");

                String data = null;
                if (networkRequest.isDummyRequest()) {
                    Thread.sleep(networkRequest.getDummyRequestTimeoutMilliseconds());
                    data = networkRequest.getMockJsonData();
                } else {
                    Response response = client.newCall(request).execute();
                    ResponseBody body = response.body();
                    if (body != null)
                        data = body.string();
                }

                if (isCancelled()) {
                    isError = true;
                    Log.d(TAG, "Request : " + networkRequest.getApi() + " canceled.");
                }

                if (TextUtils.isEmpty(data)) {
                    isError = true;
                    return "Empty response from server";
                } else {
                    isError = false;
                    return data;
                }
            } else if (networkRequest.getMethod() == RequestMethod.METHOD_POST) {
                if (networkRequest.isEmptyParams()) {
                    isError = true;
                    return "Can't perform POST request with EMPTY parameters";
                } else {
                    Request request;
                    if (!TextUtils.isEmpty(networkRequest.getJsonParam())) {
                        Log.i(TAG, "Params :" + networkRequest.getJsonParam());
                        request = new Request.Builder()
                                .url(networkRequest.getUrl() + networkRequest.getCompleteApi())
                                .addHeader("Content-type", "application/json")
                                .post(RequestBody.create(JSON, networkRequest.getJsonParam()))
                                .build();
                    } else {
                        FormBody.Builder builder = new FormBody.Builder();
                        Log.i(TAG, "Params :");
                        for (Map.Entry<String, String> entry : networkRequest.getFormEntities().entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            Log.i(TAG, key + " : " + value);
                            builder.add(key, value);
                        }

                        request = new Request.Builder()
                                .url(networkRequest.getUrl() + networkRequest.getCompleteApi())
                                .post(builder.build())
                                .build();

                    }
                    Log.i(TAG, "==========================================================================================");
                    if (isCancelled()) {
                        isError = true;
                        Log.d(TAG, "Request : " + networkRequest.getApi() + " canceled.");
                    }

                    String data = null;
                    if (networkRequest.isDummyRequest()) {
                        Thread.sleep(networkRequest.getDummyRequestTimeoutMilliseconds());
                        data = networkRequest.getMockJsonData();
                    } else {
                        Response response = client.newCall(request).execute();
                        ResponseBody body = response.body();
                        if (body != null)
                            data = body.string();
                    }

                    if (isCancelled()) {
                        isError = true;
                        Log.d(TAG, "Request : " + networkRequest.getApi() + " canceled.");
                    }

                    if (TextUtils.isEmpty(data)) {
                        isError = true;
                        return "Empty response from server";
                    } else {
                        isError = false;
                        return data;
                    }
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            isError = true;
            return "SocketTimeoutException";
        } catch (IllegalStateException e) {
            e.printStackTrace();
            isError = true;
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            isError = true;
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            isError = true;
            return e.getMessage();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String response) {
        NetworkManager.getInstance().onTaskFinished(networkRequest.getApi());
        Log.i(TAG, "==========================================================================================");
        Log.i(TAG, networkRequest.getMethod() + " : " + networkRequest.getUrl() + networkRequest.getCompleteApi());
        Log.i(TAG, "Response : " + response);
        Log.i(TAG, "==========================================================================================");
        if (mListener != null && !isError) {
            mListener.onSuccess(networkRequest.getApi(), response);
        } else if (mListener != null) {
            mListener.onError(networkRequest.getApi(), response);
        }
    }
}