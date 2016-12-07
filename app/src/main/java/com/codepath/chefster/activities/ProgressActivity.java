package com.codepath.chefster.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.views.StepProgressView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.codepath.chefster.models.Step.Status.ACTIVE;
import static com.codepath.chefster.models.Step.Status.READY;

public class ProgressActivity extends ListeningActivity implements StepProgressView.OnStepProgressListener, TextToSpeech.OnInitListener {
    private final int START_COOKING = 0;
    private final int MID_COOKING = 1;
    private final int DONE_COOKING = 2;

    @Nullable @BindViews({ R.id.horiz_scroll_view_1, R.id.horiz_scroll_view_2, R.id.horiz_scroll_view_3 })
    List<HorizontalScrollView> horizScrollViewsList;

    @Nullable @BindViews({ R.id.linear_layout_dish_progress_1, R.id.linear_layout_dish_progress_2, R.id.linear_layout_dish_progress_3 })
    List<LinearLayout> linearLayoutsList;
    @Nullable @BindViews({ R.id.text_view_title_1, R.id.text_view_title_2, R.id.text_view_title_3 })
    List<TextView> titlesList;

   // @BindView(R.id.button_finish_cooking) Button finishCookingButton;

    //@BindView(R.id.button_finish_cooking) Button finishCookingButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private TextToSpeech tts;
    private boolean shouldUseVoiceHelp;
    private int mealProgress;

    List<HashSet<Integer>> stepIndexHashSetList;
    List<Dish> chosenDishes;
    List<List<Step>> stepsLists;
    List<Integer> nextStepPerDish;
    boolean[] finished;
    // A HashMap that points on an index for each dish name
    HashMap<String,Integer> dishNameToIndexHashMap;

