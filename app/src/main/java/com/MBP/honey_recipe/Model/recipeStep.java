package com.MBP.honey_recipe.Model;

import android.net.Uri;

public class recipeStep {
    String num;
    String step;
    Uri stepImage;

    public recipeStep(String num, String step, Uri stepImage) {
        this.num = num;
        this.step = step;
        this.stepImage = stepImage;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Uri getStepImage() {
        return stepImage;
    }

    public void setStepImage(Uri stepImage) {
        this.stepImage = stepImage;
    }
}
