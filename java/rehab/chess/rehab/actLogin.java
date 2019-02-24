package rehab.chess.rehab;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;


public class actLogin extends AppCompatActivity  {
    //if luser is empty, user is not logged in, when he logs in, this attribute will be filled with his username
    private String lUser="";
    public static final String loginURL="https://chess.rehab/check.php";
    private static Boolean logged=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login);
        setScreen();
        setFonts();


    }
    private void setScreen() {
        EditText userField= (EditText) findViewById(R.id.actLoginUserEditText);
        EditText passField= (EditText) findViewById(R.id.actLoginPassEditText);

        //check in preferences if the username is stored in, so we do not have to log in everytime
        SharedPreferences sPref= getSharedPreferences("rehab",MODE_PRIVATE);
        String sUsername = sPref.getString("login_name","");

        this.lUser=sUsername;

        if (this.lUser.equals("")) {
            //if user is not logged in, setup the the login screen, hide 'continue' button
            Button logButton = (Button) findViewById(R.id.actLoginButton);
            Button logOutButton = (Button) findViewById(R.id.actLogoutButton);

            logOutButton.setVisibility(View.INVISIBLE);
            //setup a button for login in
            logButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginIn();
                }
            });
        } else {

            //preferences remember our previous login so the screen will be setup differently
            Button logButton = (Button) findViewById(R.id.actLoginButton);
            Button logOutButton = (Button) findViewById(R.id.actLogoutButton);

            logButton.setText(new StringBuilder().append(getString(R.string.loggedButtonDisplay)).append(" ").append(this.lUser).toString());
            logOutButton.setVisibility(View.VISIBLE);
            //login fields will be hidden
            userField.setVisibility(View.INVISIBLE);
            passField.setVisibility(View.INVISIBLE);

            //setup the button to 'continue' as remebered user
            logButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loggedIn();
                }
            });
            //setup a button to switch users-meaning to logout current remembered user
            logOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logMeOut();

                }
            });


        }
    }
    public void setFonts() {
        TextView tvLogin= (TextView) findViewById(R.id.actLoginUserEditText);
        TextView tvPassword= (TextView) findViewById(R.id.actLoginPassEditText);
        Button bLogin= (Button) findViewById(R.id.actLoginButton);
        Button bOtheruser= (Button) findViewById(R.id.actLogoutButton);
        Button bFlp= (Button) findViewById(R.id.buttFlip);

        Typeface sansLight=Typeface.createFromAsset(getAssets(),"fonts/Sansation_Light.ttf");
        Typeface sansLightBold=Typeface.createFromAsset(getAssets(),"fonts/Sansation_Bold.ttf");

        tvLogin.setTypeface(sansLight);
        tvPassword.setTypeface(sansLight);

        bLogin.setTypeface(sansLightBold);
        bOtheruser.setTypeface(sansLight);
    }
    private void logMeOut() {
        //function to delete preferences and rerun the login screen to login as someone else
        SharedPreferences sPref= getSharedPreferences("rehab",MODE_PRIVATE);
        SharedPreferences.Editor sEditor= sPref.edit();

        sEditor.putString("login_name","");
        sEditor.putInt("login_id",0);
        sEditor.putString("login_email","");
        sEditor.commit();

        finish();
        startActivity(getIntent());

    }

    private void loginIn() {
        //action preformed after login button is pressed
        EditText userField= (EditText) findViewById(R.id.actLoginUserEditText);
        EditText passField= (EditText) findViewById(R.id.actLoginPassEditText);
        int userLength= userField.getText().length();
        int passLength= passField.getText().length();

        //check if the fields are filled in
        if (userLength>0 & passLength>0) {
            String userName=userField.getText().toString();
            String passWord=passField.getText().toString();
            //create a string to put into one of the http post parameters (x)
            String js_text= "{\"login\":\""+userName+"\",\"password\":\""+passWord+"\"}";

            final String stringFromJSON= js_text;

            String stringForPost;

            //create a string request for http volley library, with listener that will listen to response. One listener for successful response and one for error response
            StringRequest stringRequest= new StringRequest(StringRequest.Method.POST, loginURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //this will be executed after succesfull http response from the serverside script
                            String json_response_string = response.toString();
                            //this will be executed when http response is succesfull but the user/pass combo has not been succesfull
                            if (json_response_string.equals("nula")) {
                               loginFailed();

                            } else {
                                //this will be executed after user/pass combo worked
                                try {
                                    //parsing the stupid doubled array from php
                                    JSONArray jArray0 = new JSONArray(json_response_string);
                                    JSONArray jArray = jArray0.getJSONArray(0);


                                    String userName = jArray.getString(1);
                                    int userid = jArray.getInt(0);
                                    String useremail = jArray.getString(2);
                                    //writing user info into prefs to remember the login
                                    SharedPreferences sPref = getSharedPreferences("rehab", MODE_PRIVATE);
                                    SharedPreferences.Editor sEditor = sPref.edit();
                                    sEditor.putString("login_name", userName);
                                    sEditor.putInt("login_id", userid);
                                    sEditor.putString("login_email", useremail);
                                    sEditor.commit();

                                    //launching games activity after succesful login
                                    launchGames();


                                } catch (JSONException e) {
                                    Log.e("rehab", e.toString());
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("rehab",error.toString());
                }
            }) {
                //adding the overriden method to StringRequest class instance to post the parametres, has to be a map object with <String>
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("y", "3");
                    params.put("x", stringFromJSON);
                    return params;
                }
            };
            RequestQueue queue = VolleySingleton.getMyInstance(this).getMyRequestQueue();
            queue.add(stringRequest);
        }else {
            Toast.makeText(actLogin.this, "Enter username and password first", Toast.LENGTH_SHORT).show();
        }
    }
    private void loggedIn() {
        //this method is followed when login was remembered and user wants to continue as it
        Intent intent= new Intent(this,actGames.class);
        startActivity(intent);
    }
    public void loginFailed() {
        EditText userField= (EditText) findViewById(R.id.actLoginUserEditText);
        EditText passField= (EditText) findViewById(R.id.actLoginPassEditText);
        Toast.makeText(this, R.string.tmsgLoginFailed, Toast.LENGTH_LONG).show();
        userField.requestFocus();
        userField.setText("");
        passField.setText("");
    }
    public void launchGames() {
        Intent intent = new Intent(this, actGames.class);
        startActivity(intent);
    }

}
