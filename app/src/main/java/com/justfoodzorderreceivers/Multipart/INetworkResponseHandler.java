package com.justfoodzorderreceivers.Multipart;

public interface INetworkResponseHandler<T> {

    void onNetworkResponse(NetworkRequestType type, NetworkResponse response);
}

