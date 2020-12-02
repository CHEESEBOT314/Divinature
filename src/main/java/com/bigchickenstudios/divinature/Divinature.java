package com.bigchickenstudios.divinature;

import com.bigchickenstudios.divinature.block.ModBlocks;
import com.bigchickenstudios.divinature.client.DivinatureClient;
import com.bigchickenstudios.divinature.item.ModItems;
import com.bigchickenstudios.divinature.item.crafting.ModRecipeSerializers;
import com.bigchickenstudios.divinature.network.DivinaturePacketHandler;
import com.bigchickenstudios.divinature.particle.ModParticleTypes;
import com.bigchickenstudios.divinature.research.PlayerResearch;
import com.bigchickenstudios.divinature.research.ResearchManager;
import com.bigchickenstudios.divinature.tileentity.ModTileEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Strings.MODID)
public class Divinature  {

    public static final Logger LOGGER = LogManager.getLogger();

    public Divinature() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.init(bus);
        ModItems.init(bus);
        ModParticleTypes.init(bus);
        ModRecipeSerializers.init(bus);
        ModTileEntityTypes.init(bus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> DivinatureClient::registerListeners);

        ResearchManager.init();

        DivinaturePacketHandler.init();

        MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStart);
    }

    private void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        PlayerResearch.init();
    }

    
}
