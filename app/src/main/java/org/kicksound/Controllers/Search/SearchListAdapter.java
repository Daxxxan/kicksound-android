package org.kicksound.Controllers.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleIntent;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>{

    private List<Account> userList;
    private Context context;

    public SearchListAdapter(List<Account> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.userNameItem.setText(userList.get(position).getUsername());

        FileUtil.downloadFileAndDisplay("user", userList.get(position).getPicture(), holder.userImageItem, context);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivityWithExtra(context, UserSearched.class, v, "userId", userList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageItem;
        TextView userNameItem;
        ConstraintLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageItem = itemView.findViewById(R.id.image_item);
            userNameItem = itemView.findViewById(R.id.string_item);
            itemLayout = itemView.findViewById(R.id.item);
        }
    }
}
