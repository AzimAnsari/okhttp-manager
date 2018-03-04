package com.azim.library;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Azim Ansari on 08/02/2018.
 * Basic request builder for {@link NetworkManager }
 */
public class NetworkRequest {

    private String url;
    private String jsonParam;
    private RequestMethod method;
    private String api;

    /**
     * Dummy request timeout in milliseconds
     */
    private long dummyRequestTimeoutMilliseconds;

    /**
     * Flag for dummy request
     */
    private boolean isDummyRequest;

    /**
     * Dummy Json data
     */
    private String mockJsonData;

    /**
     * Token to add in request header by default, if present.
     */
    private String headerToken;

    /**
     * Considering empty parameters for {@link RequestMethod}.POST method by default.
     */
    private boolean isEmptyParams = true;
    /**
     * Collection of key-value for {@link RequestMethod}.POST request as form entity parameters.
     */
    private Map<String, String> keyValueParams;

    private NetworkRequest(Builder builder) {
        api = builder.api;
        url = builder.url;
        jsonParam = builder.jsonParam;
        method = builder.method;
        headerToken = builder.headerToken;
        isEmptyParams = builder.isEmptyParams;
        keyValueParams = builder.keyValueParams;
        isDummyRequest = builder.isDummyRequest;
        dummyRequestTimeoutMilliseconds = builder.dummyRequestTimeoutMilliseconds;
        mockJsonData = builder.mockJsonData;
    }

    String getApi() {
        return api.substring(api.lastIndexOf("/") + 1);
    }

    String getJsonParam() {
        return jsonParam;
    }

    RequestMethod getMethod() {
        return method;
    }

    String getUrl() {
        return url;
    }

    String getCompleteApi() {
        return api;
    }

    String getHeaderToken() {
        return headerToken;
    }

    Map<String, String> getFormEntities() {
        return keyValueParams;
    }

    boolean isEmptyParams() {
        return isEmptyParams;
    }

    boolean isDummyRequest() {
        return isDummyRequest;
    }

    long getDummyRequestTimeoutMilliseconds() {
        return dummyRequestTimeoutMilliseconds;
    }

    String getMockJsonData() {
        return mockJsonData;
    }

    /**
     * {@code NetworkRequest} builder static inner class.
     */
    public static final class Builder {
        private String api;
        private String url;
        private String jsonParam;
        private String headerToken;
        private String mockJsonData;
        private RequestMethod method;
        private boolean isEmptyParams;
        private boolean isDummyRequest;
        private long dummyRequestTimeoutMilliseconds;
        private Map<String, String> keyValueParams;

        public Builder() {
            // Setting default url from {@link Constants} file
            // url = Constants.WS_URL;
            isEmptyParams = true;
            isDummyRequest = false;
            dummyRequestTimeoutMilliseconds = 1000L;    // Let it be 1 second by default
            keyValueParams = new HashMap<>();
        }

        /**
         * Sets the {@code api} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code api} to set
         * @return a reference to this Builder
         */
        public Builder setApi(String val) {
            api = val;
            return this;
        }

        /**
         * Sets the {@code url} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code url} to set
         * @return a reference to this Builder
         */
        public Builder setUrl(String val) {
            url = val;
            return this;
        }

        /**
         * Sets the {@code jsonParam} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code jsonParam} to set
         * @return a reference to this Builder
         */
        public Builder setJsonParam(String val) {
            jsonParam = val;
            if (!TextUtils.isEmpty(val))
                isEmptyParams = false;
            return this;
        }

        /**
         * Sets the {@code mockJsonData} and returns a reference to this Builder so that the methods can be chained together.
         * {@code isDummyRequest} must be enabled for this
         *
         * @param val the {@code jsonParam} to set
         * @return a reference to this Builder
         */
        public Builder setMockJsonData(String val) {
            mockJsonData = val;
            return this;
        }

        /**
         * Sets the {@code isDummyRequest} and returns a reference to this Builder so that the methods can be chained together.
         * If dummy request is enabled then {@link NetworkTask} will not call this API and will return the given mock data
         *
         * @param val the {@code isDummyRequest} to set
         * @return a reference to this Builder
         */
        public Builder setDummyRequest(boolean val) {
            isDummyRequest = val;
            return this;
        }

        /**
         * Sets the {@code dummyRequestTimeoutMilliseconds} timeout in milliseconds for the dummy request and returns a reference to this Builder so that the methods can be chained together.
         * {@code isDummyRequest} must be enabled for this
         *
         * @param val the {@code dummyRequestTimeoutMilliseconds} timeout for dummy request
         * @return a reference to this Builder
         */
        public Builder setDummyRequestTimeoutMilliseconds(long val) {
            this.dummyRequestTimeoutMilliseconds = val;
            return this;
        }

        /**
         * Sets the {@code headerToken} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code headerToken} to set
         * @return a reference to this Builder
         */
        public Builder setHeaderToken(String val) {
            headerToken = val;
            return this;
        }

        /**
         * Sets the {@code method} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code method} to set
         * @return a reference to this Builder
         */
        public Builder setMethod(RequestMethod val) {
            method = val;
            return this;
        }

        /**
         * Sets the {@code method} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param key   the key for form entity in POST method
         * @param value the value for the given key
         * @return a reference to this Builder
         */
        public Builder addParam(String key, String value) {
            keyValueParams.put(key, value);
            if (!TextUtils.isEmpty(key))
                isEmptyParams = false;
            return this;
        }

        /**
         * Returns a {@code NetworkRequest} built from the parameters previously set.
         *
         * @return a {@code NetworkRequest} built with parameters of this {@code NetworkRequest.Builder}
         */
        public NetworkRequest build() {
            return new NetworkRequest(this);
        }
    }
}