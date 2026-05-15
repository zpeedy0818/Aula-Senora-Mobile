package co.edu.aulasenora.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.edu.aulasenora.R;
import co.edu.aulasenora.models.User;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.TutorViewHolder> {

    private List<User> tutorList = new ArrayList<>();

    public void setTutors(List<User> tutors) {
        this.tutorList = tutors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutor, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        User user = tutorList.get(position);
        
        holder.tvName.setText(user.getName().getFullName());
        holder.tvEmail.setText(user.getEmail());

        // Usar Glide para cargar la imagen desde la URL de forma asíncrona
        Glide.with(holder.itemView.getContext())
                .load(user.getPicture().getLarge())
                .circleCrop()
                .placeholder(R.drawable.bg_circle_cyan) // placeholder mientras carga
                .into(holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    static class TutorViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        TextView tvName;
        TextView tvEmail;

        public TutorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.ivTutorPicture);
            tvName = itemView.findViewById(R.id.tvTutorName);
            tvEmail = itemView.findViewById(R.id.tvTutorEmail);
        }
    }
}
