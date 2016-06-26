package ca.josephroque.stayawhile.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class Dialog {

    private static List<List<String>> dialogs;
    private static int nextDialog = 0;

    public static void loadDialog() {
        dialogs = new ArrayList<List<String>>();

        FileHandle file = Gdx.files.internal("dialogs.json");
        JsonValue dialogJson= new JsonReader().parse(file);

        for (JsonValue dialogSet : dialogJson.get("dialogs").iterator()) {
            List<String> dialog = new ArrayList<String>();
            for (JsonValue text : dialogSet.get("text").iterator()) {
                dialog.add(text.asString());
            }
            dialogs.add(dialog);
        }
    }

    public static List<String> getNextDialog() {
        if (dialogs == null)
            return null;

        return dialogs.get(nextDialog++);
    }
}
