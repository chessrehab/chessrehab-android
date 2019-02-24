package rehab.chess.rehab;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.TextView;

public class mDialogPromo extends DialogFragment {
    private TextView tvTitlePromo;
    private ImageView ivQueenPromo,ivRookPromo,ivBishopPromo,ivKnightPromo;
    private Echo listener;
    private Context parentContext;
    char promoColor=' ';

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

        Typeface sansLight=Typeface.createFromAsset(view.getContext().getAssets(),"fonts/Sansation_Light.ttf");
        Typeface sansLightBold=Typeface.createFromAsset(view.getContext().getAssets(),"fonts/Sansation_Bold.ttf");

        tvTitlePromo.setTypeface(sansLightBold);
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
        View view=inflater.inflate(R.layout.dialog_promo,null);

        tvTitlePromo =(TextView) view.findViewById(R.id.tvTitlePromo);
        ivQueenPromo= (ImageView) view.findViewById(R.id.ivQueenPromo);
        ivRookPromo= (ImageView) view.findViewById(R.id.ivRookPromo);
        ivBishopPromo= (ImageView) view.findViewById(R.id.ivBishopPromo);
        ivKnightPromo= (ImageView) view.findViewById(R.id.ivKnightPromo);
        if (promoColor=='w') {
            ivQueenPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_qw0,parentContext.getTheme()));
            ivRookPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_rw0,parentContext.getTheme()));
            ivBishopPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_bw0,parentContext.getTheme()));
            ivKnightPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nw0,parentContext.getTheme()));;
        }
        if (promoColor=='b') {
            ivQueenPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_qb0,parentContext.getTheme()));
            ivRookPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_rb0,parentContext.getTheme()));
            ivBishopPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_bb0,parentContext.getTheme()));
            ivKnightPromo.setImageDrawable(getResources().getDrawable(R.drawable.ic_nb0,parentContext.getTheme()));
        }


        builder.setView(view);
        setFonts(view);
        ivQueenPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogPromoResponseGet("Queening");
                dismiss();

            }
        });
        ivRookPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogPromoResponseGet("Rooking");
                dismiss();
            }
        });
        ivBishopPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogPromoResponseGet("Bishoping");
                dismiss();
            }
        });
        ivKnightPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dialogPromoResponseGet("Knighting");
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
            parentContext=context;
            promoColor= listener.getColorForPromoDialog();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Echo {
        char getColorForPromoDialog();
        void dialogPromoResponseGet(String response);
    }
}
