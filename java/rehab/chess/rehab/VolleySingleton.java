package rehab.chess.rehab;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton myInstance;
    private RequestQueue myRequestQueue;

    private VolleySingleton(Context myContext) {
        myRequestQueue= Volley.newRequestQueue(myContext.getApplicationContext());
    }

    public static synchronized VolleySingleton getMyInstance(Context context) {
        if (myInstance==null) {
            myInstance= new VolleySingleton(context);
        }
        return myInstance;
    }
    public RequestQueue getMyRequestQueue() {
        return myRequestQueue;
    }




}
