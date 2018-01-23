package com.buuz135.togetherforever.api.data;

import com.buuz135.togetherforever.api.IOfflineSyncRecovery;
import com.buuz135.togetherforever.api.ISyncAction;
import com.buuz135.togetherforever.api.ITogetherTeam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class DataManager extends WorldSavedData {

    public static final String NAME = "TogetherForever";
    public static final String TEAM = "Teams";
    public static final String RECOVERY = "Recovery";

    private List<ITogetherTeam> teams;
    private List<IOfflineSyncRecovery> recoveries;

    public DataManager(String string) {
        super(string);
        teams = new ArrayList<>();
        recoveries = new ArrayList<>();
    }

    public DataManager() {
        this(NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        teams = new ArrayList<>();
        //
        NBTTagCompound raw = nbt.getCompoundTag(NAME);
        //TEAM READING
        NBTTagCompound teamCompound = raw.getCompoundTag(TEAM);
        for (String teamNames : teamCompound.getKeySet()) {
            NBTTagCompound team = teamCompound.getCompoundTag(teamNames);
            String teamID = team.getString("TeamID");
            Class aClass = TogetherRegistries.getTogetherTeamClass(teamID);
            if (aClass != null) {
                try {
                    ITogetherTeam togetherTeam = (ITogetherTeam) aClass.newInstance();
                    togetherTeam.readFromNBT(team.getCompoundTag("Value"));
                    teams.add(togetherTeam);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //OFFLINE RECOVERY READING
        NBTTagCompound offlineRecovery = nbt.getCompoundTag(RECOVERY);
        for (String key : offlineRecovery.getKeySet()) {
            ISyncAction action = TogetherRegistries.getSyncActionFromID(key);
            if (action != null) {
                try {
                    IOfflineSyncRecovery recovery = (IOfflineSyncRecovery) action.getOfflineRecovery().newInstance();
                    recovery.readFromNBT(offlineRecovery.getCompoundTag(key));
                    recoveries.add(recovery);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound custom = new NBTTagCompound();
        //TEAM SAVING
        NBTTagCompound teamCompound = new NBTTagCompound();
        for (ITogetherTeam togetherTeam : teams) {
            NBTTagCompound team = new NBTTagCompound();
            team.setString("TeamID", TogetherRegistries.getTogetherTeamID(togetherTeam.getClass()));
            team.setTag("Value", togetherTeam.getNBTTag());
            teamCompound.setTag(togetherTeam.getTeamName(), team);
        }
        custom.setTag(TEAM, teamCompound);
        //OFFLINE RECOVERY SAVING
        NBTTagCompound offlineRecovery = new NBTTagCompound();
        for (IOfflineSyncRecovery recovery : recoveries) {
            offlineRecovery.setTag(TogetherRegistries.getSyncActionIdFromOfflineRecovery(recovery), recovery.writeToNBT());
        }
        custom.setTag(RECOVERY, offlineRecovery);

        compound.setTag(NAME, custom);
        return compound;
    }

    public List<ITogetherTeam> getTeams() {
        return teams;
    }

    public List<IOfflineSyncRecovery> getRecoveries() {
        return recoveries;
    }
}