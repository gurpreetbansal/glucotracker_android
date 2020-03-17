package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.NoticesActivity;
import com.wellnessy.glucotracker.PowerManagementActivity;
import com.wellnessy.glucotracker.ProductBasisActivity;
import com.wellnessy.glucotracker.R;
import com.wellnessy.glucotracker.UserInstructionActivity;

import java.util.ArrayList;

import Response.GuideResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.DataHolder> {
    ArrayList<GuideResponse> guideResponseArrayList;
    Activity activity;
    Context context;

    public GuideAdapter(ArrayList<GuideResponse> data, Activity activity,Context context){
        this.guideResponseArrayList=data;
        this.activity=activity;
        this.context= context;

    }
    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_row, parent, false);
        DataHolder dataHolder = new DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {

   GuideResponse guideResponse=guideResponseArrayList.get(position);
    holder.guideTextView.setText(guideResponse.getGuideName());
        holder.guideRelativeLayout.setTag(Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return guideResponseArrayList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.guideLayout)
        RelativeLayout guideRelativeLayout;

        @BindView(R.id.guideName)
        TextView guideTextView;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.guideLayout)
        public void setGuideRelativeLayout(View v) {
            int pos = Integer.parseInt(v.getTag().toString());
            final Intent intent;
            switch (pos) {
                case 0:
                    intent = new Intent(context, UserInstructionActivity.class);
                    break;

                 case 1:
                     intent= new Intent(context, PowerManagementActivity.class);
                     break;

                case 2:
                    intent= new Intent(context, NoticesActivity.class);
                    break;

                case 3:
                    intent= new Intent(context, ProductBasisActivity.class);
                    break;

                default:
                    intent= new Intent(context, UserInstructionActivity.class);
                    break;
            }
           context.startActivity(intent);
        }
    }
    }
