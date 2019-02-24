package rehab.chess.rehab;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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


public class actBoard extends AppCompatActivity implements mDialogMove.Echo,mDialogPromo.Echo {
    Context myContext;
    String zaloha;
    Board boardx;
    Game gamex;

    ArrayList<View> alBlackOuts,alWhiteOuts;

    public static final String gamesURL="https://chess.rehab/cnnct.php";
    public static final String gamesURLCheck="https://chess.rehab/check.php";

    int screenWidth,screenHeight,kartaWidth,kartaHeight,titulkaBlackHeight,titulkaBlackWidth,boardfieldWidth,boardSize;

    TableRow trRow[]= new TableRow[8];
    FrameLayout flField[][]= new FrameLayout[8][8];
    FieldView fvField[][]=new FieldView[8][8];
    FieldPiece fpPiece[][]= new FieldPiece[8][8];

    private Paint selectedPaint= new Paint();
    private Paint stressedPaint=new Paint();
    Boolean chatOn;

    String whitePromoShow="";
    int whitePromoJ=-1;
    int whitePromoI=-1;
    String blackPromoShow="";
    int blackPromoJ=-1;
    int blackPromoI=-1;
    Boolean dropped;

    private int login_id;
    private String login_name;

    Button bRes,bDra,bFwd,bRwd,bFlp,bChat;
    ImageView ivPinSecretRefresh;

    TextView tvTopColorLabel,tvBottomColorLabel,tvTopNameLabel,tvBottomNameLabel,tvShow,tvLatest,tvCheckTop,tvCheckBot,tvStatus;
    EditText tvMessage;
    LinearLayout llTopCard,llBotCard,llChat,llChat2;
    LinearLayout.LayoutParams outImageParams,chatBlockParams,chatBubbleParams_right,chatBubbleParams_left;
    ConstraintLayout glbBoardLayout,clLayout;
    TableLayout tlBoard;
    FrameLayout flHmla;
    CardView cvDoska,cvBlack;
    ScrollView svChat;

    Typeface sansLight,sansLightBold;

    Boolean flip;
    ImageView ivHmla;

    DisplayMetrics dm;
    TableRow.LayoutParams trParams;

    char myColor=' ';
    char turnColor=' ';
    Boolean isMyTurn=false;
    Boolean showingLatestMove=false;
    Boolean showingActiveGame=false;
    int latestMove=0;
    int showingMove=0;

    Boolean selectable=false;
    char selectableColor=' ';
    Boolean selectedField;
    int selectedI;
    int selectedJ;

    Drawable dwDraw,dwWin,dwLoss;