    List<HashMap<Integer, CountDownTimer>> timersListPerDish;
    int numberOfPeople, numberOfPans, numberOfPots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.left_arrow);
            getSupportActionBar().setTitle("Cooking");
        }

        mealProgress = START_COOKING;
        tts = new TextToSpeech(this, this);
        tts.setSpeechRate((float) 0.9);

        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));
        finished = new boolean[chosenDishes.size()];
        stepsLists = new ArrayList<>(chosenDishes.size());
        timersListPerDish = new ArrayList<>();
        stepIndexHashSetList = new ArrayList<>(chosenDishes.size());
        for (Dish dish : chosenDishes) {
            stepsLists.add(dish.getSteps());
            stepIndexHashSetList.add(new HashSet<Integer>());
            timersListPerDish.add(new HashMap<Integer, CountDownTimer>());
        }

        Intent incomingIntent = getIntent();
        numberOfPeople = incomingIntent.getIntExtra("number_of_people", 1);
        numberOfPans = incomingIntent.getIntExtra("number_of_pans", 1);
        numberOfPots = incomingIntent.getIntExtra("number_of_pots", 1);
        dishNameToIndexHashMap = new HashMap<>();

        setupLinearLayouts();
        markInitialActiveSteps();
    }

    private void markInitialActiveSteps() {
        nextStepPerDish = new ArrayList<>(chosenDishes.size());
        for (int i = 0; i < stepsLists.size(); i++) {
            nextStepPerDish.add(0);
        }
        // The number of active steps is the number of people cooking
        for (int i = 0; i < numberOfPeople; i++) {
            Step thisStep = getNextBestStep();
            // There might not be a next step so check for null
            if (thisStep != null) {
                setUIStepStatus(thisStep, ACTIVE);
            }
        }
    }

    private Step getNextBestStep() {
        PriorityQueue<Step> potentialInitialSteps = new PriorityQueue<>(chosenDishes.size());
        for (int i = 0; i < stepsLists.size(); i++) {
            int nextStepOrderForThisList = nextStepPerDish.get(i);
            // We want to protect from out of bound exception. This dish might be finished
            if (nextStepOrderForThisList < stepsLists.get(i).size()) {
                Step nextStep = stepsLists.get(i).get(nextStepPerDish.get(i));
                if (nextStep.getPreRequisite() == -1
                        || !stepIndexHashSetList.isEmpty() && stepIndexHashSetList.get(i).contains(nextStep.getPreRequisite())) {
                    potentialInitialSteps.add(stepsLists.get(i).get(nextStepPerDish.get(i)));
                }
            }
        }
        if (potentialInitialSteps.isEmpty()) return null;
        else {
            Step chosenStep = potentialInitialSteps.peek();
            int index = dishNameToIndexHashMap.get(chosenStep.getDishName());
            nextStepPerDish.set(index, nextStepPerDish.get(index) + 1);

            return chosenStep;
        }
    }

    protected void setupLinearLayouts() {
        for (int i = 0; i < stepsLists.size(); i++) {
            // Prevent crashing from dishes with no steps
            if (stepsLists.get(i) != null && !stepsLists.get(i).isEmpty()) {
                // Fill hashmap for dishes names to index numbers
                dishNameToIndexHashMap.put(stepsLists.get(i).get(0).getDishName(), i);
                List<Step> currentList = stepsLists.get(i);
                for (Step step : currentList) {
                    // Populate each step in its view item
                    StepProgressView stepProgressView = new StepProgressView(this, chosenDishes.get(i), step, tts);
                    stepProgressView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    stepProgressView.setStepStatus(READY);
                    linearLayoutsList.get(i).addView(stepProgressView);
                }
                titlesList.get(i).setText(stepsLists.get(i).get(0).getDishName());
                // If we got to this index, show the horizontal scroll view
                horizScrollViewsList.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    // Visually scroll to the next step
    public void scrollToStep(int list, int step) {
        float x = linearLayoutsList.get(list).getChildAt(step).getX();
        ((HorizontalScrollView) linearLayoutsList.get(list).getParent()).smoothScrollTo((int) x, 0);
    }

    private boolean mealIsFinished() {
        for (boolean isFinished : finished) {
            if (!isFinished) return false;
        }
        return true;
    }

    public void finishCooking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.finish_cooking_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getBaseContext(), ShareActivity.class);
                        intent.putExtra(ChefsterApplication.SELECTED_DISHES_KEY, Parcels.wrap(chosenDishes));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
       builder.create().show();
    }

    @Override
    public void showNextStep(String dishName, int step, boolean isFinished) {
        int index = dishNameToIndexHashMap.get(dishName);
        List<Step> currentStepsList = stepsLists.get(index);

        // If the step is finished, we want to mark it as done and move to the next step
        // but sometime we have a 60m step to put something in the oven, and when we have
        // that, we want to see the next step and mark it as finished yet to keep the alarms
        if (isFinished) {
            stepIndexHashSetList.get(index).add(step);

            if (step == currentStepsList.size() - 1) {
                finished[index] = true;
                YoYo.with(Techniques.RubberBand)
                        .duration(1500)
                        .playOn(linearLayoutsList.get(index).getChildAt(step));
                if (mealIsFinished()) {
                    finishCooking();
                }
            } else {
                // When there are 2 people cooking, there could be 2 steps that should be done in the
                // same list
            }
        }

        Step chosenStep = getNextBestStep();
        if (chosenStep == null) {
            return;
        }
        setUIStepStatus(chosenStep, ACTIVE);
        int newStepListIndex = dishNameToIndexHashMap.get(chosenStep.getDishName());
        int order = chosenStep.getOrder();
        scrollToStep(newStepListIndex, order);


    }

    @Override
    public void pauseStep(String dishName, int step) {

    }

    @Override
    public void resumeStep(String dishName, int step) {

    }

    @Override
    public void expandStepItem(String dishName, boolean expand) {
        int index = dishNameToIndexHashMap.get(dishName);
        if (expand) {
            for (int i = 0; i < chosenDishes.size(); i++) {
                horizScrollViewsList.get(i).setVisibility(i == index ? View.VISIBLE : View.GONE);
            }
        } else {
            for (int i = 0; i < chosenDishes.size(); i++) {
                horizScrollViewsList.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    public void setUIStepStatus(Step step, Step.Status status) {
        int dishIndex = dishNameToIndexHashMap.get(step.getDishName());
        // increment the next step for this list
        int stepIndex = step.getOrder();
        ((StepProgressView) linearLayoutsList.get(dishIndex).getChildAt(stepIndex)).setStepStatus(status);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut("Hi, I'm Chefie, your cooking assistant. If you want me to read the steps for you, press on the speaker icon on the top right corner of every step");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String text) {
//        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
//            @Override
//            public void onStart(String s) {
//                stopListening();
//            }
//
//            @Override
//            public void onDone(String s) {
//                restartListeningService();
//            }
//
//            @Override
//            public void onError(String s) {
//                restartListeningService();
//            }
//        });
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void processVoiceCommands(String... voiceCommands) {
        if (voiceCommands[0].equals("shut the hell up") || voiceCommands[0].equals("stop talking")) {
            tts.speak("OK, I love you bye bye", TextToSpeech.QUEUE_FLUSH, null);
            return;
        } else if (voiceCommands[0].equals("finish cooking")) {
            finishCooking();
        } else {
            switch (mealProgress) {
                case START_COOKING:
                    if (voiceCommands[0].equals("yes")) {
                        speakOut("Great! Let's cook some amazing things together. Say 'shut the hell up' or 'stop talking' at any given moment to make me stop");
                        shouldUseVoiceHelp = true;
                    } else if (voiceCommands[0].equals("no")) {
                        speakOut("OK, i'll go find another friend to play with");
                        shouldUseVoiceHelp = false;
                    }
                    mealProgress = MID_COOKING;
                    break;

                case MID_COOKING:
                    if (voiceCommands[0].equals("next step") || voiceCommands[0].equals("finish")) {
                        speakOut("Showing next step");
                    }
                    break;

                case DONE_COOKING:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meal_inprogress, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_finish:
                finishCooking();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 0: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // Start continuous listening for commands
//                    while (tts.isSpeaking());
//                    context = getApplicationContext(); // Needs to be set
//                    VoiceRecognitionListener.getInstance().setListener(this); // Here we set the current listener
//                    startListening(); // starts listening
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
}
