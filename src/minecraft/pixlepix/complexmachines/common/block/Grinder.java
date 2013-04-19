package pixlepix.complexmachines.common.block;

import pixlepix.complexmachines.client.ClientProxy;
import pixlepix.complexmachines.common.ComplexMachines;
import pixlepix.complexmachines.common.tileentity.GrinderTileEntity;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Grinder extends BlockContainer {
	private Icon connectorIcon;
	private Icon topIcon;

	public Grinder(int id) {
		super(id, UniversalElectricity.machine);
		this.setUnlocalizedName("Grinder");
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	
	
	public Grinder() {
		super(ComplexMachines.blockStartingID + 7, UniversalElectricity.machine);
		this.setStepSound(soundMetalFootstep);
		this.setUnlocalizedName("Grinder");
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z,
			EntityLiving par5EntityLiving, ItemStack itemStack) {

		((GrinderTileEntity) par1World.getBlockTileEntity(x, y, z)).initiate();
		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 0;

		switch (angle)
		{
			case 0:
				change = 1;
				break;
			case 1:
				change = 2;
				break;
			case 2:
				change = 0;
				break;
			case 3:
				change = 3;
				break;
		}
		par1World.setBlockMetadataWithNotify(x, y, z, change, 2);
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
	}

	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z,
			EntityPlayer par5EntityPlayer, int side, float hitX, float hitY,
			float hitZ) {
		if (!par1World.isRemote) {
			par5EntityPlayer.openGui(ComplexMachines.instance, 2, par1World, x,
					y, z);
			return true;
		}

		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public TileEntity createTileEntity(World var1, int metadata) {
		return new GrinderTileEntity();

	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	// Imported code from EE, unsure if it is needed
	/*
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @Override public int getRenderType() { return ClientProxy.RENDER_ID; }
	 */

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {

		blockIcon = par1IconRegister
				.registerIcon("ComplexMachines:GrinderFront");
		connectorIcon = par1IconRegister
				.registerIcon("ComplexMachines:GrinderInput");
		topIcon = par1IconRegister.registerIcon("ComplexMachines:GrinderTop");
	}

	@Override
	public Icon getIcon(int side, int meta) {

		if (side == meta + 2) {
			return connectorIcon;
		} else {
			if (side == 1 || side == 0) {
				return topIcon;
			}
			return blockIcon;
		}
	}



	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return null;
	}


}