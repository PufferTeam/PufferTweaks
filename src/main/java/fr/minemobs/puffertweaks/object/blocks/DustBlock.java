package fr.minemobs.puffertweaks.object.blocks;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DustBlock extends FallingBlock {
    public DustBlock(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState state, IBlockReader reader, BlockPos pos) {
        return -8356741;
    }

}
