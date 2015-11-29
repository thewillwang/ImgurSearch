package fieldwire.willsays.com.fieldwireimgur.Task;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fieldwire.willsays.com.fieldwireimgur.Adapter.RecyclerImageAdapter;
import fieldwire.willsays.com.fieldwireimgur.Data.Image;
import fieldwire.willsays.com.fieldwireimgur.Data.ImgurConstants;

/**
 * Created by jingweiwang on 11/28/15.
 */
public class ImageAsyncTask extends AsyncTask<String, Void, String> {

    private String Content;
    private RecyclerImageAdapter imageAdapter;

    public ImageAsyncTask(RecyclerImageAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    //convert InputStream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }

    @Override
    protected String doInBackground(String... urls) {

        String urlString = urls[0];
        // HTTP Get
        try {

            HttpGet httpGet = new HttpGet(urlString);
            httpGet.setHeader("Authorization", "Client-ID " + ImgurConstants.MY_IMGUR_CLIENT_ID);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");

            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // Append Server Response To Content String

            Content = convertInputStreamToString(inputStream);

        } catch (Exception e) {
            Log.e("Reject", e.getMessage());
            return e.getMessage();
        }

        return null;

    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

        /****************** Start Parse Response JSON Data *************/

        String OutputData = "";
        JSONObject jsonResponse;

        try {

            List<Image> data = new ArrayList<Image>();

            jsonResponse = new JSONObject(Content);
            // parse to Json array
            JSONArray jsonMainNode = jsonResponse.optJSONArray("data");

            int lengthJsonArr = jsonMainNode.length();

            // add to the data list
            for (int i = 0; i < lengthJsonArr; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                data.add(new Image(jsonChildNode.optString("link").toString(), jsonChildNode.optString("title").toString(),
                        jsonChildNode.optString("description").toString()
                ));
            }
            // append to the data list
            imageAdapter.add(data);

        } catch (JSONException e) {

            e.printStackTrace();
        }

    }
}
