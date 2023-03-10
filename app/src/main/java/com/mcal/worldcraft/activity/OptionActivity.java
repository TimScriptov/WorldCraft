package com.mcal.worldcraft.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mcal.worldcraft.Persistence;
import com.mcal.worldcraft.R;
import com.mcal.worldcraft.SoundManager;
import com.mcal.worldcraft.multiplayer.dialogs.AddAdapter;
import com.mcal.worldcraft.ui.EntryAdapter;
import com.mcal.worldcraft.ui.EntryItem;
import com.mcal.worldcraft.ui.OptionItem;
import com.mcal.worldcraft.ui.SectionItem;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class OptionActivity extends ListActivity {
    public static final String BACK = "BACK";
    public static final String CANCEL = "Cancel";
    public static final String DISABLE = "Disable";
    public static final String ENABLE = "Enable";
    public static final String ENTRY_CHUNK_LOAD_RADIUS = "Chunk load radius";
    public static final String ENTRY_ENABLE_SOUND = "Enable sound";
    public static final String ENTRY_FOG_DISTANCE = "Fog distance";
    public static final String ENTRY_INVERT_Y = "Invert Y-axis";
    public static final String ENTRY_SKIN = "Skin";
    public static final String ENTRY_USERNAME = "Username";
    public static final String OK = "Ok";
    public static final String SELECTION_CONTROL = "Control";
    public static final String SELECTION_GRAPHICS = "Graphics";
    public static final String SELECTION_MULTIPLAYER = "Multiplayer";
    public static final String SELECTION_SOUND = "Sound";
    public static final short[] SKINS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    public static final String WORLD_CRAFT_OPTION = "WorldCraft option";
    private static final int CHANGE_SOUND_ID = 6;
    private static final int FOG_DISTANCE_ID = 1;
    private static final int INVERT_Y_AXIS_ID = 3;
    private static final int SKIN_ID = 5;
    private static final int USERNAME_ID = 4;
    private final ArrayList<OptionItem> items = new ArrayList<>();
    private float fogDistance;
    private boolean isInvertY;
    private boolean isSoundEnabled;
    private int loadRadius;
    private short skinType;
    private String userName;

    public static int getSkinResID(int skinType) {
        switch (skinType) {
            case 0:
                return R.drawable.man1;
            case 1:
                return R.drawable.man2;
            case 2:
                return R.drawable.man3;
            case 3:
                return R.drawable.man4;
            case 4:
                return R.drawable.man5;
            case 5:
                return R.drawable.man6;
            case 6:
                return R.drawable.man7;
            case 7:
                return R.drawable.man8;
            case 8:
                return R.drawable.man9;
            case 9:
                return R.drawable.man10;
            case 10:
                return R.drawable.woman1;
            case 11:
                return R.drawable.woman2;
            case 12:
                return R.drawable.woman3;
            case 13:
                return R.drawable.woman4;
            case 14:
                return R.drawable.woman5;
            case 15:
                return R.drawable.woman6;
            case 16:
                return R.drawable.woman7;
            case 17:
                return R.drawable.woman8;
            case 18:
                return R.drawable.woman9;
            case 19:
                return R.drawable.woman10;
            default:
                return R.drawable.man1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(WORLD_CRAFT_OPTION);
        Button b = new Button(this);
        b.setText(BACK);
        b.setOnClickListener(v -> {
            finish();
            items.clear();
        });
        getListView().addFooterView(b);
        updateItems();
    }

    public void updateItems() {
        items.clear();
        isInvertY = Persistence.getInstance().isInvertY();
        fogDistance = Persistence.getInstance().getFogDistance();
        loadRadius = Persistence.getInstance().getLoadRadius();
        userName = Persistence.getInstance().getPlayerName();
        skinType = Persistence.getInstance().getPlayerSkin();
        isSoundEnabled = SoundManager.isSoundEnabled();
        items.add(new SectionItem(SELECTION_MULTIPLAYER));
        items.add(new EntryItem(ENTRY_USERNAME, userName, 4));
        items.add(new EntryItem(ENTRY_SKIN, null, 5, true, getSkinResID(skinType)));
        items.add(new SectionItem(SELECTION_CONTROL));
        items.add(new EntryItem(ENTRY_INVERT_Y, isInvertY ? ENABLE : DISABLE, 3));
        items.add(new SectionItem(SELECTION_GRAPHICS));
        items.add(new EntryItem(ENTRY_FOG_DISTANCE, String.valueOf(this.fogDistance), 1));
        items.add(new SectionItem(SELECTION_SOUND));
        items.add(new EntryItem(ENTRY_ENABLE_SOUND, isSoundEnabled ? ENABLE : DISABLE, 6));
        EntryAdapter adapter = new EntryAdapter(this, items);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (!items.get(position).isSection()) {
            EntryItem item = (EntryItem) items.get(position);
            switch (item.ID) {
                case FOG_DISTANCE_ID:
                    showFogDistanceDialog();
                    item.subtitle = String.valueOf(fogDistance);
                    break;
                case INVERT_Y_AXIS_ID:
                    changeInverY();
                    break;
                case USERNAME_ID:
                    showChangeUsernameDialog();
                    break;
                case SKIN_ID:
                    showChangeSkinDialog();
                    break;
                case CHANGE_SOUND_ID:
                    changeSound();
                    break;
            }
        }
        super.onListItemClick(l, v, position, id);
    }

    private void changeInverY() {
        isInvertY = !isInvertY;
        Persistence.getInstance().setInvertY(isInvertY);
        updateItems();
    }

    private void showFogDistanceDialog() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        final EditText name = new EditText(this);
        name.setText(String.valueOf(this.fogDistance));
        builder.setTitle(ENTRY_FOG_DISTANCE).setView(name).setPositiveButton(OK, (dialog, id) -> {
            try {
                float fogDistance = Float.parseFloat(name.getText().toString());
                if (fogDistance < 0.0f) {
                    fogDistance = 0.0f;
                }
                Persistence.getInstance().setFogDistance(fogDistance);
                updateItems();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }).setNegativeButton(CANCEL, (dialog, id) -> dialog.dismiss());
        builder.show();
    }

    private void changeSound() {
        isSoundEnabled = !isSoundEnabled;
        SoundManager.setSoundEnabled(isSoundEnabled);
        updateItems();
    }

    private void showChangeChunkLoadRadiusDialog() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        final EditText name = new EditText(this);
        name.setText(String.valueOf(loadRadius));
        builder.setTitle(ENTRY_CHUNK_LOAD_RADIUS).setView(name).setPositiveButton(OK, (dialog, id) -> {
            try {
                int chunkCount = Integer.parseInt(name.getText().toString());
                if (chunkCount < 1) {
                    chunkCount = 1;
                }
                Persistence.getInstance().setLoadRadius(chunkCount);
                updateItems();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }).setNegativeButton(CANCEL, (dialog, id) -> dialog.dismiss());
        builder.show();
    }

    private void showChangeUsernameDialog() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        final EditText name = new EditText(this);
        name.setText(userName);
        builder.setTitle(ENTRY_USERNAME).setView(name).setPositiveButton(OK, (dialog, id) -> {
            String userName = name.getText().toString();
            Persistence.getInstance().setPlayerName(userName);
            updateItems();
        }).setNegativeButton(CANCEL, (dialog, id) -> dialog.dismiss());
        builder.show();
    }

    private void showChangeSkinDialog() {
        AddAdapter mAdapter = new AddAdapter(this);
        Spinner characterChooser = new Spinner(this);
        characterChooser.setAdapter(mAdapter);
        characterChooser.setSelection(this.skinType);
        characterChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                skinType = SKINS[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(ENTRY_SKIN);
        builder.setView(characterChooser);
        builder.setPositiveButton(OK, (dialog, id) -> {
            Persistence.getInstance().setPlayerSkin(skinType);
            updateItems();
        });
        builder.setNegativeButton(CANCEL, (dialog, id) -> dialog.dismiss());
        builder.show();
    }
}
