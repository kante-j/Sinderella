package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jipjung.hucomin.sinderella.Classes.Comment;
import com.jipjung.hucomin.sinderella.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> comments;
    private FirebaseAuth user;

    public CommentAdapter(Context context, List<Comment> objects) {
        user = FirebaseAuth.getInstance();
        this.context = context;
        this.comments = objects;
    }


    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_comments, parent, false);
        }

        TextView uId = (TextView)convertView.findViewById(R.id.user_id);
        TextView cBody = (TextView)convertView.findViewById(R.id.comment_body);
        TextView cTime = (TextView)convertView.findViewById(R.id.comment_time);

        Comment comment = comments.get(position);
        Log.d("fafafa", comment.getUser_id());

        uId.setText(comment.getNickname());
        cBody.setText(comment.getContext());
        try{
            Log.d("fafafa", comment.getCreated_at());
            Date date = new SimpleDateFormat("yyyyMMddkkmmss").parse(comment.getCreated_at());
            String format = new SimpleDateFormat("yyyy/MM/dd kk:mm").format(date);
            cTime.setText(format);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        if(user.getUid().equals(comment.getUser_id())){
            ImageView deleteButton = (ImageView)convertView.findViewById(R.id.btn_comment_delete);
            deleteButton.setVisibility(View.VISIBLE);
        }
        return convertView;
    }



}
