package rehab.chess.rehab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentGameActive extends Fragment {
    View v;
    Context myContext;
    public static final String gamesURL="https://chess.rehab/check.php";
    private RecyclerView myRV;
    private rvAdapter myAdapter;
    public ArrayList<Game> alActive;

    public FragmentGameActive() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myContext= container.getContext();
        v= inflater.inflate(R.layout.fragment_active_games,container,false);
        myRV= (RecyclerView) v.findViewById(R.id.rvActGames);
        myRV.setAdapter(null);
        alActive = new ArrayList<Game>();
        queryActiveGames();

        return v;
    }

    private void queryActiveGames() {
        SharedPreferences sp = myContext.getSharedPreferences("rehab", MODE_PRIVATE);
        String userName = sp.getString("login_name", "");
        String userId = "" + sp.getInt("login_id", 0);

        String js_text = "{\"login\":\"" + userName + "\",\"id\":\"" + userId + "\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {

                        JSONObject jObject = null;
                        try {
                            JSONArray jArray = new JSONArray(Response.toString());

                            for (int i = 0; i < jArray.length(); i++) {
                                Game toAdd = new Game();
                                jObject = jArray.getJSONObject(i);
                                toAdd.setGame_idgame(jObject.getInt("idgame"));
                                toAdd.setUser_name(jObject.getString("name"));
                                toAdd.setGame_black_user(jObject.getInt("black_user"));
                                toAdd.setColor(jObject.getString("color"));
                                toAdd.setGame_move(jObject.getInt("move"));
                                toAdd.setGame_init(jObject.getString("init"));
                                toAdd.setShowgame_visible(jObject.getInt("visible"));
                                toAdd.setGame_white_user(jObject.getInt("white_user"));
                                try {
                                    toAdd.setGame_winner(jObject.getInt("winner"));
                                } catch (Exception e) {
                                    toAdd.setGame_winner(0);
                                }
                                toAdd.setGame_active(jObject.getInt("active"));
                                try {
                                    toAdd.setGame_winreason(jObject.getString("winreason"));
                                } catch (Exception e) {
                                    toAdd.setGame_winreason("");
                                }
                                try {
                                    toAdd.setGame_status(jObject.getString("status"));
                                } catch (Exception e) {
                                    toAdd.setGame_status("");
                                }
                                try {
                                    toAdd.setGame_turn(jObject.getString("turn"));
                                } catch (Exception e) {
                                    toAdd.setGame_turn("");
                                }
                                try {
                                    toAdd.setTs2(jObject.getString("ts2"));
                                } catch (Exception e) {
                                    toAdd.setTs2("");
                                }
                                try {
                                    toAdd.setChatcol(jObject.getString("chatcol"));
                                } catch (Exception e) {
                                    toAdd.setChatcol("");
                                }

                                alActive.add(toAdd);

                            }
                            fillRecyclerActiveGames();
                        } catch (JSONException e) {
                            Log.e("rehab", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("y", "32");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }
    public void fillRecyclerActiveGames() {
        myRV= (RecyclerView) v.findViewById(R.id.rvActGames);
        myRV.setHasFixedSize(true);
        myRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter= new rvAdapter( alActive,getActivity());
        myRV.setAdapter(myAdapter);
    }

}
