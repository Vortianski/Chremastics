package xox.labvorty.chremastics.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;
import xox.labvorty.chremastics.particles.options.CoinSparkleOptions;

public class CoinSparkleParticle extends TextureSheetParticle {

    private final float initialQuadSize;

    protected CoinSparkleParticle(
            ClientLevel level,
            double x, double y, double z,
            double xSpeed, double ySpeed, double zSpeed,
            float red, float green, float blue,
            SpriteSet spriteSet
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.setSprite(spriteSet.get(this.random));

        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;

        this.quadSize = 0.15F + this.random.nextFloat() * 0.1F;
        this.initialQuadSize = this.quadSize;

        this.lifetime = 15 + this.random.nextInt(10);
        this.gravity = 0.0F;
        this.hasPhysics = false;

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age >= this.lifetime) {
            this.remove();
            return;
        }

        float lifeRatio = (float) this.age / (float) this.lifetime;
        this.quadSize = this.initialQuadSize * (1.0F - lifeRatio);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<CoinSparkleOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(
                CoinSparkleOptions options,
                @NotNull ClientLevel level,
                double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed
        ) {
            return new CoinSparkleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.red, options.green, options.blue, spriteSet);
        }
    }
}