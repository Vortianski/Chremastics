package xox.labvorty.chremastics.init;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xox.labvorty.chremastics.Chremastics;

import java.util.function.Supplier;

public class ChremasticsAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Chremastics.MOD_ID);
    public static final Supplier<AttachmentType<Integer>> COIN_BALANCE = ATTACHMENT_TYPES.register(
            "coin_balance",
            () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .sync(ByteBufCodecs.INT)
                    .copyOnDeath()
                    .build()
    );
}