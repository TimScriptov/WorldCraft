package com.solverlabs.worldcraft.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.solverlabs.worldcraft.R;
import com.solverlabs.worldcraft.factories.DescriptionFactory;
import com.solverlabs.worldcraft.multiplayer.Multiplayer;
import com.solverlabs.worldcraft.srv.util.ObjectCodec;

import java.util.ArrayList;
import java.util.Collection;


public class JoinRoomListAdapter extends ArrayAdapter<ObjectCodec.RoomPack> {
    public static final int ROOMS_PER_REQUEST = 50;
    private final LayoutInflater vi;
    private boolean isRoomlistFull;
    private ArrayList<ObjectCodec.RoomPack> items;
    private boolean noItems;
    private boolean roomlistLoading;
    private byte roomlistType;
    private String searchValue;
    private boolean showLoading;

    public JoinRoomListAdapter(Context context, ArrayList<ObjectCodec.RoomPack> items, byte roomlistType) {
        super(context, 0, items);
        this.roomlistLoading = false;
        this.showLoading = true;
        this.isRoomlistFull = false;
        this.noItems = false;
        this.searchValue = null;
        init(items, roomlistType);
        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }

    public JoinRoomListAdapter(Context context, ArrayList<ObjectCodec.RoomPack> items, byte roomlistType, String searchValue) {
        this(context, items, roomlistType);
        this.searchValue = searchValue;
    }

    private void init(ArrayList<ObjectCodec.RoomPack> items, byte roomlistType) {
        boolean z = true;
        if (this.items == null) {
            this.items = items;
        } else {
            this.items.clear();
            this.items.addAll(items);
        }
        this.roomlistType = roomlistType;
        this.isRoomlistFull = false;
        this.showLoading = true;
        this.searchValue = null;
        this.roomlistLoading = false;
        if (roomlistType != 0) {
            z = false;
        }
        this.noItems = z;
        this.items.add(new ObjectCodec.RoomPack());
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !this.items.isEmpty() && (position != this.items.size() + (-1) || !this.showLoading);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.noItems) {
            return this.vi.inflate(R.layout.join_room_list_no_items, null);
        } else if (this.items.isEmpty() || (position == this.items.size() - 1 && this.showLoading)) {
            return this.vi.inflate(R.layout.join_room_list_loading_item, null);
        } else {
            View v = this.vi.inflate(R.layout.join_room_list_item, null);
            ObjectCodec.RoomPack i = this.items.get(position);
            TextView title = v.findViewById(R.id.join_room_name);
            TextView userCount = v.findViewById(R.id.join_room_user_count);
            ImageView lockImage = v.findViewById(R.id.join_room_lock_image);
            TextView raiting = v.findViewById(R.id.join_room_raiting);
            title.setText(i.name);
            raiting.setText("Rating: " + i.rating);
            userCount.setText(i.userCount + "/" + i.maxUserCount);
            if (i.hasPassword) {
                lockImage.setImageResource(R.drawable.lock);
                return v;
            }
            return v;
        }
    }

    public boolean isRoomlistLoading() {
        return this.roomlistLoading;
    }

    public void loadRoomlist() {
        if (!this.isRoomlistFull && !this.roomlistLoading) {
            this.roomlistLoading = true;
            if (this.searchValue != null) {
                Multiplayer.instance.gameClient.roomSearch(this.searchValue, getCount());
            } else {
                Multiplayer.instance.gameClient.roomList(this.roomlistType, getCount());
            }
        }
    }

    public void setSearchValue(String searchValue) {
        init(new ArrayList<>(), (byte) 0);
        this.searchValue = searchValue;
        this.noItems = false;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }

    public void roomlistLoaded(Collection<ObjectCodec.RoomPack> items, short initRoomlistSize) {
        if (this.items.size() > 0 && (this.items.get(0).name == null || this.items.get(0).name.equals(DescriptionFactory.emptyText))) {
            remove(this.items.get(0));
        }
        this.items.addAll(items);
        if (initRoomlistSize < 50) {
            fullRoomlistLoaded();
            return;
        }
        notifyDataSetChanged();
        this.roomlistLoading = false;
    }

    public void fullRoomlistLoaded() {
        ObjectCodec.RoomPack roomPack;
        this.roomlistLoading = false;
        this.isRoomlistFull = true;
        this.showLoading = false;
        try {
            if (this.items.size() > 0 && ((roomPack = this.items.get(this.items.size() - 1)) == null || roomPack.id <= 0)) {
                this.items.remove(this.items.size() - 1);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (this.items.isEmpty()) {
            this.noItems = true;
            this.items.add(new ObjectCodec.RoomPack());
        }
        notifyDataSetChanged();
    }


    public static class JoinRoomOnScrollListener implements AbsListView.OnScrollListener {
        private final JoinRoomListAdapter adapter;

        public JoinRoomOnScrollListener(JoinRoomListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean loadMore = firstVisibleItem != 0 && visibleItemCount != 0 && totalItemCount != 0 && firstVisibleItem + visibleItemCount >= totalItemCount;
            if (loadMore) {
                this.adapter.loadRoomlist();
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
}