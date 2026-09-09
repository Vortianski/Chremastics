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
    public static final Supplier<AttachmentType<Long>> COIN_BALANCE = ATTACHMENT_TYPES.register(
            "coin_balance",
            () -> AttachmentType.builder(() -> 0L)
                    .serialize(Codec.LONG)
                    .sync(ByteBufCodecs.VAR_LONG)
                    .copyOnDeath()
                    .build()
    );
}