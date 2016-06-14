package com.manthansharma.everpoll.api;

import com.manthansharma.everpoll.api.model.QuestionSet;
import com.manthansharma.everpoll.api.model.QuestionSetList;
import com.manthansharma.everpoll.api.model.Room;
import com.manthansharma.everpoll.api.model.RoomList;
import com.manthansharma.everpoll.api.model.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApiEndpointInterface {
	// Request method and URL specified in the annotation
	// Callback for the parsed response is the last parameter

	@POST("auth-token/")
	Call<User> get_token(@Body User user);

	@POST("users/")
	Call<User> create_user(@Body User user);

	@GET("room/")
	Call<RoomList> get_rooms(@Query("public") Boolean public_val);


	@GET("room/{pk}/")
	Call<Room> get_room(@Path("pk") Integer pk);

	@GET("room/{pk}/user/")
	Call<ResponseBody> join_room(@Path("pk") Integer pk);

	@POST("room/")
	Call<Room> create_rooms(@Body Room room);

	@GET("question-set/")
	Call<QuestionSetList> get_question_set_list();

	@POST("question-set/")
	Call<QuestionSet> create_question_set(@Body QuestionSet questionSet);

	@POST("choice/vote/")
	Call<ResponseBody> vote(@Body ArrayList choice);
}