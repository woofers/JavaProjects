package com.jaxson.woofers3d;

import com.jaxson.lib.gdx.GameInstance;
import com.jaxson.woofers3d.states.PlayState;
import com.jaxson.lib.io.DataFile;
import com.jaxson.lib.util.MyArrayList;

public class Woofers3D extends GameInstance
{
    private static final String TITLE = "Woofers 3D";

    /**
     * TODO
     * -Fix State API
     * -Eliminate Magic Numbers
     * -Sync Sprite and Entity Units****
     * -Fix Player Anchor
     * -2D Camera
     * -Adjust PPM Scaling
     * -Fix File API
     * -Fix Zoom on Pause
     * RULES
     * -Ideally keep constructors under 4 per object
     * -Keep objects to a single unit for each type of quantity
     * (Otherwise wrap or convert outside of object)
     * -Lines no longer than 80 wide
     */

    public Woofers3D()
    {
        super();
        config().setTitle(TITLE);
        saveableConfig().save();

        DataFile listFile = new DataFile("P:/Users/Jaxson/Reinstall Stuff/Misc/Music.txt");
        MyArrayList<String> list = new MyArrayList<>(listFile.readString().split("\n"));
        list.remove(0);
        for (String song: list)
        {
            DataFile file = new DataFile("P:/Music/Music Library/" + song);
            file.copy(new DataFile("D:/Music/" + song));
            System.out.println(file);
        }
        System.out.println("Done");



    }

    @Override
    public void create()
    {
        super.create();
        pushState(new PlayState(game()));
    }
}
