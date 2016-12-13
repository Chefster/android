package com.codepath.chefster.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.views.StepProgressView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.JsonElement;

import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static android.provider.Telephony.Mms.Part.FILENAME;
import static com.codepath.chefster.models.Step.Status.ACTIVE;
import static com.codepath.chefster.models.Step.Status.READY;

public class ProgressActivity extends ListeningActivity implements
        StepProgressView.OnStepProgressListener,
        TextToSpeech.OnInitListener,
        AIListener
{
    private static final String AI_TAG = "*** AI_LISTENER ***";
    private final int START_COOKING = 0;
    private final int MID_COOKING = 1;
    private final int DONE_COOKING = 2;

    @Nullable @BindViews({ R.id.horiz_scroll_view_1, R.id.horiz_scroll_view_2, R.id.horiz_scroll_view_3 })
    List<HorizontalScrollView> horizScrollViewsList;

    @Nullable @BindViews({ R.id.linear_layout_dish_progress_1, R.id.linear_layout_dish_progress_2, R.id.linear_layout_dish_progress_3 })
    List<LinearLayout> linearLayoutsList;
    @Nullable @BindViews({ R.id.text_view_title_1, R.id.text_view_title_2, R.id.text_view_title_3 })
    List<TextView> titlesList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private TextToSpeech tts;
    private AIService aiService;
    private int mealProgress;

    List<HashSet<Integer>> stepIndexHashSetList;
    List<Dish> chosenDishes;
    List<List<Step>> stepsLists;
    List<Integer> nextStepPerDish;
    List<Step> activeSteps;
    boolean[] finished;
    // A HashMap that points on an index for each dish name
    HashMap<String,Integer> dishNameToIndexHashMap;

    List<HashMap<Integer, CountDownTimer>> timersListPerDish;
    int numberOfPeople;
    HashMap<String, Integer> timeLeftForDish;
    int timeLeftForMeal;

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

        final AIConfiguration config = new AIConfiguration("1a796bb78f7641efa85f4872db56c2f2",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        tts = new TextToSpeech(this, this);

        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));
        finished = new boolean[chosenDishes.size()];
        stepsLists = new ArrayList<>(chosenDishes.size());
        timersListPerDish = new ArrayList<>();
        timeLeftForDish = new HashMap<>();
        stepIndexHashSetList = new ArrayList<>(chosenDishes.size());
        for (Dish dish : chosenDishes) {
            stepsLists.add(dish.getSteps());
            stepIndexHashSetList.add(new HashSet<Integer>());
            timersListPerDish.add(new HashMap<Integer, CountDownTimer>());
            timeLeftForMeal += (dish.getPrepTime() + dish.getCookingTime());
            timeLeftForDish.put(dish.getTitle(), (dish.getPrepTime() + dish.getCookingTime()));
        }

        Intent incomingIntent = getIntent();
        numberOfPeople = incomingIntent.getIntExtra("number_of_people", 1);
        dishNameToIndexHashMap = new HashMap<>();

        setupLinearLayouts();
        markInitialActiveSteps();
    }

    private void markInitialActiveSteps() {
        activeSteps = new ArrayList<>();
        nextStepPerDish = new ArrayList<>(chosenDishes.size());
        for (int i = 0; i < stepsLists.size(); i++) {
            nextStepPerDish.add(0);
        }
        // The number of active steps is the number of people cooking
        for (int i = 0; i < numberOfPeople; i++) {
            Step thisStep = getNextBestStep();
            addStepToActiveSteps(thisStep);
            // There might not be a next step so check for null
            if (thisStep != null) {
                setUIStepStatus(thisStep, ACTIVE);
            }
        }
    }

    private void addStepToActiveSteps(Step step) {

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
            // Keeping track of active steps
            activeSteps.add(chosenStep);
            if (activeSteps.size() > numberOfPeople) {
                activeSteps.remove(0);
            }
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
                titlesList.get(i).setVisibility(View.VISIBLE);
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
        // Update time left for dish
        timeLeftForMeal -= currentStepsList.get(step).getDurationTime();
        timeLeftForDish.put(dishName, (timeLeftForDish.get(dishName) - currentStepsList.get(step).getDurationTime()));
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
                speakOut("Hi, I’m Cheffie, your cooking assistant. The steps you should follow have an orange background. Click on any step to make the text larger. If you want me to read a step, press the speaker icon on the top right corner of that step. Any questions?", true);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String text, final boolean keepGoing) {
        HashMap<String, String> myHashAlarm = new HashMap<>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                "end of wakeup message ID");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                ProgressActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        aiService.stopListening();
                        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    }
                });
            }

            @Override
            public void onDone(String s) {
                ProgressActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                        if (keepGoing) {
                            aiService.startListening();
                        }
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });

    }

    @Override
    protected void onPause() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            speakOut(" ", false);
            tts.stop();
            tts.shutdown();
        }

        if (aiService != null) {
            aiService.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            speakOut(" ", false);
            tts.stop();
            tts.shutdown();
        }

        if (aiService != null) {
            aiService.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void processVoiceCommands(String... voiceCommands) {
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

            case R.id.action_cheffie:
                speakOut("Hey, how can I help?", true);
                return true;

            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            case R.id.action_finish:
                finishCooking();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // AI listener!!!

    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Result result = response.getResult();

                // Get parameters
                String parameterString = "";
                if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                    for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                        parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                    }
                }

                // Show results in TextView.
                Log.d("*** AI_LISTENER ***", "Query:" + result.getResolvedQuery() +
                        "\nAction: " + result.getAction() +
                        "\nParameters: " + parameterString +
                        "\nFulfillment: " + result.getFulfillment().getSpeech());

                if (result.getAction().equals("say_dish_total_time")) {
                    JsonElement jsonElement = result.getParameters().get("dish");
                    if (jsonElement != null) {
                        String dishName = jsonElement.toString().replace("\"", "");
                        for (Dish dish : chosenDishes) {
                            if (dish.getTitle().equals(dishName)) {
                                speakOut(dishName + " takes approximately " + (dish.getCookingTime() + dish.getPrepTime()) + " minutes, if you're not on facebook while cooking", true);
                            }
                        }
                    }
                } else if (result.getAction().equals("say_dish_prep_time")) {
                    JsonElement jsonElement = result.getParameters().get("dish");
                    if (jsonElement != null) {
                        String dishName = result.getParameters().get("dish").toString().replace("\"", "");
                        for (Dish dish : chosenDishes) {
                            if (dish.getTitle().equals(dishName)) {
                                speakOut(dishName + " takes approximately " + dish.getPrepTime() + "minutes to prepare", true);
                            }
                        }
                    }
                } else if (result.getAction().equals("time_left_dish")) {
                    JsonElement jsonElement = result.getParameters().get("dish");
                    if (jsonElement != null) {
                        String dishName = result.getParameters().get("dish").toString().replace("\"", "");
                        speakOut("You have " + timeLeftForDish.get(dishName) + " minutes to finish cooking " + dishName, true);
                    }
                } else if (result.getAction().equals("time_left_total")) {
                    speakOut("Tired already? you have approximately " + timeLeftForMeal + " minutes. Why don't you grab a beer?", true);
                } else if (result.getAction().equals("im_here")) {
                    speakOut("If you need me, click the question mark button on the top toolbar", true);
                } else if (result.getAction().equals("smalltalk.greetings") ||
                        result.getAction().equals("smalltalk.dialog") ||
                        result.getAction().equals("smalltalk.emotions") ||
                        result.getAction().equals("smalltalk.confirmation") ||
                        result.getAction().equals("smalltalk.topics") ||
                        result.getAction().equals("smalltalk.agent") ||
                        result.getAction().equals("smalltalk.person")) {
                    speakOut(result.getFulfillment().getSpeech(), true);
                } else if (result.getAction().equals("smalltalk.appraisal")) {
                    speakOut(result.getFulfillment().getSpeech(), false);
                } else {
                    if (result.getFulfillment().getSpeech() != null) {
                        speakOut(result.getFulfillment().getSpeech(), true);
                    } else {
                        speakOut("Not sure I understand. Sorry.", true);
                    }
                }
            }
        });

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        Log.d(AI_TAG, "started listening!!!!");
    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {
        Log.d(AI_TAG, "finished listening!!!!");
    }
}
