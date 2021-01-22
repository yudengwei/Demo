package com.example.view.interpolator;

import android.animation.TimeInterpolator;

/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 fichardu
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class EaseInOutElasticInterpolator implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        if (input == 0f) {
            return 0f;
        }
        if ((input *= 2) >= 2.0f) {
            return 1.0f;
        }

        float p = 0.3f * 1.5f;
        float s = p / 4;
        if (input < 1.0f) {
            return (float) (-0.5 * Math.pow(2, 10*(input-=1)) * Math.sin((input - s) * (2 * Math.PI) / p));
        } else {
            return (float) (0.5f * Math.pow(2, -10 * (input-=1)) * Math.sin((input - s) * (2 * Math.PI) / p)) + 1;
        }

    }

}
