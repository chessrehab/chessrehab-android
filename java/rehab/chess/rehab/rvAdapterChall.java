package rehab.chess.rehab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class rvAdapterChall extends RecyclerView.Adapter<rvAdapterChall.myViewHolder> {
    private ArrayList<Challenge> listItems;
    private Context context;
    Fragment f_parent;

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View sourceView= LayoutInflater.from(parent.getContext()).inflate(R.layout.crd_list_item_chal,parent,false);;
        return new myViewHolder(sourceView);
    }

    public rvAdapterChall(ArrayList<Challenge> al, Context context, Fragment f) {
        this.listItems= al;
        this.context=context;
        f_parent=f;
    }

    public void showDialogAccept(Challenge g) {
        mDialogChallenge  mDialog=new mDialogChallenge();
        mDialog.setTitle("Do you wish to accept this challenge?");
        mDialog.setPurpose("acceptChallenge");
        mDialog.setChallenge(g);
        mDialog.show(f_parent.getChildFragmentManager(),"acceptChallenge");
    }
    public void showDialogDelete(Challenge g) {
        mDialogChallenge mDialog=new mDialogChallenge();
        mDialog.setTitle("Do you wish to cancel this challenge?");
        mDialog.setPurpose("cancelChallenge");
        mDialog.setChallenge(g);
        mDialog.show(f_parent.getChildFragmentManager(),"cancelChallenge");
    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Challenge g= listItems.get(position);

        SharedPreferences sp=context.getSharedPreferences("rehab",Context.MODE_PRIVATE);
        int login_id = sp.getInt("login_id",0);

        if ((g.getIdChallenger()==login_id) && (g.getIdChallenged()==0)) {
            holder.tvChallenger.setText("" + g.getChallenger());
            holder.cvCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDelete(g);
                }
            });
        }
        if ((g.getIdChallenger()==login_id) && (g.getIdChallenged()>0)) {
            holder.tvChallenger.setText("" + g.getChallenged());
            holder.cvCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDelete(g);
                }
            });
        }
        if (g.getIdChallenger()!=login_id)  {
            holder.tvChallenger.setText("" + g.getChallenger());
            holder.cvCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogAccept(g);
                }
            });
        }
        if (g.getColor().equals("b")) {
            holder.pawn.setImageResource(R.drawable.ic_pb01);
            holder.tvChallenger.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.pawn.setImageResource(R.drawable.ic_pw01);
            holder.tvChallenger.setTextColor(Color.parseColor("#000000"));
        }





    }

    @Override
    public int getItemCount() {
        return (this.listItems.size());
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        CardView cvCard;
        TextView tvChallenger;
        ImageView pawn;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cvCard= (CardView) itemView.findViewById(R.id.cvCardChal);
            tvChallenger = (TextView) itemView.findViewById(R.id.tvChallenger);
            pawn= (ImageView) itemView.findViewById(R.id.ivPawn);




            Typeface sansLight=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Sansation_Light.ttf");
            Typeface sansBold=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Sansation_Bold.ttf");

            tvChallenger.setTypeface(sansBold);



        }
    }
}
