package com.saif.chucky;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class MultipleQuestionsFragment extends Fragment {
    private final int NUMBER_OF_ANSWERS = 4;

    public static MultipleQuestionsFragment getInstance(String question, String correctAnswer, String wrongAnswer1,
                                                        String wrongAnswer2, String wrongAnswer3) {
        MultipleQuestionsFragment f = new MultipleQuestionsFragment();
        Bundle b = new Bundle();
        b.putString("question", question);
        b.putString("correctAnswer", correctAnswer);
        b.putString("wrongAnswer1", wrongAnswer1);

        b.putString("wrongAnswer2", wrongAnswer2);
        b.putString("wrongAnswer3", wrongAnswer3);

        f.setArguments(b);
        return f;
    }

    public MultipleQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_multiple_questions, container, false);

        TextView tv = (TextView) v.findViewById(R.id.question);
        tv.setText(getArguments().getString("question"));
        int correctAnswerIndex = setCorrectAnswer(v);
        setWrongAnswers(v, correctAnswerIndex);
        v.findViewById(R.id.answer1Button).setOnClickListener(new MultipleQuestionsFragment.RadioButtonClickListener());
        v.findViewById(R.id.answer2Button).setOnClickListener(new MultipleQuestionsFragment.RadioButtonClickListener());
        v.findViewById(R.id.answer3Button).setOnClickListener(new MultipleQuestionsFragment.RadioButtonClickListener());
        v.findViewById(R.id.answer4Button).setOnClickListener(new MultipleQuestionsFragment.RadioButtonClickListener());
        return v;
    }

    public class RadioButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            Handler handler = new Handler();
            if (((QuestionsActivity) getActivity()).getNumberOfQuestionsRelatedToStage() == 0) {
                final Runnable r = new Runnable() {
                    public void run() {
                        onModeFinished();
                    }
                };
                handler.postDelayed(r, 1000);
                return;
            }
            if (getArguments().getString("correctAnswer").equalsIgnoreCase(b.getText().toString())) {
                ((QuestionsActivity) getActivity()).increaseScore();
                Toast.makeText(getContext(), "Correct Answer!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Incorrect Answer!", Toast.LENGTH_LONG).show();
            }
            final Runnable r2 = new Runnable() {
                public void run() {
                    ((QuestionsActivity) getActivity()).getNextQuestion();

                }
            };
            handler.postDelayed(r2, 1100);
        }
    }

    private void setAnswerButton(View v, String text, String answerNumber) {
        String buttonID = "answer" + answerNumber + "Button";
        int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
        TextView tv = (TextView) v.findViewById(resID);
        tv.setText(text);
    }

    private int setCorrectAnswer(View v) {
        int correctAnswerIndex = (int) (Math.random() * 4 + 1);
        switch (correctAnswerIndex) {
            case 1:
                setAnswerButton(v, getArguments().getString("correctAnswer"), "1");
                break;
            case 2:
                setAnswerButton(v, getArguments().getString("correctAnswer"), "2");

                break;
            case 3:
                setAnswerButton(v, getArguments().getString("correctAnswer"), "3");

                break;
            case 4:
                setAnswerButton(v, getArguments().getString("correctAnswer"), "4");

                break;
        }
        return correctAnswerIndex;
    }

    private void setWrongAnswers(View v, int correctAnswerIndex) {
        int index = 1;
        for (int i = 0; i < NUMBER_OF_ANSWERS; i++) {
            if (i + 1 != correctAnswerIndex) {
                setAnswerButton(v, getArguments().getString("wrongAnswer" + index), Integer.toString(i + 1));
                index++;
            }
        }
    }

    private void onModeFinished() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((QuestionsActivity) getActivity());

        if (!((QuestionsActivity) getActivity()).getDifficulty().equals("hard")) {
            alertDialogBuilder.setMessage("Finished this mode, click OK to go to the next mode");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            ((QuestionsActivity) getActivity()).getNextDifficulty();
                            ((QuestionsActivity) getActivity()).getQuestions();
                        }
                    });
        } else {
            alertDialogBuilder.setMessage("Finished the game(all modes), click OK to exit");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            ((QuestionsActivity) getActivity()).finish();
                        }
                    });
        }
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}