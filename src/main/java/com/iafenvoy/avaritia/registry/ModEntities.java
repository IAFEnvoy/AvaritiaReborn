package com.iafenvoy.avaritia.registry;

import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.annotation.registration.ObjectReg;
import com.iafenvoy.annotationlib.api.IAnnotatedRegistryEntry;
import com.iafenvoy.avaritia.AvaritiaReborn;
import com.iafenvoy.avaritia.entity.InfinityArrowEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

@ModId(AvaritiaReborn.MOD_ID)
public class ModEntities implements IAnnotatedRegistryEntry {
    @ObjectReg
    public static final EntityType<InfinityArrowEntity> INFINITY_ARROW = build((entityType, world) -> new InfinityArrowEntity(world), SpawnGroup.MISC, 64, 1, false, 0.5F, 0.5F);

    public static <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> constructor, SpawnGroup category, int trackingRange, int updateInterval, boolean fireImmune, float sizeX, float sizeY) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(category, constructor).trackRangeBlocks(trackingRange).trackedUpdateRate(updateInterval).dimensions(EntityDimensions.fixed(sizeX, sizeY));
        if (fireImmune) builder.fireImmune();
        return builder.build();
    }
}
