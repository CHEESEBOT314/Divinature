package com.bigchickenstudios.divinature.client.gui.toasts;

import com.bigchickenstudios.divinature.Strings;
import com.bigchickenstudios.divinature.research.Research;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class ResearchToast implements IToast {

    private static final ResourceLocation TEXTURES = Strings.createResourceLocation("textures/gui/toasts.png");

    private final Research research;

    public ResearchToast(Research researchIn) {
        this.research = researchIn;
    }

    @Nonnull
    @Override
    public Visibility func_230444_a_(@Nonnull MatrixStack matrixStackIn, @Nonnull ToastGui toastGuiIn, long timeIn) {
        toastGuiIn.getMinecraft().getTextureManager().bindTexture(TEXTURES);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        toastGuiIn.blit(matrixStackIn, 0, 0, 0, 0, this.func_230445_a_(), this.func_238540_d_());
        boolean flag = false;
        ITextComponent info = this.research.getInfo();
        if (info != null) {
            flag = true;
            List<IReorderingProcessor> list = toastGuiIn.getMinecraft().fontRenderer.trimStringToWidth(info, 125);
            if (list.size() == 1) {
                toastGuiIn.getMinecraft().fontRenderer.func_243248_b(matrixStackIn, new StringTextComponent(I18n.format("divinature.research.toast")), 30.0F, 7.0F, 0x00FFFF00 | 0xFF000000);
                toastGuiIn.getMinecraft().fontRenderer.func_238422_b_(matrixStackIn, list.get(0), 30.0F, 18.0F, 0xFFFFFFFF);
            }
            else {
                int alpha;
                if (timeIn < 1500L) {
                    alpha = MathHelper.floor(MathHelper.clamp((float)(1500L - timeIn) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x04000000;
                    toastGuiIn.getMinecraft().fontRenderer.func_243248_b(matrixStackIn, new StringTextComponent(I18n.format("divinature.research.toast")), 30.0F, 11.0F, 0x00FFFF00 | alpha);
                }
                else {
                    alpha = MathHelper.floor(MathHelper.clamp((float)(timeIn - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x04000000;
                    int x = (this.func_238540_d_() - list.size() * 9) / 2;
                    for (Iterator<IReorderingProcessor> iterator = list.iterator(); iterator.hasNext(); x += 9) {
                        IReorderingProcessor iReorderingProcessor = iterator.next();
                        toastGuiIn.getMinecraft().fontRenderer.func_238422_b_(matrixStackIn, iReorderingProcessor, 30.0F, (float)x, 0x00FFFFFF | alpha);
                    }
                }
            }
        }

        ItemStack icon = this.research.getIcon();
        if (icon != null) {
            flag = true;
            toastGuiIn.getMinecraft().getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(icon, 8, 8);
        }

        return !flag || timeIn > 5000L ? Visibility.HIDE : Visibility.SHOW;
    }
}
