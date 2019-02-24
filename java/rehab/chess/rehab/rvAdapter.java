package rehab.chess.rehab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class rvAdapter extends RecyclerView.Adapter<rvAdapter.myViewHolder> {
    private ArrayList<Game> listItems;
    private Context context;

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View sourceView= LayoutInflater.from(parent.getContext()).inflate(R.layout.crd_list_item,parent,false);
        return new myViewHolder(sourceView);
    }

    public rvAdapter(ArrayList<Game> al,Context context) {
        this.listItems= al;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Game g= listItems.get(position);

        SharedPreferences sp=context.getSharedPreferences("rehab",Context.MODE_PRIVATE);
        int login_id = sp.getInt("login_id",0);

        holder.ivMsg.setVisibility(View.INVISIBLE);

        if (login_id==g.getGame_white_user()) {
            holder.tvOpponent.setText(g.getUser_name());
            holder.ivKing.setImageResource(R.drawable.ic_kw0);

            if (g.getGame_turn().equals("w")) {
                holder.tvStatus.setText(context.getString(R.string.rvListItemStatusMyTurn));
            } else {
                if (g.getGame_turn().equals("b")) {
                    holder.tvStatus.setText(context.getString(R.string.rvListItemStatusTheirTurn));
                } else {
                    holder.tvStatus.setText(context.getString(R.string.rvListItemStatusFinished));
                 //   holder.cvCard.setCardBackgroundColor(R.color.goldenRod);
                }
            }
            if (g.getChatcol().equals("b")) {
                holder.ivMsg.setVisibility(View.VISIBLE);
            }

        } else {
            holder.tvOpponent.setText(g.getUser_name());
            holder.ivKing.setImageResource(R.drawable.ic_kb0);
            if (g.getGame_turn().equals("b")) {
                holder.tvStatus.setText(context.getString(R.string.rvListItemStatusMyTurn));
            } else {
                if (g.getGame_turn().equals("w")) {
                    holder.tvStatus.setText(context.getString(R.string.rvListItemStatusTheirTurn));
                } else {
                    holder.tvStatus.setText(context.getString(R.string.rvListItemStatusFinished));
                }
            }
            if (g.getChatcol().equals("w")) {
                holder.ivMsg.setVisibility(View.VISIBLE);
            }
        }
        if (g.getGame_active()==0) {
            if (g.getGame_winner() == login_id) {
                holder.icon.setImageResource(R.drawable.ic_star);
                holder.tvStatus.setText("Game was won by "+g.getGame_winreason());
            } else {
                if (g.getGame_winreason().equals("draw")) {
                    holder.icon.setImageResource(R.drawable.ic_handshak);
                    holder.tvStatus.setText("Game was drawn");
                } else {
                    holder.icon.setImageResource(R.drawable.ic_dead);
                    holder.tvStatus.setText("Game was lost by "+g.getGame_winreason());
                }

            }
        }

        holder.tvMove.setText(""+g.getGame_move());

        holder.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForBoard = new Intent(context,actBoard.class);
                intentForBoard.putExtra("GameClass",g);
                context.startActivity(intentForBoard);

            }
        });



    }

    @Override
    public int getItemCount() {
        return (this.listItems.size());
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView ivKing,ivMsg;
        TextView tvOpponent;
        TextView tvStatus;
        TextView tvMove;
        TextView tvMoveLabel;
        CardView cvCard;


        ImageView icon;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMsg= (ImageView) itemView.findViewById(R.id.ivNewMsg);
            cvCard= (CardView) itemView.findViewById(R.id.cvCard);
            ivKing= (ImageView) itemView.findViewById(R.id.ivColor);
            tvOpponent = (TextView) itemView.findViewById(R.id.tvChallenger);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvMove= (TextView) itemView.findViewById(R.id.tvMove);
            tvMoveLabel=(TextView) itemView.findViewById(R.id.tvMoveLabel);

            icon= (ImageView) itemView.findViewById(R.id.ivIcon);




            Typeface sansLight=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Sansation_Light.ttf");
            Typeface sansLightBold=Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Sansation_Bold.ttf");

            tvOpponent.setTypeface(sansLightBold);
            tvStatus.setTypeface(sansLight);
            tvMoveLabel.setTypeface(sansLight);


            tvMove.setTypeface(sansLight);





        }
    }
}
