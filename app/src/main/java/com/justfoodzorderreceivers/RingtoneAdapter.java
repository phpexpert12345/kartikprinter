package com.justfoodzorderreceivers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.RecyclerView;

import com.justfoodzorderreceivers.Model.RingtoneItem;

import java.util.List;

public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder> {
    List<RingtoneItem>ringtoneItems;
     MediaPlayer mediaPlayer;
    Context context;
    ProgressDialog progressDialog;
    MyPref myPref;

    public int getSelected_position() {
        return selected_position;
    }

    int selected_position =-1;
    public RingtoneAdapter(List<RingtoneItem>ringtoneItems){
        this.ringtoneItems=ringtoneItems;
    }
    @NonNull
    @Override
    public RingtoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        myPref = new MyPref(context);
        return new RingtoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ringtone_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull final RingtoneViewHolder holder, int position) {
final RingtoneItem ringtoneItem=ringtoneItems.get(position);
String name=ringtoneItem.ring_tone_url.substring(ringtoneItem.ring_tone_url.lastIndexOf("/")+1);
if(myPref.getRingtone_url().equalsIgnoreCase(ringtoneItem.ring_tone_url)){
    holder.radio_ringtone.setChecked(true);
}
else{
    holder.radio_ringtone.setChecked(false);
}
if(selected_position>=0) {
    if (selected_position == position) {
        holder.img_play.setImageResource(R.drawable.ic_pause);
        holder.radio_ringtone.setChecked(true);
    } else {
        holder.img_play.setImageResource(R.drawable.ic_play);
        holder.radio_ringtone.setChecked(false);
    }
}
holder.linear_ringtone.setTag(position);
holder.linear_ringtone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      int pos= (int) v.getTag();
      selected_position=pos;
      if(mediaPlayer!=null && mediaPlayer.isPlaying()){
          mediaPlayer.stop();
          mediaPlayer.reset();
          mediaPlayer.release();
          mediaPlayer=null;
          holder.img_play.setImageResource(R.drawable.ic_play);
          notifyDataSetChanged();
      }
      else {
          progressDialog = progressDialog.show(context, "", "Please wait...", false, false);
          new PlayVideoAsynck(holder).execute(ringtoneItems.get(pos).ring_tone_url);
//        playVideo(context,ringtoneItems.get(pos).ring_tone_url);
          notifyDataSetChanged();
      }
    }
});
holder.txt_ringtone_name.setText(name);
holder.appCompatSeekBar.getProgressDrawable().setColorFilter(context. getColor(R.color.red), PorterDuff.Mode.SRC_IN);
holder.appCompatSeekBar.getThumb().setColorFilter(context.getColor(R.color.red),PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return ringtoneItems.size();
    }

    class RingtoneViewHolder extends RecyclerView.ViewHolder {
        TextView txt_ringtone_name;
        AppCompatRadioButton radio_ringtone;
        LinearLayout linear_ringtone;
        AppCompatSeekBar appCompatSeekBar;
        ImageView img_play;
        public RingtoneViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ringtone_name=itemView.findViewById(R.id.txt_ringtone_name);
            radio_ringtone=itemView.findViewById(R.id.radio_ringtone);
            linear_ringtone=itemView.findViewById(R.id.linear_ringtone);
            appCompatSeekBar=itemView.findViewById(R.id.seekbar_ringtone);
            img_play=itemView.findViewById(R.id.img_play);
        }
    }

    public class PlayVideoAsynck extends AsyncTask<String,Integer, String>{
        RingtoneViewHolder ringtoneViewHolder;
public PlayVideoAsynck(RingtoneViewHolder ringtoneViewHolder){
    this.ringtoneViewHolder=ringtoneViewHolder;
}
        @Override
        protected String doInBackground(String... strings) {
//
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(strings[0]));
            }

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    killMediaPlayer(ringtoneViewHolder);
                }
            });

            mediaPlayer.start();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
if(mediaPlayer!=null) {
    int mCurrentPosition = mediaPlayer.getCurrentPosition();
    int duration=mediaPlayer.getDuration();
    long percent=mCurrentPosition *100/duration;
    Log.i("currentposition", duration + "");
    Log.i("currentposition", percent + "");
    ringtoneViewHolder.appCompatSeekBar.setProgress((int) percent);
    ringtoneViewHolder.img_play.setImageResource(R.drawable.ic_pause);
    if(progressDialog!=null && progressDialog.isShowing()){
        progressDialog.dismiss();
    }
    handler.postDelayed(this, 300);
}

                }
            },300);
        }
    }
    public void playVideo(Context context, String url){
//        if (mediaPlayer == null) {
//            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
//        }
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                killMediaPlayer(ring);
//            }
//        });
//        mediaPlayer.start();
    }
    public   void killMediaPlayer( RingtoneViewHolder ringtoneViewHolder) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
ringtoneViewHolder.img_play.setImageResource(R.drawable.ic_play);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
