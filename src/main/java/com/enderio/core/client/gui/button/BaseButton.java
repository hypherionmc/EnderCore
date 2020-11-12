package com.enderio.core.client.gui.button;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class BaseButton extends Button {
    private static final IPressable DUD_PRESSABLE = new IPressable() {
        @Override
        public void onPress(Button p_onPress_1_) { }
    };

    public BaseButton(int x, int y, int width, int height, ITextComponent title) {
        super(x, y, width, height, title, DUD_PRESSABLE);
    }

    public BaseButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction) {
        super(x, y, width, height, title, pressedAction);
    }

    public BaseButton(int x, int y, int width, int height, ITextComponent title, ITooltip onTooltip) {
        super(x, y, width, height, title, DUD_PRESSABLE, onTooltip);
    }

    public BaseButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITooltip onTooltip) {
        super(x, y, width, height, title, pressedAction, onTooltip);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isHovered() {
        if (!isActive())
            return false;
        return super.isHovered();
    }

    @Override
    public void onPress() {
        if (isActive())
            super.onPress();
    }
}
