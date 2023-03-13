package com.singleevent.sdk.health.Activity;

import java.util.Random;

class Util {

    public float randomFloatBetween(float min, float max) {
        Random r = new Random();
        float random = min + r.nextFloat() * (max - min);
        return random;
    }
}
