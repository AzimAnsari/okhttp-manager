# okhttp-manager
Android okhttp network engine is encapsulated to provide simple and thread safe network calls. We are using http://square.github.io/okhttp version 3.10.0 as dependency in this project. The project has many simple and extraordinary features. It simplifies your life with Android okhttp integration in your project.

## Getting Started

### GET Request

Easy to use

```
NetworkRequest.Builder getRequest = new NetworkRequest.Builder();
getRequest.setUrl("https://github.com/AzimAnsari/");
getRequest.setApi("get_user_details");
getRequest.setMethod(RequestMethod.METHOD_GET);
NetworkManager.getInstance().addToRequestQueue(getRequest.build(), this);
```

Url will be appended with the API and fethed as string response. 

### POST Json object

You can post json object directly to any URL

```
NetworkRequest.Builder postRequest = new NetworkRequest.Builder();
postRequest.setUrl("https://github.com/AzimAnsari/");
postRequest.setApi("post_details");
postRequest.setMethod(RequestMethod.METHOD_POST);
postRequest.setJsonParam("{'name':'MyName', 'data':'MyData'}");
NetworkManager.getInstance().addToRequestQueue(postRequest.build(), this);
```

### Handling Response

Response can be either success or failure as expected. There is a NetworkListener interface by which you can receive response in 

```
void onSuccess(String API, String response);
void onError(String API, String errorMsg);
```

It gives the calling API and response string. Most probably you will be implementing it like 

```
    @Override
    public void onSuccess(String API, String response) {
        if (API.equals(callingAPI)) {
            if (response.startsWith("SyntaxError:"))
                onError(API, "Invalid JSON");
            else
                tvResponse.setText(response);
        }
    }

    @Override
    public void onError(String API, String errorMsg) {
        if (API.equals(callingAPI)) {
            tvResponse.setText(errorMsg);
        }
    }
```

### POST Form data

Offcourse you can post form data as key value pairs

```
        NetworkRequest.Builder postRequest = new NetworkRequest.Builder();
        postRequest.setUrl(baseURL);
        postRequest.setApi(api);
        postRequest.setMethod(RequestMethod.METHOD_POST);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            postRequest.addParam(entry.getKey(), entry.getValue());
        }
        NetworkManager.getInstance().addToRequestQueue(postRequest.build(), this);
```

### What if my backend team is slow

If your server APIs are not ready, you can simulate request and response using mock data

```
        NetworkRequest.Builder getRequest = new NetworkRequest.Builder();
        getRequest.setUrl(baseURL);
        getRequest.setApi(api);
        getRequest.setMethod(RequestMethod.METHOD_POST);
        getRequest.setJsonParam("{'name':'MyName', 'data':'MyData'}");
        getRequest.setDummyRequest(true);
        getRequest.setDummyRequestTimeoutMilliseconds(2000L);
        getRequest.setMockJsonData("{'sample':'sample', 'json':'json', 'response' : 'response'}");
        NetworkManager.getInstance().addToRequestQueue(getRequest.build(), this);
```

Here you can set dummy request, give its request time in which your mock json response will be returned as it is.
