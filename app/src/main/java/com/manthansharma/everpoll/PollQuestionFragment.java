package com.manthansharma.everpoll;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.manthansharma.everpoll.api.model.Choice;
import com.manthansharma.everpoll.api.model.Question;


public class PollQuestionFragment extends Fragment {
	private static final String ARG_PARAM1 = "question";
	private static final String ARG_PARAM2 = "has_next";
	private static final String ARG_PARAM3 = "question_no";
	TextView _questionText;
	RadioButton _choice1, _choice2, _choice3, _choice4;
	RadioGroup _choiceGroup;
	Button _btn;
	private Question question;
	private Boolean hasNext;
	private Integer question_no;

	private OnFragmentInteractionListener mListener;

	public PollQuestionFragment() {
		// Required empty public constructor
	}

	public static PollQuestionFragment newInstance(Question question, Boolean hasNext, Integer question_no) {
		PollQuestionFragment fragment = new PollQuestionFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, new Gson().toJson(question));
		args.putBoolean(ARG_PARAM2, hasNext);
		args.putInt(ARG_PARAM3, question_no);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			question = new Gson().fromJson(getArguments().getString(ARG_PARAM1), Question.class);
			hasNext = getArguments().getBoolean(ARG_PARAM2);
			question_no = getArguments().getInt(ARG_PARAM3);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_poll_question, container, false);

		_questionText = (TextView) view.findViewById(R.id.poll_question_text);
		_choice1 = (RadioButton) view.findViewById(R.id.poll_question_choice1);
		_choice2 = (RadioButton) view.findViewById(R.id.poll_question_choice2);
		_choice3 = (RadioButton) view.findViewById(R.id.poll_question_choice3);
		_choice4 = (RadioButton) view.findViewById(R.id.poll_question_choice4);
		_choiceGroup = (RadioGroup) view.findViewById(R.id.poll_choice_group);
		_btn = (Button) view.findViewById(R.id.poll_btn);


		_questionText.setText(String.format("%d) %s", question_no, question.getQuestionText()));
		_choice1.setText(question.getChoice().get(0).getChoiceText());
		_choice2.setText(question.getChoice().get(1).getChoiceText());
		_choice3.setText(question.getChoice().get(2).getChoiceText());
		_choice4.setText(question.getChoice().get(3).getChoiceText());

		if (!hasNext)
			_btn.setText(R.string.poll_save_btn);
		_btn.setEnabled(false);
		_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Choice selectedChoice = question.getChoice().get(getCheckedRadioButtonIndex(_choiceGroup));
				if (mListener != null) {
					if (hasNext)
						mListener.onNextButtonPressed(selectedChoice.getId(), question.getId());
					else
						mListener.onSaveButtonPressed(selectedChoice.getId(), question.getId());

				}
			}

		});

		_choiceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if (i != -1)
					_btn.setEnabled(true);
			}
		});

		return view;
	}

	public Integer getCheckedRadioButtonIndex(RadioGroup choiceGroup) {
		Integer id = choiceGroup.getCheckedRadioButtonId();

		if (id == R.id.poll_question_choice1) return 0;
		else if (id == R.id.poll_question_choice2) return 1;
		else if (id == R.id.poll_question_choice3) return 2;
		else if (id == R.id.poll_question_choice4) return 3;
		else return 0;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		void onSaveButtonPressed(Integer choice_id, Integer question_id);

		void onNextButtonPressed(Integer choice_id, Integer question_id);
	}
}
