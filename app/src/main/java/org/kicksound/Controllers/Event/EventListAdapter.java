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

import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleIntent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_event, parent, false);
        return new EventListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListAdapter.ViewHolder holder, final int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy");
        holder.nameItem.setText(eventList.get(position).getTitle());
        holder.eventDate.setText(dateFormat.format(eventList.get(position).getDate()));

        FileUtil.displayPicture("event", eventList.get(position).getPicture(), holder.imageItem, context);

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
        TextView eventDate;
        ConstraintLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.image_item_event);
            nameItem = itemView.findViewById(R.id.string_item_event);
            itemLayout = itemView.findViewById(R.id.eventItem);
            eventDate = itemView.findViewById(R.id.string_date_event);
        }
    }
}
