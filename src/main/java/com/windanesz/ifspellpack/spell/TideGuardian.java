package com.windanesz.ifspellpack.spell;

import com.windanesz.ifspellpack.IFSpellPack;
import com.windanesz.ifspellpack.registry.IFSPPotions;
import electroblob.wizardry.spell.SpellBuff;

public class TideGuardian extends SpellBuff {

    public TideGuardian() {
        super(IFSpellPack.MODID, "tide_guardian", 0f, 0.07843137f, 0.5098039215686274f, () -> IFSPPotions.TIDE_GUARDIAN);
    }

    @Override
    protected int getBonusAmplifier(float potencyModifier) {
        return 2 * super.getBonusAmplifier(potencyModifier);
    }
}
