package com.bigchickenstudios.divinature.client.renderer.overlay;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public abstract class OverlayRenderer<T extends TileEntity> {

    protected abstract void render(T t, Minecraft mc, float partialTick);

    protected void renderItem(Minecraft mc, ItemStack stack, int x, int y) {
        if (!stack.isEmpty()) {
            mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
            mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, stack, x, y);
        }
    }

    private static final Map<TileEntityType<?>, OverlayRenderer<?>> RENDERERS = Maps.newHashMap();

    public static <T extends TileEntity> void bindOverlayRenderer(TileEntityType<T> type, Supplier<? extends OverlayRenderer<? super T>> supplier) {
        RENDERERS.put(type, supplier.get());
    }

    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.objectMouseOver instanceof BlockRayTraceResult) {
                TileEntity te = mc.world.getTileEntity(((BlockRayTraceResult)mc.objectMouseOver).getPos());
                if (te != null) {
                    renderOverlay(te, mc, mc.getRenderPartialTicks());
                }
            }
        }
    }

    private static <T extends TileEntity> void renderOverlay(T t, Minecraft mc, float partialTick) {
        OverlayRenderer<T> renderer = (OverlayRenderer<T>)RENDERERS.get(t.getType());
        if (renderer != null) {
            renderer.render(t, mc, partialTick);
        }
    }
}
