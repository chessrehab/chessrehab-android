package rehab.chess.rehab;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class mDialogChallenge extends DialogFragment {
    private String title;
    private String purpose;
    private TextView tvTitle;
    private Button bOK,bCancel;
    private Echo listener;
    private Challenge ch;



    public void setTitle(String title) {
        this.title=title;
    }
    public void setPurpose (String purpose) {
        this.purpose=purpose;
    }
    public void setChallenge (Challenge ch) {
        this.ch=ch;
    }


    @Override
    public void onResume() {
        final Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.BOTTOM);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
       //lp.x = 400;        // set your X position here
       // lp.y = 10000;        // set your Y position here
        dialogWindow.setAttributes(lp);

        lp.width =WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height =WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        super.onResume();
    }

    public void setFonts(View view) {
        tvTitle= (TextView) view.findViewById(R.id.tvTitlePromo);
        bOK= (Button) view.findViewById(R.id.bOK);
        bCancel= (Button) view.findViewById(R.id.bCancel);


        Typeface sansLight=Typeface.createFromAsset(view.getContext().getAssets(),"fonts/Sansation_Light.ttf");
        Typeface sansLightBold=Typeface.createFromAsset(view.getContext().getAssets(),"fonts/Sansation_Bold.ttf");

        tvTitle.setTypeface(sansLightBold);
        bOK.setTypeface(sansLightBold);
        bCancel.setTypeface(sansLight);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     //   getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_move,null);
        bOK=(Button) view.findViewById(R.id.bOK);
        bCancel=(Button) view.findViewById(R.id.bCancel);
        builder.setView(view);
        setFonts(view);
        tvTitle.setText(title);

        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogResponseGet("OK",purpose,ch);
                dismiss();

            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogResponseGet("Cancel",purpose,ch);
                dismiss();
            }
        });


        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(Echo) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Echo {
        void dialogResponseGet(String response, String purpose, Challenge ch);
    }
}
