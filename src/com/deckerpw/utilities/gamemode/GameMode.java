package com.deckerpw.utilities.gamemode;
import com.deckerpw.utilities.ConfigurableUtility;
import com.deckerpw.utilities.gamemode.ui.GameModePanel;
import org.json.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameMode implements ConfigurableUtility {

    File configFile;
    GameModePanel panel;



    public static class Program{

        private final String name;
        private Boolean value = true;

        public Program(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Boolean getValue() {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;
        }
    }

    private Program[] programs = {
            new Program("msedge.exe"),
            new Program("steam.exe"),
            new Program("jetbrains-toolbox.exe"),
            new Program("Discord.exe"),
            new Program("Rainmeter.exe"),
            new Program("PhoneExperienceHost.exe"),
            new Program("msteams.exe"),
            new Program("OneDrive.exe"),
            new Program("RoundedTB.exe")};

    public GameMode(){}

    public String[] getNames(){
        String[] rv = new String[programs.length];
        for (int i = 0; i < programs.length; i++) {
            rv[i] = programs[i].getName();
        }
        return rv;
    }

    public Boolean[] getValues(){
        Boolean[] rv = new Boolean[programs.length];
        for (int i = 0; i < programs.length; i++) {
            rv[i] = programs[i].getValue();
        }
        return rv;
    }

    public void setProgramValue(int index,Boolean value){
        programs[index].setValue(value);
    }

    public void excCommand(String command){
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(new String[]{"cmd.exe","/c",command});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void KillPrograms(){
        for (Program program : programs) {
            if (program.getValue()) excCommand("taskkill /F /IM "+program.getName()+" /T");
        }
    }

    public void delProgram(int index){
        ArrayList<Program> list = new ArrayList<Program>(List.of(programs));
        list.remove(index);
        programs = (Program[]) list.toArray(new Program[]{});
        save();
    }

    public void addProgram(Program program){
        ArrayList<Program> list = new ArrayList<Program>(List.of(programs));
        list.add(program);
        programs = (Program[]) list.toArray(new Program[]{});
        save();
    }

    @Override
    public boolean canReset() {
        return false;
    }

    @Override
    public JSONObject getConfigObject() {
        JSONObject json = new JSONObject();
        JSONArray list = new JSONArray();
        for (Program program :
                programs) {
            list.put(program.getName());
        }

        return json.put("list",list);
    }

    @Override
    public Component createNewInstance(JSONObject config) {
        if (!config.isEmpty()){
            JSONArray json = config.getJSONArray("list");
            programs = new Program[json.length()];
            for (int i = 0; i < json.length(); i++) {
                programs[i] = new Program(json.getString(i));
            }
        }
        panel = new GameModePanel(this);
        return panel;
    }

    @Override
    public String getUtilitiyName() {
        return "Game Mode";
    }

    @Override
    public Component getComponent() {
        return panel;
    }
}
