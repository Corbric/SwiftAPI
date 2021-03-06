package net.swifthq.swiftapi;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.*;
import net.minecraft.server.MinecraftServer;
import net.swifthq.swiftapi.player.SwPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PlayerScoreboard extends Scoreboard {

    private final MinecraftServer server;
    private final Set<ScoreboardObjective> objectives = Sets.newHashSet();
    private final ServerPlayerEntity player;
    private ScoreboardState state;

    public PlayerScoreboard(MinecraftServer server, ServerPlayerEntity player) {
        this.server = server;
        this.player = player;
    }

    public void updateScore(ScoreboardPlayerScore score) {
        super.updateScore(score);
        if (this.objectives.contains(score.getObjective())) {
            this.server.getPlayerManager().sendToAll((new ScoreboardPlayerUpdateS2CPacket(score)));
        }

        this.markDirtyIfNull();
    }

    public void updatePlayerScore(String playerName) {
        super.updatePlayerScore(playerName);
        this.server.getPlayerManager().sendToAll((new ScoreboardPlayerUpdateS2CPacket(playerName)));
        this.markDirtyIfNull();
    }

    public void updatePlayerScore(String playerName, ScoreboardObjective objective) {
        super.updatePlayerScore(playerName, objective);
        this.server.getPlayerManager().sendToAll((new ScoreboardPlayerUpdateS2CPacket(playerName, objective)));
        this.markDirtyIfNull();
    }

    public void setObjectiveSlot(int slot, ScoreboardObjective objective) {
        ScoreboardObjective scoreboardObjective = this.getObjectiveForSlot(slot);
        super.setObjectiveSlot(slot, objective);
        if (scoreboardObjective != objective && scoreboardObjective != null) {
            if (this.getSlot(scoreboardObjective) > 0) {
                this.server.getPlayerManager().sendToAll((new ScoreboardDisplayS2CPacket(slot, objective)));
            } else {
                this.removeScoreboardObjective(scoreboardObjective);
            }
        }

        if (objective != null) {
            if (this.objectives.contains(objective)) {
                this.server.getPlayerManager().sendToAll((new ScoreboardDisplayS2CPacket(slot, objective)));
            } else {
                this.addScoreboardObjective(objective);
            }
        }

        this.markDirtyIfNull();
    }

    public boolean addPlayerToTeam(String string, String string2) {
        if (super.addPlayerToTeam(string, string2)) {
            Team team = this.getTeam(string2);
            this.server.getPlayerManager().sendToAll((new TeamS2CPacket(team, Arrays.asList(string), 3)));
            this.markDirtyIfNull();
            return true;
        } else {
            return false;
        }
    }

    public void removePlayerFromTeam(String playerName, Team team) {
        super.removePlayerFromTeam(playerName, team);
        this.server.getPlayerManager().sendToAll((new TeamS2CPacket(team, Arrays.asList(playerName), 4)));
        this.markDirtyIfNull();
    }

    public void updateObjective(ScoreboardObjective objective) {
        super.updateObjective(objective);
        this.markDirtyIfNull();
    }

    public void updateExistingObjective(ScoreboardObjective objective) {
        super.updateExistingObjective(objective);
        if (this.objectives.contains(objective)) {
            this.server.getPlayerManager().sendToAll((new ScoreboardObjectiveUpdateS2CPacket(objective, 2)));
        }

        this.markDirtyIfNull();
    }

    public void updateRemovedObjective(ScoreboardObjective objective) {
        super.updateRemovedObjective(objective);
        if (this.objectives.contains(objective)) {
            this.removeScoreboardObjective(objective);
        }

        this.markDirtyIfNull();
    }

    public void updateScoreboardTeamAndPlayers(Team team) {
        super.updateScoreboardTeamAndPlayers(team);
        this.server.getPlayerManager().sendToAll((new TeamS2CPacket(team, 0)));
        this.markDirtyIfNull();
    }

    public void updateScoreboardTeam(Team team) {
        super.updateScoreboardTeam(team);
        this.server.getPlayerManager().sendToAll((new TeamS2CPacket(team, 2)));
        this.markDirtyIfNull();
    }

    public void updateRemovedTeam(Team team) {
        super.updateRemovedTeam(team);
        this.server.getPlayerManager().sendToAll((new TeamS2CPacket(team, 1)));
        this.markDirtyIfNull();
    }

    public void setScoreboardState(ScoreboardState state) {
        this.state = state;
    }

    protected void markDirtyIfNull() {
        if (this.state != null) {
            this.state.markDirty();
        }

    }

    public List<Packet<?>> createChangePackets(ScoreboardObjective scoreboardObjective) {
        List<Packet<?>> list = Lists.newArrayList();
        list.add(new ScoreboardObjectiveUpdateS2CPacket(scoreboardObjective, 0));

        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveForSlot(i) == scoreboardObjective) {
                list.add(new ScoreboardDisplayS2CPacket(i, scoreboardObjective));
            }
        }

        for (ScoreboardPlayerScore scoreboardPlayerScore : this.getAllPlayerScores(scoreboardObjective)) {
            list.add(new ScoreboardPlayerUpdateS2CPacket(scoreboardPlayerScore));
        }

        return list;
    }

    public void addScoreboardObjective(ScoreboardObjective objective) {
        List<Packet<?>> list = this.createChangePackets(objective);

        for (ServerPlayerEntity serverPlayerEntity : this.server.getPlayerManager().getPlayers()) {
            for (Packet<?> packet : list) {
                serverPlayerEntity.networkHandler.sendPacket(packet);
            }
        }

        this.objectives.add(objective);
    }

    public List<Packet<?>> method_5934(ScoreboardObjective scoreboardObjective) {
        List<Packet<?>> list = Lists.newArrayList();
        list.add(new ScoreboardObjectiveUpdateS2CPacket(scoreboardObjective, 1));

        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveForSlot(i) == scoreboardObjective) {
                list.add(new ScoreboardDisplayS2CPacket(i, scoreboardObjective));
            }
        }

        return list;
    }

    public void removeScoreboardObjective(ScoreboardObjective objective) {
        List<Packet<?>> list = this.method_5934(objective);

        for (Packet<?> packet : list) {
            SwPlayer.from(player).sendPacket(packet);
        }

        this.objectives.remove(objective);
    }

    public int getSlot(ScoreboardObjective objective) {
        int i = 0;

        for (int j = 0; j < 19; ++j) {
            if (this.getObjectiveForSlot(j) == objective) {
                ++i;
            }
        }

        return i;
    }
}
