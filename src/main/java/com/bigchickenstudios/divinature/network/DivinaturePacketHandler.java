package com.bigchickenstudios.divinature.network;

import com.bigchickenstudios.divinature.Strings;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public final class DivinaturePacketHandler {

    private static final String VERSION = "1";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(Strings.createResourceLocation("main"), () -> VERSION, VERSION::equals, VERSION::equals);

    public static void init() {
        int id = 0;
        register(++id, ResearchInfoMessage.class, ResearchInfoMessage::new, NetworkDirection.PLAY_TO_CLIENT);
        register(++id, ResearchSeenMessage.class, ResearchSeenMessage::new, NetworkDirection.PLAY_TO_SERVER);
        register(++id, ResearchSelectPageMessage.class, ResearchSelectPageMessage::new, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static <T extends IMessage> void register(int id, Class<T> clazz, Supplier<T> supplier, @Nullable NetworkDirection direction) {
        INSTANCE.registerMessage(id, clazz, T::write,(pb) -> {
            T t = supplier.get();
            t.read(pb);
            return t;
        }, (t, c) -> {
            NetworkEvent.Context ctx = c.get();
            ctx.enqueueWork(() -> {
                t.handle(ctx);
            });
            ctx.setPacketHandled(true);
        }, Optional.ofNullable(direction));
    }

    public static <T extends IMessage> void send(T t, PacketDistributor.PacketTarget target) {
        INSTANCE.send(target, t);
    }

    private DivinaturePacketHandler() {}
}
