package com.hkim00.moves;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import com.hkim00.moves.fragments.Intro1Fragment;


public class QuestionnaireActivity extends AppIntro {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add your slide com.hkim00.moves.fragments here.

        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new Intro1Fragment());
//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);
        
//        SliderPage sliderPage = new SliderPage();
//        sliderPage.setTitle("info1");
//        sliderPage.setDescription("Tell us about yourself!");
//        sliderPage.setImageDrawable(R.drawable.sun_questionnaire);
//        sliderPage.setBgColor(R.color.white);
//        addSlide(AppIntroFragment.newInstance(sliderPage));

        // OPTIONAL METHODS
        // Override bar/separator color.
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}


