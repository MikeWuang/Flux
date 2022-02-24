package today.flux.utility.viaversion.platform;

import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.platform.PlatformTask;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import today.flux.utility.viaversion.ViaVersion;
import today.flux.utility.viaversion.utils.FutureTaskId;
import today.flux.utility.viaversion.utils.JLoggerToLog4j;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class VRPlatform implements ViaPlatform<UUID> {

    private final Logger logger = new JLoggerToLog4j(LogManager.getLogger("ViaVersion"));

    private final VRViaConfig config;
    private final File dataFolder;
    private final ViaAPI<UUID> api;

    public VRPlatform(File dataFolder) {
        Path configDir = dataFolder.toPath().resolve("ViaVersion");
        config = new VRViaConfig(configDir.resolve("viaversion.yml").toFile());
        this.dataFolder = configDir.toFile();
        api = new VRViaAPI();
    }

    public static String legacyToJson(String legacy) {
        return GsonComponentSerializer.gson().serialize(LegacyComponentSerializer.legacySection().deserialize(legacy));
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getPlatformName() {
        return "Flux Protocols";
    }

    @Override
    public String getPlatformVersion() {
        return ViaVersion.SHARED_VERSION+"";
    }

    @Override
    public String getPluginVersion() {
        return "4.0.0";
    }

    @Override
    public FutureTaskId runAsync(Runnable runnable) {
        return new FutureTaskId(CompletableFuture
                .runAsync(runnable, ViaVersion.getInstance().getAsyncExecutor())
                .exceptionally(throwable -> {
                    if (!(throwable instanceof CancellationException)) {
                        throwable.printStackTrace();
                    }
                    return null;
                })
        );
    }

    @Override
    public FutureTaskId runSync(Runnable runnable) {
        return new FutureTaskId(ViaVersion.getInstance().getEventLoop().submit(runnable).addListener(errorLogger()));
    }

    @Override
    public PlatformTask runSync(Runnable runnable, long ticks) {
        return new FutureTaskId(ViaVersion.getInstance().getEventLoop().schedule(() -> runSync(runnable), ticks *
                50, TimeUnit.MILLISECONDS).addListener(errorLogger()));
    }

    @Override
    public PlatformTask runRepeatingSync(Runnable runnable, long ticks) {
         return new FutureTaskId(ViaVersion.getInstance().getEventLoop().scheduleAtFixedRate(() -> runSync(runnable),
                 0, ticks * 50, TimeUnit.MILLISECONDS).addListener(errorLogger()));
    }

    private <T extends Future<?>> GenericFutureListener<T> errorLogger() {
        return future -> {
            if (!future.isCancelled() && future.cause() != null) {
                future.cause().printStackTrace();
            }
        };
    }

    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[1337];
    }

    private ViaCommandSender[] getServerPlayers() {
        return new ViaCommandSender[1337];
    }

    @Override
    public void sendMessage(UUID uuid, String s) {
    }

    @Override
    public boolean kickPlayer(UUID uuid, String s) {
        return false;
    }

    @Override
    public boolean isPluginEnabled() {
        return true;
    }

    @Override
    public ViaAPI<UUID> getApi() {
        return api;
    }

    @Override
    public ViaVersionConfig getConf() {
        return config;
    }

    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public void onReload() {
    }

    @Override
    public JsonObject getDump() {
        JsonObject platformSpecific = new JsonObject();
        return platformSpecific;
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }
}
