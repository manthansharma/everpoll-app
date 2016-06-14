package com.manthansharma.everpoll;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.manthansharma.everpoll.api.model.Room;


public class PollStartFragment extends Fragment {
	private static final String ARG_PARAM1 = "room";

	private Room room;

	private OnFragmentInteractionListener mListener;


	public static PollStartFragment newInstance(Room room) {
		PollStartFragment fragment = new PollStartFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, new Gson().toJson(room));
		fragment.setArguments(args);
		return fragment;
	}

	public PollStartFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			room = new Gson().fromJson(getArguments().getString(ARG_PARAM1), Room.class);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_poll_start, container, false);
		TextView _name = (TextView) view.findViewById(R.id.poll_name);
		TextView _desc = (TextView) view.findViewById(R.id.poll_desc);
		TextView _question_count = (TextView) view.findViewById(R.id.poll_question_count);
		TextView _response = (TextView) view.findViewById(R.id.poll_response);
		TextView _timestamp = (TextView) view.findViewById(R.id.poll_timestamp);

		Button _startBtn  = (Button) view.findViewById(R.id.poll_start_btn);
		Button _quitBtn  = (Button) view.findViewById(R.id.poll_quit_btn);

		_startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onStartButtonPressed();
				}
			}
		});

		_quitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onQuitButtonPressed();
				}
			}
		});

		_name.setText(room.getName());
		_desc.setText(room.getDescription());
		_timestamp.setText(room.getDaysAgo());
		_question_count.setText(String.format("Questions: %d", room.getQuestionSetDetail().getQuestion().size()));
		_response.setText(String.format("Response: %d", room.getResponse()));
		return view;
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
		void onStartButtonPressed();
		void onQuitButtonPressed();
	}
}
