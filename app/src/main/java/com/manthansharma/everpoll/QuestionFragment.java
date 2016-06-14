package com.manthansharma.everpoll;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.manthansharma.everpoll.api.model.Choice;
import com.manthansharma.everpoll.api.model.Question;

import java.util.ArrayList;
import java.util.List;


public class QuestionFragment extends Fragment {
	private static final String ARG_PARAM1 = "question_set_name";
	private static final String ARG_PARAM2 = "question_no";

	private String question_set_name;
	private Integer question_no;

	private OnFragmentInteractionListener mListener;

	TextView _questionLabel;
	EditText _questionText, _choice1, _choice2, _choice3, _choice4;
	TextInputLayout _questionTextWrapper, _choice1Wrapper, _choice2Wrapper, _choice3Wrapper, _choice4Wrapper;
	Button _addQuestion, _saveSet;

	public QuestionFragment() {
		// Required empty public constructor
	}

	public static QuestionFragment newInstance(String question_set_name, Integer question_no) {
		QuestionFragment fragment = new QuestionFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, question_set_name);
		args.putInt(ARG_PARAM2, question_no);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			question_set_name = getArguments().getString(ARG_PARAM1);
			question_no = getArguments().getInt(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_question, container, false);

		_questionLabel = (TextView) view.findViewById(R.id.input_question_label);

		_questionText = (EditText) view.findViewById(R.id.input_create_question_text);
		_questionTextWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_create_question_text);

		_choice1 = (EditText) view.findViewById(R.id.input_create_question_choice1);
		_choice1Wrapper = (TextInputLayout) view.findViewById(R.id.wrapper_create_question_choice1);

		_choice2 = (EditText) view.findViewById(R.id.input_create_question_choice2);
		_choice2Wrapper = (TextInputLayout) view.findViewById(R.id.wrapper_create_question_choice2);

		_choice3 = (EditText) view.findViewById(R.id.input_create_question_choice3);
		_choice3Wrapper = (TextInputLayout) view.findViewById(R.id.wrapper_create_question_choice3);

		_choice4 = (EditText) view.findViewById(R.id.input_create_question_choice4);
		_choice4Wrapper = (TextInputLayout) view.findViewById(R.id.wrapper_create_question_choice4);

		_addQuestion = (Button) view.findViewById(R.id.add_question_btn);
		_saveSet = (Button) view.findViewById(R.id.save_set_btn);

		_questionLabel.setText(String.format("Question Set: %s, No.: %d", question_set_name, question_no));

		if (question_no >= 10)
			_addQuestion.setEnabled(false);

		_addQuestion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_addQuestion.setEnabled(false);
				if (validate()) {
					Question question = set_question();
					if (mListener != null) {
						mListener.onFragmentInteraction(question, false);
					}
				} else {
					_addQuestion.setEnabled(true);
				}
			}
		});

		_saveSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_saveSet.setEnabled(false);
				if (validate()) {
					Question question = set_question();
					if (mListener != null) {
						mListener.onFragmentInteraction(question, true);
					}
				} else {
					_saveSet.setEnabled(true);
				}
			}
		});
		return view;
	}

	public Question set_question() {
		List<Choice> choiceList = new ArrayList<>();
		choiceList.add(new Choice(_choice1.getText().toString()));
		choiceList.add(new Choice(_choice2.getText().toString()));
		choiceList.add(new Choice(_choice3.getText().toString()));
		choiceList.add(new Choice(_choice4.getText().toString()));
		return new Question(_questionText.getText().toString(), choiceList);
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
		void onFragmentInteraction(Question question, Boolean save);
	}

	public boolean validate() {
		boolean valid = true;
		String question_text = _questionText.getText().toString();
		String choice1 = _choice1.getText().toString();
		String choice2 = _choice2.getText().toString();
		String choice3 = _choice3.getText().toString();
		String choice4 = _choice4.getText().toString();

		if (question_text.isEmpty() || question_text.length() < 4) {
			_questionTextWrapper.setError("greater than 4 alphanumeric characters");
			valid = false;
		} else {
			_questionTextWrapper.setError(null);
			_questionTextWrapper.setErrorEnabled(false);
		}

		if (choice1.isEmpty() || choice1.length() < 4) {
			_choice1Wrapper.setError("greater than 4 alphanumeric characters");
			valid = false;
		} else {
			_choice1Wrapper.setError(null);
			_choice1Wrapper.setErrorEnabled(false);
		}

		if (choice2.isEmpty() || choice2.length() < 4) {
			_choice2Wrapper.setError("greater than 4 alphanumeric characters");
			valid = false;
		} else {
			_choice2Wrapper.setError(null);
			_choice2Wrapper.setErrorEnabled(false);
		}

		if (choice3.isEmpty() || choice3.length() < 4) {
			_choice3Wrapper.setError("greater than 4 alphanumeric characters");
			valid = false;
		} else {
			_choice3Wrapper.setError(null);
			_choice3Wrapper.setErrorEnabled(false);
		}

		if (choice4.isEmpty() || choice4.length() < 4) {
			_choice4Wrapper.setError("greater than 4 alphanumeric characters");
			valid = false;
		} else {
			_choice4Wrapper.setError(null);
			_choice4Wrapper.setErrorEnabled(false);
		}

		return valid;
	}
}
