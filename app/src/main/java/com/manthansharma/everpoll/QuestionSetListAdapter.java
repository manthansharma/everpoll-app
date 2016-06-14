package com.manthansharma.everpoll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.manthansharma.everpoll.api.model.Question;
import com.manthansharma.everpoll.api.model.QuestionSet;

import java.util.HashMap;
import java.util.List;

public class QuestionSetListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<QuestionSet> _questionSetList;
	private HashMap<Integer, List<Question>> _questionMap;

	public QuestionSetListAdapter(Context context, List<QuestionSet> _questionSetList,
	                              HashMap<Integer, List<Question>> _questionMap) {
		this._context = context;
		this._questionSetList = _questionSetList;
		this._questionMap = _questionMap;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._questionMap.get(this._questionSetList.get(groupPosition).getId())
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return this._questionMap.get(this._questionSetList.get(groupPosition).getId())
				.get(childPosition).getId();
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
	                         boolean isLastChild, View convertView, ViewGroup parent) {

		final Question question = (Question) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.question_set_list_child, null);
		}

		TextView _question_text = (TextView) convertView.findViewById(R.id.question_set_item_question_text);

		_question_text.setText(String.format("%d. %s", childPosition + 1, question.getQuestionText()));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._questionMap.get(this._questionSetList.get(groupPosition).getId())
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._questionSetList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._questionSetList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return _questionSetList.get(groupPosition).getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
	                         View convertView, ViewGroup parent) {
		QuestionSet questionSet = (QuestionSet) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.question_set_list_header, null);
		}

		TextView _question_set = (TextView) convertView.findViewById(R.id.question_set_item_question_set);
		TextView _question_count = (TextView) convertView.findViewById(R.id.question_set_item_question_count);

		_question_set.setText(questionSet.getName());
		_question_count.setText(String.format("No. of questions: %d", getChildrenCount(groupPosition)));

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}