    private String moveResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sansLightBold=Typeface.createFromAsset(getAssets(),"fonts/Sansation_Bold.ttf");
        sansLight=Typeface.createFromAsset(getAssets(),"fonts/Sansation_Light.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        SharedPreferences sp=getSharedPreferences("rehab",Context.MODE_PRIVATE);
        login_id = sp.getInt("login_id",0);
        login_name=sp.getString("login_name","");
        bRes= (Button) findViewById(R.id.buttResign);
        bDra= (Button) findViewById(R.id.buttDraw);
        bRes= (Button) findViewById(R.id.buttResign);
        bFwd= (Button) findViewById(R.id.buttFwd);
        bRwd= (Button) findViewById(R.id.buttRwnd);
        bFlp= (Button) findViewById(R.id.buttFlip);
        tvMessage= (EditText) findViewById(R.id.tvMessage);
      //  tvMessage.setImeActionLabel("Send message", KeyEvent.KEYCODE_ENTER);
        ivPinSecretRefresh= (ImageView) findViewById(R.id.ivPinSecretRefresh);
        bChat= (Button) findViewById(R.id.buttChat);
        tvShow= (TextView) findViewById(R.id.tvShow);
        tvLatest= (TextView) findViewById(R.id.tvLatest);
        tvStatus= (TextView) findViewById(R.id.tvStatus);
        flHmla= (FrameLayout) findViewById(R.id.flHmla);
        ivHmla= (ImageView) findViewById(R.id.ivHmla);
        selectedPaint.setColor(getColor(R.color.goldenRod));
        stressedPaint.setColor(getColor(R.color.goldenRod50));
        svChat= (ScrollView) findViewById(R.id.svScrollChat) ;
        zaloha="";
        dropped=false;
        moveResponse="";
        dwDraw=getResources().getDrawable(R.drawable.ic_handshak_white,this.getTheme());
        dwLoss=getResources().getDrawable(R.drawable.ic_dead_white,this.getTheme());
        dwWin=getResources().getDrawable(R.drawable.ic_star_white,this.getTheme());
        chatOn=false;


        tvTopColorLabel= (TextView) findViewById(R.id.tvTopNameColorLabel);
        tvBottomColorLabel= (TextView) findViewById(R.id.tvBottomNameColorLabel);

        tvTopNameLabel= (TextView) findViewById(R.id.tvTopNameLabel);
        tvBottomNameLabel= (TextView) findViewById(R.id.tvBottomNameLabel);

        tvCheckTop= (TextView) findViewById(R.id.tvCheckUp);
        tvCheckBot= (TextView) findViewById(R.id.tvCheckBot);
        tvCheckTop.setText("is in check!");
        tvCheckBot.setText("is in check!");
        tvCheckBot.setVisibility(View.INVISIBLE);
        tvCheckTop.setVisibility(View.INVISIBLE);

        gamex= (Game)  getIntent().getSerializableExtra("GameClass");

        boardx= new Board(gamex);
        boardx.setBoardStart();
        alBlackOuts= new ArrayList<>();
        alWhiteOuts= new ArrayList<>();
        alWhiteOuts.clear();
        alBlackOuts.clear();
        llBotCard= (LinearLayout) findViewById(R.id.llBotCard);
        llTopCard= (LinearLayout) findViewById(R.id.llTopCard);
        llChat= (LinearLayout) findViewById(R.id.llChat);
        llChat2= (LinearLayout) findViewById(R.id.llChat2);
        myContext= this.getApplicationContext();
        flip=false;
        cvDoska= (CardView) findViewById(R.id.cvDoska);
        cvBlack= (CardView) findViewById(R.id.cvBlack);
        clLayout= (ConstraintLayout) findViewById(R.id.clLayout);
        tlBoard= (TableLayout) findViewById(R.id.tlBoard);


        setListeners();
        setFonts();
        setButtons();
        selectable=false;
        selectedField=false;
        selectableColor=' ';
        selectedI=-1;
        selectedJ=-1;


        glbBoardLayout = (ConstraintLayout) findViewById(R.id.glbBoardLayout);
        ViewTreeObserver vto = glbBoardLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                glbBoardLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setDimensions();
                queryBoard("init");

            }
        });

    }

    private void setListeners() {
        ivHmla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showingLatestMove) {
                    queryBoard("latest");
                }


            }
        });
        tlBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingLatestMove & selectedField) {
                    selectPiece(-1,-1);
                }


            }
        });
        tvLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showingLatestMove) {
                    queryBoard("latest");
                }
            }
        });
        tvMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0) {
                    if (s.charAt(s.length() - 1) == '\n') {
                        sendMessage(s, tvMessage);
                    }
                }
            }
        });

    }

    private void sendMessage(Editable s, EditText tvM) {
        String toSend= s.toString();
        toSend=toSend.replaceAll("\"","'");
        toSend=toSend.replaceAll("\\n","");
        tvM.setText("");
        if (toSend.length()>0) {
            querySendMessage(toSend);
        }

    }

    private void setDimensions() {
        dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        screenHeight=dm.heightPixels;
        screenWidth=dm.widthPixels;


        kartaHeight= cvDoska.getHeight();
        kartaWidth= cvDoska.getWidth();
        titulkaBlackHeight= cvBlack.getHeight();
        titulkaBlackWidth= cvBlack.getWidth();

        outImageParams = new LinearLayout.LayoutParams(titulkaBlackHeight/3,titulkaBlackHeight/3);
        boardfieldWidth= (int) titulkaBlackWidth/8;
        boardSize=titulkaBlackWidth;
        chatBlockParams= new LinearLayout.LayoutParams(boardSize,(int) (boardSize * 0.9));

        trParams = new TableRow.LayoutParams(boardfieldWidth,boardfieldWidth,1.0f);
        ivHmla.setLayoutParams(new FrameLayout.LayoutParams(boardSize,boardSize));
        ivHmla.setVisibility(View.INVISIBLE);
        svChat.setLayoutParams(chatBlockParams);
        chatBubbleParams_right= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chatBubbleParams_right.gravity=Gravity.RIGHT;
        chatBubbleParams_right.bottomMargin=5;


        chatBubbleParams_left= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chatBubbleParams_left.gravity=Gravity.LEFT;
        chatBubbleParams_left.bottomMargin=5;






    }

    private void setButtons() {


        bRwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBack();
            }
        });
        bFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFwd();
            }
        });
        bFlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipMe();
            }
        });

        ivPinSecretRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBoard();
            }
        });
        bDra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerDraw();
            }
        });
        bRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResignation();
            }
        });
        bChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatSwitch();
            }
        });


    }

    private void chatSwitch() {
        if (chatOn) {
           hideChat();
        } else {
           showChat();
        }


    }

    private void showChat() {
        chatOn=true;
        llChat.setVisibility(View.VISIBLE);
        queryChatBefore();
    }
    private void queryChatBefore() {
        llChat2.removeAllViews();
        String js_text = "{\"col\":\"" + myColor + "\",\"id\":\"" + login_id + "\",\"gid\":\""+boardx.getGameObj().getGame_idgame()+"\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURLCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                       queryChat();
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
                params.put("y", "20");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }

    private void querySendMessage(String msg) {

        String js_text = "{\"msg\":\"" + msg + "\",\"iduser\":\"" + login_id + "\",\"idgame\":\""+boardx.getGameObj().getGame_idgame()+"\",\"col\":\""+myColor+"\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURLCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        if (Response.equals("OK")) {
                            queryChat();
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
                params.put("y", "19");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }

    private void queryChat() {
        llChat2.removeAllViews();
        String js_text = "{\"login\":\"" + login_name + "\",\"id\":\"" + login_id + "\",\"gid\":\""+boardx.getGameObj().getGame_idgame()+"\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURLCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        try {
                            JSONArray jArray = new JSONArray(Response.toString());
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);


                                TextView msg_text= new TextView(myContext);
                                msg_text.setTypeface(sansLightBold);
                                msg_text.setPadding(5,5,5,5);
                                msg_text.setTextSize(10);
                                msg_text.setTextColor(getResources().getColor(R.color.ghostWhite,myContext.getTheme()));


                                msg_text.setText(jObject.getString("msg"));
                                if (jObject.getInt("iduser")==login_id) {
                                    msg_text.setBackground(getResources().getDrawable(R.drawable.bubble_orange,myContext.getTheme()));
                                    msg_text.setLayoutParams(chatBubbleParams_right);
                                } else {
                                    msg_text.setBackground(getResources().getDrawable(R.drawable.bubble_blue,myContext.getTheme()));
                                    msg_text.setLayoutParams(chatBubbleParams_left);
                                }

                                msg_text.setElevation(7);
                                llChat2.addView(msg_text);


                            }
                            svChat.post(new Runnable() {
                                public void run() {
                                    svChat.fullScroll(View.FOCUS_DOWN);
                                }
                            });


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
                params.put("y", "18");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }

    private void hideChat() {
        chatOn=false;
        llChat.setVisibility(View.INVISIBLE);
    }

    private void sendResignation() {
        if (isMyTurn) {
            mDialogMove mDialog=new mDialogMove();
            mDialog.setTitle("Resign the game?");
            mDialog.setPurpose("sendResign");
            mDialog.show(getSupportFragmentManager(),"sendResign");
        } else {
            Toast.makeText(this,"It is not your turn",Toast.LENGTH_SHORT).show();
        }
    }

    private void offerDraw() {
        if (isMyTurn) {
            mDialogMove mDialog=new mDialogMove();
            mDialog.setTitle("Send a draw offer?");
            mDialog.setPurpose("drawOffer");
            mDialog.show(getSupportFragmentManager(),"offerDraw");
        } else {
            Toast.makeText(this,"It is not your turn",Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshBoard() {
        queryGameToRefresh();
    }

    private void flipMe() {
        if(!flip) {
            flip=true;
        } else {
            flip=false;
        }
        selectedJ=-1;
        selectedI=-1;
        selectedField=false;
        paintMeInit();
        paintNewPieces();
    }

    private void moveFwd() {
        if (!showingLatestMove) {
            queryBoard("fwd");
        }
    }

    private void moveBack() {
        if (showingMove>0) {
            queryBoard("back");
        }
    }


    private void setLabels() {

        if (!flip) {
            tvBottomNameLabel.setText(login_name);
            tvTopNameLabel.setText(boardx.getGameObj().getUser_name());
            if (login_id == boardx.getGameObj().getGame_white_user()) {
                tvBottomColorLabel.setText("White:");
                tvTopColorLabel.setText("Black:");
            } else {
                tvBottomColorLabel.setText("Black:");
                tvTopColorLabel.setText("White:");
            }
        } else {
            tvTopNameLabel.setText(login_name);
            tvBottomNameLabel.setText(boardx.getGameObj().getUser_name());
            if (login_id == boardx.getGameObj().getGame_white_user()) {
                tvBottomColorLabel.setText("Black:");
                tvTopColorLabel.setText("White:");
            } else {
                tvBottomColorLabel.setText("White:");
                tvTopColorLabel.setText("Black:");
            }
        }
    }

    public void setFonts() {
        Typeface sansLight=Typeface.createFromAsset(getAssets(),"fonts/Sansation_Light.ttf");
        Typeface sansLightBold=Typeface.createFromAsset(getAssets(),"fonts/Sansation_Bold.ttf");

        tvTopColorLabel.setTypeface(sansLightBold);
        tvBottomColorLabel.setTypeface(sansLightBold);
        tvTopNameLabel.setTypeface(sansLight);
        tvBottomNameLabel.setTypeface(sansLight);

        bRes.setTypeface(sansLight);
        bDra.setTypeface(sansLight);
        bFlp.setTypeface(sansLight);
        bFwd.setTypeface(sansLightBold);
        bRwd.setTypeface(sansLightBold);

        bChat.setTypeface(sansLight);

        tvLatest.setTypeface(sansLight);
        tvShow.setTypeface(sansLight);
        tvCheckTop.setTypeface(sansLightBold);
        tvCheckBot.setTypeface(sansLightBold);
        tvStatus.setTypeface(sansLightBold);
        tvMessage.setTypeface(sansLight);

    }




    private void  queryBoard(String switcher) {
        String switcher2;
        switch(switcher) {
            case "latest": switcher2="4"; break;
            case "back": switcher2="2";selectPiece(-1,-1); selectable=false;break;
            case "fwd": switcher2="3";selectPiece(-1,-1); selectable=false; break;
            case "init": switcher2="4";  break;
            default: switcher2="4";
        }
        tvStatus.setVisibility(View.INVISIBLE);
        final String switcher3=switcher2;
        String js_text = boardx.toJSON();
        final String stringFromJSON = js_text;

        final String finalSwitcher = switcher2;
        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        try {
                            JSONObject jObject0 = new JSONObject(Response.toString());
                                boardx.setId(Integer.parseInt(jObject0.getString("id")));
                                boardx.setGame(jObject0.getString("game"));
                                boardx.setTurn0(jObject0.getString("turn0"));
                                boardx.setBlack_id(Integer.parseInt(jObject0.getString("black_id")));
                                boardx.setMoveid(jObject0.getInt("moveid"));
                                boardx.setCastleBK(jObject0.getBoolean("castleBK"));
                                boardx.setCastleBQ(jObject0.getBoolean("castleBQ"));
                                boardx.setCastleWK(jObject0.getBoolean("castleWK"));
                                boardx.setCastleWQ(jObject0.getBoolean("castleWQ"));
                                boardx.setWhite_id(Integer.parseInt(jObject0.getString("white_id")));
                                boardx.setEnPassantI(jObject0.getInt("enPassantI"));
                                boardx.setEnPassantJ(jObject0.getInt("enPassantJ"));
                                boardx.setKingBcheck(jObject0.getBoolean("kingBcheck"));
                                boardx.setKingWcheck(jObject0.getBoolean("kingWcheck"));
                                boardx.setCheckMateIs(jObject0.getString("checkMateIs"));
                                boardx.setBlack_resign(jObject0.getBoolean("black_resign"));
                                boardx.setWhite_resign(jObject0.getBoolean("white_resign"));
                                boardx.setEnPassantColor(jObject0.getString("enPassantColor"));
                                boardx.setKingBpositionI(jObject0.getInt("kingBpositionI"));
                                boardx.setKingBpositionJ(jObject0.getInt("kingBpositionJ"));
                                boardx.setKingWpositionI(jObject0.getInt("kingWpositionI"));
                                boardx.setKingWpositionJ(jObject0.getInt("kingWpositionJ"));
                                boardx.setBlack_draw_offer(jObject0.getBoolean("black_draw_offer"));
                                boardx.setWhite_draw_offer(jObject0.getBoolean("white_draw_offer"));

                            if (switcher3.equals("4")) { boardx.setLatestmove(true);}
                            if (switcher3.equals("2")) { boardx.setLatestmove(false);}
                            if (switcher3.equals("3")) { boardx.setLatestmove(false);}

                            //cleaning the activities parameters for a shown game
                            latestMove=-1;
                            showingMove=-1;
                            showingLatestMove=false;
                            showingActiveGame=false;
                            myColor=' ';
                            isMyTurn=false;
                            selectableColor=' ';
                            turnColor=' ';

                            //filling in the activities params from the latest board query

                            if (boardx.getMoveid()==boardx.getGameObj().getGame_move()) { showingLatestMove=true;}
                            if (!boardx.getGameObj().getGame_status().equals("")) { showingActiveGame=true;}

                            if (showingActiveGame) {
                                String kokos=boardx.getGameObj().getGame_turn();
                                if (kokos.length()>0) {
                                    turnColor = kokos.charAt(0);
                                }
                            }
                            latestMove=boardx.getGameObj().getGame_move();
                            showingMove=boardx.getMoveid();
                            if (latestMove==showingMove & latestMove>0) { showingLatestMove=true;}
                            if (boardx.getGameObj().getGame_white_user()==login_id) {myColor='w';}
                            if (boardx.getGameObj().getGame_black_user()==login_id) {myColor='b';}
                            if (turnColor==myColor & showingActiveGame) { isMyTurn=true;};
                            if (showingLatestMove & isMyTurn & showingActiveGame) {selectable=true;}
                            if (showingLatestMove & isMyTurn & showingActiveGame) { selectableColor=myColor;}

                            if (!showingLatestMove) {
                                tvLatest.setBackgroundColor(getResources().getColor(R.color.goldenRod,myContext.getTheme()));
                                ivHmla.setVisibility(View.VISIBLE);
                            } else {
                                tvLatest.setBackgroundColor(getResources().getColor(R.color.ghostWhite50,myContext.getTheme()));
                                ivHmla.setVisibility(View.INVISIBLE);
                            }





                            //filling in kicked out pieces
                            boardx.clearWhiteouts();
                            JSONArray jWhiteouts= jObject0.getJSONArray("whiteouts");
                                for (int i=0;i<jWhiteouts.length();i++) {
                                    boardx.addWhiteout((String) jWhiteouts.get(i));

                                }
                            boardx.clearBlackouts();
                            JSONArray jBlackouts= jObject0.getJSONArray("blackouts");
                            for (int i=0;i<jBlackouts.length();i++) {
                                boardx.addBlackout((String) jBlackouts.get(i));
                            }
                            JSONArray jFields= jObject0.getJSONArray("fields");
                            JSONArray jRows[]= new JSONArray[9];
                            JSONObject jField[][]= new JSONObject[9][9];
                            for (int i=1; i<9;i++) {
                                jRows[i]= jFields.getJSONArray(i);
                                for (int j=1;j<9;j++) {
                                    jField[i][j]= jRows[i].getJSONObject(j);
                                    boardx.setFieldPiece(i,j,(String) jField[i][j].getString("piece1"));
                                    try {
                                        boardx.setFieldStress(i, j, jField[i][j].getInt("stress"));
                                    } catch(Exception e) {
                                        boardx.setFieldStress(i,j,0);
                                    }
                                }
                            }
                            if (chatOn) {
                                llChat.setVisibility(View.VISIBLE);
                            } else {
                                llChat.setVisibility(View.INVISIBLE);
                            }
                            if (switcher3.equals("4")) { paintMeInit();}

                            paintNewPieces();
                            if (showingLatestMove) {
                                if (boardx.getGameObj().getGame_active() == 0) {
                                    if (boardx.getGameObj().getGame_winreason().equals("draw")) {
                                        tvStatus.setCompoundDrawablesWithIntrinsicBounds(dwDraw,null,null,null);
                                        tvStatus.setVisibility(View.VISIBLE);
                                        tvStatus.setText("Game was drawn");
                                    }
                                    if (boardx.getGameObj().getGame_winreason().equals("checkmate")) {
                                        if (boardx.getGameObj().getGame_winner() == login_id) {
                                            tvStatus.setCompoundDrawablesWithIntrinsicBounds(dwWin,null,null,null);
                                            tvStatus.setVisibility(View.VISIBLE);
                                            tvStatus.setText("Won by checkmate!");
                                        } else {
                                            tvStatus.setCompoundDrawablesWithIntrinsicBounds(dwLoss,null,null,null);
                                            tvStatus.setVisibility(View.VISIBLE);
                                            tvStatus.setText("Lost by checkmate");
                                        }

                                    }
                                    if (boardx.getGameObj().getGame_winreason().equals("resignation")) {
                                        if (boardx.getGameObj().getGame_winner() == login_id) {
                                            tvStatus.setCompoundDrawablesWithIntrinsicBounds(dwWin,null,null,null);
                                            tvStatus.setVisibility(View.VISIBLE);
                                            tvStatus.setText("Opponent resigned!");
                                        } else {
                                            tvStatus.setCompoundDrawablesWithIntrinsicBounds(dwLoss,null,null,null);
                                            tvStatus.setVisibility(View.VISIBLE);
                                            tvStatus.setText("You resigned");
                                        }

                                    }
                                } else {
                                    if (!boardx.getGameObj().getGame_status().equals("")) {
                                        if ((boardx.getGameObj().getGame_status().equals("w_drawoff")) && myColor == 'w') {
                                            tvStatus.setVisibility(View.VISIBLE);
                                            tvStatus.setText("You offered a draw");
                                        }
                                        if ((boardx.getGameObj().getGame_status().equals("w_drawoff")) && myColor == 'b') {
                                            boardx.setWhite_draw_offer(true);
                                            acceptDeclineDraw();
                                        }
                                        if ((boardx.getGameObj().getGame_status().equals("b_drawoff")) && myColor == 'b') {
                                            tvStatus.setVisibility(View.VISIBLE);
                                            tvStatus.setText("You offered a draw");
                                        }
                                        if ((boardx.getGameObj().getGame_status().equals("b_drawoff")) && myColor == 'w') {
                                            boardx.setBlack_draw_offer(true);
                                            acceptDeclineDraw();
                                        }
                                    }

                                }
                            }

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
                params.put("y", switcher3);
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }

    private void acceptDeclineDraw() {
            mDialogMove mDialog=new mDialogMove();
            mDialog.setTitle("Do you accept draw offer?");
            mDialog.setPurpose("acceptDraw");
            mDialog.show(getSupportFragmentManager(),"acceptDraw");
    }

    private void loadBoardFromJSON(String js_text) {
        try {
            JSONObject jObject0 = new JSONObject(js_text);
            boardx.setId(Integer.parseInt(jObject0.getString("id")));
            boardx.setGame(jObject0.getString("game"));
            boardx.setTurn0(jObject0.getString("turn0"));
            boardx.setBlack_id(Integer.parseInt(jObject0.getString("black_id")));
            boardx.setMoveid(jObject0.getInt("moveid"));
            boardx.setCastleBK(jObject0.getBoolean("castleBK"));
            boardx.setCastleBQ(jObject0.getBoolean("castleBQ"));
            boardx.setCastleWK(jObject0.getBoolean("castleWK"));
            boardx.setCastleWQ(jObject0.getBoolean("castleWQ"));
            boardx.setWhite_id(Integer.parseInt(jObject0.getString("white_id")));
            boardx.setEnPassantI(jObject0.getInt("enPassantI"));
            boardx.setEnPassantJ(jObject0.getInt("enPassantJ"));
            boardx.setKingBcheck(jObject0.getBoolean("kingBcheck"));
            boardx.setKingWcheck(jObject0.getBoolean("kingWcheck"));
            boardx.setCheckMateIs(jObject0.getString("checkMateIs"));
            boardx.setBlack_resign(jObject0.getBoolean("black_resign"));
            boardx.setWhite_resign(jObject0.getBoolean("white_resign"));
            boardx.setEnPassantColor(jObject0.getString("enPassantColor"));
            boardx.setKingBpositionI(jObject0.getInt("kingBpositionI"));
            boardx.setKingBpositionJ(jObject0.getInt("kingBpositionJ"));
            boardx.setKingWpositionI(jObject0.getInt("kingWpositionI"));
            boardx.setKingWpositionJ(jObject0.getInt("kingWpositionJ"));
            boardx.setBlack_draw_offer(jObject0.getBoolean("black_draw_offer"));
            boardx.setWhite_draw_offer(jObject0.getBoolean("white_draw_offer"));
            boardx.setLatestmove(true);

            //cleaning the activities parameters for a shown game
            latestMove=-1;
            showingMove=-1;
            showingLatestMove=false;
            showingActiveGame=false;
            myColor=' ';
            isMyTurn=false;
            selectableColor=' ';
            turnColor=' ';

            //filling in the activities params from the latest board query

            if (boardx.getMoveid()==boardx.getGameObj().getGame_move()) { showingLatestMove=true;}
            if (!boardx.getGameObj().getGame_status().equals("")) { showingActiveGame=true;}

            if (showingActiveGame) {
                String kokos=boardx.getGameObj().getGame_turn();
                if (kokos.length()>0) {
                    turnColor = kokos.charAt(0);
                }
            }
            latestMove=boardx.getGameObj().getGame_move();
            showingMove=boardx.getMoveid();
            if (latestMove==showingMove & latestMove>0) { showingLatestMove=true;}
            if (boardx.getGameObj().getGame_white_user()==login_id) {myColor='w';}
            if (boardx.getGameObj().getGame_black_user()==login_id) {myColor='b';}
            if (turnColor==myColor) { isMyTurn=true;};
            if (showingLatestMove & isMyTurn) {selectable=true;}
            if (showingLatestMove & isMyTurn & showingActiveGame) { selectableColor=myColor;}


            if (!showingLatestMove) {
                tvLatest.setBackgroundColor(getResources().getColor(R.color.goldenRod,myContext.getTheme()));
                ivHmla.setVisibility(View.VISIBLE);


            } else {
                tvLatest.setBackgroundColor(getResources().getColor(R.color.ghostWhite50,myContext.getTheme()));
                ivHmla.setVisibility(View.INVISIBLE);


            }

            boardx.clearWhiteouts();
            JSONArray jWhiteouts= jObject0.getJSONArray("whiteouts");
            for (int i=0;i<jWhiteouts.length();i++) {
                boardx.addWhiteout((String) jWhiteouts.get(i));

            }
            boardx.clearBlackouts();
            JSONArray jBlackouts= jObject0.getJSONArray("blackouts");
            for (int i=0;i<jBlackouts.length();i++) {
                boardx.addBlackout((String) jBlackouts.get(i));
            }
            JSONArray jFields= jObject0.getJSONArray("fields");
            JSONArray jRows[]= new JSONArray[9];
            JSONObject jField[][]= new JSONObject[9][9];
            for (int i=1; i<9;i++) {
                jRows[i]= jFields.getJSONArray(i);
                for (int j=1;j<9;j++) {
                    jField[i][j]= jRows[i].getJSONObject(j);
                    boardx.setFieldPiece(i,j,(String) jField[i][j].getString("piece1"));

                    try {
                        boardx.setFieldStress(i, j, jField[i][j].getInt("stress"));
                    } catch(Exception e) {
                        boardx.setFieldStress(i,j,0);
                    }


                }
            }
            paintMeInit();
            paintNewPieces();

        } catch (JSONException e) {
            Log.e("rehab", e.toString());
        }
    }

    public void paintMeInit() {
        tlBoard.removeAllViews();
        for (int i=0; i<8;i++) {
            trRow[i]=new TableRow(this);
            tlBoard.addView(trRow[i]);
            for (int y = 0; y < 8; y++) {
                flField[i][y]= new FrameLayout(this);
                flField[i][y].setLayoutParams(trParams);
                trRow[i].addView(flField[i][y]);

                Paint toPaint= new Paint();


                if ((i % 2 == 0) & (y % 2 == 0)) {
                    toPaint.setColor(getColor(R.color.ghostWhite));
                }
                if ((i % 2 == 0) & (y % 2 != 0)) {
                    toPaint.setColor(getColor(R.color.cornFlowerBlue));
                }
                if ((i % 2 != 0) & (y % 2 == 0)) {
                    toPaint.setColor(getColor(R.color.cornFlowerBlue));
                }
                if ((i % 2 != 0) & (y % 2 != 0)) {
                    toPaint.setColor(getColor(R.color.ghostWhite));
                }

                final int ii=i;
                final int yy=y;
                fvField[i][y]= new FieldView(this,toPaint,boardfieldWidth);
                flField[i][y].addView(fvField[i][y]);
                fpPiece[i][y] = new FieldPiece(this, boardfieldWidth,getDrawable(R.drawable.ic_blank),i,y);
                flField[i][y].addView(fpPiece[i][y]);


            }
        }
        setLabels();
    }
    public void selectPiece(int i, int y){
        int ii=-1, yy=-1,s_ii=-1,s_yy=-1,nr_of_moves;
        int trns[];
        int trns2[];
        trns=transValues(i,y);
        ii=trns[0];
        yy=trns[1];
        trns2=transValues(selectedI,selectedJ);
        s_ii=trns2[0];
        s_yy=trns2[1];



        if (selectable) {
           // Toast.makeText(myContext, "" + i + "," + y, Toast.LENGTH_SHORT).show();
            if (selectedField) {
                boardx.setFieldSelected(selectedI,selectedJ,false);
                fvField[s_ii][s_yy].deSelected();
                fvField[s_ii][s_yy].postInvalidate();
                fpPiece[s_ii][s_yy].postInvalidate();
                selectedField=false;
                selectedI=-1;
                selectedJ=-1;
                cleanOptions();
            }
            if (i>-1) {
                boardx.setFieldSelected(i, y, true);
                fvField[ii][yy].setSelected();
                selectedI = i;
                selectedJ = y;
                selectedField=true;
                fvField[ii][yy].postInvalidate();
                fpPiece[ii][yy].postInvalidate();
                nr_of_moves=checkMoves(i,y);
                paintNewPieces();
               // Toast.makeText(myContext,""+nr_of_moves,Toast.LENGTH_SHORT).show();
            }




        }
    }

    private void cleanOptions() {
        for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
                optionField(i,j,false);
                boardx.setFieldOption(i,j,false);
            }
        }
    }
    private void cleanSelect() {
        for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
                boardx.setFieldSelected(i,j,false);

            }
        }


        selectedI=-1;
        selectedJ=-1;
        selectedField=false;
    }

    public void stressField(int i, int y,int toStress){
        int ii=-1, yy=-1;
        int trns[];


        trns=transValues(i,y);
        ii=trns[0];
        yy=trns[1];

        if (toStress==1) {
            fvField[ii][yy].setStressed();
            fvField[ii][yy].postInvalidate();
        }
        if (toStress==0) {
            boardx.setFieldStress(i,y,0);
            fvField[ii][yy].deStressed();
            fvField[ii][yy].postInvalidate();
        }

    }
    public int[] transValues(int i, int j) {
        int ii=-1,yy=-1;
        int[] trans = new int[2];
        if (!flip & myColor=='b') {
            ii=i-1;
            yy=8-j;

        }
        if (flip & myColor=='b') {
            ii = 8 - i;
            yy = j - 1;

        }
        if (!flip & myColor=='w') {
            ii=8-i;
            yy=j-1;

        }
        if (flip & myColor=='w') {
            ii=i-1;
            yy=8-j;

        }
        trans[0]=ii;
        trans[1]=yy;
        return trans;

    }
    public int[] transValues_reversed(int ii, int jj) {
        int i=-1,y=-1;
        int[] trans = new int[2];
        if (!flip & myColor=='b') {
            i=ii+1;
            y=8-jj;

        }
        if (flip & myColor=='b') {
            i = 8 - ii;
            y=jj+1 ;

        }
        if (!flip & myColor=='w') {
            i=8-ii;
            y=jj+1;

        }
        if (flip & myColor=='w') {
            i=ii+1;
            y=8-jj;

        }
        trans[0]=i;
        trans[1]=y;
        return trans;

    }

    public void optionField(int i, int y,Boolean isOption){
        int ii=-1, yy=-1;
        int trns[];


        trns=transValues(i,y);
        ii=trns[0];
        yy=trns[1];

        if (isOption) {
            fpPiece[ii][yy].setOption();
            fpPiece[ii][yy].postInvalidate();
        }
        if (!isOption) {
            fpPiece[ii][yy].deOption();
            fpPiece[ii][yy].postInvalidate();
        }

    }
    public void paintNewPieces() {
        this.tvShow.setText(""+boardx.getMoveid());
        tvLatest.setText(""+boardx.getGameObj().getGame_move());
        llBotCard.removeAllViews();
        llTopCard.removeAllViews();

        tvCheckBot.setVisibility(View.INVISIBLE);
        tvCheckTop.setVisibility(View.INVISIBLE);

        if (!flip) {
            if (myColor=='b') {
                if (boardx.getKingWcheck()) { tvCheckTop.setVisibility(View.VISIBLE); }
                if (boardx.getKingBcheck()) { tvCheckBot.setVisibility(View.VISIBLE); }
            } else {
                if (boardx.getKingWcheck()) { tvCheckBot.setVisibility(View.VISIBLE); }
                if (boardx.getKingBcheck()) { tvCheckTop.setVisibility(View.VISIBLE); }
            }
        } else {
            if (myColor=='b') {
                if (boardx.getKingWcheck()) { tvCheckBot.setVisibility(View.VISIBLE); }
                if (boardx.getKingBcheck()) { tvCheckTop.setVisibility(View.VISIBLE); }
            } else {
                if (boardx.getKingWcheck()) { tvCheckTop.setVisibility(View.VISIBLE); }
                if (boardx.getKingBcheck()) { tvCheckBot.setVisibility(View.VISIBLE); }
            }
        }

        alWhiteOuts.clear();
        alBlackOuts.clear();
        ImageView outPiece;
        String strOutPiece;
        Drawable drwOutPiece;
        //filling in white outted pieces into a arraylist of views
        for (int i=0; i<boardx.getBlackoutsCount();i++) {
            outPiece=new ImageView(myContext);
            outPiece.setLayoutParams(outImageParams);
            strOutPiece =boardx.getBlackout(i);
            drwOutPiece= getPieceResourceDrawable(strOutPiece);
            outPiece.setImageDrawable(drwOutPiece);
            alBlackOuts.add(outPiece);
        }

        //filling in white outted pieces into arraylist of views
        for (int i=0; i<boardx.getWhiteoutsCount();i++) {
            outPiece=new ImageView(myContext);
            outPiece.setLayoutParams(outImageParams);
            strOutPiece =boardx.getWhiteout(i);
            drwOutPiece= getPieceResourceDrawable(strOutPiece);
            outPiece.setImageDrawable(drwOutPiece);
            alWhiteOuts.add(outPiece);
        }

        if(login_id==boardx.getBlack_id()) {
            /////if player is playing black pieces
            for (int i = 0; i < 8; i++) {
                for (int y = 0; y < 8; y++) {
                    final int ii=i+1;
                    final int yy=y+1;

                    if (!flip) {
                        //if the board is not flipped and player is playinmg black pieces
                        fpPiece[i][7 - y].setPiece(getPieceResourceDrawable(boardx.getFieldPiece(i + 1, y + 1)));
                        if (boardx.getFieldStress(i+1,y+1)==1) {
                            stressField(ii,yy,1);
                        }
                        if (boardx.getFieldStress(i+1,y+1)==0 & fvField[i][7-y].getStressed()) {
                            stressField(ii,yy,0);
                        }
                        if(boardx.getFieldPiece(i + 1, y + 1).charAt(1)==selectableColor) {

                            fpPiece[i][7-y].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectPiece(ii,yy);
                                }
                            });
                        }
                        //if the board is flipped and player is playing black pieces
                    } else {

                        fpPiece[7-i][y].setPiece(getPieceResourceDrawable(boardx.getFieldPiece(i + 1, y + 1)));
                        if (boardx.getFieldStress(i+1,y+1)==1) {
                            stressField(ii,yy,1);
                        }
                        if (boardx.getFieldStress(i+1,y+1)==0 & fvField[7-i][y].getStressed()) {
                            stressField(ii,yy,0);
                        }
                        if(boardx.getFieldPiece(i + 1, y + 1).charAt(1)==selectableColor) {
                            fpPiece[7-i][y].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectPiece(ii,yy);

                                }
                            });
                        }
                    }
                }
            }

            for (int i=0; i<alBlackOuts.size();i++) {
                if (!flip) {
                    //if board is not flipped, filling in the black outted pieces
                    llBotCard.addView(alBlackOuts.get(i));
                } else {
                    //if the board is flipped, filling in black outed pieces
                    llTopCard.addView(alBlackOuts.get(i));
                }
            }

            for (int i=0; i<alWhiteOuts.size();i++) {
                if (!flip) {
                    //if the board is not flipped, filling in the
                    llTopCard.addView(alWhiteOuts.get(i));
                } else {
                    llBotCard.addView(alWhiteOuts.get(i));
                }

            }
            ///// if player is playing white pieces
        } else {
            for (int i = 0; i < 8; i++) {
                for (int y = 0; y < 8; y++) {
                    final int ii=i+1;
                    final int yy=y+1;
                    if (!flip) {

                        fpPiece[7-i][y].setPiece(getPieceResourceDrawable(boardx.getFieldPiece(i + 1, y + 1)));
                        if (boardx.getFieldStress(i+1,y+1)==1) {
                            stressField(ii,yy,1);
                        }
                        if (boardx.getFieldStress(i+1,y+1)==0 & fvField[7-i][y].getStressed()) {
                            stressField(ii,yy,0);
                        }
                        if(boardx.getFieldPiece(i + 1, y + 1).charAt(1)==selectableColor) {
                            fpPiece[7-i][y].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectPiece(ii,yy);
                                }
                            });
                        }
                    } else {

                        fpPiece[i][7-y].setPiece(getPieceResourceDrawable(boardx.getFieldPiece(i + 1, y + 1)));
                        if (boardx.getFieldStress(i+1,y+1)==1) {
                            stressField(ii,yy,1);
                        }
                        if (boardx.getFieldStress(i+1,y+1)==0 & fvField[i][7-y].getStressed()) {
                            stressField(ii,yy,0);
                        }
                        if(boardx.getFieldPiece(i + 1, y + 1).charAt(1)==selectableColor) {
                            fpPiece[i][7-y].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectPiece(ii,yy);
                                }
                            });
                        }
                    }
                }
            }

            for (int i=0; i<alBlackOuts.size();i++) {
                if (!flip) {
                    llTopCard.addView(alBlackOuts.get(i));
                } else {
                    llBotCard.addView(alBlackOuts.get(i));
                }
            }

            for (int i=0; i<alWhiteOuts.size();i++) {
                if (!flip) {
                    llBotCard.addView(alWhiteOuts.get(i));
                } else {
                    llTopCard.addView(alWhiteOuts.get(i));
                }
            }

        }


    }

    private Drawable getPieceResourceDrawable(String fieldPiece) {
        switch(fieldPiece) {
            case "Rw0":
                return getResources().getDrawable(R.drawable.ic_rw0,this.getTheme());

            case "Rb0":
                return getResources().getDrawable(R.drawable.ic_rb0, this.getTheme());

            case "Qw0":
                return getResources().getDrawable(R.drawable.ic_qw0,this.getTheme());

            case "Qb0":
                return getResources().getDrawable(R.drawable.ic_qb0,this.getTheme());

            case "Kw0":
                return getResources().getDrawable(R.drawable.ic_kw0,this.getTheme());

            case "Kb0":
                return getResources().getDrawable(R.drawable.ic_kb0, this.getTheme());

            case "Bw0":
                return getResources().getDrawable(R.drawable.ic_bw0,this.getTheme());

            case "Bb0":
                return getResources().getDrawable(R.drawable.ic_bb0,this.getTheme());

            case "Nw0":
                return getResources().getDrawable(R.drawable.ic_nw0,this.getTheme());

            case "Nb0":
                return getResources().getDrawable(R.drawable.ic_nb0,this.getTheme());

            case "pb0":
                return getResources().getDrawable(R.drawable.ic_pb0,this.getTheme());

            case "pw0":
                return getResources().getDrawable(R.drawable.ic_pw0,this.getTheme());

            case "nula":
                return getResources().getDrawable(R.drawable.ic_blank,this.getTheme());
            default:
                return getResources().getDrawable(R.drawable.ic_blank,this.getTheme());
        }
    }

    @Override
    public void dialogResponseGet(String response,String purpose) {
        moveResponse=response;
        if (purpose.equals("confirmMove")) {
            if (moveResponse.equals("Cancel")) {
                sendMoveNot();
            }
            if (moveResponse.equals("OK")) {
                sendMove("mov");
            }
        }
        if (purpose.equals("drawOffer")) {
            if (moveResponse.equals("Cancel")) {
            }
            if (moveResponse.equals("OK")) {
                if (myColor=='w') {
                    boardx.setWhite_draw_offer(true);
                }
                if (myColor=='b') {
                    boardx.setBlack_draw_offer(true);
                }
                sendMove("drw");
            }
        }
        if (purpose.equals("sendResign")) {
            if (moveResponse.equals("Cancel")) {
            }
            if (moveResponse.equals("OK")) {
                if (myColor=='w') {
                    boardx.setWhite_resign(true);
                }
                if (myColor=='b') {
                    boardx.setBlack_resign(true);
                }
                sendMove("res");
            }
        }
        if (purpose.equals("acceptDraw")) {
            if (moveResponse.equals("Cancel")) {
                declineDraw();
            }
            if (moveResponse.equals("OK")) {
                queryAcceptDraw();
            }
        }
    }

    private void queryAcceptDraw() {
        int moveTemp=boardx.getMoveid();
        moveTemp++;
        boardx.setMoveid(moveTemp);
        String js_text = boardx.toJSON();
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {

                            queryGameToRefresh();

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

    private void declineDraw() {
        String js_text = boardx.toJSON();
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.charAt(2)=='o') {
                            queryGameToRefresh();
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
                params.put("y", "8");
                params.put("x", stringFromJSON);
                return params;
            }
        };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);

    }

    @Override
    public char getColorForPromoDialog() {
        return(myColor);
    }
    public void dialogPromoResponseGet(String response) {
        moveResponse=response;
        if (whitePromoI>-1) {

            if (moveResponse.equals("Queening")) {
                dropMove(whitePromoI,whitePromoJ,"Qw");
            }
            ;
            if (moveResponse.equals("Rooking")) {
                dropMove(whitePromoI,whitePromoJ,"Rw");
            }
            ;
            if (moveResponse.equals("Bishoping")) {
                dropMove(whitePromoI,whitePromoJ,"Bw");
            }
            ;
            if (moveResponse.equals("Knighting")) {
                dropMove(whitePromoI,whitePromoJ,"Nw");
            }
            ;
        }
        if (blackPromoI>-1) {

            if (moveResponse.equals("Queening")) {
                dropMove(blackPromoI,blackPromoJ,"Qb");
            }
            ;
            if (moveResponse.equals("Rooking")) {
                dropMove(blackPromoI,blackPromoJ,"Rb");
            }
            ;
            if (moveResponse.equals("Bishoping")) {
                dropMove(blackPromoI,blackPromoJ,"Bb");
            }
            ;
            if (moveResponse.equals("Knighting")) {
                dropMove(blackPromoI,blackPromoJ,"Nb");
            }
            ;
        }
    }


    public class FieldView extends View {
        private Paint col;
        private Boolean isStressed;
        private Rect rect0;
        private Rect rect1;
        private Boolean isSelected;
        int border;

        int size;
        public FieldView(Context context, Paint col, int size) {
            super(context);
            this.col=col;
            this.size=size;
            rect0= new Rect(0,0,size,size);
            this.isSelected=false;
            this.isStressed=false;
            border=size/10;
            rect1=new Rect(border,border,size-border,size-border);

        }
        public void setSelected() {
            this.isSelected=true;
        }
        public void deSelected() {
            this.isSelected=false;
        }
        public void setStressed() {
            this.isStressed=true;
        }
        public void deStressed() {
            this.isStressed=false;
        }

        public Boolean getSelected() {
            return isSelected;
        }
        public Boolean getStressed() {
            return isStressed;
        }

        public void setCol(Paint col) {
            this.col = col;
            postInvalidate();
        }
        public Paint getCol() {
            return(this.col);
        }



        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRect(rect0,col);
            if (isStressed) {
                canvas.drawRect(rect0,stressedPaint);
            }
            if (isSelected) {
                canvas.drawRect(rect0,selectedPaint);
                canvas.drawRect(rect1,col);
            }
        }
    }

    public void clearLastMove() {
        for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
                stressField(i,j,0);
            }
        }
    
    }
    public void sendMove(String action) {
          String swtch0="";
          final String strJson;
          int newMove;

          newMove= boardx.getMoveid();
          final String swtch;

          if (action.equals("mov")) {
                if (boardx.getTurn0().equals("w")) {
                    boardx.setTurn0("b");
                    boardx.getGameObj().setGame_turn("b");
                }
                else {
                    boardx.setTurn0("w");
                    boardx.getGameObj().setGame_turn("w");
                }
                newMove=newMove+1;
                boardx.setMoveid(newMove);
                swtch0="1";


                if (boardx.getCheckMateIs().equals("b")) {
                    boardx.setGame("off");
                    boardx.getGameObj().setGame_winner(boardx.getGameObj().getGame_black_user());
                    boardx.getGameObj().setGame_winreason("checkmate");
                }
                if (boardx.getCheckMateIs().equals("w")) {
                    boardx.setGame("off");
                    boardx.getGameObj().setGame_winner(boardx.getGameObj().getGame_white_user());
                    boardx.getGameObj().setGame_winreason("checkmate");
                }

          }
          if (action.equals("mov0")){
                swtch0="0";
          }

          if (action=="res"){
                newMove=newMove+1;
                boardx.setMoveid(newMove);


                swtch0="5";


                boardx.setGame("off");
                if (boardx.getWhite_resign()) {
                    if (myColor=='w') {
                        boardx.getGameObj().setGame_winner(boardx.getGameObj().getGame_black_user());
                        boardx.getGameObj().setGame_winreason("resignation");
                    }
                    if (myColor=='b') {
                        boardx.getGameObj().setGame_winner(boardx.getGameObj().getGame_black_user());
                        boardx.getGameObj().setGame_winreason("resignation");
                    }
                }
                if (boardx.getBlack_resign()) {
                    if (myColor=='w') {
                        boardx.getGameObj().setGame_winner(boardx.getGameObj().getGame_white_user());
                        boardx.getGameObj().setGame_winreason("resignation");
                    }
                    if (myColor=='b') {
                        boardx.getGameObj().setGame_winner(boardx.getGameObj().getGame_white_user());
                        boardx.getGameObj().setGame_winreason("resignation");
                    }
                }

          }

          if (action.equals("drw")){
                if (boardx.getTurn0().equals("w")) {
                    boardx.setTurn0("b");
                }
                else {
                    boardx.setTurn0("w");
                }
                swtch0="6";
          }




          boardx.getGameObj().setGame_move(boardx.getMoveid());
          boardx.setLatestmove(true);
          dropped=false;
          


          if (!action.equals("mov00")) {

                paintNewPieces();

          }
          swtch=swtch0;
          strJson= boardx.toJSON();
          StringRequest strReq = new StringRequest(Request.Method.POST, gamesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        String resp=Response;
                        selectable=false;
                        selectableColor=' ';
                        isMyTurn=false;
                        queryGameToRefresh();

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
                              params.put("y", swtch);
                              params.put("x", strJson);
                              return params;
                          }
                    };
                RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
                queue.add(strReq);


    }
    public void sendMoveNot() {
        loadBoardFromJSON(zaloha);
    }
    public void dropMove0(int i, int y) {
        int drgi, drgj, drpi, drpj;
        drgi = selectedI;
        drgj = selectedJ;
        drpi = i;
        drpj = y;
        if ((boardx.getFieldPiece(drgi, drgj).charAt(0) == 'p') && (drpi == 8)) {
            whitePromoJ = drpj;
            whitePromoI = drpi;
            blackPromoJ = -1;
            blackPromoI = -1;
            mDialogPromo mDialogP = new mDialogPromo();
            mDialogP.show(getSupportFragmentManager(), "promo");
        }
        if ((boardx.getFieldPiece(drgi, drgj).charAt(0) == 'p') && (drpi == 1)) {
            blackPromoJ = drpj;
            blackPromoI = drpi;
            whitePromoJ = -1;
            whitePromoI = -1;
            mDialogPromo mDialogP = new mDialogPromo();
            mDialogP.show(getSupportFragmentManager(), "promo");
        }
        if ((boardx.getFieldPiece(drgi, drgj).charAt(0) != 'p') || ((drpi != 8) && (drpi != 1))) {
            dropMove(i, y, "nula");
        }
    }
    public void dropMove(int i, int y,String promo) {
        Boolean takeX;
        String takenX;
        int drgi, drgj, drpi, drpj;
        drgi=selectedI;
        drgj=selectedJ;
        drpi=i;
        drpj=y;
        char farba_drop;
        zaloha=boardx.toJSON();
        

            this.clearLastMove();
            takeX=false;
            takenX="";

            //enpassant take after drop to empty field in a different column

            if ((boardx.getFieldPiece(drpi,drpj).equals("nula")) && (drgj!=drpj) && (boardx.getFieldPiece(drgi,drgj).charAt(0)=='p')) {
                if (boardx.getFieldPiece(drgi,drgj).charAt(1)=='b') {
                    takeX=true;
                    takenX=boardx.getFieldPiece(drpi+1,drpj);

                    boardx.setFieldPiece(drpi+1,drpj,"nula");
                    boardx.setFieldStress(drpi+1,drpj,1);

                }
                else {
                    takeX=true;
                    takenX=boardx.getFieldPiece(drpi-1,drpj);
                    boardx.setFieldPiece(drpi-1,drpj,"nula");
                    boardx.setFieldStress(drpi+1,drpj,1);
                }
            }
            //en passant variable checked after a move by 2 fields, relevant for next move

            boardx.setEnPassantI(0);
            boardx.setEnPassantJ(0);
            boardx.setEnPassantColor("nula");
            if (boardx.getFieldPiece(drgi,drgj).charAt(0)=='p') {

                if (boardx.getFieldPiece(drgi,drgj).charAt(1)=='b') {
                    if ((drgi-drpi)==2) {

                        boardx.setEnPassantI(drpi);
                        boardx.setEnPassantJ(drpj);
                        boardx.setEnPassantColor("b");
                    }
                }
                else {
                    if ((drpi-drgi)==2) {

                        boardx.setEnPassantI(drpi);
                        boardx.setEnPassantJ(drpj);
                        boardx.setEnPassantColor("w");
                    }
                }
            }

            // castling check (if the king or rooks have moved to remove castling rights). IF Kings drop is +-2, commits the rook castling move

            if (boardx.getFieldPiece(drgi,drgj).charAt(0)=='K') {
                if (boardx.getFieldPiece(drgi,drgj).charAt(1)=='b') {
                    boardx.setCastleBK(false);
                    boardx.setCastleBQ(false);
                    boardx.setKingBpositionI(drpi);
                    boardx.setKingBpositionJ(drpj);
                    if ((drpj-drgj)==2) {
                        boardx.setFieldPiece(8,8,"nula");
                        boardx.setFieldStress(8,8,1);
                        boardx.setFieldPiece(8,6,"Rb0");
                        boardx.setFieldStress(8,6,1);
                    }
                    if ((drpj-drgj)==-2) {
                        boardx.setFieldPiece(8,1,"nula");
                        boardx.setFieldStress(8,1,1);
                        boardx.setFieldPiece(8,4,"Rb0");
                        boardx.setFieldStress(8,4,1);
                    }
                }
                else {
                    boardx.setCastleWK(false);
                    boardx.setCastleWQ(false);
                    boardx.setKingWpositionI(drpi);
                    boardx.setKingWpositionJ(drpj);
                    if ((drpj-drgj)==2) {
                        boardx.setFieldPiece(1,8,"nula");
                        boardx.setFieldStress(1,8,1);
                        boardx.setFieldPiece(1,6,"Rw0");
                        boardx.setFieldStress(1,6,1);
                    }
                    if ((drpj-drgj)==-2) {
                        boardx.setFieldPiece(1,1,"nula");
                        boardx.setFieldStress(1,1,1);
                        boardx.setFieldPiece(1,4,"Rw0");
                        boardx.setFieldStress(1,4,1);
                    }
                }
            }
            if (boardx.getFieldPiece(drgi,drgj).charAt(0)=='R') {
                if (boardx.getFieldPiece(drgi,drgj).charAt(1)=='b') {
                    if (drgj==1) {
                        boardx.setCastleBQ(false);
                    }
                    if (drgj==8) {
                        boardx.setCastleBK(false);
                    }
                }
                else {
                    if (drgj==1) {
                        boardx.setCastleWQ(false);
                }
                    if (drgj==8) {

                        boardx.setCastleWK(false);
                    }
                }
            }



            if ((!takeX) && (!boardx.getFieldPiece(drpi,drpj).equals("nula"))) {

                takeX=true;
                takenX=boardx.getFieldPiece(drpi,drpj);
            }
            boardx.setFieldPiece(drpi,drpj,boardx.getFieldPiece(drgi,drgj));
            boardx.setFieldStress(drpi,drpj,1);

            if (!promo.equals("nula")) {
                boardx.setFieldPiece(drpi,drpj,promo+"0");
            }


            boardx.setFieldPiece(drgi,drgj,"nula");
            boardx.setFieldStress(drgi,drgj,1);
            farba_drop=boardx.getFieldPiece(drpi,drpj).charAt(1);

            //checks if the kings are in check after the move

            int trns[]=transValues(selectedI,selectedJ);
            int ii=trns[0];
            int yy=trns[1];
            fvField[ii][yy].deSelected();
            fvField[ii][yy].postInvalidate();
            cleanSelect();

            boardx.setKingWcheck(checkCheck('w',1,1,"Kw0",true));
            boardx.setKingBcheck(checkCheck('b',1,1,"Kb0",true));


            //pushes the taken piece into an array of taken pieces
            if (takeX) {
                if (takenX.charAt(1)=='b')  {
                    boardx.addBlackout(takenX);
                }
                else {
                    boardx.addWhiteout(takenX);
                }
            }

            if (boardx.getKingWcheck()) {
                checkMate('w');
            }
            if (boardx.getKingBcheck()) {
                this.checkMate('b');
            }
            //this.moveid++;
            dropped=true;

            cleanOptions();
            paintNewPieces();
            mDialogMove mDialog=new mDialogMove();
            mDialog.setTitle("Send the move?");
            mDialog.setPurpose("confirmMove");
            mDialog.show(getSupportFragmentManager(),"saveMove");
           // Toast.makeText(this,moveResponse,Toast.LENGTH_SHORT).show();

        }

    public void checkMate(char colorCH) {
        // function to check if it is mate
        int movs=0;
        char color1;
        if (colorCH=='b') {
            color1='w';
        }
        else {
            color1='b';
        }

        for (int v=1;v<9;v++) {
            for (int b=1;b<9;b++) {
                if ((!boardx.getFieldPiece(v,b).equals("nula")) && (boardx.getFieldPiece(v,b).charAt(1)!=color1)) {
                    selectedI=v;
                    selectedJ=b;
                    movs+=checkMoves(v,b);
                    cleanSelect();
                    cleanOptions();
                }
            }
        }

        if (movs==0) {
            boardx.setCheckMateIs(String.valueOf(color1));
        }


    }
    public class FieldPiece extends View {
        int size;
        final int ii,yy,i,y;

        Drawable piece;
        Bitmap source;
        Bitmap bitmap;
        private Boolean isOption;
        Paint pt;



        public FieldPiece(Context context,  int size,Drawable drawablePiece,int i,int y) {
            super(context);
            this.size=size;
            piece = drawablePiece;
            isOption=false;
            Bitmap bitmap = ((BitmapDrawable) piece).getBitmap();
            source=Bitmap.createScaledBitmap(bitmap, size, size, true);
            pt= new Paint();
            this.ii=i;
            this.yy=y;
            int[] temp=transValues_reversed(this.ii,this.yy);
            this.i=temp[0];
            this.y=temp[1];




        }
        public void setOption() {
            this.isOption=true;
            this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   dropMove0(i,y);
                 //  dropMove(i,y);

                }
            });
        }
        public void deOption() {
            this.isOption=false;
            this.setOnClickListener(null);
        }
        public void setPiece(Drawable drawablePiece) {
            piece = drawablePiece;
            Bitmap bitmap = ((BitmapDrawable) piece).getBitmap();
            source=Bitmap.createScaledBitmap(bitmap, size, size, true);
            postInvalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(source,0,0,pt);
            if (isOption) {
                canvas.drawCircle(size/2,size/2,size/8, selectedPaint);

            }
            
        }
    }
    public int checkMoves(int i,int j) {
        //function to check and select regular options for moves
        int movcnt=0;
        char piece0= boardx.getFieldPiece(i,j).charAt(0);
        char color0= boardx.getFieldPiece(i,j).charAt(1);
        switch (piece0) {
            case 'p':
                movcnt+=pawnOptions(i,j,color0);
                break;
            case 'N':
                movcnt+=knightOptions(i,j,color0);
                break;
            case 'R':
                movcnt+=rookOptions(i,j,color0);
                break;
            case 'B':
                movcnt+=bishopOptions(i,j,color0);
                break;
            case 'Q':
                movcnt+=queenOptions(i,j,color0);
                break;
            case 'K':
                movcnt+=kingOptions(i,j,color0);
                break;

        }
        return(movcnt);
    }
    public Boolean setOption(int i,int j,char col) {
        Boolean isok=false;
        if (!checkCheck(col,i,j,boardx.getFieldPiece(selectedI,selectedJ),false)) {
            boardx.setFieldOption(i,j,true);
            isok=true;
        }
        return(isok);
    }
    public Boolean checkCheck(char colorX,int posIX,int posJX,String piece0X,Boolean kingX) {
        //function to check if king/field is in check
        int kralI=-1,kralJ=-1,y0,j,i,z0;
        String pieceX="";
        Boolean check0=false,contx;
        char color2=' ',farba,kus;
        if (kingX==false) {

            pieceX=boardx.getFieldPiece(posIX,posJX);

            boardx.setFieldPiece(posIX,posJX,piece0X);


            boardx.setFieldPiece(selectedI,selectedJ,"nula");
            if (piece0X.charAt(0)=='K') {
                kralI=posIX;
                kralJ=posJX;
            }
            else {
                if (colorX=='b') {
                    kralI=boardx.getKingBpositionI();
                    kralJ=boardx.getKingBpositionJ();
                }
                else {
                    kralI=boardx.getKingWpositionI();
                    kralJ=boardx.getKingWpositionJ();
                }
            }
        }

        if (colorX=='b') {
            color2='w';
            //check0=this.kingBcheck;
            if (kingX) {
                kralI=boardx.getKingBpositionI();
                kralJ=boardx.getKingBpositionJ();
            }
        }
        if (colorX=='w') {
            color2='b';
            //check0=this.kingWcheck;
            if (kingX) {
                kralI=boardx.getKingWpositionI();
                kralJ=boardx.getKingWpositionJ();
            }
        }


        //pependicular check check

        if (!check0) {
            contx=true;
            y0=kralI;
            j=kralJ;
            while (contx) {
                y0=y0+1;
                if (y0>8) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(y0,j).charAt(1);
                kus=boardx.getFieldPiece(y0,j).charAt(0);

                if (!boardx.getFieldPiece(y0,j).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='R')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((y0-kralI==1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }

                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        if (check0==false) {
            contx=true;
            y0=kralI;
            j=kralJ;
            while (contx==true) {
                y0=y0-1;
                if (y0<1) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(y0,j).charAt(1);
                kus=boardx.getFieldPiece(y0,j).charAt(0);

                if (!boardx.getFieldPiece(y0,j).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='R')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((y0-kralI==-1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        if (check0==false) {
            contx=true;
            i=kralI;
            z0=kralJ;
            while (contx==true) {
                z0=z0+1;
                if (z0>8) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(i,z0).charAt(1);
                kus=boardx.getFieldPiece(i,z0).charAt(0);

                if (!boardx.getFieldPiece(i,z0).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='R') ) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((z0-kralJ==1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        if (check0==false) {
            contx=true;
            i=kralI;
            z0=kralJ;
            while (contx==true) {
                z0=z0-1;
                if (z0<1) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(i,z0).charAt(1);
                kus=boardx.getFieldPiece(i,z0).charAt(0);

                if (!boardx.getFieldPiece(i,z0).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='R') ) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((z0-kralJ==-1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }

                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        //diagonal check check

        if (!check0) {
            contx=true;
            y0=kralI;
            z0=kralJ;
            while (contx) {
                y0=y0+1;
                z0=z0+1;
                if (y0>8) {
                    contx=false;
                    break;
                }
                if (z0>8) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);

                if (!boardx.getFieldPiece(y0,z0).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='B')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((y0-kralI==1) && (z0-kralJ==1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='b') && (kralI-y0==1) && (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='w') && (y0-kralI==1) && (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        if (!check0) {
            contx=true;
            y0=kralI;
            z0=kralJ;
            while (contx) {
                y0=y0+1;
                z0=z0-1;
                if (y0>8) {
                    contx=false;
                    break;
                }
                if (z0<1) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);

                if (!boardx.getFieldPiece(y0,z0).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='B') ) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((y0-kralI==1) && (z0-kralJ==-1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='b') & (kralI-y0==1) & (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='w') & (y0-kralI==1) & (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        if (!check0) {
            contx=true;
            y0=kralI;
            z0=kralJ;
            while (contx) {
                y0=y0-1;
                z0=z0-1;
                if (y0<1) {
                    contx=false;
                    break;
                }
                if (z0<1) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);

                if (!boardx.getFieldPiece(y0,z0).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='B') ) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((y0-kralI==-1) && (z0-kralJ==-1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='b') && (kralI-y0==1) && (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='w') && (y0-kralI==1) && (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        if (!check0) {
            contx=true;
            y0=kralI;
            z0=kralJ;
            while (contx) {
                y0=y0-1;
                z0=z0+1;
                if (y0<1) {
                    contx=false;
                    break;
                }
                if (z0>8) {
                    contx=false;
                    break;
                }

                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);

                if (!boardx.getFieldPiece(y0,z0).equals("nula")) {
                    if (farba==color2) {
                        if ((kus=='Q') || (kus=='B')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((y0-kralI==-1) && (z0-kralJ==1) && (kus=='K')) {
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='b') && (kralI-y0==1) && (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        if ((colorX=='w') && (y0-kralI==1) && (kus=='p')){
                            check0=true;
                            contx=false;
                            break;
                        }
                        contx=false;
                        break;
                    }
                    else {
                        contx=false;
                        break;
                    }
                }
            }
        }

        // L shape checks check

        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0+1;
            z0=z0-2;
            if ((y0<9) && (z0>0)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0+1;
            z0=z0+2;
            if ((y0<9) && (z0<9)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0+2;
            z0=z0+1;
            if ((y0<9) && (z0<9)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0+2;
            z0=z0-1;
            if ((y0<9) && (z0>0)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0-1;
            z0=z0-2;
            if ((y0>0) && (z0>0)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0-1;
            z0=z0+2;
            if ((y0>0) && (z0<9)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0-2;
            z0=z0+1;
            if ((y0>0) && (z0<9)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);;
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!check0) {

            y0=kralI;
            z0=kralJ;
            y0=y0-2;
            z0=z0-1;
            if ((y0>0) && (z0>0)) {
                farba=boardx.getFieldPiece(y0,z0).charAt(1);
                kus=boardx.getFieldPiece(y0,z0).charAt(0);
                if ((kus =='N') && (farba==color2)) {
                    check0=true;
                }
            }
        }
        if (!kingX) {

            boardx.setFieldPiece(posIX,posJX,pieceX);
            boardx.setFieldPiece(selectedI,this.selectedJ,piece0X);

        }

        return(check0);

    }
    public int pawnOptions(int i,int j,char color0) {
        // pawn options functionm
        int cntopt=0;
        if (color0=='w') {
            if ((i!=2) && (i<8)) {
                if (boardx.getFieldPiece(i+1,j).equals("nula")) {
                    if(setOption(i+1,j,color0)) { cntopt++; optionField(i+1,j,true);  }

                }
            }
            if (i==2) {
                if (boardx.getFieldPiece(i+1,j).equals("nula")) {
                    if(setOption(i+1,j,color0)) { cntopt++;optionField(i+1,j,true);}
                    if (boardx.getFieldPiece(i+2,j).equals("nula")) {
                        if(setOption(i+2,j,color0)) {cntopt++;optionField(i+2,j,true); }
                    }
                }
            }
            String toTakeLeft="nula";
            String toTakeRight="nula";
            if (i<8) {
                if (j<8) {
                    toTakeRight=boardx.getFieldPiece(i+1,j+1);
                }
                if (j>1) {
                    toTakeLeft=boardx.getFieldPiece(i+1,j-1);
                }
                if ((!toTakeRight.equals("nula")) && (toTakeRight.charAt(1)!='w')) {

                    if(setOption(i+1,j+1,color0)) {cntopt++;optionField(i+1,j+1,true); }
                }
                if ((!toTakeLeft.equals("nula")) && (toTakeLeft.charAt(1)!='w')) {
                    if(setOption(i+1,j-1,color0)) { cntopt++;optionField(i+1,j-1,true); }
                }
            }
            if ((i==5) && (boardx.getEnPassantColor().equals("b"))) {
                if (boardx.getEnPassantJ()==(j-1)) {
                    if (setOption(6,j-1,color0)) {cntopt++;optionField(6,j-1,true); }
                }
                if (boardx.getEnPassantJ()==(j+1)) {
                    if (setOption(6,j+1,color0)) {cntopt++;optionField(6,j+1,true);}
                }
            }
        }
        if (color0=='b') {
            if ((i!=7) && (i>1)) {
                if (boardx.getFieldPiece(i-1,j).equals("nula")) {
                    if(setOption(i-1,j,color0)) {cntopt++;optionField(i-1,j,true);}
                }
            }
            if (i==7) {
                if (boardx.getFieldPiece(i-1,j).equals("nula")) {
                    if(setOption(i-1,j,color0)) {cntopt++;optionField(i-1,j,true);}
                    if (boardx.getFieldPiece(i-2,j).equals("nula")) {
                        if(setOption(i-2,j,color0)) {cntopt++;optionField(i-2,j,true);}
                    }
                }
            }
            String toTakeLeft="nula";
            String toTakeRight="nula";
            if (i>1) {
                if (j<8) {
                    toTakeLeft=boardx.getFieldPiece(i-1,j+1);
                }
                if (j>1) {
                    toTakeRight=boardx.getFieldPiece(i-1,j-1);
                }
                if ((!toTakeRight.equals("nula")) && (toTakeRight.charAt(1)!='b')) {
                    if(setOption(i-1,j-1,color0)) {cntopt++;optionField(i-1,j-1,true);}
                }
                if ((!toTakeLeft.equals("nula")) && (toTakeLeft.charAt(1)!='b')) {
                    if(this.setOption(i-1,j+1,color0)) {cntopt++;optionField(i-1,j+1,true);}
                }
            }
            if ((i==4) && (boardx.getEnPassantColor().equals("w"))) {
                if (boardx.getEnPassantJ()==(j-1)) {
                    if(setOption(3,j-1,color0)) {cntopt++;optionField(3,j-1,true);}
                }
                if (boardx.getEnPassantJ()==(j+1)) {
                    if(setOption(3,j+1,color0)) {cntopt++;optionField(3,j+1,true);}
                }
            }
        }
        return(cntopt);
    }
    public int knightOptions(int i,int j,char color0) {
        //knight moves options
        int cntopt=0;
        char color2=' ';
        if (color0=='w') {
            color2='b';
        }
        if (color0=='b') {
            color2='w';
        }

        if ((i<7) && (j>1)) {
            if (boardx.getFieldPiece(i+2,j-1).equals("nula")) {
                if(setOption(i+2,j-1,color0)) {cntopt++;optionField(i+2,j-1,true);}
            }
            else {
                if (boardx.getFieldPiece(i+2,j-1).charAt(1)==color2) {
                    if(setOption(i+2,j-1,color0)) {cntopt++;optionField(i+2,j-1,true);}
                }
            }
        }
        if ((i<8) && (j>2)) {
            if (boardx.getFieldPiece(i+1,j-2).equals("nula")) {
                if(setOption(i+1,j-2,color0)) {cntopt++;optionField(i+1,j-2,true);}
            }
            else {
                if (boardx.getFieldPiece(i+1,j-2).charAt(1)==color2) {
                    if(this.setOption(i+1,j-2,color0)) {cntopt++;optionField(i+1,j-2,true);}
                }
            }
        }
        if ((i<7) && (j<8)) {
            if (boardx.getFieldPiece(i+2,j+1).equals("nula")) {
                if(this.setOption(i+2,j+1,color0)) {cntopt++;optionField(i+2,j+1,true);}
            }
            else {
                if (boardx.getFieldPiece(i+2,j+1).charAt(1)==color2) {
                    if(this.setOption(i+2,j+1,color0)) {cntopt++;optionField(i+2,j+1,true);}
                }
            }
        }
        if ((i<8) && (j<7)) {
            if (boardx.getFieldPiece(i+1,j+2).equals("nula")) {
                if(this.setOption(i+1,j+2,color0)) {cntopt++;optionField(i+1,j+2,true);}
            }
            else {
                if (boardx.getFieldPiece(i+1,j+2).charAt(1)==color2) {
                    if(this.setOption(i+1,j+2,color0)) {cntopt++;optionField(i+1,j+2,true);}
                }
            }
        }
        if ((i>2) && (j>1)) {
            if (boardx.getFieldPiece(i-2,j-1).equals("nula")) {
                if(this.setOption(i-2,j-1,color0)) {cntopt++;optionField(i-2,j-1,true);}
            }
            else {
                if (boardx.getFieldPiece(i-2,j-1).charAt(1)==color2) {
                    if(this.setOption(i-2,j-1,color0)) {cntopt++;optionField(i-2,j-1,true);}
                }
            }
        }
        if ((i>1) && (j>2)) {
            if (boardx.getFieldPiece(i-1,j-2).equals("nula")) {
                if(this.setOption(i-1,j-2,color0)) {cntopt++;optionField(i-1,j-2,true);}
            }
            else {
                if (boardx.getFieldPiece(i-1,j-2).charAt(1)==color2) {
                    if(this.setOption(i-1,j-2,color0)) {cntopt++;optionField(i-1,j-2,true);}
                }
            }
        }
        if ((i>2) && (j<8)) {
            if (boardx.getFieldPiece(i-2,j+1).equals("nula")) {
                if(this.setOption(i-2,j+1,color0)) {cntopt++;optionField(i-2,j+1,true);}
            }
            else {
                if (boardx.getFieldPiece(i-2,j+1).charAt(1)==color2) {
                    if(this.setOption(i-2,j+1,color0)) {cntopt++;optionField(i-2,j+1,true);}
                }
            }
        }
        if ((i>1) && (j<7)) {
            if (boardx.getFieldPiece(i-1,j+2).equals("nula")) {
                if(this.setOption(i-1,j+2,color0)) {cntopt++;optionField(i-1,j+2,true);}
            }
            else {
                if (boardx.getFieldPiece(i-1,j+2).charAt(1)==color2) {
                    if(this.setOption(i-1,j+2,color0)) {cntopt++;optionField(i-1,j+2,true);}
                }
            }
        }
        return(cntopt);
    }
    public int rookOptions(int i,int j,char color0) {
        //rook moves options
        int cntopt=0,i0,j0;
        char color2=' ';
        Boolean conti0;
        if (color0=='w') {
            color2='b';
        }
        if (color0=='b') {
            color2='w';
        }
        conti0=true;
        i0=i;
        while (conti0) {
            i0=i0+1;
            if (i0>8) {
                conti0=false;
                break;
            }

            if (boardx.getFieldPiece(i0,j).equals("nula")) {
                if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j).charAt(1)==color2) {
                    if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }

        conti0=true;
        i0=i;
        while (conti0) {
            i0=i0-1;
            if (i0<1) {
                conti0=false;
                break;
            }

            if (boardx.getFieldPiece(i0,j).equals("nula")) {
                if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j).charAt(1)==color2) {
                    if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        j0=j;
        while (conti0) {
            j0=j0+1;
            if (j0>8) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i,j0).equals("nula")) {
                if(this.setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i,j0).charAt(1)==color2) {
                    if(this.setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        j0=j;
        while (conti0) {
            j0=j0-1;
            if (j0<1) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i,j0).equals("nula")) {
                if(this.setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i,j0).charAt(1)==color2) {
                    if(this.setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        return(cntopt);
    }
    public int bishopOptions(int i,int j,char color0) {
        //bishops move options
        int cntopt=0,i0,j0;
        char color2=' ';
        Boolean conti0;
        if (color0=='w') {
            color2='b';
        }
        if (color0=='b') {
            color2='w';
        }
        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {

            i0=i0+1;
            j0=j0+1;

            if (i0>8) {
                conti0=false;
                break;
            }
            if (j0>8) {
                conti0=false;
                break;
            }

            if (boardx.getFieldPiece(i0,j0).equals("nula")) {

                if(setOption(i0,j0,color0)) {cntopt++; optionField(i0,j0,true);}

            }

            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }


        }

        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {
            i0=i0-1;
            j0=j0-1;
            if (i0<1) {
                conti0=false;
                break;
            }
            if (j0<1) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(this.setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {
            j0=j0-1;
            i0=i0+1;
            if (j0<1) {
                conti0=false;
                break;
            }
            if (i0>8) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {
            j0=j0+1;
            i0=i0-1;
            if (j0>8) {
                conti0=false;
                break;
            }
            if (i0<1) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        return(cntopt);
    }
		public int queenOptions(int i,int j,char color0) {
        //Queen mnove options
        int cntopt=0,i0,j0;
        char color2=' ';
        Boolean conti0;
        if (color0=='w') {
            color2='b';
        }
        if (color0=='b') {
            color2='w';
        }
        conti0=true;
        i0=i;
        while (conti0) {
            i0=i0+1;
            if (i0>8) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j).equals("nula")) {
                if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j).charAt(1)==color2) {
                    if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }

        conti0=true;
        i0=i;
        while (conti0) {
            i0=i0-1;
            if (i0<1) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j).equals("nula")) {
                if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j).charAt(1)==color2) {
                    if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        j0=j;
        while (conti0) {
            j0=j0+1;
            if (j0>8) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i,j0).equals("nula")) {
                if(setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i,j0).charAt(1)==color2) {
                    if(setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        j0=j;
        while (conti0) {
            j0=j0-1;
            if (j0<1) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i,j0).equals("nula")) {
                if(setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i,j0).charAt(1)==color2) {
                    if(setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {
            i0=i0+1;
            j0=j0+1;
            if (i0>8) {
                conti0=false;
                break;
            }
            if (j0>8) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }

        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {
            i0=i0-1;
            j0=j0-1;
            if (i0<1) {
                conti0=false;
                break;
            }
            if (j0<1) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {
            j0=j0-1;
            i0=i0+1;
            if (j0<1) {
                conti0=false;
                break;
            }
            if (i0>8) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        conti0=true;
        i0=i;
        j0=j;
        while (conti0) {
            j0=j0+1;
            i0=i0-1;
            if (j0>8) {
                conti0=false;
                break;
            }
            if (i0<1) {
                conti0=false;
                break;
            }
            if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                    conti0=false;
                }
                else {
                    conti0=false;
                }
            }
        }
        return(cntopt);
    }
		public int kingOptions(int i,int j,char color0) {
        //King moves options
        int cntopt=0,i0,j0;
        char color2=' ';
        if (color0=='w') {
            color2='b';
        }
        if (color0=='b') {
            color2='w';
        }

        if ((color0=='w') && (!boardx.getKingWcheck()) && (boardx.getCastleWK()) && (boardx.getFieldPiece(i,j+1).equals("nula")) && (boardx.getFieldPiece(i,j+2).equals("nula")) && (!checkCheck(color0,i,j+2,boardx.getFieldPiece(selectedI,selectedJ),false)) && (!checkCheck(color0,i,j+1,boardx.getFieldPiece(selectedI,selectedJ),false))) {
            if(this.setOption(i,j+2,color0)) {cntopt++;optionField(i,j+2,true);}
        }
        if ((color0=='w') && (!boardx.getKingWcheck()) && (boardx.getCastleWQ()) && (boardx.getFieldPiece(i,j-1).equals("nula")) && (boardx.getFieldPiece(i,j-2).equals("nula")) && (boardx.getFieldPiece(i,j-3).equals("nula")) && (!checkCheck(color0,i,j-2,boardx.getFieldPiece(selectedI,selectedJ),false)) && (!checkCheck(color0,i,j-1,boardx.getFieldPiece(selectedI,selectedJ),false))) {
            if(this.setOption(i,j-2,color0)) {cntopt++;optionField(i,j-2,true);}
        }
        if ((color0=='b') && (!boardx.getKingBcheck()) && (boardx.getCastleBK()) && (boardx.getFieldPiece(i,j+1).equals("nula")) && (boardx.getFieldPiece(i,j+2).equals("nula")) && (!checkCheck(color0,i,j+2,boardx.getFieldPiece(selectedI,selectedJ),false)) && (!checkCheck(color0,i,j+1,boardx.getFieldPiece(selectedI,selectedJ),false))) {
            if(this.setOption(i,j+2,color0)) {cntopt++;optionField(i,j+2,true);}
        }
        if ((color0=='b') && (!boardx.getKingBcheck()) && (boardx.getCastleBQ()) && (boardx.getFieldPiece(i,j-1).equals("nula")) && (boardx.getFieldPiece(i,j-2).equals("nula")) && (boardx.getFieldPiece(i,j-3).equals("nula")) && (!checkCheck(color0,i,j-2,boardx.getFieldPiece(selectedI,selectedJ),false))  && (!checkCheck(color0,i,j-1,boardx.getFieldPiece(selectedI,selectedJ),false))) {
            if(this.setOption(i,j-2,color0)) {cntopt++;optionField(i,j-2,true);}
        }
        i0=i;
        i0=i0+1;
        if (i0<9) {
            if (boardx.getFieldPiece(i0,j).equals("nula")) {
                if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j).charAt(1)==color2) {
                    if(this.setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}

                }
            }
        }

        i0=i;
        i0=i0-1;
        if (i0>0) {
            if (boardx.getFieldPiece(i0,j).equals("nula")) {
                if(setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}
            }
            else {
                if (boardx.getFieldPiece(i0,j).charAt(1)==color2) {
                    if(this.setOption(i0,j,color0)) {cntopt++;optionField(i0,j,true);}

                }
            }
        }
        j0=j;
        j0=j0+1;
        if (j0<9) {
            if (boardx.getFieldPiece(i,j0).equals("nula")) {
                if(setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i,j0).charAt(1)==color2) {
                    if(this.setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}

                }
            }
        }
        j0=j;
        j0=j0-1;
        if (j0>0) {
            if (boardx.getFieldPiece(i,j0).equals("nula")) {
                if(setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}
            }
            else {
                if (boardx.getFieldPiece(i,j0).charAt(1)==color2) {
                    if(this.setOption(i,j0,color0)) {cntopt++;optionField(i,j0,true);}

                }
            }
        }
        i0=i;
        j0=j;
        i0=i0+1;
        j0=j0+1;
        if (i0<9) {
            if (j0<9) {
                if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                }
                else {
                    if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                        if(this.setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}

                    }
                }
            }
        }
        i0=i;
        j0=j;
        i0=i0-1;
        j0=j0-1;
        if (i0>0) {
            if (j0>0) {
                if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                }
                else {
                    if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                        if(this.setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}

                    }
                }
            }
        }
        i0=i;
        j0=j;
        j0=j0-1;
        i0=i0+1;
        if (j0>0) {
            if (i0<9) {
                if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                }
                else {
                    if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                        if(this.setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}

                    }
                }
            }
        }

        i0=i;
        j0=j;
        j0=j0+1;
        i0=i0-1;
        if (j0<9) {
            if (i0>0) {
                if (boardx.getFieldPiece(i0,j0).equals("nula")) {
                    if(setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}
                }
                else {
                    if (boardx.getFieldPiece(i0,j0).charAt(1)==color2) {
                        if(this.setOption(i0,j0,color0)) {cntopt++;optionField(i0,j0,true);}

                    }
                }
            }

        }
        return(cntopt);
    }
    private void queryGameToRefresh() {

        String js_text = "{\"login\":\"" + login_name + "\",\"id\":\"" + login_id + "\",\"gid\":\""+boardx.getGameObj().getGame_idgame()+"\"}";
        final String stringFromJSON = js_text;

        StringRequest strReq = new StringRequest(Request.Method.POST, gamesURLCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        try {
                                JSONArray jArray = new JSONArray(Response.toString());
                                JSONObject jObject = jArray.getJSONObject(0);
                                Game toAdd = new Game();
                                toAdd.setGame_move(jObject.getInt("move"));
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

                                boardx.getGameObj().setGame_move(toAdd.getGame_move());
                                boardx.getGameObj().setGame_turn(toAdd.getGame_turn());
                                boardx.getGameObj().setGame_status(toAdd.getGame_status());
                                boardx.getGameObj().setGame_active(toAdd.getGame_active());
                                boardx.getGameObj().setGame_winner(toAdd.getGame_winner());
                                boardx.getGameObj().setGame_winreason(toAdd.getGame_winreason());

                                queryBoard("latest");
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
                        params.put("y", "15");
                        params.put("x", stringFromJSON);
                        return params;
                    }
                 };
        RequestQueue queue = VolleySingleton.getMyInstance(myContext).getMyRequestQueue();
        queue.add(strReq);
    }

}
