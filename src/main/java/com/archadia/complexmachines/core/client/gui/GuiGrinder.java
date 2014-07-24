package com.archadia.complexmachines.core.client.gui;

import com.archadia.complexmachines.core.common.container.ContainerGrinder;
import com.archadia.complexmachines.core.common.tileentity.TileEntityGrinder;
import com.archadia.complexmachines.helper.ArchHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author Archadia
 */
public class GuiGrinder extends GuiContainer {

  private static TileEntityGrinder tileINV = new TileEntityGrinder();
  private ArchHelper helper = new ArchHelper();

  public GuiGrinder(InventoryPlayer par1InventoryPlayer, TileEntityGrinder tile) {
    super(new ContainerGrinder(par1InventoryPlayer, tile));
  }

  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    String s = this.tileINV.hasCustomInventoryName() ? this.tileINV.getInvName() : I18n.format(this.tileINV.getInvName());
    this.fontRendererObj.drawString(s, this.xSize / 2 - 44, 7, 4210752);
  }

  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(new ResourceLocation("complexmachines", "textures/gui/grinder.png"));
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    int i1;

    i1 = TileEntityGrinder.instance().getProcessProgressScaled(24);
    this.drawTexturedModalRect(k + 112, l + 48, 176, 0, i1 + 1, 6);
  }
}
