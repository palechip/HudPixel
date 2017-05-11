package eladkay.hudpixel.asm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mixin(Chunk.class)
public abstract class MixinChunk {
    @Shadow
    private static Logger logger;
    /**
     * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata. Each entry corresponds to a
     * logical segment of 16x16x16 blocks, stacked vertically.
     */
    @Shadow
    private ExtendedBlockStorage[] storageArrays;
    /**
     * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs.
     */
    @Shadow
    private byte[] blockBiomeArray;
    /**
     * A map, similar to heightMap, that tracks how far down precipitation can fall.
     */
    @Shadow
    private int[] precipitationHeightMap;
    /**
     * Which columns need their skylightMaps updated.
     */
    @Shadow
    private boolean[] updateSkylightColumns;
    /**
     * Whether or not this Chunk is currently loaded into the World
     */
    @Shadow
    private boolean isChunkLoaded;
    /**
     * Reference to the World object.
     */
    @Shadow
    private World worldObj;
    @Shadow
    private int[] heightMap;
    /**
     * The x coordinate of the chunk.
     */
    @Shadow
    private int xPosition;
    /**
     * The z coordinate of the chunk.
     */
    @Shadow
    private int zPosition;
    private boolean isGapLightingUpdated;
    @Shadow
    private Map<BlockPos, TileEntity> chunkTileEntityMap;
    @Shadow
    private ClassInheritanceMultiMap<Entity>[] entityLists;
    /**
     * Boolean value indicating if the terrain is populated.
     */
    @Shadow
    private boolean isTerrainPopulated;
    @Shadow
    private boolean isLightPopulated;
    @Shadow
    private boolean field_150815_m;
    /**
     * Set to true if the chunk has been modified and needs to be updated internally.
     */
    @Shadow
    private boolean isModified;
    /**
     * Whether this Chunk has any Entities and thus requires saving on every tick
     */
    @Shadow
    private boolean hasEntities;
    /**
     * The time according to World.worldTime when this chunk was last saved
     */
    @Shadow
    private long lastSaveTime;
    /**
     * Lowest value in the heightmap.
     */
    @Shadow
    private int heightMapMinimum;
    /**
     * the cumulative number of ticks players have been in this chunk
     */
    @Shadow
    private long inhabitedTime;
    /**
     * Contains the current round-robin relight check index, and is implied as the relight check location as well.
     */
    @Shadow
    private int queuedLightChecks;
    @Shadow
    private ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue;

    @SideOnly(Side.CLIENT)
    @Overwrite
    public void fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_) {
        try {
            for (TileEntity tileEntity : chunkTileEntityMap.values()) {
                tileEntity.updateContainingBlockInfo();
                tileEntity.getBlockMetadata();
                tileEntity.getBlockType();
            }

            int i = 0;
            boolean flag = !this.worldObj.provider.getHasNoSky();

            for (int j = 0; j < this.storageArrays.length; ++j) {
                if ((p_177439_2_ & 1 << j) != 0) {
                    if (this.storageArrays[j] == null) {
                        this.storageArrays[j] = new ExtendedBlockStorage(j << 4, flag);
                    }

                    char[] achar = this.storageArrays[j].getData();

                    for (int k = 0; k < achar.length; ++k) {

                        achar[k] = (char) ((p_177439_1_[i + 1] & 255) << 8 | p_177439_1_[i] & 255);
                        i += 2;

                    }
                } else if (p_177439_3_ && this.storageArrays[j] != null) {
                    this.storageArrays[j] = null;
                }
            }

            for (int l = 0; l < this.storageArrays.length; ++l) {
                if ((p_177439_2_ & 1 << l) != 0 && this.storageArrays[l] != null) {
                    NibbleArray nibblearray = this.storageArrays[l].getBlocklightArray();
                    System.arraycopy(p_177439_1_, i, nibblearray.getData(), 0, nibblearray.getData().length);
                    i += nibblearray.getData().length;
                }
            }

            if (flag) {
                for (int i1 = 0; i1 < this.storageArrays.length; ++i1) {
                    if ((p_177439_2_ & 1 << i1) != 0 && this.storageArrays[i1] != null) {
                        NibbleArray nibblearray1 = this.storageArrays[i1].getSkylightArray();
                        System.arraycopy(p_177439_1_, i, nibblearray1.getData(), 0, nibblearray1.getData().length);
                        i += nibblearray1.getData().length;
                    }
                }
            }

            if (p_177439_3_) {
                System.arraycopy(p_177439_1_, i, this.blockBiomeArray, 0, this.blockBiomeArray.length);
                int k1 = i + this.blockBiomeArray.length;
            }

            for (int j1 = 0; j1 < this.storageArrays.length; ++j1) {
                if (this.storageArrays[j1] != null && (p_177439_2_ & 1 << j1) != 0) {
                    this.storageArrays[j1].removeInvalidBlocks();
                }
            }

            this.isLightPopulated = true;
            this.isTerrainPopulated = true;
            this.generateHeightMap();

            List<TileEntity> invalidList = new java.util.ArrayList<TileEntity>();

            for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
                if (tileentity.shouldRefresh(this.worldObj, tileentity.getPos(), tileentity.getBlockType().getStateFromMeta(tileentity.getBlockMetadata()), getBlockState(tileentity.getPos())))
                    invalidList.add(tileentity);
                tileentity.updateContainingBlockInfo();
            }

            for (TileEntity te : invalidList) te.invalidate();
        } catch (ArrayIndexOutOfBoundsException ignored) {

        }
    }

    @Shadow
    abstract IBlockState getBlockState(BlockPos pos);

    @Shadow
    abstract void generateHeightMap();
}