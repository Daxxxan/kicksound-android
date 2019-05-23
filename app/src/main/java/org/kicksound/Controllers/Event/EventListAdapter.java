package org.kicksound.Controllers.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Controllers.Search.UserSearched;
import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleIntent;

import java.util.List;

public class EventListAdapter  extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> eventList;
    private Context context;

    public EventListAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new EventListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListAdapter.ViewHolder holder, final int position) {
        holder.nameItem.setText(eventList.get(position).getTitle());

        FileUtil.downloadFileAndDisplay("event", eventList.get(position).getPicture(), holder.imageItem, context);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivityWithExtra(context, EventView.class, v, "eventId", eventList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageItem;
        TextView nameItem;
        ConstraintLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.image_item);
            nameItem = itemView.findViewById(R.id.string_item);
            itemLayout = itemView.findViewById(R.id.item);
        }
    }
}
