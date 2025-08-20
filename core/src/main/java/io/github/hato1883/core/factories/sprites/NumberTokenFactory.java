package io.github.hato1883.core.factories.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.hato1883.core.ui.gui.sprites.NumberTokenSprite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class NumberTokenFactory {
    public List<NumberTokenSprite> createTokens(Collection<Integer> numbers, float cx, float cy, BitmapFont baseFont) {
        List<NumberTokenSprite> tokens = new ArrayList<>(numbers.size());
        for (int n : numbers) {
            configureFont(baseFont, n);
            NumberTokenSprite token = new NumberTokenSprite(String.valueOf(n), baseFont);
            token.setPosition(cx, cy);
            tokens.add(token);
        }
        return tokens;
    }

    private void configureFont(BitmapFont font, int number) {
        if (number == 6 || number == 8) {
            font.getData().setScale(0.30f);
            font.setColor(Color.RED);
        } else if (number == 5 || number == 9) {
            font.getData().setScale(0.25f);
            font.setColor(Color.BLACK);
        } else if (number == 4 || number == 10) {
            font.getData().setScale(0.20f);
            font.setColor(Color.BLACK);
        } else if (number == 3 || number == 11) {
            font.getData().setScale(0.15f);
            font.setColor(Color.BLACK);
        } else if (number == 2 || number == 12) {
            font.getData().setScale(0.10f);
            font.setColor(Color.BLACK);
        } else {
            font.getData().setScale(0.25f);
            font.setColor(Color.BLACK);
        }
    }
}
