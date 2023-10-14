package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;
import java.util.*;

public class BarakoaVillagePieces {
    private static final Set<Block> BLOCKS_NEEDING_POSTPROCESSING = ImmutableSet.<Block>builder().add(Blocks.NETHER_BRICK_FENCE).add(Blocks.TORCH).add(Blocks.WALL_TORCH).add(Blocks.OAK_FENCE).add(Blocks.SPRUCE_FENCE).add(Blocks.DARK_OAK_FENCE).add(Blocks.ACACIA_FENCE).add(Blocks.BIRCH_FENCE).add(Blocks.JUNGLE_FENCE).add(Blocks.LADDER).add(Blocks.IRON_BARS).add(Blocks.SKELETON_SKULL).build();

    public static final ResourceLocation PLATFORM_1 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_platform_1");
    public static final ResourceLocation PLATFORM_2 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_platform_2");
    public static final ResourceLocation[] PLATFORMS = new ResourceLocation[] {
            PLATFORM_1,
            PLATFORM_2
    };
    public static final ResourceLocation FIREPIT = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_firepit");
    public static final ResourceLocation TREE_1 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_tree_1");
    public static final ResourceLocation TREE_2 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_tree_2");
    public static final ResourceLocation TREE_3 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_tree_3");
    public static final ResourceLocation[] TREES = new ResourceLocation[] {
            TREE_1,
            TREE_2,
            TREE_3
    };
    public static final ResourceLocation SPIKE_1 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_spike_1");
    public static final ResourceLocation SPIKE_2 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_spike_2");
    public static final ResourceLocation SPIKE_3 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_spike_3");
    public static final ResourceLocation SPIKE_4 = new ResourceLocation(MowziesMobs.MODID, "barakoa/barakoa_spike_4");
    public static final ResourceLocation[] SPIKES = new ResourceLocation[] {
            SPIKE_1,
            SPIKE_2,
            SPIKE_3,
            SPIKE_4
    };
    public static final ResourceLocation THRONE = new ResourceLocation(MowziesMobs.MODID, "barakoa/barako_throne");

    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.<ResourceLocation, BlockPos>builder()
            .put(PLATFORM_1, new BlockPos(-5, 0, -5))
            .put(PLATFORM_2, new BlockPos(-5, 0, -5))
            .put(FIREPIT, new BlockPos(-3, 0, -3))
            .put(TREE_1, new BlockPos(-5, 1, -3))
            .put(TREE_2, new BlockPos(-3, 1, -3))
            .put(TREE_3, new BlockPos(-3, 1, -3))
            .put(SPIKE_1, new BlockPos(-1, 1, 0))
            .put(SPIKE_2, new BlockPos(0, 1, 0))
            .put(SPIKE_3, new BlockPos(0, 1, 0))
            .put(SPIKE_4, new BlockPos(0, 1, 0))
            .put(THRONE, new BlockPos(-9, 0, 0))
            .build();

