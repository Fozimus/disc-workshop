package io.github.fozimus.discworkshop.audio;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;

public class FileSound implements SoundInstance {
    private final String fileName;
    private final Vec3d position;
    private final boolean loop;
    public final static String ID_PREFIX = "customsound_";

    public FileSound(String fileName, Vec3d position, boolean loop) {
        this.fileName = fileName;
        this.position = position;
        this.loop = loop;
    }

    @Override
	public Identifier getId() {
        return DiscWorkshop.id(ID_PREFIX + fileName) ;
	}

	@Override
	public WeightedSoundSet getSoundSet(SoundManager soundManager) {
		return new WeightedSoundSet(getId(), null);
	}

	@Override
	public Sound getSound() {
        return new Sound(getId(), ConstantFloatProvider.create(getVolume()), ConstantFloatProvider.create(getPitch()), 1, Sound.RegistrationType.SOUND_EVENT, true, false, 64);
	}

	@Override
	public SoundCategory getCategory() {
        return SoundCategory.RECORDS;
    }

	@Override
	public boolean isRepeatable() {
        return loop;
	}

	@Override
	public boolean isRelative() {
        return false;
	}

	@Override
	public int getRepeatDelay() {
        return 0;
	}

	@Override
	public float getVolume() {
        return 4;
	}

	@Override
	public float getPitch() {
        return 1;
	}

	@Override
	public double getX() {
        return position.x;
	}

	@Override
	public double getY() {
        return position.y;
	}

	@Override
	public double getZ() {
        return position.z;
	}

	@Override
	public AttenuationType getAttenuationType() {
        return AttenuationType.LINEAR;
	}
    
}
