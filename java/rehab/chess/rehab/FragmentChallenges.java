package rehab.chess.rehab;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentChallenges  extends Fragment  {
    View v;
    Context myContext;
    public static final String gamesURL="https://chess.rehab/check.php";

    private RecyclerView myRVMy;
    private RecyclerView myRVPri;
    private RecyclerView myRVPub;

    private rvAdapterChall myAdapterMy;
    private rvAdapterChall myAdapterPri;
    private rvAdapterChall myAdapterPub;

    public ArrayList<Challenge> al_my;
    public ArrayList<Challenge> al_priv;
    public ArrayList<Challenge> al_pub;
    public LinearLayoutManager lm;
    private TextView tvChalPlaced;
    private TextView tvChalChallenged;
    private TextView tvChalPublic;
    private Typeface sansLight;
    private Typeface sansLightBold;
    public FragmentChallenges() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myContext= container.getContext();
        lm= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        v= inflater.inflate(R.layout.fragment_challenges,container,false);
        myRVMy= (RecyclerView) v.findViewById(R.id.rvChalls);
        myRVMy.setAdapter(null);

        myRVPri= (RecyclerView) v.findViewById(R.id.rvChalPriv);
        myRVPri.setAdapter(null);
        myRVPub= (RecyclerView) v.findViewById(R.id.rvChallPub);
        myRVPub.setAdapter(null);

        tvChalPlaced= (TextView) v.findViewById(R.id.tvChalPlaced);
        tvChalPublic= (TextView) v.findViewById(R.id.tvChalPublic);
        tvChalChallenged= (TextView) v.findViewById(R.id.tvChalChallenged);
        sansLight=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Sansation_Light.ttf");
        sansLightBold=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Sansation_Bold.ttf");
        tvChalPlaced.setTypeface(sansLightBold);
        tvChalChallenged.setTypeface(sansLightBold);
        tvChalPublic.setTypeface(sansLightBold);




        queryActiveChallenges();

        return v;
    }


    private void queryActiveChallenges() {
        SharedPreferences sp = myContext.getSharedPreferences("rehab", MODE_PRIVATE);
        String userName = sp.getString("login_name", "");
        String userId = "" + sp.getInt("login_id", 0);
        final int UserIdInt= Integer.parseInt(userId);

        String js_text = "{\"login\":\"" + userName + "\",\"id\":\"" + userId + "\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        al_my = new ArrayList<Challenge>();
                        al_priv= new ArrayList<Challenge>();
                        al_pub= new ArrayList<Challenge>();

                        JSONObject jObject = null;
                        try {
                            JSONArray jArray = new JSONArray(Response.toString());

                            for (int i = 0; i < jArray.length(); i++) {
                                Challenge toAdd = new Challenge();
                                jObject = jArray.getJSONObject(i);
                                toAdd.setIdChallenge(jObject.getInt("idchallenge"));
                                toAdd.setChallenger(jObject.getString("name"));
                                toAdd.setColor(jObject.getString("color"));
                                toAdd.setIdChallenger(jObject.getInt("iduser"));
                                toAdd.setPrivate0(jObject.getInt("private"));
                                toAdd.setIdChallenged(jObject.getInt("idchallenged"));
                                toAdd.setChallenged(jObject.getString("challenged"));

                                if (toAdd.getIdChallenger()==UserIdInt) {
                                    al_my.add(toAdd);
                                } else {
                                    if (toAdd.getPrivate0()==1) {
                                        al_priv.add(toAdd);
                                    } else {
                                        al_pub.add(toAdd);
                                    }
                                }

                            }
                            fillRecyclerChallenges();
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
                params.put("y", "5");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }
    public void fillRecyclerChallenges() {
        myRVMy= (RecyclerView) v.findViewById(R.id.rvChalls);
        myRVPri= (RecyclerView) v.findViewById(R.id.rvChalPriv);
        myRVPub= (RecyclerView) v.findViewById(R.id.rvChallPub);

        myRVMy.setHasFixedSize(true);
        myRVMy.setLayoutManager(lm);
        myRVPri.setHasFixedSize(true);
        myRVPri.setLayoutManager( new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myRVPub.setHasFixedSize(true);
        myRVPub.setLayoutManager( new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        myAdapterMy= new rvAdapterChall(al_my,getActivity(),this);
        myAdapterPri= new rvAdapterChall(al_priv,getActivity(),this);
        myAdapterPub= new rvAdapterChall(al_pub,getActivity(),this);

        myRVMy.setAdapter(myAdapterMy);
        myRVPri.setAdapter(myAdapterPri);
        myRVPub.setAdapter(myAdapterPub);

    }


}
