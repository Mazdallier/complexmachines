package pixlepix.complexmachines.common.tileentity;

import pixlepix.complexmachines.common.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;

public class SinglePointTileEntity extends TileEntityElectrical implements
		IPacketReceiver, IElectricityStorage {
	public final double WATTS_PER_TICK = 5000;
	public final double TRANSFER_LIMIT = 12500;
	private int drawingTicks = 0;
	private double joulesStored = 0;
	public static double maxJoules = 2000000;
	public int ticks = 10000;
	/**
	 * The ItemStacks that hold the items currently being used in the wire mill;
	 * 0 = battery; 1 = input; 2 = output;
	 */

	private int playersUsing = 0;
	public int orientation;
	private int targetID = 0;
	private int targetMeta = 0;

	private boolean initialized;
	private IConductor connectedElectricUnit;

	@Override
	public void initiate() {
		this.initialized = true;
	}

	public void updateEntity() {
		// System.out.println("Focal Points have been spawned at "+"  "+xCoord+"  "+yCoord+"  "+zCoord);
		super.updateEntity();

		if (!this.worldObj.isRemote) {
			if (atCorrectLocation()) {
				// Check nearby blocks and see if the conductor is full. If so,
				// then it is connected
				ForgeDirection outputDirection = ForgeDirection
						.getOrientation(this.getBlockMetadata() + 2);
				TileEntity outputTile = VectorHelper.getConnectorFromSide(
						this.worldObj, new Vector3(this.xCoord, this.yCoord,
								this.zCoord), outputDirection);

				IElectricityNetwork network = ElectricityNetworkHelper
						.getNetworkFromTileEntity(outputTile, outputDirection);

				if (network != null) {
					if (network.getRequest().getWatts() > 0) {
						this.connectedElectricUnit = (IConductor) outputTile;
					} else {
						this.connectedElectricUnit = null;
					}
				} else {
					this.connectedElectricUnit = null;
				}

				if (!this.isDisabled()) {

					if (this.connectedElectricUnit != null) {

						this.connectedElectricUnit.getNetwork().startProducing(
								this, (Config.singlePointGeneratorOutput / this.getVoltage()) / 20,
								this.getVoltage());

					}
				}

			}

		}
	}

	private boolean atCorrectLocation() {
		int target=Config.singlePointRadius;
			System.out.println("Target");
			if(xCoord==target&&zCoord==target&&yCoord==60){
				return true;
			}
			if(xCoord==target&&zCoord==-1*target&&yCoord==60){
				return true;
			}
			if(xCoord==-1*target&&zCoord==target&&yCoord==60){
				return true;
			}
			if(xCoord==-1*target&&zCoord==-1*target&&yCoord==60){
				return true;
			}
			
		
		return false;
	}

	@Override
	public void handlePacketData(INetworkManager inputNetwork, int type,
			Packet250CustomPayload packet, EntityPlayer player,
			ByteArrayDataInput dataStream) {
		try {
			this.drawingTicks = dataStream.readInt();
			this.disabledTicks = dataStream.readInt();
			this.joulesStored = dataStream.readDouble();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getVoltage() {
		return 120;
	}

	@Override
	public double getJoules() {
		return this.joulesStored;
	}

	@Override
	public void setJoules(double joules) {
		this.joulesStored = joules;
	}

	@Override
	public double getMaxJoules() {
		return FillerMachineTileEntity.maxJoules;
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		return direction.ordinal() == this.getBlockMetadata() + 2;
	}

}
