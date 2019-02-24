package rehab.chess.rehab;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class actGames extends AppCompatActivity implements mDialogChallenge.Echo {
    public static final String gamesURLCheck="https://chess.rehab/check.php";
    public static final String gamesURL="https://chess.rehab/cnnct.php";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter vpAdapter;
    Context myContext;
    int login_id;
    String login_name;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_games);
        myContext=this;

        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        vpAdapter= new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);

        SharedPreferences sp=getSharedPreferences("rehab",Context.MODE_PRIVATE);
        login_id = sp.getInt("login_id",0);
        login_name=sp.getString("login_name","");



        tabLayout.getTabAt(0).setText(R.string.tabActiveGames);
        tabLayout.getTabAt(2).setText(R.string.tabChallenges);
        tabLayout.getTabAt(1).setText(R.string.tabFinishedGames);

        changeTabsFont();
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_qb0);
        //tabLayout.getTabAt(2).setIcon(R.drawable.ic_challenge);

        

    }
    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    AssetManager mgr = getApplicationContext().getAssets();
                    Typeface tf = Typeface.createFromAsset(mgr, "fonts/Sansation_Bold.ttf");//Font file in /assets
                    ((TextView) tabViewChild).setTypeface(tf);
                }
            }
        }
    }

    @Override
    public void dialogResponseGet(String response, String purpose,Challenge ch) {
        String moveResponse=response;
        if (purpose.equals("acceptChallenge")) {
            if (moveResponse.equals("Cancel")) {
            }
            if (moveResponse.equals("OK")) {
                queryAcceptChallenge(ch);
            }
        }
        if (purpose.equals("cancelChallenge")) {
            if (moveResponse.equals("Cancel")) {
            }
            if (moveResponse.equals("OK")) {
                queryDeleteChallenge(ch);
            }
        }

    }
    private void createNewBoard(Challenge ch,String newGameId) {
        Game g=new Game();
        g.setGame_idgame(Integer.parseInt(newGameId));
        if (ch.getIdChallenger()==login_id) {
            if(ch.getColor().equals("w")) {
                g.setGame_black_user(login_id);
                g.setGame_white_user(ch.getIdChallenged());
            } else {
                g.setGame_black_user(ch.getIdChallenged());
                g.setGame_white_user(login_id);
            }
        } else {
            if(ch.getColor().equals("w")) {
                g.setGame_black_user(ch.getIdChallenged());
                g.setGame_white_user(login_id);
            } else {
                g.setGame_black_user(login_id);
                g.setGame_white_user(ch.getIdChallenged());
            }
        }
        Board newBoard=new Board(g);
        newBoard.setBoardStart();
        queryCreateNewBoard(newBoard);

    }

    private void queryCreateNewBoard(Board newBoard) {
        final String strJson= newBoard.toJSON();
        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        if (Response.equals("ok")) {
                            Toast.makeText(myContext,"Game has been activated",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(myContext,"error:"+ Response,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();
                    }}) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("y", "0");
                params.put("x", strJson);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }


    private void queryAcceptChallenge(Challenge ch) {
        final Challenge ch0=ch;
        String js_text = "{\"login\":\"" + login_name + "\",\"id\":\"" + login_id + "\",\"chid\":\""+ch.getIdChallenge()+"\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURLCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        try {
                            JSONObject jObject0 = new JSONObject(Response.toString());
                            String new_game_id = jObject0.getString("gameid");
                            //Toast.makeText(myContext, new_game_id, Toast.LENGTH_SHORT).show();
                            createNewBoard(ch0,new_game_id);

                        } catch(JSONException e) {
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
                params.put("y", "6");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }
    private void queryDeleteChallenge(Challenge ch) {

        String js_text = "{\"login\":\"" + login_name + "\",\"id\":\"" + login_id + "\",\"chid\":\""+ch.getIdChallenge()+"\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURLCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                      //  Toast.makeText(myContext,idChallenge+" "+Response,Toast.LENGTH_SHORT).show();
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
                params.put("y", "7");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }

}
