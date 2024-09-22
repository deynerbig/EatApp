package com.example.eatsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Comment {
    private String recipeId;
    private String userId;
    private String text;
    private long timestamp;

    public Comment() {
    } // Constructor vacío requerido por Firestore

    public Comment(String recipeId, String userId, String text, long timestamp) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public String getRecipeId() {
        return recipeId;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Clase com.example.eatsapp.Comment.CommentAdapter
    public static class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
        private List<Comment> commentList;

        public CommentAdapter(List<Comment> commentList) {
            this.commentList = commentList;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = commentList.get(position);
            holder.bind(comment);
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {
            private TextView commentTextView;

            public CommentViewHolder(View itemView) {
                super(itemView);
                commentTextView = itemView.findViewById(R.id.commentTextView);
            }

            public void bind(Comment comment) {
                commentTextView.setText(comment.getText());
            }
        }
    }
}
