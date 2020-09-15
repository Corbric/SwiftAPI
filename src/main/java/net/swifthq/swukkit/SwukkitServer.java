package net.swifthq.swukkit;

import com.avaje.ebean.config.ServerConfig;
import com.google.common.collect.Lists;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.text.LiteralText;
import net.swifthq.swiftapi.SwiftApi;
import net.swifthq.swiftapi.player.SwPlayer;
import net.swifthq.swukkit.schedule.SwukkitScheduler;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SwukkitServer implements Server {

    public static String NAME = "Sukkit";
    public static String VERSION = "69";

    public DedicatedServer server;

    private final Logger logger = Logger.getLogger("Swukkit");
    private final BukkitScheduler scheduler;
    private final PluginManager pluginManager;

    List<SwPlayer> playerView;

    public SwukkitServer() {
        this.server = (DedicatedServer) DedicatedServer.getServer();
        this.scheduler = new SwukkitScheduler(this);
        this.pluginManager = new SimplePluginManager(this, new SimpleCommandMap(this));
        this.playerView = Collections.unmodifiableList(Lists.transform(server.getPlayerManager().getPlayers(), SwPlayer::from));
    }

    public void loadPlugins() {
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = new File(server.gameDir, "plugins");

        if (pluginFolder.exists()) {
            Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
            for (Plugin plugin : plugins) {
                try {
                    String message = "Loading Bukkit plugin " + plugin.getDescription().getFullName();
                    plugin.getLogger().info(message);
                    plugin.onLoad();
                } catch (Throwable ex) {
                    SwiftApi.LOGGER.error(ex.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Get better plugins lmao)", ex);
                }
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String getBukkitVersion() {
        return "1.8.8 on 1.8.9";
    }

    @Override
    public Player[] _INVALID_getOnlinePlayers() {
        throw new RuntimeException("Invalid call to get online players!");
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return null;
    }

    @Override
    public int getMaxPlayers() {
        return server.getMaxPlayerCount();
    }

    @Override
    public int getPort() {
        return 25565;
    }

    @Override
    public int getViewDistance() {
        return -1;
    }

    @Override
    public String getIp() {
        return server.getServerIp();
    }

    @Override
    public String getServerName() {
        return "Fabric";
    }

    @Override
    public String getServerId() {
        return "UNKNOWN";
    }

    @Override
    public String getWorldType() {
        return "UNKNOWN";
    }

    @Override
    public boolean getGenerateStructures() {
        return false;
    }

    @Override
    public boolean getAllowEnd() {
        return true;
    }

    @Override
    public boolean getAllowNether() {
        return true;
    }

    @Override
    public boolean hasWhitelist() {
        throw new RuntimeException("Whitelist commit no exist. please dont use thank");
    }

    @Override
    public void setWhitelist(boolean value) {
        throw new RuntimeException("Whitelist commit no exist. please dont use thank");
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        throw new RuntimeException("Whitelist commit no exist. please dont use thank");
    }

    @Override
    public void reloadWhitelist() {
        throw new RuntimeException("Whitelist commit no exist. please dont use thank");
    }

    @Override
    public int broadcastMessage(String message) {
        server.getPlayerManager().broadcastChatMessage(new LiteralText(message), false);
        return 0;
    }

    @Override
    public String getUpdateFolder() {
        return server.getPropertiesFilePath();
    }

    @Override
    public File getUpdateFolderFile() {
        return null;
    }

    @Override
    public long getConnectionThrottle() {
        return 0;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public Player getPlayer(String name) {
        return SwPlayer.from(server.getPlayerManager().getPlayer(name));
    }

    @Override
    public Player getPlayerExact(String name) {
        return getPlayer(name);
    }

    @Override
    public List<Player> matchPlayer(String name) {
        throw new RuntimeException("WTF is this for?");
    }

    @Override
    public Player getPlayer(UUID id) {
        return SwPlayer.from(server.getPlayerManager().getPlayer(id));
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        return null;
    }

    @Override
    public List<World> getWorlds() {
        return null;
    }

    @Override
    public World createWorld(WorldCreator creator) {
        return null;
//        return CustomWorldManager.loadWorld(creator.name(), creator.seed(), server, GameMode.SURVIVAL);
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        return false;
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        return false;
    }

    @Override
    public World getWorld(String name) {
        return null;
    }

    @Override
    public World getWorld(UUID uid) {
        return null;
    }

    @Override
    public MapView getMap(short id) {
        return null;
    }

    @Override
    public MapView createMap(World world) {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        return null;
    }

    @Override
    public void savePlayers() {

    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        return false;
    }

    @Override
    public void configureDbConfig(ServerConfig config) {

    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        return false;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        return null;
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return null;
    }

    @Override
    public void clearRecipes() {

    }

    @Override
    public void resetRecipes() {

    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        return null;
    }

    @Override
    public int getSpawnRadius() {
        return 0;
    }

    @Override
    public void setSpawnRadius(int value) {

    }

    @Override
    public boolean getOnlineMode() {
        return false;
    }

    @Override
    public boolean getAllowFlight() {
        return false;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public boolean useExactLoginLocation() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public int broadcast(String message, String permission) {
        return 0;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        return null;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        return null;
    }

    @Override
    public Set<String> getIPBans() {
        return null;
    }

    @Override
    public void banIP(String address) {

    }

    @Override
    public void unbanIP(String address) {

    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        return null;
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        return null;
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        return null;
    }

    @Override
    public GameMode getDefaultGameMode() {
        return null;
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {

    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return null;
    }

    @Override
    public File getWorldContainer() {
        return null;
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        return new OfflinePlayer[0];
    }

    @Override
    public Messenger getMessenger() {
        return null;
    }

    @Override
    public HelpMap getHelpMap() {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        return null;
    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public boolean isPrimaryThread() {
        return false;
    }

    @Override
    public String getMotd() {
        return null;
    }

    @Override
    public String getShutdownMessage() {
        return null;
    }

    @Override
    public Warning.WarningState getWarningState() {
        return null;
    }

    @Override
    public ItemFactory getItemFactory() {
        return null;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override
    public CachedServerIcon getServerIcon() {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(File file) {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(BufferedImage image) {
        return null;
    }

    @Override
    public void setIdleTimeout(int threshold) {

    }

    @Override
    public int getIdleTimeout() {
        return 0;
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        return null;
    }

    @Override
    public UnsafeValues getUnsafe() {
        throw new RuntimeException("No");
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        throw new RuntimeException("No");
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        throw new RuntimeException("No");
    }
}