    private static final Map<ResourceLocation, Pair<BlockPos, BlockPos>> BOUNDS_OFFSET = ImmutableMap.<ResourceLocation, Pair<BlockPos, BlockPos>>builder()
            .put(PLATFORM_1, new Pair<>(new BlockPos(1, 0, 0), new BlockPos(-3, 0, -3)))
            .put(PLATFORM_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, -3)))
            .put(FIREPIT, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(TREE_1, new Pair<>(new BlockPos(1, 0, 1), new BlockPos(-3, 0, -3)))
            .put(TREE_2, new Pair<>(new BlockPos(2, 0, 1), new BlockPos(-1, 0, -3)))
            .put(TREE_3, new Pair<>(new BlockPos(2, 0, 2), new BlockPos(-2, 0, -2)))
            .put(SPIKE_1, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_2, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_3, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(SPIKE_4, new Pair<>(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0)))
            .put(THRONE, new Pair<>(new BlockPos(4, 0, 1), new BlockPos(-4, 0, -3)))
            .build();

    public static StructurePiece addPiece(ResourceLocation resourceLocation, StructureManager manager, BlockPos pos, Rotation rot, StructurePieceAccessor pieces, WorldgenRandom rand) {
        StructurePiece newPiece = new BarakoaVillagePieces.Piece(manager, resourceLocation, rot, pos);
        pieces.addPiece(newPiece);
        return newPiece;
    }

    public static StructurePiece addPieceCheckBounds(ResourceLocation resourceLocation, StructureManager manager, BlockPos pos, Rotation rot, StructurePieceAccessor pieces, WorldgenRandom rand, List<StructurePiece> ignore) {
        BarakoaVillagePieces.Piece newPiece = new BarakoaVillagePieces.Piece(manager, resourceLocation, rot, pos);
        StructurePiece collisionPiece = pieces.findCollisionPiece(newPiece.getCollisionBoundingBox());
        if (collisionPiece != null && !ignore.contains(collisionPiece)) return null;
        pieces.addPiece(newPiece);
        return newPiece;
    }

    public static StructurePiece addPlatform(StructureManager manager, BlockPos pos, Rotation rot, StructurePiecesBuilder builder, WorldgenRandom rand) {
        int whichPlatform = rand.nextInt(PLATFORMS.length);
        Piece newPiece = new Piece(manager, PLATFORMS[whichPlatform], rot, pos);
        if (findCollisionPiece(builder.pieces, newPiece.getCollisionBoundingBox()) != null) return null;
        builder.addPiece(newPiece);
        return newPiece;
    }

    public static StructurePiece addPieceCheckBounds(ResourceLocation resourceLocation, StructureManager manager, BlockPos pos, Rotation rot, StructurePiecesBuilder pieces, WorldgenRandom rand) {
       return addPieceCheckBounds(resourceLocation, manager, pos, rot, pieces, rand, Collections.emptyList());
    }

    @Nullable
    public static StructurePiece findCollisionPiece(List<StructurePiece> pieces, BoundingBox bounds) {
        for(StructurePiece structurePiece : pieces) {
            if (structurePiece instanceof Piece && ((Piece)structurePiece).getCollisionBoundingBox().intersects(bounds)) {
                return structurePiece;
            }
            else if (structurePiece.getBoundingBox().intersects(bounds)) {
                return structurePiece;
            }
        }
        return null;
    }

    public static class Piece extends TemplateStructurePiece {
        protected ResourceLocation resourceLocation;
        public BoundingBox collisionBoundingBox;

        public Piece(StructurePieceType pieceType, StructureManager manager, ResourceLocation resourceLocationIn, Rotation rotation, BlockPos pos) {
            super(pieceType, 0, manager, resourceLocationIn, resourceLocationIn.toString(), makeSettings(rotation, resourceLocationIn), makePosition(resourceLocationIn, pos, rotation));
            this.resourceLocation = resourceLocationIn;
            this.collisionBoundingBox = makeCollisionBoundingBox();
            if (resourceLocation == THRONE || resourceLocation == FIREPIT) boundingBox = getBoundingBox().moved(0, 1, 0);
        }

        public Piece(StructurePieceType pieceType, StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super(pieceType, tagCompound, context.structureManager(), (resourceLocation) -> makeSettings(Rotation.valueOf(tagCompound.getString("Rot")), resourceLocation));
            this.collisionBoundingBox = makeCollisionBoundingBox();
            if (resourceLocation == THRONE || resourceLocation == FIREPIT) boundingBox = getBoundingBox().moved(0, 1, 0);
        }

        public Piece(StructureManager manager, ResourceLocation resourceLocationIn, Rotation rotation, BlockPos pos) {
            this(FeatureHandler.BARAKOA_VILLAGE_PIECE, manager, resourceLocationIn, rotation, pos);
        }

        public Piece(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            this(FeatureHandler.BARAKOA_VILLAGE_PIECE, context, tagCompound);
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation resourceLocation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        private static BlockPos makePosition(ResourceLocation resourceLocation, BlockPos pos, Rotation rotation) {
            return pos.offset(BarakoaVillagePieces.OFFSET.get(resourceLocation).rotate(rotation));
        }

        public BoundingBox makeCollisionBoundingBox() {
            StructureTemplate structuretemplate = this.template;
            BlockPos boundsMinOffset, boundsMaxOffset;
            boundsMinOffset = boundsMaxOffset = new BlockPos(0, 0, 0);
            Pair<BlockPos, BlockPos> boundsOffset = BOUNDS_OFFSET.get(resourceLocation);
            if (boundsOffset != null) {
                boundsMinOffset = boundsOffset.getFirst();
                boundsMaxOffset = boundsOffset.getSecond();
            }

            Vec3i sizeVec = structuretemplate.getSize().offset(-1, -1, -1);
            BlockPos blockpos = StructureTemplate.transform(BlockPos.ZERO.offset(boundsMinOffset), placeSettings.getMirror(), placeSettings.getRotation(), placeSettings.getRotationPivot());
            BlockPos blockpos1 = StructureTemplate.transform(BlockPos.ZERO.offset(sizeVec).offset(boundsMaxOffset), placeSettings.getMirror(), placeSettings.getRotation(), placeSettings.getRotationPivot());
            return BoundingBox.fromCorners(blockpos, blockpos1).move(templatePosition);
        }

        public BoundingBox getCollisionBoundingBox() {
            return collisionBoundingBox;
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super.addAdditionalSaveData(context, tagCompound);
            tagCompound.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        public void postProcess(WorldGenLevel p_192682_, StructureFeatureManager p_192683_, ChunkGenerator p_192684_, Random p_192685_, BoundingBox p_192686_, ChunkPos p_192687_, BlockPos p_192688_) {
            super.postProcess(p_192682_, p_192683_, p_192684_, p_192685_, p_192686_, p_192687_, p_192688_);
        }

        /*
         * If you added any data marker structure blocks to your structure, you can access and modify them here. In this case,
         * our structure has a data maker with the string "chest" put into it. So we check to see if the incoming function is
         * "chest" and if it is, we now have that exact position.
         *
         * So what is done here is we replace the structure block with a chest and we can then set the loottable for it.
         *
         * You can set other data markers to do other behaviors such as spawn a random mob in a certain spot, randomize what
         * rare block spawns under the floor, or what item an Item Frame will have.
         */
        @Override
        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {
            Rotation rotation = this.placeSettings.getRotation();
            if (function.equals("support")) {
                worldIn.setBlock(pos, Blocks.OAK_FENCE.defaultBlockState(), 3);
                fillAirLiquidDown(worldIn, Blocks.OAK_FENCE.defaultBlockState(), pos.below());
            }
            else if (function.equals("trunk")) {
                fillAirLiquidDownTrunk(worldIn, pos, rand);
            }
            else if (function.equals("leg")) {
                fillAirLiquidDown(worldIn, Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState(), pos);
            }
            else if (function.equals("base")) {
                fillAirLiquidDownBase(worldIn, pos, rand);
            }
            else if (function.equals("stairs")) {
                Direction stairDirection = Direction.EAST;
                stairDirection = rotation.rotate(stairDirection);
                setBlockState(worldIn, pos.relative(Direction.UP, 1), Blocks.AIR.defaultBlockState());
                setBlockState(worldIn, pos.relative(Direction.UP, 2), Blocks.AIR.defaultBlockState());
                setBlockState(worldIn, pos, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, stairDirection.getOpposite()));
                pos = pos.relative(Direction.DOWN);
                setBlockState(worldIn, pos, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, stairDirection).setValue(StairBlock.HALF, Half.TOP));
                for (int i = 1; i < 20; i++) {
                    pos = pos.relative(stairDirection);
                    if (!Block.canSupportRigidBlock(worldIn, pos)) {
                        setBlockState(worldIn, pos, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, stairDirection.getOpposite()));
                        pos = pos.relative(Direction.DOWN);
                        setBlockState(worldIn, pos, Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, stairDirection).setValue(StairBlock.HALF, Half.TOP));
                    } else {
                        break;
                    }
                }
            }
            else if (function.equals("umvuthi")) {
                setBlockState(worldIn, pos, Blocks.AIR.defaultBlockState());
                EntityBarako barako = new EntityBarako(EntityHandler.BARAKO.get(), worldIn.getLevel());
                barako.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                int i = rotation.rotate(3, 4);
                barako.setDirection(i);
                barako.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(barako.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                BlockPos offset = new BlockPos(0, 0, -18);
                offset = offset.rotate(rotation);
                BlockPos firePitPos = pos.offset(offset);
                firePitPos = worldIn.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, firePitPos);
                barako.restrictTo(firePitPos, -1);
                worldIn.addFreshEntity(barako);
            }
            else if ("chest".equals(function)) {
                Direction facing = Direction.NORTH;
                facing = rotation.rotate(facing);
                createChest(worldIn, sbb, rand, pos, LootTableHandler.BARAKOA_VILLAGE_HOUSE, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing));
            }
            else if ("skull".equals(function)) {
                BlockPos groundPos = getGroundPos(worldIn, pos);
                setBlockState(worldIn, groundPos.above(), Blocks.SKELETON_SKULL.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, rand.nextInt(16)));
            }
            else if ("chest_under".equals(function)) {
                if (rand.nextFloat() < 0.5) worldIn.removeBlock(pos, false);
                else {
                    BlockPos groundPos = getGroundPos(worldIn, pos);
                    Direction facing = rand.nextFloat() < 0.5 ? Direction.NORTH : Direction.EAST;
                    facing = rotation.rotate(facing);
                    createChest(worldIn, sbb, rand, groundPos.above(), LootTableHandler.BARAKOA_VILLAGE_HOUSE, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing));
                }
            }
            else if ("mask".equals(function)) {
                worldIn.removeBlock(pos, false);
                ItemFrame itemFrame = new ItemFrame(worldIn.getLevel(), pos, rotation.rotate(Direction.NORTH));
                int i = rand.nextInt(MaskType.values().length);
                MaskType type = MaskType.values()[i];
                ItemBarakoaMask mask = ItemHandler.BARAKOA_MASK_FURY;
                switch (type) {
                    case BLISS:
                        mask = ItemHandler.BARAKOA_MASK_BLISS;
                        break;
                    case FEAR:
                        mask = ItemHandler.BARAKOA_MASK_FEAR;
                        break;
                    case FURY:
                        mask = ItemHandler.BARAKOA_MASK_FURY;
                        break;
                    case MISERY:
                        mask = ItemHandler.BARAKOA_MASK_MISERY;
                        break;
                    case RAGE:
                        mask = ItemHandler.BARAKOA_MASK_RAGE;
                        break;
                    case FAITH:
                        mask = ItemHandler.BARAKOA_MASK_FAITH;
                        break;
                }
                ItemStack stack = new ItemStack(mask);
                itemFrame.setItem(stack, false);
                worldIn.addFreshEntity(itemFrame);
            }
            else {
                worldIn.removeBlock(pos, false);
            }
        }

        protected void setBlockState(LevelAccessor worldIn, BlockPos pos, BlockState state) {
            FluidState ifluidstate = worldIn.getFluidState(pos);
            if (!ifluidstate.isEmpty()) {
                worldIn.getFluidTicks().willTickThisTick(pos, ifluidstate.getType());
                if (state.hasProperty(BlockStateProperties.WATERLOGGED)) state = state.setValue(BlockStateProperties.WATERLOGGED, true);
            }
            worldIn.setBlock(pos, state, 2);
            if (BLOCKS_NEEDING_POSTPROCESSING.contains(state.getBlock())) {
                worldIn.getChunk(pos).markPosForPostprocessing(pos);
            }
        }

        public BlockPos getGroundPos(LevelAccessor worldIn, BlockPos startPos) {
            while(!Block.canSupportRigidBlock(worldIn, startPos) && startPos.getY() > worldIn.getMinBuildHeight()) {
                startPos = startPos.below();
            }
            return startPos;
        }

        public void fillAirLiquidDown(LevelAccessor worldIn, BlockState state, BlockPos startPos) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while(!Block.canSupportRigidBlock(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                setBlockState(worldIn, pos, state);
                --j;
            }
        }

        public void fillAirLiquidDownTrunk(LevelAccessor worldIn, BlockPos startPos, Random rand) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while(!Block.canSupportRigidBlock(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                setBlockState(worldIn, pos, rand.nextFloat() < 0.2 ? BlockHandler.CLAWED_LOG.get().defaultBlockState() : Blocks.STRIPPED_JUNGLE_WOOD.defaultBlockState());
                --j;
            }
        }

        public void fillAirLiquidDownBase(LevelAccessor worldIn, BlockPos startPos, Random rand) {
            int i = startPos.getX();
            int j = startPos.getY();
            int k = startPos.getZ();
            while(!Block.canSupportRigidBlock(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                setBlockState(worldIn, pos, rand.nextFloat() < 0.5 ? Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState() : Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState());
                --j;
            }
        }
    }

    public static class PlatformPiece extends Piece {
        private int tableCorner;
        private int tableContent;
        private int bedCorner;
        private int bedDirection;
        private int chestCorner;
        private int chestDirection;

        public PlatformPiece(StructureManager manager, ResourceLocation resourceLocationIn, Rotation rotation, BlockPos pos) {
            super(FeatureHandler.BARAKOA_VILLAGE_HOUSE, manager, resourceLocationIn, rotation, pos);
        }

        public PlatformPiece(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super(FeatureHandler.BARAKOA_VILLAGE_HOUSE, context, tagCompound);
            tableCorner = tagCompound.getInt("TableCorner");
            tableContent = tagCompound.getInt("TableContent");
            bedCorner = tagCompound.getInt("BedCorner");
            bedDirection = tagCompound.getInt("bedDirection");
            chestCorner = tagCompound.getInt("ChestCorner");
            chestDirection = tagCompound.getInt("ChestDirection");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super.addAdditionalSaveData(context, tagCompound);
            tagCompound.putInt("TableCorner", tableCorner);
            tagCompound.putInt("TableContent", tableContent);
            tagCompound.putInt("BedCorner", bedCorner);
            tagCompound.putInt("bedDirection", bedDirection);
            tagCompound.putInt("ChestCorner", chestCorner);
            tagCompound.putInt("ChestDirection", chestDirection);
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {
            Rotation rotation = this.placeSettings.getRotation();
            if ("corner".equals(function.substring(0, function.length() - 1))) {
                worldIn.removeBlock(pos, false);
                pos = pos.below();
                int whichCorner = Integer.parseInt(function.substring(function.length() - 1));
                Rotation cornerRotation = Rotation.values()[4 - whichCorner];
                if (whichCorner == tableCorner) {
                    setBlockState(worldIn, pos, Blocks.ACACIA_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
                    if (tableContent <= 1) {
                        setBlockState(worldIn, pos.above(), Blocks.TORCH.defaultBlockState());
                    } else if (tableContent == 2) {
                        int skullRot = cornerRotation.rotate(rotation.rotate(2, 16), 16);
                        setBlockState(worldIn, pos.above(), Blocks.SKELETON_SKULL.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, skullRot));
                    }
                } else if (whichCorner == bedCorner) {
                    setBlockState(worldIn, pos, Blocks.YELLOW_CARPET.defaultBlockState());
                    BlockPos offset = new BlockPos(1, 0, 0);
                    if (bedDirection == 1) offset = new BlockPos(0, 0, -1);
                    offset = offset.rotate(rotation);
                    offset = offset.rotate(cornerRotation);
                    setBlockState(worldIn, pos.offset(offset), Blocks.YELLOW_CARPET.defaultBlockState());
                }
            }
            else {
                super.handleDataMarker(function, pos, worldIn, rand, sbb);
            }
        }
    }

    public abstract static class NonTemplatePiece extends ScatteredFeaturePiece {

        protected NonTemplatePiece(StructurePieceType structurePieceTypeIn, int xIn, int yIn, int zIn, int widthIn, int heightIn, int depthIn, Direction direction) {
            super(structurePieceTypeIn, xIn, yIn, zIn, widthIn, heightIn, depthIn, direction);
        }

        protected NonTemplatePiece(StructurePieceType structurePieceTypeIn, CompoundTag nbt) {
            super(structurePieceTypeIn, nbt);
        }

        public BlockPos findGround(LevelAccessor worldIn, int x, int z) {
            int i = this.getWorldX(x, z);
            int k = this.getWorldZ(x, z);
            int j = worldIn.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k);
            return new BlockPos(i, j, k);
        }

        public void fillAirLiquidBelowHeightmap(LevelAccessor worldIn, BlockState state, int x, int z) {
            int i = this.getWorldX(x, z);
            int k = this.getWorldZ(x, z);
            int j = worldIn.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k) - 2;
            while(!Block.canSupportRigidBlock(worldIn, new BlockPos(i, j, k)) && j > 1) {
                BlockPos pos = new BlockPos(i, j, k);
                FluidState ifluidstate = worldIn.getFluidState(pos);
                BlockState toPut = state;
                if (!ifluidstate.isEmpty()) {
                    worldIn.getFluidTicks().willTickThisTick(pos, ifluidstate.getType());
                    if (toPut.hasProperty(BlockStateProperties.WATERLOGGED)) toPut = toPut.setValue(BlockStateProperties.WATERLOGGED, true);
                }
                worldIn.setBlock(pos, toPut, 2);
                if (BLOCKS_NEEDING_POSTPROCESSING.contains(state.getBlock())) {
                    worldIn.getChunk(pos).markPosForPostprocessing(pos);
                }
                --j;
            }
        }

        protected void setBlockState(LevelAccessor worldIn, BlockPos pos, BlockState state) {
            FluidState ifluidstate = worldIn.getFluidState(pos);
            if (!ifluidstate.isEmpty()) {
                worldIn.getFluidTicks().willTickThisTick(pos, ifluidstate.getType());
                if (state.hasProperty(BlockStateProperties.WATERLOGGED)) state = state.setValue(BlockStateProperties.WATERLOGGED, true);
            }
            worldIn.setBlock(pos, state, 2);
            if (BLOCKS_NEEDING_POSTPROCESSING.contains(state.getBlock())) {
                worldIn.getChunk(pos).markPosForPostprocessing(pos);
            }
        }
    }

    public static class FirepitPiece extends Piece {

        public FirepitPiece(StructureManager manager, Rotation rotation, BlockPos pos) {
            super(FeatureHandler.BARAKOA_VILLAGE_FIREPIT, manager, FIREPIT, rotation, pos);
        }

        public FirepitPiece(StructurePieceSerializationContext context, CompoundTag tagCompound) {
            super(FeatureHandler.BARAKOA_VILLAGE_FIREPIT, context, tagCompound);
        }

        public BlockPos findGround(LevelAccessor worldIn, int x, int z) {
            int i = this.getWorldX(x, z);
            int k = this.getWorldZ(x, z);
            int j = worldIn.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k);
            return new BlockPos(i, j, k);
        }

        @Override
        public void postProcess(WorldGenLevel worldIn, StructureFeatureManager structureManager, ChunkGenerator chunkGenerator, Random randomIn, BoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            super.postProcess(worldIn, structureManager, chunkGenerator, randomIn, p_230383_5_, p_230383_6_, p_230383_7_);
            BlockPos centerPos = findGround(worldIn, 4, 4);

            // Spawn Barakoa
            int numBarakoa = randomIn.nextInt(5) + 5;
            for (int i = 1; i <= numBarakoa; i++) {
                int distance;
                int angle;
                EntityBarakoaVillager barakoa = new EntityBarakoaVillager(EntityHandler.BARAKOA_VILLAGER.get(), worldIn.getLevel());
                for (int j = 1; j <= 20; j++) {
                    distance = randomIn.nextInt(10) + 2;
                    angle = randomIn.nextInt(360);
                    int x = (int) (distance * Math.sin(Math.toRadians(angle))) + 4;
                    int z = (int) (distance * Math.cos(Math.toRadians(angle))) + 4;
                    BlockPos bPos = findGround(worldIn, x, z);
                    barakoa.setPos(bPos.getX(), bPos.getY(), bPos.getZ());
                    if (bPos.getY() > 0 && barakoa.checkSpawnRules(worldIn, MobSpawnType.STRUCTURE) && worldIn.noCollision(barakoa.getBoundingBox())) {
                        barakoa.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(barakoa.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                        barakoa.restrictTo(centerPos, 25);
                        worldIn.addFreshEntity(barakoa);
                        break;
                    }
                }
            }
        }
    }
}
