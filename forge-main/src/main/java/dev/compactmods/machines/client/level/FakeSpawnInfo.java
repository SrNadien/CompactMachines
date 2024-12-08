package dev.compactmods.machines.client.level;

import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.WritableLevelData;

/**
 * Credits to Immersive Engineering manual code for a baseline in 1.18.x.
 * Source: https://github.com/BluSunrize/ImmersiveEngineering/blob/1.18.2/src/main/java/blusunrize/immersiveengineering/common/util/fakeworld/FakeSpawnInfo.java
 */
public class FakeSpawnInfo implements WritableLevelData {
    private static final GameRules RULES = new GameRules();

    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private float spawnAngle;

    @Override
    public void setXSpawn(int x)
    {
        spawnX = x;
    }

    @Override
    public void setYSpawn(int y)
    {
        spawnY = y;
    }

    @Override
    public void setZSpawn(int z)
    {
        spawnZ = z;
    }

    @Override
    public void setSpawnAngle(float angle)
    {
        spawnAngle = angle;
    }

    @Override
    public int getXSpawn()
    {
        return spawnX;
    }

    @Override
    public int getYSpawn()
    {
        return spawnY;
    }

    @Override
    public int getZSpawn()
    {
        return spawnZ;
    }

    @Override
    public float getSpawnAngle()
    {
        return spawnAngle;
    }

    @Override
    public long getGameTime()
    {
        return 0;
    }

    @Override
    public long getDayTime()
    {
        return 0;
    }

    @Override
    public boolean isThundering()
    {
        return false;
    }

    @Override
    public boolean isRaining()
    {
        return false;
    }

    @Override
    public void setRaining(boolean isRaining)
    {

    }

    @Override
    public boolean isHardcore()
    {
        return false;
    }

    @Override
    public GameRules getGameRules()
    {
        return RULES;
    }

    @Override
    public Difficulty getDifficulty()
    {
        return Difficulty.PEACEFUL;
    }

    @Override
    public boolean isDifficultyLocked()
    {
        return false;
    }
}
