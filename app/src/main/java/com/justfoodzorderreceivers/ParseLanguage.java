package com.justfoodzorderreceivers;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseLanguage {
    String langResponse;
    Context context;

    public ParseLanguage(String langResponse, Context context) {
        this.langResponse = langResponse;
        this.context = context;
    }

    public String getLangResponse() {
        return langResponse;
    }

    public void setLangResponse(String langResponse) {
        this.langResponse = langResponse;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public String getParseString(String word){

        String response="";
        try {
            JSONObject jsonObject = new JSONObject(langResponse);

            JSONArray jsonArray = jsonObject.getJSONArray("LanguageList");

            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

            response= jsonObject1.getString(word);
            Log.v("langResponse",response+"   word not availble");


        } catch (JSONException e) {
            response="";
            Log.v("langResponse",response+"   word not availble");
            e.printStackTrace();
        }

        if(response.equals(""))
        return "No Response";
        else
            return response;


    }
}
